import java.util.*

data class State(val x: Int, val y: Int, val d: Int)

fun parseGrid(grid: List<List<Char>>): Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int,Int>> {
    val rows = grid.size
    val cols = grid[0].size
    var start: Pair<Int, Int>? = null
    var end: Pair<Int, Int>? = null
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (grid[i][j] == 'S') {
                start = Pair(i, j)
            } else if (grid[i][j] == 'E') {
                end = Pair(i, j)
            }
        }
    }
    return Triple(start!!, end!!, Pair(rows, cols))
}

fun dijkstra(grid: List<List<Char>>, start: Pair<Int, Int>, end: Pair<Int, Int>, rows: Int, cols: Int): Map<State, Int> {
    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
    val startState = State(start.first, start.second, 1)

    val pq = PriorityQueue<Pair<Int, State>>(compareBy { it.first })
    pq.offer(Pair(0, startState))
    val visited = mutableMapOf(startState to 0)

    while (pq.isNotEmpty()) {
        val (cost, state) = pq.poll()

        if (visited.getOrDefault(State(state.x, state.y, state.d), Int.MAX_VALUE) < cost) {
            continue
        }

        val (dx, dy) = directions[state.d]
        val nx = state.x + dx
        val ny = state.y + dy
        if (nx in 0 until rows && ny in 0 until cols && grid[nx][ny] != '#') {
            val newCost = cost + 1
            if (newCost < visited.getOrDefault(State(nx,ny,state.d), Int.MAX_VALUE)) {
                visited[State(nx,ny,state.d)] = newCost
                pq.offer(Pair(newCost, State(nx, ny, state.d)))
            }
        }

        for (nd in listOf((state.d - 1).mod(4), (state.d + 1).mod(4))) {
            val newCost = cost + 1000
            if (newCost < visited.getOrDefault(State(state.x,state.y,nd), Int.MAX_VALUE)) {
                visited[State(state.x,state.y,nd)] = newCost
                pq.offer(Pair(newCost, State(state.x, state.y, nd)))
            }
        }
    }
    return visited
}

fun backtrackShortestPaths(grid: List<List<Char>>, visited: Map<State, Int>, end: Pair<Int, Int>, rows: Int, cols: Int): Set<Pair<Int, Int>> {
    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    val minEndCost = (0..3).filter { visited.containsKey(State(end.first, end.second, it)) }.minOf { visited[State(end.first, end.second, it)]!! }

    val onShortestPath = mutableSetOf<State>()
    val q: Queue<State> = LinkedList()

    for (d in 0..3) {
        val endState = State(end.first, end.second, d)
        if (visited.containsKey(endState) && visited[endState] == minEndCost) {
            onShortestPath.add(endState)
            q.offer(endState)
        }
    }

    while (q.isNotEmpty()) {
        val (cx, cy, cd) = q.poll()
        val currentCost = visited[State(cx,cy,cd)]!!

        val (dx, dy) = directions[cd]
        val px = cx - dx
        val py = cy - dy
        if (px in 0 until rows && py in 0 until cols && grid[px][py] != '#') {
            val prevCost = currentCost - 1
            if (prevCost >= 0) {
                val prevState = State(px, py, cd)
                if (visited.containsKey(prevState) && visited[prevState] == prevCost) {
                    if (!onShortestPath.contains(prevState)) {
                        onShortestPath.add(prevState)
                        q.offer(prevState)
                    }
                }
            }
        }

        val turnCost = currentCost - 1000
        if (turnCost >= 0) {
            for (pd in listOf((cd - 1).mod(4), (cd + 1).mod(4))) {
                val prevState = State(cx, cy, pd)
                if (visited.containsKey(prevState) && visited[prevState] == turnCost) {
                    if (!onShortestPath.contains(prevState)) {
                        onShortestPath.add(prevState)
                        q.offer(prevState)
                    }
                }
            }
        }
    }

    return onShortestPath.map { Pair(it.x, it.y) }.toSet()
}

fun solveMazePart1(grid: List<List<Char>>): Int {
    val (start, end, size) = parseGrid(grid)
    val visited = dijkstra(grid, start, end, size.first, size.second)
    return (0..3).filter { visited.containsKey(State(end.first, end.second, it)) }.minOf { visited[State(end.first, end.second, it)]!! }
}

fun solveMazePart2(grid: List<List<Char>>): Int {
    val (start, end, size) = parseGrid(grid)
    val visited = dijkstra(grid, start, end, size.first, size.second)
    val shortestPathTiles = backtrackShortestPaths(grid, visited, end, size.first, size.second)
    return shortestPathTiles.size
}

fun main() {
    val grid = readInput("Day16").map { it.toList() }
    println(solveMazePart1(grid))
    println(solveMazePart2(grid))
}