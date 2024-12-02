import kotlin.math.abs

fun incOrDec(myList: List<Int>): Boolean {
    return (myList.all { it > 0 } || myList.all { it < 0 })
}

fun differ(myList: List<Int>): Boolean {
    var prev: Int? = null
    for (num in myList) {
        if (prev == null) {
            prev = num
        } else {
            if (num == prev || abs(num - prev) > 3) {
                return false
            }
            prev = num
        }
    }
    return true
}

fun solution(filename: String = "Day02"): Pair<Int, Int> {
    var p1 = 0
    var p2 = 0
    val lines = readInput(filename)
    for (line in lines) {
        val nums = line.trim().split("\\s+".toRegex()).map { it.toInt() }
        val diff = mutableListOf<Int>()
        for (i in 1 until nums.size) {
            diff.add(nums[i] - nums[i - 1])
        }
        if (incOrDec(diff) && differ(nums)) {
            p1++
        } else {
            for (i in nums.indices) {
                val newNums = nums.subList(0, i) + nums.subList(i + 1, nums.size)
                val newDiff = mutableListOf<Int>()
                for (j in 1 until newNums.size) {
                    newDiff.add(newNums[j] - newNums[j - 1])
                }
                if (incOrDec(newDiff) && differ(newNums)) {
                    p2++
                    break
                }
            }
        }
    }
    return Pair(p1, p2)
}

fun main() {
    val (p1, p2) = solution("Day02")
    println("Part 1: $p1")
    println("Part 2: $p2, which means $p1 + $p2 = ${p1 + p2}")
}
