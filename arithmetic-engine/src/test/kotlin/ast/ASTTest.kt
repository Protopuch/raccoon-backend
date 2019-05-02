package ast

import com.raccoon.backend.arithmetic.internal.ast.Add
import com.raccoon.backend.arithmetic.internal.ast.DoubleConstant
import com.raccoon.backend.arithmetic.internal.ast.Mul
import com.raccoon.backend.arithmetic.internal.walker.EvaluationWalker
import com.raccoon.backend.arithmetic.internal.walker.RecursiveEvaluationWalker
import org.junit.Test
import kotlin.test.assertEquals

class ASTTest {
    @Test
    fun smoke() {
        val root = Add(DoubleConstant(2.0), Mul(DoubleConstant(2.0), DoubleConstant(2.0)))
        assertEquals(6.0, EvaluationWalker().walk(root))
        assertEquals(6.0, RecursiveEvaluationWalker().walk(root))
    }
}