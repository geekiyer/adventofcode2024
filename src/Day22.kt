fun main() {
    val input = readInput("Day22")
    val buyerNumbers: List<Long> = input.map { it.toLong() }

    println("Part 1: ${buyerNumbers.solvePart1()}")
    println("Part 2: ${buyerNumbers.solvePart2()}")
}

fun List<Long>.solvePart1(): Long =
    this.sumOf {
        it.secretNumbers().drop(2000).first()
    }

fun List<Long>.solvePart2(): Int {
    return this
        .flatMap { buyer ->
            buyer.secretNumbers().take(2001).map { i -> (i % 10).toInt() }
                .windowed(5, 1)
                .map { it.zipWithNext { first, second -> second - first } to it.last() }
                .distinctBy { it.first }
        }
        .groupingBy { it.first }
        .fold(0) { acc, (_, value) -> acc + value }
        .maxOfOrNull { it.value } ?: 0
}

private fun Long.secretNumbers(): Sequence<Long> =
    generateSequence(this) { secret ->
        secret
            .mixAndPrune { it shl 6 }
            .mixAndPrune { it shr 5 }
            .mixAndPrune { it shl 11 }
    }

private fun Long.mixAndPrune(function: (Long) -> Long): Long =
    (this xor function(this)) % 16777216L

