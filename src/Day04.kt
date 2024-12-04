
fun countXMAS(grid: List<String>, isPartTwo: Boolean): Int {
    return if (isPartTwo) {
        countXMASPartTwo(grid)
    } else {
        countXMASPartOne(grid)
    }
}

fun countXMASPartOne(grid: List<String>): Int {
    var count = 0
    val rows = grid.size
    val cols = grid[0].length

    for (row in 0 until rows) {
        for (col in 0 until cols) {
            if (grid[row][col] == 'X') {
                // Check all eight directions
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr == 0 && dc == 0) continue // Skip the current position

                        var r = row + dr
                        var c = col + dc
                        var xmas = "X"

                        for (i in 1..3) { // Check the next 3 letters
                            if (r in 0 until rows && c in 0 until cols) {
                                xmas += grid[r][c]
                                r += dr
                                c += dc
                            } else {
                                break
                            }
                        }

                        if (xmas == "XMAS" || xmas == "SAMX") { // Check forward and backward
                            count++
                        }
                    }
                }
            }
        }
    }

    return count
}

fun countXMASPartTwo(grid: List<String>): Int {
    var count = 0
    val rows = grid.size
    val cols = grid[0].length

    for (row in 1 until rows - 1) {
        for (col in 1 until cols - 1) {
            if (grid[row][col] == 'A') {
                val topLeft = grid[row - 1][col - 1]
                val topRight = grid[row - 1][col + 1]
                val bottomLeft = grid[row + 1][col - 1]
                val bottomRight = grid[row + 1][col + 1]

                // Check for valid 'M' and 'S' positions around 'A' to form an X
                if ((topLeft == 'M' && bottomRight == 'S' || topLeft == 'S' && bottomRight == 'M') &&
                    (topRight == 'S' && bottomLeft == 'M' || topRight == 'M' && bottomLeft == 'S')
                ) {
                    count++
                }
            }
        }
    }

    return count
}

fun main() {
    val input = readInput("Day04")
    val countPartOne = countXMAS(input, false)
    val countPartTwo = countXMAS(input, true)
    println("XMAS appears $countPartOne times")
    println("X-MAS appears $countPartTwo times")
}