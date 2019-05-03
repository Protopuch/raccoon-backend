package com.raccoon.backend.arithmetic.internal.walker

import com.raccoon.backend.arithmetic.internal.ast.*

/**
 * A walker traversing the given AST and evaluating the arithmetic value represented by it.
 * If some part of the AST doesn't represent an arithmetic exception, the [IllegalArgumentException] is thrown.
 */
class EvaluationWalker: AbstractSuspendWalker<Expression, Double> {

    constructor(): super()
    constructor(dispatcher: SuspendWalkerDispatcher<Double>): super(dispatcher)

    override suspend fun visitElement(element: Element): Double = error("Cannot evaluate AST node: $element")

    override suspend fun visitUnaryPlus(plus: UnaryPlus): Double = process(plus.argument)
    override suspend fun visitUnaryMinus(minus: UnaryMinus): Double = -process(minus.argument)

    override suspend fun visitAdd(add: Add): Double = process(add.lhs) + process(add.rhs)
    override suspend fun visitSub(sub: Sub): Double = process(sub.lhs) - process(sub.lhs)
    override suspend fun visitMul(mul: Mul): Double = process(mul.lhs) * process(mul.rhs)
    override suspend fun visitDiv(div: Div): Double = process(div.lhs) / process(div.rhs)
    override suspend fun visitMod(mod: Mod): Double = process(mod.lhs) % process(mod.rhs)

    override suspend fun visitDoubleConstant(constant: DoubleConstant): Double = constant.value
}

/**
 * A walker traversing the given AST and evaluating the arithmetic value represented by it.
 * Unlike [EvaluationWalker], it uses recursive traversal thus it can fail with an
 * [StackOverflowError] on very deep ASTs. If some part of the AST doesn't represent
 * an arithmetic exception, the [IllegalArgumentException] is thrown.
 */
class RecursiveEvaluationWalker: AbstractRecursiveWalker<Expression, Double>() {
    override fun visitElement(element: Element): Double = error("Cannot evaluate AST node: $element")

    override fun visitUnaryPlus(plus: UnaryPlus): Double = process(plus.argument)
    override fun visitUnaryMinus(minus: UnaryMinus): Double = -process(minus.argument)

    override fun visitAdd(add: Add): Double = process(add.lhs) + process(add.rhs)
    override fun visitSub(sub: Sub): Double = process(sub.lhs) - process(sub.lhs)
    override fun visitMul(mul: Mul): Double = process(mul.lhs) * process(mul.rhs)
    override fun visitDiv(div: Div): Double = process(div.lhs) / process(div.rhs)
    override fun visitMod(mod: Mod): Double = process(mod.lhs) % process(mod.rhs)

    override fun visitDoubleConstant(constant: DoubleConstant): Double = constant.value
}