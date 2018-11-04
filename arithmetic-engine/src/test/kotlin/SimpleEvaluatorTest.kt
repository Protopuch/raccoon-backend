import com.raccoon.backend.arithmetic.ExpressionEvaluator
import com.raccoon.backend.arithmetic.UnexpectedTokenException
import com.raccoon.backend.arithmetic.internal.SimpleParser
import com.raccoon.backend.arithmetic.internal.SimpleStackMachine
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.test.*

class SimpleEvaluatorTest {

    private fun assertEvaluate(input: String, expected: Double, epsilon: Double = 0.0) {
        check(epsilon >= 0)
        val actual = ExpressionEvaluator.simpleEvaluator().evaluate(input)
        val message = """
            For input: $input.
            Expected :$expected
            Actual   :$actual
            Epsilon  :$epsilon
        """.trimIndent()
        when {
            expected.isInfinite() -> assertEquals(expected, actual)
            expected.isNaN() -> assertTrue(actual.isNaN(), message)
            else -> assertTrue(abs(expected - actual) <= epsilon, message)
        }
    }

    @Test
    fun `Smoke test`() {
        val expressions = listOf(
            "2+3" to 5.0,
            "2+2*2" to 6.0,
            "25/5" to 5.0,
            "-1+2" to 1.0,
            "0*1000" to 0.0,
            "2-1" to 1.0,
            "(2+2)*2" to 8.0,
            "++1--2" to 3.0,
            "4.2 * 4" to 16.8,
            "2 / 8" to 0.25,
            "2 * -(2 + 2)" to -8.0,
            "25 % 2" to 1.0
        )
        expressions.forEach { (input, expected) ->
            assertEvaluate(input, expected)
        }
    }

    @Test
    fun `Parser can be reused`() {
        val stackMachine = SimpleStackMachine()
        val parser = SimpleParser(stackMachine = stackMachine)
        assertEquals(4.0, parser.parse("2+2"))
        assertFailsWith<IndexOutOfBoundsException> { stackMachine.dPop() }
        stackMachine.dPush(1.0)
        assertEquals(6.0, parser.parse("2+2*2"))
        assertFailsWith<IndexOutOfBoundsException> { stackMachine.dPop() }
    }

    @Test
    fun `Parser throws correct exceptions`() {
        val inputs = listOf(
            "10 11" to 3,
            "5*/10" to 2,
            ")10(" to 0,
            "()" to 1,
            "(42" to 3,
            "2*2)" to 3
        )
        inputs.forEach { (input, expectedOffset) ->
            assertFailsWith<UnexpectedTokenException>("Input: ${input}") {
                ExpressionEvaluator.simpleEvaluator().evaluate(input)
            }.also {
                assertEquals(input, it.expression, "Input: ${input}")
                assertEquals(expectedOffset, it.offset, "Input: ${input}")
            }
        }
    }

    @Test
    fun `Evaluator supports border cases`() {
        // Division by 0 -> Inf (with a correct sign).
        assertEvaluate("42/0.0", Double.POSITIVE_INFINITY)
        assertEvaluate("42/+0.0", Double.POSITIVE_INFINITY)
        assertEvaluate("42/-0.0", Double.NEGATIVE_INFINITY)
        assertEvaluate("-42/0.0", Double.NEGATIVE_INFINITY)
        assertEvaluate("-42/-0.0", Double.POSITIVE_INFINITY)
        assertEvaluate("-(42/+0.0)", Double.NEGATIVE_INFINITY)
        assertEvaluate("-(42/-0.0)", Double.POSITIVE_INFINITY)

        // 0 / 0 -> NaN.
        val evaluator = ExpressionEvaluator.simpleEvaluator()
        assertTrue(evaluator.evaluate("0 / 0").isNaN())
        assertTrue(evaluator.evaluate("0 / -0").isNaN())
        assertTrue(evaluator.evaluate("-0 / 0").isNaN())
        assertTrue(evaluator.evaluate("-0 / -0").isNaN())

        // 0 % x -> 0.
        assertEvaluate("0 % 42", 0.0)
        assertEvaluate("-0 % 42", -0.0)

        // x % 0 -> NaN.
        assertTrue(evaluator.evaluate("42 % 0").isNaN())
        assertTrue(evaluator.evaluate("42 % -0").isNaN())
    }

