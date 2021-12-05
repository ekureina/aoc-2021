import java.lang.Integer.parseInt
import kotlin.math.abs

enum class LineOrientation {
    VERTICAL,
    HORIZONTAL,
    DIAGONAL;
}

fun main() {
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point) {
        fun generateDiagonalPoints(start: Point, end: Point): List<Point> {
            val xSequence = if (start.x > end.x) {
                start.x downTo end.x
            } else {
                start.x..end.x
            }

            val ySequence = if (start.y > end.y) {
                start.y downTo end.y
            } else {
                start.y..end.y
            }

            return xSequence.zip(ySequence).map { (x, y) -> Point(x, y) }
        }

        fun orientation(): LineOrientation {
            return if (start.x == end.x) {
                LineOrientation.VERTICAL
            } else if (start.y == end.y) {
                LineOrientation.HORIZONTAL
            } else {
                LineOrientation.DIAGONAL
            }
        }

        fun slope(): Int {
            return (end.y - start.y)/(end.x - start.x)
        }

        fun minX(): Int = minOf(start.x, end.x)

        fun minY(): Int = minOf(start.y, end.y)

        fun maxX(): Int = maxOf(start.x, end.x)

        fun maxY(): Int = maxOf(start.y, end.y)

        private fun horizontalVerticalIntersect(horizontalLine: Line, verticalLine: Line): List<Point> {
            return if (verticalLine.start.x in (horizontalLine.minX()..horizontalLine.maxX()) &&
                horizontalLine.start.y in (verticalLine.minY()..verticalLine.maxY())) {
                listOf(Point(verticalLine.start.x, horizontalLine.start.y))
            } else {
                listOf()
            }
        }

        private fun verticalVerticalIntersect(verticalLine1: Line, verticalLine2: Line): List<Point> {
            return if (verticalLine1.start.x == verticalLine2.start.x) {
                val minIntersect = maxOf(verticalLine1.minY(), verticalLine2.minY())
                val maxIntersect = minOf(verticalLine1.maxY(), verticalLine2.maxY())
                (minIntersect..maxIntersect).map { y ->
                    Point(verticalLine1.start.x, y)
                }
            } else {
                listOf()
            }
        }

        private fun horizontalHorizontalIntersect(horizontalLine1: Line, horizontalLine2: Line): List<Point> {
            return if (horizontalLine1.start.y == horizontalLine2.start.y) {
                val minIntersect = maxOf(horizontalLine1.minX(), horizontalLine2.minX())
                val maxIntersect = minOf(horizontalLine1.maxX(), horizontalLine2.maxX())
                (minIntersect..maxIntersect).map { x ->
                    Point(x, horizontalLine1.start.y)
                }
            } else {
                listOf()
            }
        }

        private fun diagonalDiagonalIntersect(diagonalLine1: Line, diagonalLine2: Line): List<Point> {
            return if ((diagonalLine1.slope() > 0) xor (diagonalLine2.slope() > 0)) {
                if (diagonalLine1.start == diagonalLine2.start || diagonalLine1.start == diagonalLine2.end) {
                    listOf(diagonalLine1.start)
                } else if (diagonalLine1.end == diagonalLine2.start || diagonalLine1.end == diagonalLine2.start) {
                    listOf(diagonalLine1.end)
                } else {
                    generateDiagonalPoints(diagonalLine1.start, diagonalLine1.end)
                        .intersect(generateDiagonalPoints(diagonalLine2.start, diagonalLine2.end).toSet()).toList()
                }
            } else {
                // Same slope direction, can only intersect if they would be part of the same line.
                val calculatedDy = diagonalLine1.slope() * (diagonalLine1.end.x - diagonalLine2.end.x)
                if (calculatedDy == diagonalLine1.end.y - diagonalLine2.end.y) {
                    generateDiagonalPoints(diagonalLine1.start, diagonalLine1.end)
                        .intersect(generateDiagonalPoints(diagonalLine2.start, diagonalLine2.end).toSet())
                        .toList()
                } else {
                    listOf()
                }
            }
        }

        private fun diagonalVerticalIntersect(diagonalLine: Line, verticalLine: Line): List<Point> {
            return if (verticalLine.start.x in (diagonalLine.minX()..diagonalLine.maxX())) {
                val potentialPoint = if (diagonalLine.slope() > 0) {
                    Point(verticalLine.start.x, (verticalLine.start.x - diagonalLine.minX()) + diagonalLine.minY())
                } else {
                    Point(verticalLine.start.x, diagonalLine.maxY() - (verticalLine.start.x - diagonalLine.minX()))
                }

                if (potentialPoint.y in (verticalLine.minY()..verticalLine.maxY())) {
                    listOf(potentialPoint)
                } else {
                    listOf()
                }
            } else {
                listOf()
            }
        }

        private fun diagonalHorizontalIntersect(diagonalLine: Line, horizontalLine: Line): List<Point> {
            return if (horizontalLine.start.y in (diagonalLine.minY()..diagonalLine.maxY())) {
                val potentialPoint = if (diagonalLine.slope() > 0) {
                    Point(
                        (horizontalLine.start.y - diagonalLine.minY()) + diagonalLine.minX(), horizontalLine.start.y
                    )
                } else {
                    Point(
                        (diagonalLine.maxY() - horizontalLine.start.y) + diagonalLine.minX(), horizontalLine.start.y
                    )
                }

                if (potentialPoint.x in (horizontalLine.minX()..horizontalLine.maxX())) {
                    listOf(potentialPoint)
                } else {
                    listOf()
                }
            } else {
                listOf()
            }
        }

        fun intersection(other: Line): List<Point> {
            return when (orientation()) {
                LineOrientation.VERTICAL -> {
                    when (other.orientation()) {
                        LineOrientation.VERTICAL -> {
                            verticalVerticalIntersect(this, other)
                        }
                        LineOrientation.HORIZONTAL -> {
                            horizontalVerticalIntersect(other, this)
                        }
                        LineOrientation.DIAGONAL -> {
                            diagonalVerticalIntersect(other, this)
                        }
                    }
                }
                LineOrientation.HORIZONTAL -> {
                    when (other.orientation()) {
                        LineOrientation.VERTICAL -> {
                            horizontalVerticalIntersect(this, other)
                        }
                        LineOrientation.HORIZONTAL -> {
                            horizontalHorizontalIntersect(this, other)
                        }
                        LineOrientation.DIAGONAL -> {
                            diagonalHorizontalIntersect(other, this)
                        }
                    }
                }
                LineOrientation.DIAGONAL -> {
                    when (other.orientation()) {
                        LineOrientation.VERTICAL -> {
                            diagonalVerticalIntersect(this, other)
                        }
                        LineOrientation.HORIZONTAL -> {
                            diagonalHorizontalIntersect(this, other)
                        }
                        LineOrientation.DIAGONAL -> {
                            diagonalDiagonalIntersect(this, other)
                        }
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val straightLines = input.map { line ->
            val lineList = line.split(" -> ").take(2).map { point ->
                val pointList = point.split(",").take(2).map(::parseInt)
                Point(pointList[0], pointList[1])
            }
            Line(lineList[0], lineList[1])
        }.filterNot { it.orientation() == LineOrientation.DIAGONAL }

        return straightLines.asSequence().map { line ->
            straightLines.dropWhile { otherLine -> otherLine != line }.drop(1).map(line::intersection)
        }.flatten().flatten().distinct().count()
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { line ->
            val lineList = line.split(" -> ").take(2).map { point ->
                val pointList = point.split(",").take(2).map(::parseInt)
                Point(pointList[0], pointList[1])
            }
            Line(lineList[0], lineList[1])
        }

        val dimensionality = lines.fold(Point(0,0)) { dimension, line ->
            Point(maxOf(dimension.x, line.maxX()), maxOf(dimension.y, line.maxY()))
        }.let { point -> Point(point.x + 1, point.y + 1) }
        return lines.asSequence().map { line ->
            lines.dropWhile { otherLine -> otherLine != line }.drop(1).map(line::intersection)
        }.flatten().flatten().distinct().count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5) { "${part1(testInput)}" }
    val part2Res = part2(testInput)
    check(part2Res == 12) { "$part2Res" }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}