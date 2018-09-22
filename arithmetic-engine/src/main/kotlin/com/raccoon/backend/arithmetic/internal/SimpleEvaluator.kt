package com.raccoon.backend.arithmetic.internal

import com.raccoon.backend.arithmetic.ExpressionEvaluator

class SimpleEvaluator: ExpressionEvaluator {

    private val stackMachine = SimpleStackMachine()

    override fun evaluate(expression: String): Double =
        SimpleParser(stackMachine = stackMachine).parse(expression)
}
