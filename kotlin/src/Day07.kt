import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val positions = input.first().split(",").map(::parseInt)
        val maxPosition = positions.maxOrNull()!!
        return (0..maxPosition).fold(positions.size * maxPosition) { latestMax, finalPosition ->
            val potentialMax = positions.sumOf { position -> abs(finalPosition - position) }
            if (potentialMax < latestMax) {
                potentialMax
            } else {
                latestMax
            }
        }

    }

    fun part2(input: List<String>): Long {
        val positions = input.first().split(",").map(::parseLong)
        val maxPosition = positions.maxOrNull()!!

        return (0..maxPosition).fold(positions.size * maxPosition * maxPosition) { latestMax, finalPosition ->
            val potentialMax = positions.sumOf { position ->
                val distance = abs(finalPosition - position)
                distance * (distance + 1) / 2
            }

            if (potentialMax < latestMax) {
                potentialMax
            } else {
                latestMax
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37) { "${part1(testInput)}" }
    check(part2(testInput) == 168L) { "${part2(testInput)}" }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}