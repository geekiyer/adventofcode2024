import java.util.*

fun parseInput(file: String): List<Pair<Int, Int>> {
    return readInput(file).map {
        val (x, y) = it.split(",").map(String::toInt)
        Pair(x, y)
    }
}

fun simulateCorruption(fallingBytes: List<Pair<Int, Int>>, gridSize: Pair<Int, Int> = Pair(71, 71)): Array<BooleanArray> {
    val grid = Array(gridSize.second) { BooleanArray(gridSize.first) }
    for ((x, y) in fallingBytes) {
        grid[y][x] = true
    }
    return grid
}

fun isValid(x: Int, y: Int, grid: Array<BooleanArray>): Boolean {
    val rows = grid.size
    val cols = grid[0].size
    return x in 0 until cols && y in 0 until rows && !grid[y][x]
}

fun canReachExit(grid: Array<BooleanArray>, start: Pair<Int, Int> = Pair(0, 0), goal: Pair<Int, Int>? = null): Boolean {
    val end = goal ?: Pair(grid[0].size - 1, grid.size - 1)
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    val queue: Queue<Pair<Int, Int>> = LinkedList()
    queue.add(start)
    val visited = mutableSetOf(start)

    while (queue.isNotEmpty()) {
        val (x, y) = queue.poll()

        if (x to y == end) {
            return true
        }

        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (isValid(nx, ny, grid) && (nx to ny) !in visited) {
                visited.add(nx to ny)
                queue.add(nx to ny)
            }
        }
    }
    return false
}

fun shortestPath(grid: Array<BooleanArray>, start: Pair<Int, Int> = Pair(0, 0), goal: Pair<Int, Int>? = null): Int {
    val end = goal ?: Pair(grid[0].size - 1, grid.size - 1)
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    val pq = PriorityQueue(compareBy<Pair<Int, Pair<Int, Int>>> { it.first })
    pq.add(0 to start)
    val distances = mutableMapOf(start to 0)

    while (pq.isNotEmpty()) {
        val (currentCost, current) = pq.poll()
        val (x, y) = current

        if (current == end) {
            return currentCost
        }

        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (isValid(nx, ny, grid)) {
                val newCost = currentCost + 1
                if (distances.getOrDefault(nx to ny, Int.MAX_VALUE) > newCost) {
                    distances[nx to ny] = newCost
                    pq.add(newCost to (nx to ny))
                }
            }
        }
    }

    return Int.MAX_VALUE // No path exists
}

fun findFirstBlockingByte(fallingBytes: List<Pair<Int, Int>>, gridSize: Pair<Int, Int> = Pair(71, 71)): Pair<Int, Int>? {
    val grid = Array(gridSize.second) { BooleanArray(gridSize.first) }
    for ((i, byte) in fallingBytes.withIndex()) {
        val (x, y) = byte
        grid[y][x] = true
        if (!canReachExit(grid)) {
            return x to y
        }
    }
    return null
}

fun main() {
    val inputFile = "Day18"
    val fallingBytes = parseInput(inputFile)

    // Part 1: Simulate the first 1024 bytes falling
    val corruptedGrid = simulateCorruption(fallingBytes.take(1024))
    val steps = shortestPath(corruptedGrid)
    println("Minimum number of steps to reach the exit: $steps")

    // Part 2: Find the first blocking byte
    val blockingByte = findFirstBlockingByte(fallingBytes)
    if (blockingByte != null) {
        println("Coordinates of the first blocking byte: ${blockingByte.first},${blockingByte.second}")
    } else {
        println("No byte blocks the path.")
    }
}
