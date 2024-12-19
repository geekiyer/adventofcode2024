fun parseDay19Input(file: String): Pair<List<String>, List<String>> {
    val lines = readInput(file).filter { it.isNotBlank() }
    val towelPatterns = lines.first().split(", ")
    val desiredDesigns = lines.drop(1)
    return towelPatterns to desiredDesigns
}

fun canCreateDesign(patterns: List<String>, design: String): Boolean {
    val queue = ArrayDeque<String>()
    val visited = mutableSetOf<String>()

    queue.add(design)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current in visited) continue
        visited.add(current)

        for (pattern in patterns) {
            if (current.startsWith(pattern)) {
                val remaining = current.removePrefix(pattern)
                if (remaining.isEmpty()) return true
                queue.add(remaining)
            }
        }
    }
    return false
}

fun countArrangements(patterns: List<String>, design: String, memo: MutableMap<String, Long> = mutableMapOf()): Long {
    if (design.isEmpty()) return 1L
    if (memo.containsKey(design)) return memo[design]!!

    var totalWays: Long = 0
    for (pattern in patterns) {
        if (design.startsWith(pattern)) {
            val remaining = design.removePrefix(pattern)
            totalWays += countArrangements(patterns, remaining, memo)
        }
    }

    memo[design] = totalWays
    return totalWays
}

fun countPossibleDesigns(patterns: List<String>, designs: List<String>): Int {
    return designs.count { canCreateDesign(patterns, it) }
}

fun sumOfArrangements(patterns: List<String>, designs: List<String>): Long {
    return designs.filter { canCreateDesign(patterns, it) } // Exclude impossible designs
        .sumOf { countArrangements(patterns, it) }
}

fun main() {
    val inputFile = "Day19"
    val (towelPatterns, desiredDesigns) = parseDay19Input(inputFile)

    // Part 1: Count possible designs
    val possibleCount = countPossibleDesigns(towelPatterns, desiredDesigns)
    println("Number of possible designs: $possibleCount")

    // Part 2: Sum of all possible arrangements
    val totalArrangements = sumOfArrangements(towelPatterns, desiredDesigns)
    println("Sum of all possible arrangements: $totalArrangements")
}
