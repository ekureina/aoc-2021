import java.lang.IllegalStateException

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.split("|").last().trim().split(" ").count { output ->
                val segments = output.length
                output.length == 2 || output.length == 3 || output.length == 4 || output.length == 7
            }
        }
    }

    fun part2(label: String, input: List<String>): Int {
        return input.sumOf { line ->
            val (signals, entries) = line.split("|")
            val options = signals.trim().split(" ").map { sequence ->
                when (sequence.length) {
                    2 -> {
                        setOf(1) to sequence.toSortedSet()
                    }
                    3 -> {
                        setOf(7) to sequence.toSortedSet()
                    }
                    4 -> {
                        setOf(4) to sequence.toSortedSet()
                    }
                    5 -> {
                        setOf(2, 3, 5) to sequence.toSortedSet()
                    }
                    6 -> {
                        setOf(0, 6, 9) to sequence.toSortedSet()
                    }
                    7 -> {
                        setOf(8) to sequence.toSortedSet()
                    }
                    else -> throw IllegalStateException("Bad State! $sequence; ${sequence.length}")
                }
            }

            val onOnes = options.first { it.first == setOf(1) }
            val onSevens = options.first { it.first == setOf(7) }
            val onFours = options.first { it.first == setOf(4) }
            val onEights = options.first { it.first == setOf(8) }
            val onNines = options.filter { it.first == setOf(0, 6, 9) }.first {
                it.second.count { on ->
                    onFours.second.contains(on)
                } == 4
            }

            val onZeros = options
                .filter { it.first == setOf(0, 6, 9) && it.second != onNines.second }
                .first { it.second.count { on ->
                    onOnes.second.contains(on)
                } == 2 }

            val onSixes = options.first { it.first == setOf(0, 6, 9) && it.second != onZeros.second && it.second != onNines.second }
            val onThrees = options
                .filter { it.first == setOf(2, 3, 5) }
                .first { it.second.count { on ->
                    onOnes.second.contains(on)
                } == 2 }

            val onFives = options
                .filter { it.first == setOf(2, 3, 5) && it.second != onThrees.second }
                .first { it.second.count { on ->
                    onFours.second.contains(on)
                } == 3 }

            val onTwos = options
                .first { it.first == setOf(2, 3, 5) && it.second != onThrees.second && it.second != onFives.second }

            val mapping = mapOf(
                onZeros.second to 0,
                onOnes.second to 1,
                onTwos.second to 2,
                onThrees.second to 3,
                onFours.second to 4,
                onFives.second to 5,
                onSixes.second to 6,
                onSevens.second to 7,
                onEights.second to 8,
                onNines.second to 9
            )

            entries.trim().split(" ").map { entry ->
                mapping[entry.toSortedSet()]!!
            }.fold(0) { current, digit ->
                current * 10 + digit
            }.toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26) { "${part1(testInput)}" }
    val miniFail = "miniFail"
    check(part2("micro", listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")) == 5353)
    check(part2("mini", testInput) == 61229 ) { "${part2(miniFail, testInput)}" }

    val input = readInput("Day08")
    println(part1(input))
    println(part2("mega", input))
}