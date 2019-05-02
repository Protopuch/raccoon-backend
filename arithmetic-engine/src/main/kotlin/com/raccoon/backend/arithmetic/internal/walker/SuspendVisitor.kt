package com.raccoon.backend.arithmetic.internal.walker

import com.raccoon.backend.arithmetic.internal.ast.*

/**
 * A visitor used to process the different AST nodes in a different way.
 * Unlike the regular [Visitor], it has only suspend methods. Thus it can be used only in a
 * coroutine but its methods can suspend execution of the current coroutine.
 */
interface SuspendVisitor<out R> {
    suspend fun visitElement(element: Element): R
    suspend fun visitExpression(expression: Expression): R = visitElement(expression)

    suspend fun visitBinaryOperator(operator: BinaryOperator): R = visitExpression(operator)
    suspend fun visitUnaryOperator(operator: UnaryOperator): R = visitExpression(operator)
    suspend fun <T> visitConstant(constant: Constant<T>): R = visitExpression(constant)

    suspend fun visitUnaryPlus(plus: UnaryPlus): R = visitUnaryOperator(plus)
    suspend fun visitUnaryMinus(minus: UnaryMinus): R = visitUnaryOperator(minus)

    suspend fun visitAdd(add: Add): R = visitBinaryOperator(add)
    suspend fun visitSub(sub: Sub): R = visitBinaryOperator(sub)
    suspend fun visitMul(mul: Mul): R = visitBinaryOperator(mul)
    suspend fun visitDiv(div: Div): R = visitBinaryOperator(div)
    suspend fun visitMod(mod: Mod): R = visitBinaryOperator(mod)

    suspend fun visitDoubleConstant(constant: DoubleConstant) = visitConstant(constant)
}
