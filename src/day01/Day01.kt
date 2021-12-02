package day01

import readInput

fun day01() {
    fun List<Int>.countIncreases() =
        this.zipWithNext()
            .map { it.second > it.first }
            .count { it }

    fun part1(input: List<String>): Int {
        return input.map(String::toInt)
            .countIncreases()
    }

    fun part2(input: List<String>): Int {
        return input.map(String::toInt)
            .windowed(3)
            .map { it.sum() }
            .countIncreases()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("day01/Day01")
    println(part1(input))
    println(part2(input))
}
