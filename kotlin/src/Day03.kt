import java.lang.Long.parseLong

data class BitCount(var ones: Long, var zeros: Long)

fun main() {
    fun part1(input: List<String>): Long {
        val initialCounts = input.first().map { bit ->
            if (bit == '0') {
                BitCount(0L, 1L)
            } else {
                BitCount(1L, 0L)
            }
        }
        val (gamma, epsilon) = input.drop(0).fold(initialCounts) { bitCounts, bitString ->
            bitCounts.zip(bitString.toList()).map { (count, bit) ->
                if (bit == '0') {
                    BitCount(count.ones, count.zeros + 1)
                } else {
                    BitCount(count.ones + 1, count.zeros)
                }
            }
        }.fold(0L to 0L) { (gamma, epsilon), count ->
            if (count.ones > count.zeros) {
                ((gamma shl 1) + 1) to (epsilon shl 1)
            } else {
                (gamma shl 1) to ((epsilon shl 1) + 1)
            }
        }
        return gamma * epsilon
    }

    fun part2(input: List<String>): Long {
        val oxygen = input[0].indices.fold(input) { filteredInput, index ->
            if (filteredInput.size == 1) {
                return@fold filteredInput
            }

            val frequency = filteredInput.fold(BitCount(0L, 0L)) { bitCount, bitString ->
                if (bitString[index] == '1') {
                    BitCount(bitCount.ones + 1, bitCount.zeros)
                } else {
                    BitCount(bitCount.ones, bitCount.zeros + 1)
                }
            }
            filteredInput.filter { bitNum ->
                parseLong(bitNum, 2)
                if (frequency.ones >= frequency.zeros) {
                    bitNum[index] == '1'
                } else {
                    bitNum[index] == '0'
                }
            }
        }.first()

        val co2 = input[0].indices.fold(input) { filteredInput, index ->
            if (filteredInput.size == 1) {
                return@fold filteredInput
            }

            val frequency = filteredInput.fold(BitCount(0L, 0L)) { bitCount, bitString ->
                if (bitString[index] == '1') {
                    BitCount(bitCount.ones + 1, bitCount.zeros)
                } else {
                    BitCount(bitCount.ones, bitCount.zeros + 1)
                }
            }
            filteredInput.filter { bitNum ->
                parseLong(bitNum, 2)
                if (frequency.ones >= frequency.zeros) {
                    bitNum[index] == '0'
                } else {
                    bitNum[index] == '1'
                }
            }
        }.first()

        return parseLong(oxygen, 2) * parseLong(co2, 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198L)
    check(part2(testInput) == 230L)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}