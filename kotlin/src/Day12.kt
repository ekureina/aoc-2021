fun main() {

    fun String.isLargeCave(): Boolean = get(0).isUpperCase()

    fun buildMap(input: List<String>): Map<String, List<String>> {
        val connections = mutableMapOf<String, MutableList<String>>()
        input.forEach { line ->
            val (start, end) = line.split("-")
            connections[start] = connections[start]?.also { list -> list.add(end) } ?: mutableListOf(end)
            connections[end] = connections[end]?.also { list -> list.add(start) } ?: mutableListOf(start)
        }
        return connections
    }

    fun pathsFrom(source: String, connections: Map<String, List<String>>, visited: List<String> = listOf()): Int {
        return if (source == "end") {
            1
        } else {
            val newVisited = buildList {
                addAll(visited)
                add(source)
            }
            connections[source]!!
                .filterNot { neighbor -> visited.contains(neighbor) && !neighbor.isLargeCave() }
                .sumOf { neighbor ->
                    pathsFrom(neighbor, connections, newVisited)
                }
        }
    }

    fun pathsFromWithRepeat(
        source: String,
        connections: Map<String, List<String>>,
        visited: List<String> = listOf(),
        repeats: Int = 1
    ): Int {
        return if (source == "end") {
            1
        } else {
            val newVisited = buildList {
                addAll(visited)
                add(source)
            }
            val noRepeatPaths = connections[source]!!
                .filterNot { neighbor -> visited.contains(neighbor) && !neighbor.isLargeCave() }
                .sumOf { neighbor ->
                    pathsFromWithRepeat(neighbor, connections, newVisited, repeats)
                }
            val withRepeats = if (repeats > 0) {
                connections[source]!!.filter { neighbor -> visited.contains(neighbor) && !neighbor.isLargeCave() && neighbor != "start" && neighbor != "end" }
                    .sumOf { neighbor ->
                        pathsFromWithRepeat(neighbor, connections, newVisited, repeats - 1)
                    }
            } else {
                0
            }
            noRepeatPaths + withRepeats
        }
    }

    fun part1(input: List<String>): Int {
        val connections = buildMap(input)

        return pathsFrom("start", connections)
    }

    fun part2(input: List<String>): Int {
        val connections = buildMap(input)

        return pathsFromWithRepeat("start", connections)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10) { "${part1(testInput)}" }
    check(part2(testInput) == 36) { "${part2(testInput)}" }

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
