import kotlin.io.path.Path

val REGEX_MUL = """mul\((\d+),\s*(\d+)\)""".toRegex()
val REGEX_DO_DONT_MUL = """(do\(\)|don't\(\)|mul\((\d+),\s*(\d+)\))""".toRegex()

fun wholeFile(fileName: String): String {
    return Path("src/$fileName.txt").toFile().readText()
}

fun main() {
    val mem = wholeFile("Day03")
    var sum = 0
    var mulEnabled = true

    for (matchResult in REGEX_MUL.findAll(mem)) {
        val (num1, num2) = matchResult.destructured
        sum += num1.toInt() * num2.toInt()
    }
    println("Sum of multiplications: $sum")

    sum = 0
    for (matchResult in REGEX_DO_DONT_MUL.findAll(mem)) {
        val groups = matchResult.groupValues

        when (groups[1]) {
            "do()" -> mulEnabled = true
            "don't()" -> mulEnabled = false
            else -> {
                if (mulEnabled) {
                    val (num1, num2) = groups[2].toInt() to groups[3].toInt()
                    sum += num1 * num2
                }
            }
        }
    }

    println("Sum of enabled multiplications: $sum")
}
