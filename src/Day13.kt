import kotlin.math.roundToLong

fun main() {
    val lines = readInput("Day13")

    solvePart1(1, input = lines)
    solvePart1(2, input = lines)
}

fun solvePart1(part: Int, input: List<String>) {
    var tokens = 0L
    val add = if (part == 2) 10000000000000 else 0L
    var x1 = 0L
    var y1 = 0L
    var x2 = 0L
    var y2 = 0L

    for (line in input) {
        if (line.startsWith("Button")) {
            val l = line.split(" ")
            val a = l[1].split(":")[0]
            if (a == "A") {
                x1 = l[2].substring(2, l[2].length - 1).toLong()
                y1 = l[3].substring(2).toLong()
            } else {
                x2 = l[2].substring(2, l[2].length - 1).toLong()
                y2 = l[3].substring(2).toLong()
            }
        } else if (line.startsWith("Prize")) {
            val l = line.split(" ")
            val c = l[1].substring(2, l[1].length - 1).toLong() + add
            val d = l[2].substring(2).toLong() + add
            val a = (c * y2 - d * x2) / (x1 * y2 - y1 * x2).toDouble()
            val b = (d * x1 - c * y1) / (x1 * y2 - y1 * x2).toDouble()
            if (a == a.roundToLong().toDouble() && b == b.roundToLong().toDouble()) {
                tokens += (3 * a + b).roundToLong()
            }
        }
    }

    println(tokens)
}