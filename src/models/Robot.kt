package models

data class Robot(
    val position: Point2D,
    val velocity: Point2D
) {
    fun move(moves: Int, area: Point2D): Robot =
        copy(
            position = (position + (velocity * moves)).wrap(area)
        )

    fun Point2D.wrap(other: Point2D): Point2D {
        val nextX = x % other.x
        val nextY = y % other.y
        return Point2D(
            if (nextX < 0) nextX + other.x else nextX,
            if (nextY < 0) nextY + other.y else nextY
        )
    }

    fun quadrant(midpoint: Point2D): Int =
        when {
            position.x < midpoint.x && position.y < midpoint.y -> 1
            position.x > midpoint.x && position.y < midpoint.y -> 2
            position.x < midpoint.x && position.y > midpoint.y -> 3
            position.x > midpoint.x && position.y > midpoint.y -> 4
            else -> 0
        }

    companion object {
        fun of(input: String): Robot =
            Robot(
                position = Point2D(
                    input.substringAfter("=").substringBefore(",").toInt(),
                    input.substringAfter(",").substringBefore(" ").toInt()
                ),
                velocity = Point2D(
                    input.substringAfterLast("=").substringBefore(",").toInt(),
                    input.substringAfterLast(",").toInt()
                )
            )
    }
}