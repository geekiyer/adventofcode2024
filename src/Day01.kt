import kotlin.math.abs

fun calculateTotalDistance(leftList: List<Int>, rightList: List<Int>): Int {
    val sortedLeft = leftList.sorted()
    val sortedRight = rightList.sorted()

    var totalDistance = 0
    for (i in sortedLeft.indices) {
        totalDistance += abs(sortedLeft[i] - sortedRight[i])
    }

    return totalDistance
}

fun calculateSimilarityScore(leftList: List<Int>, rightList: List<Int>): Int {
    var similarityScore = 0
    for (leftNumber in leftList) {
        val count = rightList.count { it == leftNumber }
        similarityScore += leftNumber * count
    }
    return similarityScore
}

fun main() {
    val input = readInput("Day01")
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()

    for (line in input) {
        val (left, right) = line.trim().split("\\s+".toRegex())
        leftList.add(left.toInt())
        rightList.add(right.toInt())
    }

    val totalDistance = calculateTotalDistance(leftList, rightList)
    println("Total distance between lists: $totalDistance")
    val similarityScore = calculateSimilarityScore(leftList, rightList)
    println("Similarity Score: $similarityScore")
}
