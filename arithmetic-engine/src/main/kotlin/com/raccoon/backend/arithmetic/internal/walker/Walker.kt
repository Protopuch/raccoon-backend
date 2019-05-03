package com.raccoon.backend.arithmetic.internal.walker

import com.raccoon.backend.arithmetic.internal.ast.Element

/**
 * Basic interface for classes performing some processing over an AST.
 */
interface Walker<in T: Element, out R> {
    /** Traverse the given AST returning some result. */
    fun walk(root: T): R
}

/**
 * Base class for recursive AST traversing.
 * Usage:
 *  1. Create a subclass and implement all `visit...` methods you need.
 *  2. To process a child of a node call the [process] method.
 */
abstract class AbstractRecursiveWalker<in T: Element, out R>: Visitor<R>, Walker<T, R> {

    /** Traverse the given AST returning a result determined by certain subclass implementation. */
    override fun walk(root: T): R = process(root)

    /** Processes the given node and returns a result determined by subclass implementation*/
    protected fun process(node: Element): R = node.accept(this)
}

/**
 * Base class for non-recursive AST traversing. It starts a coroutine for each node being processed.
 * The [process] method is used to suspend the current coroutine and start processing of another node (e.g. a
 * child node of the current one). The way in which the class interacts with the underlying coroutine
 * machinery is determined by the [SuspendWalkerDispatcher] object passed to the constructor.
 *
 * Usage:
 *  1. Create a subclass and implement all `visit...` methods you need.
 *  2. To process a child of a node call the [process] method.
 *
 * @param dispatcher - a dispatching object encapsulating logic of starting and suspending of
 *  coroutines used to traverse the AST (see [SuspendWalkerDispatcher]).
 */
abstract class AbstractSuspendWalker<in T: Element, out R>(
    private val dispatcher: SuspendWalkerDispatcher<R> = DefaultDispatcher()
): SuspendVisitor<R>, Walker<T, R> {

    /** Traverse the given AST returning a result determined by certain subclass implementation. */
    override fun walk(root: T): R = dispatcher.walk(root, this)

    /**
     * Processes the given node probably suspending the current coroutine.
     * The certain result returning is determined by subclass implementation.
     */
    protected suspend fun process(node: Element): R = dispatcher.process(node, this)
}