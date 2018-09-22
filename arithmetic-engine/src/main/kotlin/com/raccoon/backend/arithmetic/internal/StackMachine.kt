package com.raccoon.backend.arithmetic.internal

/**
 * A stack machine supporting double arithmetic.
 */
interface StackMachine {

    // Work with stack.
    fun dPush(value: Double)
    fun dPop(): Double
    fun dPeek(): Double

    // Basic arithmetic.
    fun dAdd()
    fun dSub()
    fun dMul()
    fun dDiv()

    // Unary minus.
    fun dNeg()

    // Reset the machine - remove all values from the stack.
    fun reset()
}

/**
 * Simple stack machine implementation with a growing stack.
 */
class SimpleStackMachine(initialSize: Int = DEFAULT_SIZE) : StackMachine {

    // Backing array for the stack.
    private var stack = DoubleArray(initialSize)

    // Index of the top element in the stack.
    private var top = -1

    private fun ensureSize() {
        if (top == stack.lastIndex) {
            stack = stack.copyOf((stack.size * SCALE_FACTOR).toInt())
        }
    }

    override fun dPush(value: Double) {
        ensureSize()
        stack[++top] = value
    }

    /** @throws IndexOutOfBoundsException - if stack is empty */
    override fun dPop(): Double = stack[top].also { top-- }
    override fun dPeek(): Double = stack[top]

    /** @throws IndexOutOfBoundsException - if stack doesn't contain enough elements */
    override fun dAdd() {
        val rhs = dPop()
        stack[top] += rhs
    }

    /** @throws IndexOutOfBoundsException - if stack doesn't contain enough elements */
    override fun dSub() {
        val rhs = dPop()
        stack[top] -= rhs
    }

    /** @throws IndexOutOfBoundsException - if stack doesn't contain enough elements */
    override fun dMul() {
        val rhs = dPop()
        stack[top] *= rhs
    }

    /** @throws IndexOutOfBoundsException - if stack doesn't contain enough elements */
    override fun dDiv() {
        val rhs = dPop()
        stack[top] /= rhs
    }

    override fun dNeg() {
        stack[top] = -stack[top]
    }

    override fun reset() {
        top = -1
    }

    companion object {
        private const val DEFAULT_SIZE = 256
        private const val SCALE_FACTOR = 1.5
    }
}


