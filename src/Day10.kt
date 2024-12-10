
fun main() {
    val input = readInput("Day10")
    val heightMap = input.map { it.map { c -> c.digitToInt() } }

    val trailheads = findTrailheads(heightMap)

    val totalScore = trailheads.sumOf { calculateScore(heightMap, it) }
    println("Part 1: Sum of trailhead scores: $totalScore")

    val totalRating = trailheads.sumOf { calculateRating(heightMap, it) }
    println("Part 2: Sum of trailhead ratings: $totalRating")
}

fun findTrailheads(heightMap: List<List<Int>>): List<Pair<Int, Int>> {
    val trailheads = mutableListOf<Pair<Int, Int>>()
    for (y in heightMap.indices) {
        for (x in heightMap[y].indices) {
            if (heightMap[y][x] == 0) {
                trailheads.add(Pair(x, y))
            }
        }
    }
    return trailheads
}

fun calculateScore(heightMap: List<List<Int>>, start: Pair<Int, Int>): Int {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(start)
    var score = 0

    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (Pair(x, y) in visited) continue
        visited.add(Pair(x, y))

        if (heightMap[y][x] == 9) {
            score++
            continue
        }

        // Explore adjacent positions with height + 1
        listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1)).forEach { (nx, ny) ->
            if (nx in heightMap[0].indices && ny in heightMap.indices && heightMap[ny][nx] == heightMap[y][x] + 1) {
                queue.add(Pair(nx, ny))
            }
        }
    }

    return score
}

fun calculateRating(heightMap: List<List<Int>>, start: Pair<Int, Int>): Int {
    val paths = mutableSetOf<List<Pair<Int, Int>>>()
    val queue = ArrayDeque<List<Pair<Int, Int>>>()
    queue.add(listOf(start))

    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()
        val (x, y) = path.last()

        if (heightMap[y][x] == 9) {
            paths.add(path) // Add the complete path to the set
            continue
        }

        listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1)).forEach { (nx, ny) ->
            if (nx in heightMap[0].indices && ny in heightMap.indices && heightMap[ny][nx] == heightMap[y][x] + 1 && Pair(nx, ny) !in path) {
                queue.add(path + Pair(nx, ny))
            }
        }
    }

    return paths.size
}