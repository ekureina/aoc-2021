import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::parseInt).zipWithNext().count { (first, second) ->
            first < second
        }
    }

    fun part2(input: List<String>): Int {
        return input.map(::parseInt).windowed(3, 1) { window ->
            window.sum()
        }.zipWithNext().count { (firstSum, secondSum) ->
            firstSum < secondSum
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
