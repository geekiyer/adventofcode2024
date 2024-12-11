import java.math.BigInteger

fun main() {
    val input = readInput("Day11")[0]
    val stones = input.split(" ").map { BigInteger(it) }

    val finalStonesPart1 = transformStones(stones, 25)
    println("Part 1: Number of stones after 25 blinks: ${finalStonesPart1.size}")

    val stoneCounts = stones.groupingBy { it }.eachCount().mapValues { it.value.toBigInteger() }
    val finalCountPart2 = blinkAll(stoneCounts, 75)
    println("Part 2: Number of stones after 75 blinks: $finalCountPart2")
}

fun transformStones(stones: List<BigInteger>, numBlinks: Int): List<BigInteger> {
    var currentStones = stones
    repeat(numBlinks) {
        currentStones = currentStones.flatMap { transformStone(it) }
    }
    return currentStones
}

fun transformStone(stone: BigInteger): List<BigInteger> {
    if (stone == BigInteger.ZERO) {
        return listOf(BigInteger.ONE)
    } else if (stone.toString().length % 2 == 0) {
        val str = stone.toString()
        val mid = str.length / 2
        return listOf(BigInteger(str.substring(0, mid)), BigInteger(str.substring(mid)))
    } else {
        return listOf(stone * BigInteger("2024"))
    }
}

fun blinkOne(stone: BigInteger): List<BigInteger> {
    if (stone == BigInteger.ZERO) {
        return listOf(BigInteger.ONE)
    }
    val stoneStr = stone.toString()
    val length = stoneStr.length
    return if (length % 2 == 0) {
        listOf(
            BigInteger(stoneStr.substring(0, length / 2)),
            BigInteger(stoneStr.substring(length / 2))
        )
    } else {
        listOf(stone * BigInteger("2024"))
    }
}

fun blinkAll(stoneCounts: Map<BigInteger, BigInteger>, numBlinks: Int): BigInteger {
    var currentCounts = stoneCounts
    repeat(numBlinks) {
        val newCounts = mutableMapOf<BigInteger, BigInteger>()
        for ((stone, count) in currentCounts) {
            for (newStone in blinkOne(stone)) {
                newCounts[newStone] = newCounts.getOrDefault(newStone, BigInteger.ZERO) + count
            }
        }
        currentCounts = newCounts
    }
    return currentCounts.values.reduce { acc, count -> acc + count }
}