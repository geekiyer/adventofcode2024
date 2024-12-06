
fun main() {
    val inputList = readInput("Day06")
    val (guardMap, initialGuard) = parseInput(inputList)

    val answer1 = countVisitedPositions(guardMap, initialGuard)
    println("Answer 1: ${answer1.size}")

    val answer2 = countObstaclesAffectingLoop(guardMap, initialGuard, answer1)
    println("Answer 2: $answer2")
}

data class GuardState(val position: Pair<Int, Int>, val direction: String)

fun parseInput(inputList: List<String>): Pair<MutableMap<Pair<Int, Int>, Char>, Pair<Int, Int>> {
    val guardMap = mutableMapOf<Pair<Int, Int>, Char>()
    var initialGuard = Pair(0, 0)
    for (i in inputList.indices) {
        for (j in inputList[i].indices) {
            guardMap[Pair(i, j)] = inputList[i][j]
            if (inputList[i][j] == '^') {
                initialGuard = Pair(i, j)
                guardMap[Pair(i, j)] = '.'
            }
        }
    }
    return Pair(guardMap, initialGuard)
}

fun countVisitedPositions(guardMap: MutableMap<Pair<Int, Int>, Char>, initialGuard: Pair<Int, Int>): MutableSet<Pair<Int, Int>> {
    var guard = initialGuard
    var curDir = "UP"
    val visited = mutableSetOf<Pair<Int, Int>>()

    while (true) {
        visited.add(guard)
        val nextState = getNextState(guardMap, guard, curDir) ?: break
        guard = nextState.position
        curDir = nextState.direction
    }

    return visited
}

fun countObstaclesAffectingLoop(guardMap: MutableMap<Pair<Int, Int>, Char>, initialGuard: Pair<Int, Int>, visitedPositions: Set<Pair<Int, Int>>): Int {
    var answer2 = 0

    for (obstacle in visitedPositions) {
        guardMap[obstacle] = '#'
        var guard = initialGuard
        var curDir = "UP"
        var loop = true
        var k = 0
        while (loop && k < visitedPositions.size * 2) {
            val nextState = getNextState(guardMap, guard, curDir)
            if (nextState == null) {
                loop = false
            } else {
                guard = nextState.position
                curDir = nextState.direction
            }
            k++
        }
        if (loop) {
            answer2++
        }
        guardMap[obstacle] = '.'
    }

    return answer2
}

fun getNextState(guardMap: Map<Pair<Int, Int>, Char>, guard: Pair<Int, Int>, curDir: String): GuardState? {
    return when (curDir) {
        "UP" -> {
            val nextPos = Pair(guard.first - 1, guard.second)
            if (nextPos !in guardMap) null
            else if (guardMap[nextPos] == '.') GuardState(nextPos, "UP")
            else GuardState(guard, "RIGHT")
        }
        "RIGHT" -> {
            val nextPos = Pair(guard.first, guard.second + 1)
            if (nextPos !in guardMap) null
            else if (guardMap[nextPos] == '.') GuardState(nextPos, "RIGHT")
            else GuardState(guard, "DOWN")
        }
        "DOWN" -> {
            val nextPos = Pair(guard.first + 1, guard.second)
            if (nextPos !in guardMap) null
            else if (guardMap[nextPos] == '.') GuardState(nextPos, "DOWN")
            else GuardState(guard, "LEFT")
        }
        "LEFT" -> {
            val nextPos = Pair(guard.first, guard.second - 1)
            if (nextPos !in guardMap) null
            else if (guardMap[nextPos] == '.') GuardState(nextPos, "LEFT")
            else GuardState(guard, "UP")
        }
        else -> throw IllegalArgumentException("Invalid direction: $curDir")
    }
}