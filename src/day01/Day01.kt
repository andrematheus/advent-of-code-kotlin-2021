package day01

import Day

fun day01(): Day<Int, Int> {
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

    return Day(1, 7, ::part1, 5, ::part2)
}
