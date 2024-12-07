package self.adragon

import model.Lexer
import self.adragon.model.RPN
import self.adragon.model.Token
import self.adragon.model.TokenType


fun main() {
    println("Enter mathematical expression:")
    val expr = readln()//"7 - 5 * 2 ^ 4 + 10 / 10 * 5"
    println("\n")

    val tokens = Lexer().tokenize(expr)
    val rpn = RPN().constructRPN(tokens)
    val result = RPN().evalRPN(rpn)

    val tokensStr = getTokensAsString(tokens)
    val rpnStr = getRPNAsString(rpn)
    println("Your input: $tokensStr")
    println("RPN representation: $rpnStr")
    println("Result: $result")
}


fun getRPNAsString(rpn: ArrayDeque<Token>): String {
    var s = ""
    rpn.forEach {
        val value = getTokenString(it)
        s += "$value "
    }

    return s
}

fun getTokensAsString(tokens: List<Token>) =
    tokens.filter { it.type != TokenType.SPACE }.map { getTokenString(it) }.joinToString(" ")

private fun getTokenString(token: Token) = when (token.type) {
    TokenType.NUMBER -> {
        val splitted = token.value.toString().split('.')
        if (splitted[1].any { it != '0' }) token.value else splitted[0]
    }

    TokenType.PLUS -> "+"
    TokenType.MINUS -> "-"
    TokenType.TIMES -> "*"
    TokenType.DIVIDE -> "/"
    TokenType.OPEN_PAREN -> "("
    TokenType.CLOSE_PAREN -> ")"
    TokenType.POWER -> "^"
    else -> "?"
}