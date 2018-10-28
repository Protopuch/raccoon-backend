package com.raccoon.backend.arithmetic.internal

/** An utility class allowing creating substrings without copying. */
data class NonCopyingSequence(
    private val backing: CharSequence,
    private val start: Int = 0,
    private val endExclusive: Int = backing.length
): CharSequence {

    init {
        if (start < 0 || endExclusive > backing.length) {
            throw IllegalArgumentException("Incorrect bounds: [$start, $endExclusive)")
        }
    }

    override val length: Int
        get() = endExclusive - start

    override fun get(index: Int): Char = backing[start + index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        NonCopyingSequence(backing, start + startIndex, start + endIndex)

    override fun toString(): String = backing.substring(start, endExclusive)
}

fun CharSequence.nonCopyingSubSequence(startIndex: Int, endIndex: Int = length) =
        NonCopyingSequence(this, startIndex, endIndex)
