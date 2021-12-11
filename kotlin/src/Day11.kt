import java.lang.Integer.parseInt

fun main() {
    val part1Iterations = 100

    fun surroundingCells(line: Int, row: Int, constraints: Pair<Int, Int>): Collection<Pair<Int, Int>> {
        return ((line-1).coerceAtLeast(0)..(line+1).coerceAtMost(constraints.first)).flatMap { lineNum ->
            ((row-1).coerceAtLeast(0)..(row+1).coerceAtMost(constraints.second)).map { rowNum ->
                lineNum to rowNum
            }
        }
    }

    fun flash(
        octopi: List<MutableList<Int>>,
        lineIdex: Int,
        rowIdex: Int,
        flashed: MutableSet<Pair<Int, Int>>
    ) {
        val toVisit = surroundingCells(lineIdex, rowIdex, (octopi.size - 1) to (octopi[0].size - 1))
            .toMutableList()

        while (toVisit.isNotEmpty()) {
            val visit = toVisit.removeFirst()
            octopi[visit.first][visit.second]++
            if (octopi[visit.first][visit.second] > 9 && !flashed.contains(visit)) {
                flashed.add(visit)
                toVisit.addAll(surroundingCells(visit.first, visit.second, (octopi.size - 1) to (octopi[0].size - 1)))
            }
        }
    }

    fun part1(input: List<String>): Int {
        val octopi = input.map { line ->
            line.map(Char::toString).map(::parseInt).toMutableList()
        }

        return (0 until part1Iterations).sumOf {
            val flashed = mutableSetOf<Pair<Int, Int>>()
            octopi.indices.forEach { lineNumber ->
                octopi[lineNumber].indices.forEach { octopusNumber ->
                    octopi[lineNumber][octopusNumber]++
                    val octopusEnergy = octopi[lineNumber][octopusNumber]
                    if (octopusEnergy > 9) {
                        if (!flashed.contains(lineNumber to octopusNumber)) {
                            flashed.add(lineNumber to octopusNumber)
                            flash(octopi, lineNumber, octopusNumber, flashed)
                        }
                    }
                }
            }

            octopi.indices.sumOf { lineNumber ->
                octopi[lineNumber].indices.sumOf { octopusNumber ->
                    if (octopi[lineNumber][octopusNumber] > 9) {
                        octopi[lineNumber][octopusNumber] = 0
                        1.toInt()
                    } else {
                        0
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val octopi = input.map { line ->
            line.map(Char::toString).map(::parseInt).toMutableList()
        }

        return generateSequence(1) { it + 1 }.first {
            val flashed = mutableSetOf<Pair<Int, Int>>()
            octopi.indices.forEach { lineNumber ->
                octopi[lineNumber].indices.forEach { octopusNumber ->
                    octopi[lineNumber][octopusNumber]++
                    val octopusEnergy = octopi[lineNumber][octopusNumber]
                    if (octopusEnergy > 9) {
                        if (!flashed.contains(lineNumber to octopusNumber)) {
                            flashed.add(lineNumber to octopusNumber)
                            flash(octopi, lineNumber, octopusNumber, flashed)
                        }
                    }
                }
            }

            val flashes = octopi.indices.sumOf { lineNumber ->
                octopi[lineNumber].indices.sumOf { octopusNumber ->
                    if (octopi[lineNumber][octopusNumber] > 9) {
                        octopi[lineNumber][octopusNumber] = 0
                        1
                    } else {
                        0.toInt()
                    }
                }
            }

            flashes == octopi.size * octopi[0].size
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656) { "${part1(testInput)}" }
    check(part2(testInput) == 195) { "${part2(testInput)}" }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}