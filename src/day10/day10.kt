package day10

import Day
import java.util.*

fun day10(): Day<*, *> {
    val closingChars = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    fun part1(input: List<String>): Int {
        val pointsByChar = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )

        fun lineScore(line: String): Int {
            val stack = Stack<Char>()

            for (c in line) {
                if (closingChars.keys.contains(c)) {
                    stack.push(c)
                } else if (closingChars.values.contains(c)) {
                    val lastOpen = stack.pop()
                    if (closingChars[lastOpen] != c) {
                        return pointsByChar[c]!!
                    }
                } else {
                    throw IllegalStateException("Invalid state: $c")
                }
            }
            return 0
        }

        return input.map(::lineScore).sum()
    }

    fun part2(input: List<String>): Long {
        val pointsByChar = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4,
        )

        fun lineScore(line: String): Long {
            val stack = Stack<Char>()

            for (c in line) {
                if (closingChars.keys.contains(c)) {
                    stack.push(c)
                } else if (closingChars.values.contains(c)) {
                    val lastOpen = stack.pop()
                    if (closingChars[lastOpen] != c) {
                        return 0
                    }
                } else {
                    throw IllegalStateException("Invalid state: $c")
                }
            }

            var score = 0L
            while (stack.isNotEmpty()) {
                val c = stack.pop()
                val needed = closingChars[c]
                score *= 5
                score += pointsByChar[needed]!!
            }
            return score
        }

        return input.map(::lineScore)
            .filter { it > 0 }
            .also { if (it.size % 2 == 0) throw IllegalStateException("Something's wrong") }
            .sorted()
            .let {
                it.get(it.size / 2)
            }
    }

    return Day(10, 26397, ::part1, 288957, ::part2)
}
