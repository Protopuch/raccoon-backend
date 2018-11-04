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
 *  <Factors> : *<Factor><Factors> | /<Factor><Factors>  | %<Factor><Factors> | empty
 *  <Factor>  : constant | (<Expr>) | -<Factor> | +<Factor>
 */
class SimpleParser(
    private val lexerFactory: LexerFactory = SimpleLexer.factory,
    private val stackMachine: StackMachine = SimpleStackMachine()
) {
    private lateinit var lexer: Lexer
    private lateinit var input: String

    private fun error(): Nothing = throw UnexpectedTokenException(input, lastToken.offset)
    private fun nextToken() = lexer.next()
    private val lastToken: Token
        get() = lexer.lastToken

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
        processExpr()
        processEof()
        return stackMachine.dPop()
    }

    private fun processEof() {
        if (lastToken !is EOF) {
            error()
        }
    }

    private fun processExpr() {
        processTerm()
        processTerms()
    }

    private fun processTerm() {
        processFactor()
        processFactors()
    }

    private tailrec fun processTerms() {
        when(lastToken) {
            is Plus -> {
                nextToken()
                processTerm()
                stackMachine.dAdd()
                processTerms()
            }
            is Minus -> {
                nextToken()
                processTerm()
                stackMachine.dSub()
                processTerms()
            }
            is EOF, is RightParenthesis -> { /* no op, empty rule */ }
            else -> error()
        }
    }


    private fun processFactor() {
        val token = lastToken // Read into a local variable to allow smart casting.
        when (token) {
            is Constant -> {
                stackMachine.dPush(token.value)
                nextToken()
            }
            is LeftParenthesis -> {
                nextToken()
                processExpr()
                if (lastToken is RightParenthesis) {
                    nextToken()
                } else {
                    error()
                }
            }
            is Minus -> {
                nextToken()
                processFactor()
                stackMachine.dNeg()
            }
            is Plus -> {
                nextToken()
                processFactor()
                // Nothing to do with the stack machine since +a == a.
            }
            else -> error()
        }
    }

    private tailrec fun processFactors() {
        when(lastToken) {
            is Mul -> {
                nextToken()
                processFactor()
                stackMachine.dMul()
                processFactors()
            }
            is Div -> {
                nextToken()
                processFactor()
                stackMachine.dDiv()
                processFactors()
            }
            is Mod -> {
                nextToken()
                processFactor()
                stackMachine.dMod()
                processFactors()
            }
            is EOF, is RightParenthesis, is Plus, is Minus -> { /* no op, empty rule */ }
            else -> error()
        }
    }
}
