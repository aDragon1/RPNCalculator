package self.adragon.model

import java.util.Stack
import kotlin.math.pow

class RPN {
    val priorityMap = mapOf(
        TokenType.POWER to 3,

        TokenType.TIMES to 2,
        TokenType.DIVIDE to 2,

        TokenType.PLUS to 1,
        TokenType.MINUS to 1,

        TokenType.NUMBER to 0,
    )
    val operations = listOf(TokenType.PLUS, TokenType.MINUS, TokenType.TIMES, TokenType.DIVIDE, TokenType.POWER)

    fun constructRPN(tokens: List<Token>): ArrayDeque<Token> {
        val stack = Stack<Token>()
        val queue = ArrayDeque<Token>()

        for (tok in tokens) {
            if (tok.type == TokenType.SPACE) continue

            when (tok.type) {
                TokenType.NUMBER -> queue.add(tok)
                in operations -> {
                    while (stack.isNotEmpty() && getPriority(stack.last()) >= getPriority(tok)) {
                        queue.add(stack.pop())
                    }

                    stack.push(tok)
                }

                TokenType.OPEN_PAREN -> stack.push(tok)
                TokenType.CLOSE_PAREN -> {
                    while (stack.isNotEmpty() && stack.peek().type != TokenType.OPEN_PAREN) {
                        queue.add(stack.pop())
                    }

                    if (stack.isEmpty() || stack.pop().type != TokenType.OPEN_PAREN)
                        error("Mismatched parentheses: missing open parenthesis")
                }

                else -> error("Unsupported token type: ${tok.type}")
            }
        }

        while (stack.isNotEmpty()) {
            val rem = stack.pop()
            if (rem.type == TokenType.OPEN_PAREN)
                error("Mismatched parentheses: missing close parenthesis")
            queue.add(rem)
        }
        return queue
    }

    fun evalRPN(rpn: ArrayDeque<Token>): Float {
        val stack = Stack<Float>()


        rpn.forEach {
            val curType = it.type
            when (curType) {
                TokenType.NUMBER -> stack.push(it.value)

                in operations -> {
                    val b = stack.pop()

                    val result = if (stack.isEmpty())
                        when (curType) {
                            TokenType.PLUS -> +b
                            TokenType.MINUS -> -b

                            else -> error("Evaluation error")
                        }
                    else {
                        val a = stack.pop()
                        when (curType) {
                            TokenType.PLUS -> a + b
                            TokenType.MINUS -> a - b
                            TokenType.TIMES -> a * b
                            TokenType.DIVIDE -> {
                                check(b != 0f) { "Division by zero. $a $curType $b" }
                                a / b
                            }

                            TokenType.POWER -> a.pow(b)

                            else -> error("Unsupported operation: $curType")
                        }
                    }

                    stack.push(result)
                }

                else -> error("Unsupported token type: $curType")
            }
        }
        val result = stack.pop()
        check(stack.isEmpty()) { "Missing operator after ${stack.pop()}" }
        return result
    }

    private fun getPriority(token: Token) = priorityMap.getOrDefault(token.type, -1)
}