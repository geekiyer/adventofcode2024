import kotlin.math.pow

fun main() {
    val input = readInput("Day07")
    val equations = parseEquations(input)
    val result1 = calculateCalibrationResult(equations, false)
    println("Part 1 calibration result: $result1")

    val result2 = calculateCalibrationResult(equations, true)
    println("Part 2 calibration result: $result2")
}

fun parseEquations(input: List<String>): List<Pair<Long, List<Long>>> {
    return input.map { line ->
        val parts = line.split(": ")
        val testValue = parts[0].toLong()
        val numbers = parts[1].split(" ").map { it.toLong() }
        Pair(testValue, numbers)
    }
}

fun calculateCalibrationResult(equations: List<Pair<Long, List<Long>>>, isPart2: Boolean): Long {
    var total = 0L
    for ((testValue, numbers) in equations) {
        when(isPart2) {
            true -> if (canEquationBeTruePart2(testValue, numbers)) { total += testValue }
            false -> if (canEquationBeTruePart1(testValue, numbers)) { total += testValue }
        }
    }
    return total
}

fun canEquationBeTruePart1(testValue: Long, numbers: List<Long>): Boolean {
    val numOperators = numbers.size - 1

    for (i in 0 until (1 shl numOperators)) {
        var result = numbers[0]
        var j = 1
        var mask = 1
        for (k in 0 until numOperators) {
            if (i and mask != 0) {
                result += numbers[j]
            } else {
                result *= numbers[j]
            }
            j++
            mask = mask shl 1
        }
        if (result == testValue) {
            return true
        }
    }
    return false
}

fun canEquationBeTruePart2(testValue: Long, numbers: List<Long>): Boolean {
    val operators = listOf('+', '*', '|')
    val numOperators = numbers.size - 1

    for (i in 0 until (operators.size.toDouble().pow(numOperators).toInt())) {
        var result = numbers[0]
        var j = 1
        var temp = i
        for (k in 0 until numOperators) {
            val op = operators[temp % operators.size]
            temp /= operators.size
            when (op) {
                '+' -> result += numbers[j]
                '*' -> result *= numbers[j]
                '|' -> {
                    // Create a new variable for concatenation to avoid modifying 'result' directly
                    val concatenated = result.toString() + numbers[j].toString()
                    result = concatenated.toLong()
                }
            }
            j++
        }
        if (result == testValue) {
            return true
        }
    }
    return false
}