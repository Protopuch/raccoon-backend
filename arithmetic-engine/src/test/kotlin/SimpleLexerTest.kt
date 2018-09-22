import com.raccoon.backend.arithmetic.UnexpectedSymbolException
import com.raccoon.backend.arithmetic.internal.*
import kotlin.test.*

class SimpleLexerTest {

    @Test
    fun `Smoke test`() = forInput("42") {
        assertTokenEquals(constant(42), next())
        assertTokenEquals(eof, next())
    }

    @Test
    fun `All basic tokens are supported`() {
        assertTokens("+-*/()", plus, minus, mul, div, leftParenthesis, rightParenthesis)
    }

    @Test
    fun `Lexer supports empty strings`() = forInput("") {
        assertTokenEquals(eof, next())
        assertTrue(finished)
    }

    @Test
    fun `Lexer throws an exception for invalid symbols`() {
        val inputs = listOf(
            "%" to 0,
            "123+456=579" to 7,
            "abc" to 0
        )

        inputs.forEach { (input, expectedOffset) ->
            forInput(input) {
                assertFailsWith<UnexpectedSymbolException> {
                    readAll()
                }.also {
                    assertEquals(expectedOffset, it.offset)
                    assertEquals(input, it.expression)
                    assertEquals(input[expectedOffset], it.symbol)
                }
            }
        }
    }

    @Test
    fun `Reading from a finished lexer causes an exception`() {
        assertTokens("+", plus) {
            assertFailsWith<IllegalStateException> { next() }
        }
    }

    @Test
    fun `Lexer correctly reads real numbers`() {
        val numbers = listOf(
            "5" to 5.0,
            "75" to 75.0,
            "88.77" to 88.77,
            "0" to 0.0,
            "0.0" to 0.0,
            "1.0" to 1.0,
            "0.1" to 0.1,

            "5e10"  to 5e10,
            "5E10"  to 5e10,
            "5e+10" to 5e10,
            "5E+10" to 5e10,
            "5e-10" to 5e-10,
            "5E-10" to 5e-10,

            "5.6e10"  to 5.6e10,
            "5.6E10"  to 5.6e10,
            "5.6e+10" to 5.6e10,
            "5.6E+10" to 5.6e10,
            "5.6e-10" to 5.6e-10,
            "5.6E-10" to 5.6e-10,

            "0.6e10"  to 0.6e10,
            "0.6E10"  to 0.6e10,
            "0.6e+10" to 0.6e10,
            "0.6E+10" to 0.6e10,
            "0.6e-10" to 0.6e-10,
            "0.6E-10" to 0.6e-10
        )
        numbers.forEach { (input, expected) ->
            assertTokens(input, constant(expected))
        }
    }

    @Test
    fun `Lexer ignores whitespace characters`() {
        assertTokens("     2.0", constant(2.0))
        assertTokens("3 +\t2\n-1", constant(3), plus, constant(2), minus, constant(1))
        assertTokens("+    ", plus)
        assertTokens("    ")
    }

    companion object {
        // Mock tokens
        fun constant(value: Int) = Constant(value.toDouble(), -1)
        fun constant(value: Double) = Constant(value, -1)
        val plus = Plus(-1)
        val minus = Minus(-1)
        val mul = Mul(-1)
        val div = Div(-1)

        val leftParenthesis = LeftParenthesis(-1)
        val rightParenthesis = RightParenthesis(-1)
        val eof = EOF(-1)

        private inline fun forInput(input: String, block: SimpleLexer.() -> Unit) =
            SimpleLexer(input).block()

        /**
         * Asserts than two tokens are equals ignoring their offsets.
         */
        private fun assertTokenEquals(expected: Token, actual: Token) {
            assertEquals(expected::class, actual::class)
            if (expected is Constant && actual is Constant) {
                assertEquals(expected.value, actual.value)
            }
        }

        /**
         *  Asserts that the input consists of a given list of tokens.
         *  No need to pass [EOF] as a final token - the method already takes this into account.
         */
        private fun assertTokens(
            input: String,
            vararg expectedTokens: Token,
            action: SimpleLexer.() -> Unit = {}
        ) = forInput(input) {
            expectedTokens.forEach {
                assertTokenEquals(it, next())
            }
            assertTokenEquals(eof, next())
            assertTrue(finished)
            action()
        }

        private fun SimpleLexer.readAll(): List<Token> = mutableListOf<Token>().apply {
            while (!finished) {
                add(next())
            }
        }
    }
}