fun main() {
    fun part1(input: List<String>): Int {
        val template = input.first()
        val rules = input.drop(2).associate { line ->
            val (pair, insert) = line.split(" -> ")
            (pair[0] to pair[1]) to insert[0]
        }

        val finalString = (0 until 10).fold(template) { polymer, _ ->
            val insertions = polymer.zipWithNext().map { pair ->
                rules[pair]!!
            }.joinToString("")

            polymer.zip(insertions).flatMap { (polymer, inserted) ->
                listOf(polymer, inserted)
            }.joinToString("") + polymer.last()
        }
        val charCounts = finalString.toSet().associateWith { char -> finalString.count(char::equals) }

        return charCounts.values.maxOrNull()!! - charCounts.values.minOrNull()!!
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val rules = input.drop(2).associate { line ->
            val (pair, insert) = line.split(" -> ")
            (pair[0] to pair[1]) to ((pair[0] to insert[0]) to (insert[0] to pair[1]))
        }

        val pairs = rules.keys.associateWith { pair -> template.zipWithNext().count { it == pair }.toLong() }
        val finalPairs = (0 until 40).fold(pairs) { pairs, _ ->
            val newPairs = pairs.keys.flatMap { pair ->
                listOf(rules[pair]!!.first to pairs[pair]!!, rules[pair]!!.second to pairs[pair]!!)
            }

            newPairs.toSet()
                .map(Pair<Pair<Char, Char>, Long>::first)
                .associateWith { pair -> newPairs.filter { (key, _) -> key == pair }.sumOf { it.second } }
        }

        val charCounts = rules.keys.map(Pair<Char, Char>::first).toSet().associateWith { char ->
            finalPairs.filter { (pair, count) ->
                pair.first == char
            }.values.sum() + if (char == template.last()) { 1 } else { 0 }
        }

        return charCounts.values.maxOrNull()!! - charCounts.values.minOrNull()!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588) { "${part1(testInput)}" }
    check(part2(testInput) == 2188189693529L) { "${part2(testInput)}"}

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
