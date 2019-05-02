package com.raccoon.backend.arithmetic.internal.ast

import com.raccoon.backend.arithmetic.internal.walker.SuspendVisitor
import com.raccoon.backend.arithmetic.internal.walker.Visitor

abstract class Element {
    abstract fun <R> accept(visitor: Visitor<R>): R
    abstract suspend fun <R> accept(visitor: SuspendVisitor<R>): R

    abstract val children: List<Element>
}

abstract class Expression: Element() {
    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitExpression(this)
    override suspend fun <R> accept(visitor: SuspendVisitor<R>): R = visitor.visitExpression(this)
}
