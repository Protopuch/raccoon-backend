package com.raccoon.backend.arithmetic.internal.walker

import com.raccoon.backend.arithmetic.internal.ast.*

/** A visitor used to process the different AST nodes in a different way. */
interface Visitor<out R> {
    fun visitElement(element: Element): R
    fun visitExpression(expression: Expression): R = visitElement(expression)

    fun visitBinaryOperator(operator: BinaryOperator): R = visitExpression(operator)
    fun visitUnaryOperator(operator: UnaryOperator): R = visitExpression(operator)
    fun <T> visitConstant(constant: Constant<T>): R = visitExpression(constant)

    fun visitUnaryPlus(plus: UnaryPlus): R = visitUnaryOperator(plus)
    fun visitUnaryMinus(minus: UnaryMinus): R = visitUnaryOperator(minus)

    fun visitAdd(add: Add): R = visitBinaryOperator(add)
    fun visitSub(sub: Sub): R = visitBinaryOperator(sub)
    fun visitMul(mul: Mul): R = visitBinaryOperator(mul)
    fun visitDiv(div: Div): R = visitBinaryOperator(div)
    fun visitMod(mod: Mod): R = visitBinaryOperator(mod)

    fun visitDoubleConstant(constant: DoubleConstant): R = visitConstant(constant)
}
