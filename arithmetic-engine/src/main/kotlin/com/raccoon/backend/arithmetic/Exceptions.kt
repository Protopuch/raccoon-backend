package com.raccoon.backend.arithmetic

open class ParsingException(
    val expression: String,
    val offset: Int,
    message: String = "Cannot parse expression '$expression' at position $offset",
    cause: Throwable? = null
) : Exception(message, cause)

class UnexpectedSymbolException(
    expression: String,
    offset: Int,
    val symbol: Char,
    message: String = "Unexpected symbol '$symbol' in expression '$expression' at position $offset",
    cause: Throwable? = null
) : ParsingException(expression, offset, message, cause)

class InvalidNumberFormatException(
    expression: String,
    offset: Int,
    message: String = "Invalid number format in expression '$expression' at position $offset",
    cause: Throwable? = null
) : ParsingException(expression, offset, message, cause)

class UnexpectedTokenException(
    expression: String,
    offset: Int,
    message: String = "Unexpected token in expression '$expression' at position $offset",
    cause: Throwable? = null
) : ParsingException(expression, offset, message, cause)

