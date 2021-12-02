import java.lang.IllegalArgumentException
import java.lang.Long.parseLong

data class PositionWithAim(val position: Long, val depth: Long, val aim: Long)
data class Position(val position: Long, val depth: Long)

sealed class Command(protected val magnitude: Long) {
    abstract fun move(position: Position): Position
    abstract fun moveAimed(positionWithAim: PositionWithAim): PositionWithAim
}

class Forward(magnitude: Long): Command(magnitude) {
    override fun move(position: Position): Position {
        return Position(position.position + magnitude, position.depth)
    }

    override fun moveAimed(positionWithAim: PositionWithAim): PositionWithAim {
        return PositionWithAim(
            positionWithAim.position + magnitude,
            positionWithAim.depth + (positionWithAim.aim * magnitude),
            positionWithAim.aim
        )
    }
}

class Down(magnitude: Long): Command(magnitude) {
    override fun move(position: Position): Position {
        return Position(position.position, position.depth + magnitude)
    }

    override fun moveAimed(positionWithAim: PositionWithAim): PositionWithAim {
        return PositionWithAim(positionWithAim.position, positionWithAim.depth, positionWithAim.aim + magnitude)
    }
}

class Up(magnitude: Long): Command(magnitude) {
    override fun move(position: Position): Position {
        return Position(position.position, position.depth - magnitude)
    }

    override fun moveAimed(positionWithAim: PositionWithAim): PositionWithAim {
        return PositionWithAim(positionWithAim.position, positionWithAim.depth, positionWithAim.aim - magnitude)
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val finalPosition = input.map { line ->
            val command = line.split(" ")
            when (command.first()) {
                "forward" -> Forward(parseLong(command[1]))
                "down" -> Down(parseLong(command[1]))
                "up" -> Up(parseLong(command[1]))
                else -> throw IllegalArgumentException("$command")
            }
        }.fold(Position(0L, 0L)) { position, command ->
            command.move(position)
        }
        return finalPosition.position * finalPosition.depth
    }

    fun part2(input: List<String>): Long {
        val finalPosition = input.map { line ->
            val command = line.split(" ")
            when (command.first()) {
                "forward" -> Forward(parseLong(command[1]))
                "down" -> Down(parseLong(command[1]))
                "up" -> Up(parseLong(command[1]))
                else -> throw IllegalArgumentException("$command")
            }
        }.fold(PositionWithAim(0L, 0L, 0L)) { position, command ->
            command.moveAimed(position)
        }
        return finalPosition.position * finalPosition.depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150L)
    check(part2(testInput) == 900L)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}