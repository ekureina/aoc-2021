import java.lang.Long.parseLong

data class BingoSquare(val number: Long, val isPicked:Boolean = false)

data class BingoBoard(val numbers: List<List<BingoSquare>>) {
    fun markNumber(number:Long): BingoBoard {
        return BingoBoard(numbers.map { row ->
            row.map { square ->
                if (square.number == number) {
                    BingoSquare(square.number, true)
                } else {
                    BingoSquare(square.number, square.isPicked)
                }
            }
        })
    }

    fun score(finalPick: Long): Long {
        return numbers
            .flatten()
            .filterNot(BingoSquare::isPicked)
            .map(BingoSquare::number)
            .sum() * finalPick
    }
}

data class BingoGame(val numberPicks: List<Long>, var boards: List<BingoBoard>)

fun parseBingo(input: List<String>): BingoGame {
    val picks = input.first()
    val numberPicks = picks.split(",").map(::parseLong)
    val boards = input.drop(2).chunked(6).map(::parseBingoBoard)
    return BingoGame(numberPicks, boards)
}

fun parseBingoBoard(input: List<String>): BingoBoard {
    return BingoBoard(input.filter { it != "" }.map { bingoLine ->
        bingoLine.split(" ").filter { it != "" }.map(::parseLong).map(::BingoSquare)
    })
}

fun winningBingo(board: BingoBoard): Boolean {
    val rowWin = board.numbers.any { row ->
        row.all(BingoSquare::isPicked)
    }
    if (rowWin) {
        return true
    }

    return board.numbers.indices.asSequence().map { index ->
        val column = board.numbers.map { row ->
            row[index]
        }
        column.all(BingoSquare::isPicked)
    }.any { it }
}

fun main() {
    fun part1(input: List<String>): Long {
        val game = parseBingo(input)
        game.numberPicks.forEach { pick ->
            game.boards = game.boards.map { board ->
                board.markNumber(pick)
            }
            val score = game.boards.firstOrNull(::winningBingo)?.score(pick)
            if (score != null ) {
                return score
            }
        }
        return -1
    }

    fun part2(input: List<String>): Long {
        val game = parseBingo(input)
        game.numberPicks.forEach { pick ->
            val boards = game.boards.map { board ->
                board.markNumber(pick)
            }

            if (boards.filterNot(::winningBingo).isEmpty()) {
                return boards.firstOrNull(::winningBingo)!!.score(pick)
            } else {
                game.boards = boards.filterNot(::winningBingo)
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512L)
    val output = part2(testInput)
    check(output == 1924L) { "$output" }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}