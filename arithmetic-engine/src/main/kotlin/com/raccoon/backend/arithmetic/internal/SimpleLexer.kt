package com.raccoon.backend.arithmetic.internal

import com.raccoon.backend.arithmetic.InvalidNumberFormatException
import com.raccoon.backend.arithmetic.UnexpectedSymbolException

/**
 * Supported:
 *  - Basic tokens: +, -, *, /, %, (, )
 *  - Double values with dot and in the scientific notation, e.g.
 *      1.2, 42, 1.0e34, 1e2, 1e-2
 */

class SimpleLexer(val string: String): Lexer {

    private var index = 0

    override var finished: Boolean = false
        private set

    private var lastTokenInternal: Token? = null

    override val lastToken: Token
        get() = lastTokenInternal ?: throw IllegalStateException(
            "No tokens read from this lexer yet. Call next() to read the first one."
        )

    private fun readDouble(startIndex: Int): Double {
        // Check that a substring starting from the startIndex starts with a constant.
        // The requirement that the substring starts with the constant is already included in the regex.
        return CONSTANT_REGEX.find(string.nonCopyingSubSequence(startIndex))?.let {
            index = startIndex + it.value.length
            it.value.toDoubleOrNull()
        } ?: throw InvalidNumberFormatException(string, startIndex)
    }

    override fun next(): Token {
        if (finished) {
            throw IllegalStateException("Lexer is in the finished state.")
        }

        // Skip whitespaces
        while (index < string.length && string[index].isWhitespace()) {
            index++
        }

        // No more symbols in the string
        if (index >= string.length) {
            finished = true
            return EOF(index).also {
                lastTokenInternal = it
            }
        }

        val startIndex: Int = index
        val char: Char = string[index++]
        return if (char.isDigit() || char == '.') {
            Constant(readDouble(startIndex), startIndex)
        } else {
            when (char) {
                '+' -> Plus(startIndex)
                '-' -> Minus(startIndex)
                '*' -> Mul(startIndex)
                '/' -> Div(startIndex)
                '%' -> Mod(startIndex)
                '(' -> LeftParenthesis(startIndex)
                ')' -> RightParenthesis(startIndex)
                else -> throw UnexpectedSymbolException(string, startIndex, char)
            }
        }.also {
            lastTokenInternal = it
        }
    }

    companion object {
        // A decimal positive real constant optionally in the scientific notation.
        // E.g. 1.0, 42, 5.46e+134 etc.
        val CONSTANT_REGEX = "^\\d+(\\.\\d+)?(e[+-]?\\d+)?".toRegex(RegexOption.IGNORE_CASE)
        val factory: LexerFactory = object: LexerFactory {
            override fun create(string: String): Lexer = SimpleLexer(string)
        }
    }
}
