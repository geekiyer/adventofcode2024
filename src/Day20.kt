import models.Point2D

fun readMap(): List<List<Char>> {
    return readInput("Day20").map { it.toList() }
}

fun findStartEnd(grid: List<List<Char>>): Pair<Point2D?, Point2D?> {
    var start: Point2D? = null
    var end: Point2D? = null
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            when (grid[i][j]) {
                'S' -> start = Point2D(i, j)
                'E' -> end = Point2D(i, j)
            }
        }
    }
    return start to end
}

fun buildGraph(grid: List<List<Char>>, allowWalls: Boolean = false): MutableMap<Point2D, MutableSet<Point2D>> {
    val graph = mutableMapOf<Point2D, MutableSet<Point2D>>()
    val height = grid.size
    val width = grid[0].size
    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    for (y in 0 until height) {
        for (x in 0 until width) {
            if (!allowWalls && grid[y][x] == '#') continue
            val pos = Point2D(y, x)
            graph.putIfAbsent(pos, mutableSetOf())
            for ((dy, dx) in directions) {
                val newY = y + dy
                val newX = x + dx
                if (newY in 0 until height && newX in 0 until width &&
                    (allowWalls || grid[newY][newX] in listOf('.', 'S', 'E'))) {
                    graph[pos]?.add(Point2D(newY, newX))
                }
            }
        }
    }
    return graph
}

fun findAllCheats(grid: List<List<Char>>, start: Point2D?, end: Point2D?, maxCheatSteps: Int): List<Int> {
    println("Building normal graph and finding shortest path...")
    val graph = buildGraph(grid)
    val normalTime = shortestPathLength(graph, start, end) ?: return emptyList()
    println("Normal path length: $normalTime")

    println("Building graph with wall passages...")
    val graphWithWalls = buildGraph(grid, allowWalls = true)

    println("Pre-calculating distances...")
    val startDistances = dijkstra(graph, start)
    val endDistances = dijkstra(graph, end)

    val savedTimes = mutableListOf<Int>()
    val height = grid.size
    val width = grid[0].size
    val maxEndDist = normalTime - 100

    println("Finding cheats...")
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (grid[y][x] !in listOf('.', 'S', 'E')) continue
            val startPos = Point2D(y, x)
            val startDist = startDistances[startPos] ?: continue

            val cheatEnds = dijkstraWithCutoff(graphWithWalls, startPos, maxCheatSteps)
            for ((endPos, cheatSteps) in cheatEnds) {
                if (grid[endPos.x][endPos.y] !in listOf('.', 'S', 'E')) continue
                val endDist = endDistances[endPos] ?: continue
                if (endDist > maxEndDist) continue

                val cheatTime = startDist + cheatSteps + endDist
                val timeSaved = normalTime - cheatTime

                if (timeSaved >= 100) {
                    savedTimes.add(timeSaved)
                }
            }
        }
    }
    println("Found ${savedTimes.size} cheats that save ≥100 picoseconds")
    return savedTimes
}

fun dijkstra(graph: MutableMap<Point2D, MutableSet<Point2D>>, start: Point2D?): Map<Point2D?, Int> {
    val distances = mutableMapOf(start to 0)
    val toVisit = mutableListOf(start)
    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeAt(0)
        val currentDist = distances[current]!!
        for (neighbor in graph[current] ?: emptySet()) {
            if (neighbor !in distances) {
                distances[neighbor] = currentDist + 1
                toVisit.add(neighbor)
            }
        }
    }
    return distances
}

fun dijkstraWithCutoff(graph: MutableMap<Point2D, MutableSet<Point2D>>, start: Point2D, cutoff: Int): Map<Point2D, Int> {
    val distances = mutableMapOf(start to 0)
    val toVisit = mutableListOf(start)
    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeAt(0)
        val currentDist = distances[current]!!
        if (currentDist >= cutoff) continue
        for (neighbor in graph[current] ?: emptySet()) {
            if (neighbor !in distances) {
                distances[neighbor] = currentDist + 1
                toVisit.add(neighbor)
            }
        }
    }
    return distances
}

fun shortestPathLength(graph: MutableMap<Point2D, MutableSet<Point2D>>, start: Point2D?, end: Point2D?): Int? {
    val distances = dijkstra(graph, start)
    return distances[end]
}

fun solvePart1(): Int {
    val grid = readMap()
    val (start, end) = findStartEnd(grid)
    val savedTimes = findAllCheats(grid, start, end, maxCheatSteps = 2)
    return savedTimes.size
}

fun solvePart2(): Int {
    val grid = readMap()
    val (start, end) = findStartEnd(grid)
    val savedTimes = findAllCheats(grid, start, end, maxCheatSteps = 20)
    return savedTimes.size
}

fun main() {
    println("Part 1: ${solvePart1()}")
    println("Part 2: ${solvePart2()}")
}
