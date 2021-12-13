import java.lang.Integer.parseInt
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.takeWhile { line -> line != "" }.map { line ->
            val (x, y) = line.split(",")
            parseInt(x) to parseInt(y)
        }

        val (type, value) = input
            .dropWhile { line -> line != "" }
            .drop(1)
            .first()
            .split(" ")
            .last()
            .split("=")
            .let { (type, value) -> type to parseInt(value) }
        return pairs.map { point ->
            when (type) {
                "x" -> {
                    (value - abs(value - point.first)) to point.second
                }
                "y" -> {
                    point.first to (value - abs(value - point.second))
                }
                else -> {
                    throw IllegalStateException("Wrong type of fold: $type")
                }
            }
        }.toSet().size
    }

    fun part2(input: List<String>) {
        val pairs = input.takeWhile { line -> line != "" }.map { line ->
            val (x, y) = line.split(",")
            parseInt(x) to parseInt(y)
        }

        val instructions = input
            .dropWhile { line -> line != "" }
            .drop(1)
            .map { instructionLine ->
                instructionLine
                    .split(" ")
                    .last()
                    .split("=")
                    .let { (type, value) -> type to parseInt(value) }
            }
        val points = instructions.fold(pairs.toSet()) { pairs, instruction ->
            pairs.map { point ->
                when (instruction.first) {
                    "x" -> {
                        (instruction.second - abs(instruction.second - point.first)) to point.second
                    }
                    "y" -> {
                        point.first to (instruction.second - abs(instruction.second - point.second))
                    }
                    else -> {
                        throw IllegalStateException("Wrong type of fold: ${instruction.first}")
                    }
                }
            }.toSet()
        }
        val dimensions = points.fold(Pair(0, 0)) { dimensions, point ->
            Pair(maxOf(dimensions.first, point.first), maxOf(dimensions.second, point.second))
        }

        (0..dimensions.second).forEach { yCoord ->
            (0..dimensions.first).forEach { xCoord ->
                if (points.contains(Pair(xCoord, yCoord))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17) { "${part1(testInput)}" }
    part2(testInput)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}
