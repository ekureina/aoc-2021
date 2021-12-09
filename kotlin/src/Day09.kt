import java.lang.IllegalStateException
import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<String>): Int {
        val heightMap = input.map { line ->
            line.map(Char::toString).map(::parseInt)
        }
        return heightMap.flatMapIndexed { lineIndex, line ->
            line.filterIndexed { rowIndex, height ->
                when (lineIndex) {
                    0 -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[1][0] &&
                                        height < heightMap[0][1]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[1][rowIndex] &&
                                        height < heightMap[0][rowIndex - 1]
                            }
                            else -> {
                                height < heightMap[1][rowIndex] &&
                                        height < heightMap[0][rowIndex - 1] &&
                                        height < heightMap[0][rowIndex + 1]
                            }
                        }
                    }
                    heightMap.size - 1 -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[lineIndex - 1][0] &&
                                        height < heightMap[lineIndex][1]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1]
                            }
                            else -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex][rowIndex + 1]
                            }
                        }
                    }
                    else -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[lineIndex - 1][0] &&
                                        height < heightMap[lineIndex][1] &&
                                        height < heightMap[lineIndex + 1][0]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex + 1][rowIndex]
                            }
                            else -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex][rowIndex + 1] &&
                                        height < heightMap[lineIndex + 1][rowIndex]
                            }
                        }
                    }
                }
            }
        }.sumOf { height -> height + 1 }
    }

    fun part2(input: List<String>): Int {
        val heightMap = input.map { line ->
            line.map(Char::toString).map(::parseInt)
        }

        val lowPointIndexes = heightMap.flatMapIndexed { index, line ->
            line.mapIndexed { rowIndex, height ->
                index to rowIndex to height
            }.filter { (indexes, height) ->
                val rowIndex = indexes.second
                when (val lineIndex = indexes.first) {
                    0 -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[1][0] &&
                                        height < heightMap[0][1]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[1][rowIndex] &&
                                        height < heightMap[0][rowIndex - 1]
                            }
                            else -> {
                                height < heightMap[1][rowIndex] &&
                                        height < heightMap[0][rowIndex - 1] &&
                                        height < heightMap[0][rowIndex + 1]
                            }
                        }
                    }
                    heightMap.size - 1 -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[lineIndex - 1][0] &&
                                        height < heightMap[lineIndex][1]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1]
                            }
                            else -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex][rowIndex + 1]
                            }
                        }
                    }
                    else -> {
                        when (rowIndex) {
                            0 -> {
                                height < heightMap[lineIndex - 1][0] &&
                                        height < heightMap[lineIndex][1] &&
                                        height < heightMap[lineIndex + 1][0]
                            }
                            heightMap[0].size - 1 -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex + 1][rowIndex]
                            }
                            else -> {
                                height < heightMap[lineIndex - 1][rowIndex] &&
                                        height < heightMap[lineIndex][rowIndex - 1] &&
                                        height < heightMap[lineIndex][rowIndex + 1] &&
                                        height < heightMap[lineIndex + 1][rowIndex]
                            }
                        }
                    }
                }
            }.map { (indexes, _) ->
                indexes
            }
        }

        val largeBasinSizes = lowPointIndexes.map { lowPosition ->
            var basinSize = 0

            val visited = Array(heightMap.size) { Array(heightMap[0].size) { false } }
            val stack = arrayListOf(lowPosition)

            while (stack.isNotEmpty()) {
                val nextPosition = stack.removeAt(stack.size - 1)
                if (nextPosition.first in visited.indices && nextPosition.second in visited[0].indices) {
                    if (!visited[nextPosition.first][nextPosition.second]) {
                        visited[nextPosition.first][nextPosition.second] = true
                        if (heightMap[nextPosition.first][nextPosition.second] != 9) {
                            basinSize++
                            stack.addAll(
                                setOf(
                                    nextPosition.first to nextPosition.second - 1,
                                    nextPosition.first to nextPosition.second + 1,
                                    nextPosition.first + 1 to nextPosition.second,
                                    nextPosition.first - 1 to nextPosition.second
                                )
                            )
                        }
                    }
                }
            }

            basinSize
        }.sorted().reversed()

        return largeBasinSizes[0] * largeBasinSizes[1] * largeBasinSizes[2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15) { "${part1(testInput)}" }
    check(part2(testInput) == 1134) { "${part2(testInput)}" }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}