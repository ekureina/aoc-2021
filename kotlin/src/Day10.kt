import java.lang.IllegalStateException
import java.lang.Integer.parseInt
import java.util.*

fun main() {
    val illegalCharMap = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val stack = Stack<Char>()
            for (char in line) {
                if (char !in illegalCharMap.keys) {
                    stack.push(char)
                } else {
                    when (stack.peek()) {
                        '(' -> {
                            if (char == ')') {
                                stack.pop()
                            } else {
                                return@sumOf illegalCharMap[char] ?: 0
                            }
                        }
                        '{' -> {
                            if (char == '}') {
                                stack.pop()
                            } else {
                                return@sumOf illegalCharMap[char] ?: 0
                            }
                        }
                        '[' -> {
                            if (char == ']') {
                                stack.pop()
                            } else {
                                return@sumOf illegalCharMap[char] ?: 0
                            }
                        }
                        '<' -> {
                            if (char == '>') {
                                stack.pop()
                            } else {
                                return@sumOf illegalCharMap[char] ?: 0
                            }
                        }
                    }
                }
            }
            0
        }
    }

    fun part2(input: List<String>): Long {
        val scores = input.map { line ->
            val stack = Stack<Char>()
            var score = 0L
            for (char in line) {
                if (char !in illegalCharMap.keys) {
                    stack.push(char)
                } else {
                    when (stack.peek()) {
                        '(' -> {
                            if (char == ')') {
                                stack.pop()
                            } else {
                                return@map -1L
                            }
                        }
                        '{' -> {
                            if (char == '}') {
                                stack.pop()
                            } else {
                                return@map -1L
                            }
                        }
                        '[' -> {
                            if (char == ']') {
                                stack.pop()
                            } else {
                                return@map -1L
                            }
                        }
                        '<' -> {
                            if (char == '>') {
                                stack.pop()
                            } else {
                                return@map -1L
                            }
                        }
                    }
                }
            }

            while (stack.isNotEmpty()) {
                score *= 5
                when (stack.pop()) {
                    '(' -> {
                        score += 1
                    }
                    '[' -> {
                        score += 2
                    }
                    '{' -> {
                        score += 3
                    }
                    '<' -> {
                        score += 4
                    }
                }
            }
            score
        }.filter { it != -1L }.sorted()
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397) { "${part1(testInput)}" }
    check(part2(testInput) == 288957L) { "${part2(testInput)}" }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}