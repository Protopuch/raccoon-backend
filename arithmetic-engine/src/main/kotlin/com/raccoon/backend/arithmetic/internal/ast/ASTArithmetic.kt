package com.raccoon.backend.arithmetic.internal.ast

import com.raccoon.backend.arithmetic.internal.walker.SuspendVisitor
import com.raccoon.backend.arithmetic.internal.walker.Visitor

// TODO: Move tokens with the same names in separate package/class

abstract class UnaryOperator(val argument: Expression): Expression() {

    override val children: List<Expression>
        get() = listOf(argument)

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryOperator(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitUnaryOperator(this)
}

abstract class BinaryOperator(
    val lhs: Expression,
    val rhs: Expression
): Expression() {

    override val children: List<Element>
        get() = listOf(lhs, rhs)

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBinaryOperator(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitBinaryOperator(this)
}

abstract class Constant<T>: Expression() {
    abstract val value: T

    override val children: List<Element>
        get() = emptyList()

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConstant(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitConstant(this)
}

data class DoubleConstant(override val value: Double) : Constant<Double>() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitDoubleConstant(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitDoubleConstant(this)
}

class UnaryMinus(argument: Expression) : UnaryOperator(argument) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryMinus(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitUnaryMinus(this)

}
class UnaryPlus(argument: Expression) : UnaryOperator(argument) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryPlus(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitUnaryPlus(this)
}

class Add(lhs: Expression, rhs: Expression): BinaryOperator(lhs, rhs) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitAdd(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitAdd(this)
}

class Sub(lhs: Expression, rhs: Expression): BinaryOperator(lhs, rhs) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSub(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitSub(this)
}

class Mul(lhs: Expression, rhs: Expression): BinaryOperator(lhs, rhs) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitMul(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitMul(this)
}

class Div(lhs: Expression, rhs: Expression): BinaryOperator(lhs, rhs) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitDiv(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitDiv(this)
}

class Mod(lhs: Expression, rhs: Expression): BinaryOperator(lhs, rhs) {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitMod(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitMod(this)
}
