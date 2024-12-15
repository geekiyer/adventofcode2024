import models.Point2D
import models.Robot

fun main() {
    val input = readInput("Day14")
    val area = Point2D(101, 103)
    val robots: List<Robot> = input.map { Robot.of(it) }

    val part1 = solvePart1(robots, area)
    println("Part 1: $part1")
    val part2 = solvePart2b(robots, area)
    println("Part 2: $part2")
}

fun solvePart1(robots: List<Robot>, area: Point2D): Int =
    robots
        .map { it.move(100, area) }
        .groupingBy { it.quadrant(area / 2) }
        .eachCount()
        .filterNot { it.key == 0 }
        .values
        .reduce(Int::times)

/*fun solvePart2a(robots: List<Robot>) {
    var printTheseRobots = robots
    File("10_000_robots.txt").printWriter().use { out ->
        repeat(10_000) { move ->
            printTheseRobots = printTheseRobots.map { it.move(1, area) }
            val uniquePlaces = printTheseRobots.map { it.position }.toSet()
            out.println("::::$move::::")
            repeat(area.y) { y ->
                repeat(area.x) { x ->
                    out.print(if (Point2D(x, y) in uniquePlaces) "#" else '.')
                }
                out.println()
            }
        }
    }
}*/

fun solvePart2b(input: List<Robot>, area: Point2D): Int {
    var moves = 0
    var robotsThisTurn = input
    do {
        moves++
        robotsThisTurn = robotsThisTurn.map { it.move(1, area) }
    } while (robotsThisTurn.distinctBy { it.position }.size != robotsThisTurn.size)
    return moves
}
