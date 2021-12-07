package day07

import readInput
import kotlin.math.abs

fun day07() {
    fun findMinimum(min: Int, max: Int, costFn: (Int) -> Int): Int {
        var lastCost = costFn(min)

        for (i in min..max) {
            val cost = costFn(i)
            if (cost > lastCost) {
                return lastCost
            } else {
                lastCost = cost
            }
        }

        return lastCost
    }

    fun part1(input: List<String>): Int {
        val positions: List<Int> = input[0].split(",").map(String::toInt)

        val maxPos = positions.maxOrNull()!!
        val minPos = positions.minOrNull()!!

        return findMinimum(minPos, maxPos) { pivot ->
            positions.sumOf { position -> abs(position - pivot) }
        }
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(",").map(String::toInt)

        val maxPos = positions.maxOrNull()!!
        val minPos = positions.minOrNull()!!

        return findMinimum(minPos, maxPos) { pivot ->
            positions.sumOf { position ->
                val n = abs(position - pivot)
                (n * (n + 1)) / 2
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    val part1 = part1(testInput)
    println("part1: $part1")
    check(part1 == 37)
    val part2 = part2(testInput)
    println("part2: $part2")
    check(part2 == 168)

    val input = readInput("day07/Day07")
    println(part1(input))
    println(part2(input))
}
