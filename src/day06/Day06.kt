package day06

import readInput

fun day06() {
    fun laternFishGrowth(input: List<String>, totalDays: Int): Long {
        val initial = input[0].split(",").map(String::toInt).toMutableList()
        val agesRing: MutableList<Long> = List(7) { _ -> 0L }.toMutableList()
        val sevensAndEights: MutableList<Long> = List(2) { _ -> 0L }.toMutableList()

        for (age in initial) {
            agesRing[age]++
        }
        var ringIndex = 0

        fun cycle() {
            val sevens = sevensAndEights[0]
            sevensAndEights[0] = sevensAndEights[1]
            sevensAndEights[1] = agesRing[ringIndex]
            agesRing[ringIndex] += sevens
        }

        for (day in 1..totalDays) {
            cycle()

            ringIndex++
            if (ringIndex == agesRing.size) {
                ringIndex = 0
            }
        }
        return agesRing.sum() + sevensAndEights.sum()
    }

    fun part1(input: List<String>): Long {
        return laternFishGrowth(input, 80)
    }

    fun part2(input: List<String>): Long {
        return laternFishGrowth(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    val part1 = part1(testInput)
    println("part1: $part1")
    check(part1 == 5934L)
    val part2 = part2(testInput)
    println("part2: $part2")
    check(part2 == 26984457539)

    val input = readInput("day06/Day06")
    println(part1(input))
    println(part2(input))
}
