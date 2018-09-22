package com.raccoon.backend.arithmetic.internal

interface LexerFactory {
    /**
     * Creates a lexer for the given string.
     */
    fun create(string: String): Lexer
}

interface Lexer {
    /**
     * Returns the next token in the input string.
     * Returns [EOF] if this token is the last one in the input string.
     * @throws com.raccoon.backend.arithmetic.ParsingException - if the input string is incorrect.
     * @throws IllegalStateException - if called when no tokens left in the input string.
     */
    fun next(): Token

    /**
     * True if there is no more tokens in the input string, false otherwise.
     * Note than the [next] function still can throw [com.raccoon.backend.arithmetic.ParsingException] even
     * if [finished] is false.
     */
    val finished: Boolean

    /**
     * The token returned by the previous [next] call.
     * @throws IllegalStateException - if no [next] calls occurred before the [lastToken] access.
     */
    val lastToken: Token
}

sealed class Token(val offset: Int)

class Constant(val value: Double, offset: Int) : Token(offset)

class Plus(offset: Int): Token(offset)
class Minus(offset: Int): Token(offset)
class Mul(offset: Int): Token(offset)
class Div(offset: Int): Token(offset)

class LeftParenthesis(offset: Int): Token(offset)
class RightParenthesis(offset: Int): Token(offset)

// Marker of the end of the input string.
class EOF(offset: Int): Token(offset)