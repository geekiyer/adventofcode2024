
fun main() {
    val grid = readInput("Day12").map { it.toCharArray() }
    val rows = grid.size
    val cols = grid[0].size

    val (part1, part2) = calculateFencePrices(grid, rows, cols)
    println("Part 1: $part1")
    println("Part 2: $part2")
}

fun calculateFencePrices(grid: List<CharArray>, rows: Int, cols: Int): Pair<Int, Int> {
    val seen = mutableSetOf<Pair<Int, Int>>()
    var part1 = 0
    var part2 = 0
    val dirs = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (Pair(r, c) in seen) continue

            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.add(Pair(r, c))
            var area = 0
            var perimeter = 0
            val periMap = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()

            while (queue.isNotEmpty()) {
                val (r2, c2) = queue.removeFirst()
                if (Pair(r2, c2) in seen) continue
                seen.add(Pair(r2, c2))
                area++

                for ((dr, dc) in dirs) {
                    val rr = r2 + dr
                    val cc = c2 + dc
                    if (rr in 0 until rows && cc in 0 until cols && grid[rr][cc] == grid[r2][c2]) {
                        queue.add(Pair(rr, cc))
                    } else {
                        perimeter++
                        periMap.getOrPut(Pair(dr, dc)) { mutableSetOf() }.add(Pair(r2, c2))
                    }
                }
            }

            var sides = 0
            for ((_, vs) in periMap) {
                val seenPerim = mutableSetOf<Pair<Int, Int>>()
                for ((pr, pc) in vs) {
                    if (Pair(pr, pc) in seenPerim) continue
                    sides++
                    val q = ArrayDeque<Pair<Int, Int>>()
                    q.add(Pair(pr, pc))
                    while (q.isNotEmpty()) {
                        val (r2, c2) = q.removeFirst()
                        if (Pair(r2, c2) in seenPerim) continue
                        seenPerim.add(Pair(r2, c2))
                        for ((dr, dc) in dirs) {
                            val rr = r2 + dr
                            val cc = c2 + dc
                            if (Pair(rr, cc) in vs) {
                                q.add(Pair(rr, cc))
                            }
                        }
                    }
                }
            }

            part1 += area * perimeter
            part2 += area * sides
        }
    }

    return Pair(part1, part2)
}