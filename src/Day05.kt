
fun isUpdateCorrect(update: List<Int>, rules: Map<Int, Set<Int>>): Boolean {
    for (i in 0 until update.size - 1) {
        val page1 = update[i]
        for (j in i + 1 until update.size) {
            val page2 = update[j]
            if (rules[page2]?.contains(page1) == true) {
                return false // Violation: page2 should be printed before page1
            }
        }
    }
    return true
}

fun middlePageNumber(update: List<Int>): Int {
    return update[update.size / 2]
}

fun orderUpdate(update: List<Int>, rules: Map<Int, Set<Int>>): List<Int> {
    val orderedUpdate = mutableListOf<Int>()
    val remainingPages = update.toMutableSet()

    while (remainingPages.isNotEmpty()) {
        val nextPage = remainingPages.find { page ->
            rules[page]?.none { it in remainingPages } ?: true
        }
        if (nextPage != null) {
            orderedUpdate.add(nextPage)
            remainingPages.remove(nextPage)
        } else {
            // Handle potential cycles in rules (shouldn't happen in this puzzle)
            error("Circular dependency in page ordering rules")
        }
    }

    return orderedUpdate
}

fun main() {
    val input = readInput("Day05")
    val rules = mutableMapOf<Int, MutableSet<Int>>()
    val updates = mutableListOf<List<Int>>()

    // Parse input
    var parsingRules = true
    for (line in input) {
        if (line.isEmpty()) {
            parsingRules = false
            continue
        }

        if (parsingRules) {
            val (page1, page2) = line.split("|").map { it.toInt() }
            rules.getOrPut(page1) { mutableSetOf() }.add(page2)
        } else {
            updates.add(line.split(",").map { it.toInt() })
        }
    }

    var sumOfMiddlePages = 0
    for (update in updates) {
        if (isUpdateCorrect(update, rules)) {
            sumOfMiddlePages += middlePageNumber(update)
        }
    }

    println("Sum of middle page numbers from correctly-ordered updates: $sumOfMiddlePages")
    sumOfMiddlePages = 0
    for (update in updates) {
        if (!isUpdateCorrect(update, rules)) {
            val orderedUpdate = orderUpdate(update, rules)
            sumOfMiddlePages += middlePageNumber(orderedUpdate)
        }
    }

    println("Sum of middle page numbers from incorrectly-ordered updates after ordering: $sumOfMiddlePages")
}