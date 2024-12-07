package model

import self.adragon.model.Token
import self.adragon.model.TokenType


class Lexer {
    val numbers = ('0'..'9')

    fun tokenize(expr: String): List<Token> {
        val tokens = mutableListOf<Token>()

        var i = 0
        while (i < expr.length) {

            val (token, tokenSize) = nextToken(expr, i)
            tokens.add(token)

            i += tokenSize
        }
        return tokens
    }

    private fun nextToken(expr: String, i: Int): Pair<Token, Int> {
        val ch = expr[i]
        var tokenSize = 1
        val token = when (ch) {
            '+' -> Token(0f, TokenType.PLUS)
            '-' -> Token(0f, TokenType.MINUS)
            '*' -> Token(0f, TokenType.TIMES)
            '/' -> Token(0f, TokenType.DIVIDE)
            '(' -> Token(0f, TokenType.OPEN_PAREN)
            ')' -> Token(0f, TokenType.CLOSE_PAREN)
            '^' -> Token(0f, TokenType.POWER)
            ' '-> Token(0f, TokenType.SPACE)

            in numbers -> {
                val j = consumeNumber(expr, i)
                val number = expr.slice(i..(i + j - 1)).toFloat()
                tokenSize = j - 1 + 1

                Token(number, TokenType.NUMBER)
            }

            else -> Token(-1f, TokenType.UNKNOWN)
        }

        return token to tokenSize
    }

    private fun consumeNumber(expr: String, i: Int): Int {
        var j = i + 1
        while (j < expr.length && expr[j] in numbers + '.')
            j++

        return j - i
    }
}