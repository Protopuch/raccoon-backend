package com.raccoon.backend.arithmetic

import com.raccoon.backend.arithmetic.internal.SimpleEvaluator

interface ExpressionEvaluator {

    /**
     * Evaluates the given expression and returns the evaluation result as a double value.
     *
     * @throws ParsingException - if the expression is incorrect.
     */
    fun evaluate(expression: String): Double

    companion object {
        @JvmStatic
        fun simpleEvaluator(): ExpressionEvaluator = SimpleEvaluator()
    }
}
