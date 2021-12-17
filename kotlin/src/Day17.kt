import java.lang.Integer.parseInt
import kotlin.math.abs

fun main() {
    fun updateVelocity(velocity: Pair<Int, Int>): Pair<Int, Int> {
        val xVelocityChange = if (velocity.first == 0) { 0 } else if (velocity.first > 0) { -1 } else { 1 }
        return (velocity.first + xVelocityChange) to (velocity.second - 1)
    }

    fun runFiring(initialVelocity: Pair<Int, Int>, xRange: IntRange, yRange: IntRange): Int? {
        var xPos = 0
        var yPos = 0
        var highestY = 0
        var velocity = initialVelocity
        while (xPos !in xRange || yPos !in yRange) {
            if (yPos < yRange.first) {
                return null
            }
            xPos += velocity.first
            yPos += velocity.second
            highestY = maxOf(highestY, yPos)
            velocity = updateVelocity(velocity)
        }
        return highestY
    }

    fun part1(input: List<String>): Int {
        val (xSpec, ySpec) = input[0].removePrefix("target area: x=").split(", y=")
        val xRange = xSpec.split("..").let { (low, high) -> parseInt(low)..parseInt(high) }
        val yRange = ySpec.split("..").let { (low, high) -> parseInt(low)..parseInt(high) }
        var highestY = 0
        (0..xRange.last).forEach { initialXVelocity ->
            (yRange.first..abs(yRange.first) * 3).forEach { initialYVelocity ->
                runFiring(initialXVelocity to initialYVelocity, xRange, yRange)?.also { foundMax ->
                    highestY = maxOf(highestY, foundMax)
                }
            }
        }

        return highestY
    }

    fun part2(input: List<String>): Int {
        val (xSpec, ySpec) = input[0].removePrefix("target area: x=").split(", y=")
        val xRange = xSpec.split("..").let { (low, high) -> parseInt(low)..parseInt(high) }
        val yRange = ySpec.split("..").let { (low, high) -> parseInt(low)..parseInt(high) }
        return (0..xRange.last).sumOf { initialXVelocity ->
            (yRange.first..abs(yRange.first) * 3).count { initialYVelocity ->
                runFiring(initialXVelocity to initialYVelocity, xRange, yRange) != null
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45) { "${part1(testInput)}" }
    check(part2(testInput) == 112) { "${part2(testInput)}" }

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
