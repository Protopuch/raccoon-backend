package com.raccoon.backend.arithmetic.internal.walker

import com.raccoon.backend.arithmetic.internal.ast.Element
import java.lang.IllegalStateException
import kotlin.coroutines.*

/**
 * A base class for coroutine dispatchers used by the [AbstractSuspendWalker].
 * Such a dispatcher encapsulates all low-level logic of creating, suspending and
 * resuming coroutines used by the [AbstractSuspendWalker] to traverse an AST.
 */
sealed class SuspendWalkerDispatcher<R> {

    /**
     * Starts traversing the AST using the given [visitor].
     *
     * @param root - the root node of an AST to be traversed.
     * @param visitor - the visitor used to process nodes of the AST.
     * @return traversing result determined by the [visitor].
     */
    internal abstract fun walk(root: Element, visitor: SuspendVisitor<R>): R

    /**
     * Processes the given AST node. It's a suspend function thus it can be called only from a
     * coroutine processing some other node. Depending on an implementation this method can
     * suspend the current coroutine and start a new one to process the given node or process
     * the given node without starting a new coroutine.
     *
     * @param node - the AST node to be processed.
     * @param visitor - the visitor used to process the node.
     * @return traversing result determined by the [visitor]
     */
    internal abstract suspend fun process(node: Element, visitor: SuspendVisitor<R>): R
}

class DefaultDispatcher<R>(): SuspendWalkerDispatcher<R>() {
    private val continuationStack: MutableList<Continuation<Unit>> = mutableListOf()
    private var result = Result.failure<R>(IllegalStateException("Not initialized result."))

    private fun <T> MutableList<Continuation<T>>.pop(): Continuation<T> = removeAt(lastIndex)
    private fun <T> MutableList<Continuation<T>>.push(value: Continuation<T>) = add(value)

    private fun walkerLoop() {
        while(continuationStack.isNotEmpty()) {
            continuationStack.pop().resume(Unit)
        }
    }

    private fun createProcessingCoroutine(node: Element, visitor: SuspendVisitor<R>): Continuation<Unit> =
        suspend {
            node.accept(visitor)
        }.createCoroutine(Continuation(EmptyCoroutineContext) { result = it })

    /**
     * Starts traversing the AST using the given [visitor]. Creates and starts a coroutine processing the [root] node.
     *
     * @param root - the root node of an AST to be traversed.
     * @param visitor - the visitor used to process nodes of the AST.
     * @return traversing result determined by the [visitor].
     */
    override fun walk(root: Element, visitor: SuspendVisitor<R>): R {
        continuationStack.push(createProcessingCoroutine(root, visitor))
        walkerLoop()
        return result.getOrThrow()
    }

    /**
     * Processes the given AST node. Suspends the current coroutine and
     * starts a new one for processing of the given [node].
     *
     * @param node - the AST node to be processed.
     * @param visitor - the visitor used to process the node.
     * @return traversing result determined by the given [visitor].
     */
    override suspend fun process(node: Element, visitor: SuspendVisitor<R>): R {
        return suspendCoroutine { continuation ->
            val processNext = suspend {
                continuation.resumeWith(result)
            }.createCoroutine(Continuation(EmptyCoroutineContext) {})
            continuationStack.push(processNext)
            continuationStack.push(createProcessingCoroutine(node, visitor))
        }
    }
}