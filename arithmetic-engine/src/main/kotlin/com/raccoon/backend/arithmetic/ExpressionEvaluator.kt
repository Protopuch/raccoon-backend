package com.raccoon.backend.arithmetic

import com.raccoon.backend.arithmetic.internal.SimpleEvaluator

interface ExpressionEvaluator {
    fun evaluate(expression: String): Double

    companion object {
        fun simpleEvaluator(): ExpressionEvaluator = SimpleEvaluator()
    }
}
