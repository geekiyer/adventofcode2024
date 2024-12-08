
fun main() {
    val grid = readInput("Day08").filter { it.isNotBlank() }
    val n = grid.size
    val m = grid[0].length

    val nodes = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

    for (i in 0 until n) {
        for (j in 0 until m) {
            if (grid[i][j] != '.') {
                nodes.getOrPut(grid[i][j]) { mutableListOf() }.add(Pair(i, j))
            }
        }
    }
    val part1 = part1(nodes, n, m)
    println(part1)
    val part2 = part2(nodes, n, m)
    println(part2)
}

fun part1(nodes: MutableMap<Char, MutableList<Pair<Int, Int>>>, n: Int, m: Int): Int {
    val antinodes = mutableSetOf<Pair<Int, Int>>()

    fun antinode(pr1: Pair<Int, Int>, pr2: Pair<Int, Int>) {
        val (x1, y1) = pr1
        val (x2, y2) = pr2
        val newX = x2 + (x2 - x1)
        val newY = y2 + (y2 - y1)
        if (newX in 0 until n && newY in 0 until m) {
            antinodes.add(Pair(newX, newY))
        }
    }

    for ((_, nodeList) in nodes) {
        val L = nodeList.size
        for (i in 0 until L) {
            for (j in 0 until i) {
                val node1 = nodeList[i]
                val node2 = nodeList[j]
                antinode(node1, node2)
                antinode(node2, node1)
            }
        }
    }
    return antinodes.size
}

fun part2(nodes: MutableMap<Char, MutableList<Pair<Int, Int>>>, n: Int, m: Int): Int {
    val antinodes = mutableSetOf<Pair<Int, Int>>()

    fun antinode(pr1: Pair<Int, Int>, pr2: Pair<Int, Int>) {
        val (x1, y1) = pr1
        val (x2, y2) = pr2
        var newX = x2 + (x2 - x1)
        var newY = y2 + (y2 - y1)
        antinodes.add(pr2) // Add the second node as an antinode

        while (newX in 0 until n && newY in 0 until m) {
            antinodes.add(Pair(newX, newY))
            newX += (x2 - x1)
            newY += (y2 - y1)
        }
    }

    for ((_, nodeList) in nodes) {
        val L = nodeList.size
        for (i in 0 until L) {
            for (j in 0 until i) {
                val node1 = nodeList[i]
                val node2 = nodeList[j]
                antinode(node1, node2)
                antinode(node2, node1)
            }
        }
    }

    return antinodes.size
}