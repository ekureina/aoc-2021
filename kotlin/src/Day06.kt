import java.lang.Integer.parseInt

fun main() {
    val part1Iterations = 80
    val part2Iterations = 256

    data class LanternFish(val timer: Int)

    fun updateLanternFish(fish: List<LanternFish>): List<LanternFish> {
        val oldFish = fish.map { singleFish ->
            if (singleFish.timer == 0) {
                LanternFish(6)
            } else {
                LanternFish(singleFish.timer - 1)
            }
        }
        val newFish = (0 until fish.count { singleFish -> singleFish.timer == 0 }).map {
            LanternFish(8)
        }

        return listOf(oldFish, newFish).flatten()
    }

    fun part1(input: List<String>): Int {
        val lanternFish = input.first().split(",").map(::parseInt).map(::LanternFish)
        return (0 until part1Iterations).fold(lanternFish) { fish, _ ->
            updateLanternFish(fish)
        }.size
    }

    fun part2(input: List<String>): Long {
        val lanternFish = input.first().split(",").map(::parseInt).map(::LanternFish)

        var timerMap = lanternFish.associate { fish ->
            fish.timer to lanternFish.count { it == fish }.toLong()
        }.toMutableMap()

        (0 until part2Iterations).forEach { _ ->
            val newCounts = timerMap.keys.flatMap { count ->
                if (count == 0) {
                    listOf(8 to timerMap[count]!!, 6 to timerMap[count]!!)
                } else {
                    listOf((count - 1) to timerMap[count]!!)
                }
            }
            val combinedCount = newCounts.filter { count -> count.first == 6 }.sumOf { count -> count.second }
            timerMap = newCounts.associate { it }.toMutableMap()
            if (combinedCount > 0) {
                timerMap[6] = combinedCount
            }
        }
        return timerMap.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934) { "${part1(testInput)}" }
    check(part2(testInput) == 26984457539L) { "${part2(testInput)}" }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}