    @Test
    fun `Evaluator supports all actions with different numbers`() {
        val inputs = listOf(
            // Zero.
            "42+0" to 42.0,
            "42-0" to 42.0,
            "42*0" to 0.0,
            "0/42" to 0.0,
            "42*-0.0" to -0.0,

            // Positive integer numbers.
            "42+2" to 44.0,
            "42-2" to 40.0,
            "40/4" to 10.0,
            "10*4" to 40.0,
            "41%3" to 2.0,

            // Negative integer numbers.
            "-42+(-2)" to -44.0,
            "-42-(-2)" to -40.0,
            "-40/(-4)" to 10.0,
            "-10*(-4)" to 40.0,
            "-41%(-3)" to -2.0,
            "-2+42" to 40.0,
            "2-42"  to -40.0,
            "-40/4" to -10.0,
            "-10*4" to -40.0,
            "-41%3" to -2.0,
            "41%-3" to 2.0,

            // Positive real numbers.
            "42.5+2.25" to 44.75,
            "42.5-2.25" to 40.25,
            "40.5/2.5"  to 16.2,
            "10.5*4.5"  to 47.25,
            "11.5%1.5"  to 1.0,

            // Negative real numbers.
            "-42.5+(-2.25)" to -44.75,
            "-42.5-(-2.25)" to -40.25,
            "-40.5/(-2.5)"  to 16.2,
            "-10.5*(-4.5)"  to 47.25,
            "-11.5%(-1.5)"  to -1.0,
            "-2.25+42.5" to 40.25,
            "2.25-42.5"  to -40.25,
            "-40.5/2.5"  to -16.2,
            "-10.5*4.5 " to -47.25,
            "-11.5%1.5"  to -1.0,

            // Positive big numbers.
            "25e12+1e13" to 3.5e13,
            "1e13-2e12"  to 8e12,
            "2e3*3e2"    to 6e5,
            "8e10/4e2"   to 2e8,
            "8e10%3e10"  to 2e10,

            // Negative big numbers.
            "-25e12+(-1e13)" to -3.5e13,
            "-1e13-(-2e12)"  to -8e12,
            "-2e3*(-3e2)"    to 6e5,
            "-8e10/(-4e2)"   to 2e8,
            "-8e10%(-3e10)"  to -2e10,
            "-10e12+1.5e13" to 5e12,
            "10e12-1.5e13" to -5e12,
            "-2e3*3e2"    to -6e5,
            "-8e10/4e2"   to -2e8,
            "-8e10%3e10"  to -2e10,

            // Positive small numbers.
            "1e-12+25e-13" to 3.5e-12,
            "3e-12-20e-13" to 10e-13,
            "2e-3*3e-2"    to 6e-5,
            "8e-10/4e-2"   to 2e-8,
            "8e-10%3e-10"  to 2e-10,

            // Negative small numbers.
            "-1e-12+(-25e-13)" to -3.5e-12,
            "-3e-12-(-20e-13)" to -10e-13,
            "-2e-3*(-3e-2)"    to 6e-5,
            "-8e-10/(-4e-2)"   to 2e-8,
            "-8e-10%(-3e-10)"  to -2e-10,
            "-1e-12+3e-12" to 2e-12,
            "1e-12-3e-12"  to -2e-12,
            "-2e-3*3e-2"   to -6e-5,
            "-8e-10/4e-2"  to -2e-8,
            "-8e-10%3e-10" to -2e-10
        )

        inputs.forEach { (input, expected) ->
            assertEvaluate(input, expected, expected.absoluteValue * EPSILON_FACTOR)
        }
    }

    @Test
    fun `Evaluator support correct operator priority`() {
        val inputs = listOf(
            "1+2*3" to 7.0,
            "1+8/2" to 5.0,
            "9-2*3" to 3.0,
            "9-8/2" to 5.0,
            "2+5%2" to 3.0,

            "(1+2)*3" to 9.0,
            "(1+8)/2" to 4.5,
            "(9-2)*3" to 21.0,
            "(9-8)/2" to 0.5,
            "(2+5)%2" to 1.0,

            "((1+2))*3" to 9.0,
            "((1+8))/2" to 4.5,
            "((9-2))*3" to 21.0,
            "((9-8))/2" to 0.5,
            "((2+5))%2" to 1.0,

            "2*((1+2)*3+1)" to 20.0,
            "22/((1+8)/2+1)" to 4.0
        )

        inputs.forEach { (input, expected) ->
            assertEvaluate(input, expected, expected.absoluteValue * EPSILON_FACTOR)
        }
    }

    @Test
    fun `Simple stack machine reallocates storage for stack if needed`() {
        assertFailsWith<IllegalArgumentException> { SimpleStackMachine(0) }
        assertFailsWith<IllegalArgumentException> { SimpleStackMachine(-1) }
        with(SimpleStackMachine(1)) {
            dPush(2.0)
            dPush(2.0)
            dAdd()
            assertEquals(4.0, dPop())
        }
    }

    companion object {
        const val EPSILON_FACTOR = 0.000001
    }
}

