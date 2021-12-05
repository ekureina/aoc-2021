import java.lang.Integer.parseInt

enum class LineOrientation {
    VERTICAL,
    HORIZONTAL,
    DIAGONAL;
}

fun main() {
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point) {
        fun generateDiagonalPoints(start: Point, end: Point): Set<Point> {
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

            return xSequence.zip(ySequence).map { (x, y) -> Point(x, y) }.toSet()
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

        fun minX(): Int = minOf(start.x, end.x)

        fun minY(): Int = minOf(start.y, end.y)

        fun maxX(): Int = maxOf(start.x, end.x)

        fun maxY(): Int = maxOf(start.y, end.y)

        fun intersection(other: Line): List<Point> {
            return if (orientation() == other.orientation()) {
                if (orientation() == LineOrientation.VERTICAL && start.x == other.start.x) {
                    (minY()..maxY()).intersect(other.minY()..other.maxY()).map { y ->
                        Point(start.x, y)
                    }
                } else if (orientation() == LineOrientation.HORIZONTAL && start.y == other.start.y) {
                    (minX()..maxX()).intersect(other.minX()..other.maxX()).map { x ->
                        Point(x, start.y)
                    }
                } else if (orientation() == LineOrientation.DIAGONAL) {
                    generateDiagonalPoints(start, end).intersect(generateDiagonalPoints(other.start, other.end)).toList()
                } else {
                    listOf()
                }
            } else {
                if (orientation() == LineOrientation.VERTICAL) {
                    if (other.orientation() == LineOrientation.HORIZONTAL) {
                        if (start.x in (other.minX()..other.maxX()) && other.start.y in (minY()..maxY())) {
                            listOf(Point(start.x, other.start.y))
                        } else {
                            listOf()
                        }
                    } else {
                        generateDiagonalPoints(other.start, other.end).filter { point ->
                            point.x == start.x && point.y in (minY()..maxY())
                        }.toList()
                    }
                } else if (orientation() == LineOrientation.HORIZONTAL) {
                    if (other.orientation() == LineOrientation.VERTICAL) {
                        if (start.y in (other.minY()..other.maxY()) && other.start.x in (minX()..maxX())) {
                            listOf(Point(other.start.x, start.y))
                        } else {
                            listOf()
                        }
                    } else {
                        generateDiagonalPoints(other.start, other.end).filter { point ->
                            point.y == start.y && point.x in (minX()..maxX())
                        }.toList()
                    }
                } else {
                    if (other.orientation() == LineOrientation.VERTICAL) {
                        generateDiagonalPoints(start, end).filter { point ->
                            point.x == other.start.x && point.y in (other.minY()..other.maxY())
                        }.toList()
                    } else {
                        generateDiagonalPoints(start, end).filter { point ->
                            point.y == other.start.y && point.x in (other.minX()..other.maxX())
                        }.toList()
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