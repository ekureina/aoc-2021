import java.lang.Integer.parseInt
import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val riskLevels = input.map { line ->
            line.map(Char::toString).map(::parseInt)
        }

        val totalRisks = Array(riskLevels.size) { Array(riskLevels[0].size) { 0 } }
        (riskLevels.indices).forEach { row ->
            riskLevels[0].indices.forEach { column ->
                if (row != 0 || column != 0) {
                    val backRow = (row - 1).coerceAtLeast(0)
                    val backColumn = (column - 1).coerceAtLeast(0)
                    if (row == 0) {
                        totalRisks[row][column] = totalRisks[row][backColumn] + riskLevels[row][column]
                    } else if (column == 0) {
                        totalRisks[row][column] = totalRisks[backRow][column] + riskLevels[row][column]
                    } else {
                        totalRisks[row][column] =
                            minOf(totalRisks[backRow][column], totalRisks[row][backColumn]) + riskLevels[row][column]
                    }
                }
            }
        }

        return totalRisks[riskLevels.size - 1][riskLevels[0].size - 1]
    }

    fun part2(input: List<String>): Int {
        val riskLevelsSmall = input.map { line ->
            line.map(Char::toString).map(::parseInt)
        }

        val riskLevels = Array(riskLevelsSmall.size * 5) { Array(riskLevelsSmall[0].size * 5) { 0 } }
        riskLevels.indices.forEach { row ->
            riskLevels[0].indices.forEach { column ->
                val tileIndex = row / riskLevelsSmall.size to column / riskLevelsSmall[0].size
                val cell =
                    riskLevelsSmall[row % riskLevelsSmall.size][column % riskLevelsSmall[0].size] + tileIndex.first + tileIndex.second
                if (cell <= 9) {
                    riskLevels[row][column] = cell
                } else {
                    riskLevels[row][column] = if (cell % 9 == 0) {
                        9
                    } else {
                        cell % 9
                    }
                }
            }
        }

        val distances = Array(riskLevels.size) { Array(riskLevels[0].size) { Int.MAX_VALUE } }
        val queue = PriorityQueue<Pair<Int, Pair<Int, Int>>> { left, right -> left.first.compareTo(right.first) }
        queue.addAll(listOf(riskLevels[1][0] to Pair(1, 0), riskLevels[0][1] to Pair(0, 1)))
        while (distances[riskLevels.size - 1][riskLevels[0].size - 1] == Int.MAX_VALUE && queue.isNotEmpty()) {
            val (distance, nextPoint) = queue.remove()
            if (distance < distances[nextPoint.first][nextPoint.second]) {
                distances[nextPoint.first][nextPoint.second] = distance
                setOf(
                    Pair(nextPoint.first - 1, nextPoint.second),
                    Pair(nextPoint.first + 1, nextPoint.second),
                    Pair(nextPoint.first, nextPoint.second - 1),
                    Pair(nextPoint.first, nextPoint.second + 1)
                ).filter { it.first in riskLevels.indices && it.second in riskLevels[0].indices }.forEach { point ->
                    queue.add((distance + riskLevels[point.first][point.second]) to point)
                }
            }
        }

        return distances[riskLevels.size - 1][riskLevels[0].size - 1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40) { "${part1(testInput)}" }
    check(part2(testInput) == 315) { "${part2(testInput)}" }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
