package com.raccoon.backend.arithmetic.internal

import com.raccoon.backend.arithmetic.UnexpectedTokenException

/**
 * A simple arithmetic parser.
 * Parses a given string and evaluates the expression it contains.
 *
 *  Grammar:
 *  <Input>   : <Expr>EOF
 *  <Expr>    : <Term><Terms>
 *  <Terms>   : +<Term><Terms> | -<Term><Terms> | empty
 *  <Term>    : <Factor><Factors>
 *  <Factors> : *<Factor><Factors> | /<Factor><Factors> | empty
 *  <Factor>  : constant | (<Expr>) | -<Factor> | +<Factor>
 */
class SimpleParser(
    private val lexerFactory: LexerFactory = SimpleLexer.factory,
    private val stackMachine: StackMachine = SimpleStackMachine()
) {
    private lateinit var lexer: Lexer
    private lateinit var input: String

    private fun error(): Nothing = throw UnexpectedTokenException(input, lexer.lastToken.offset)
    private fun next() = lexer.next()

    private fun init(input: String) {
        stackMachine.reset()
        this.input = input
        lexer = lexerFactory.create(input)
        lexer.next() // Read the first token.
    }

    /**
     * Parses the [input] string and evaluate the expression it contains.
     * Resets the stack machine before parsing.
     * @return the result of the evaluation.
     * @throws com.raccoon.backend.arithmetic.ParsingException - if the input expression is incorrect.
     */
    fun parse(input: String): Double {
        init(input)
        expr()
        eof()
        return stackMachine.dPop()
    }

    private fun eof() {
        if (lexer.lastToken !is EOF) {
            error()
        }
    }

    private fun expr() {
        term()
        terms()
    }

    private fun term() {
        factor()
        factors()
    }

    private tailrec fun terms() {
        val token = lexer.lastToken
        when(token) {
            is Plus -> {
                next() // +
                term()
                stackMachine.dAdd()
                terms()
            }
            is Minus -> {
                next() // -
                term()
                stackMachine.dSub()
                terms()
            }
            is EOF, is RightParenthesis -> { /* no op, empty rule */ }
            else -> error()
        }
    }


    private fun factor() {
        val token = lexer.lastToken
        when (token) {
            is Constant -> {
                stackMachine.dPush(token.value)
                next() // constant
            }
            is LeftParenthesis -> {
                next() // (
                expr()
                if (lexer.lastToken is RightParenthesis) {
                    next() // )
                } else {
                    error()
                }
            }
            is Minus -> {
                next() // -
                factor()
                stackMachine.dNeg()
            }
            is Plus -> {
                next() // +
                factor()
                // Nothing to do with the stack machine since +a == a
            }
            else -> error()
        }
    }

    private tailrec fun factors() {
        val token = lexer.lastToken
        when(token) {
            is Mul -> {
                next() // *
                factor()
                stackMachine.dMul()
                factors()
            }
            is Div -> {
                next() // /
                factor()
                stackMachine.dDiv()
                factors()
            }
            is EOF, is RightParenthesis, is Plus, is Minus -> { /* no op, empty rule */ }
            else -> error()
        }
    }
}
