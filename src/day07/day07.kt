package day07

import Day
import kotlin.math.abs

fun day07(): Day<Int, Int> {
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

    return Day(7, 37, ::part1, 168, ::part2)
}
