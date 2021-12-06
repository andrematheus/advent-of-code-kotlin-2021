package day05

import readInput

fun day05() {

    fun markPoints(
        hits: MutableMap<Pair<Int, Int>, Int>, from: Pair<Int, Int>, to: Pair<Int, Int>
    ) {
        var x1 = from.first
        var y1 = from.second
        val x2 = to.first
        val y2 = to.second

        while (true) {
            hits[Pair(x1, y1)] = hits.getOrDefault(Pair(x1, y1), 0) + 1
            if (x1 == x2 && y1 == y2) break
            if (x1 < x2) x1++
            if (y1 < y2) y1++
            if (x1 > x2) x1--
            if (y1 > y2) y1--
        }
    }

    fun readPoints(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val lineRegex = "([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)".toRegex()

        return input.map { lineRegex.matchEntire(it)!! }
            .map { it.groupValues.drop(1).map(String::toInt) }
            .map { Pair(Pair(it[0], it[1]), Pair(it[2], it[3])) }
    }

    fun crosses(points: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        val hits: MutableMap<Pair<Int, Int>, Int> = mutableMapOf<Pair<Int, Int>, Int>()
        for (point in points) {
            markPoints(hits, point.first, point.second)
        }
        return hits.count { it.value > 1 }
    }

    fun part1(input: List<String>): Int {
        return readPoints(input)
            .filter { it.first.first == it.second.first || it.first.second == it.second.second }
            .let { crosses(it) }
    }

    fun part2(input: List<String>): Int {
        return crosses(readPoints(input))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    val part1 = part1(testInput)
    println("part1: $part1")
    check(part1 == 5)
    val part2 = part2(testInput)
    println("part2: $part2")
    check(part2 == 12)

    val input = readInput("day05/Day05")
    println(part1(input))
    println(part2(input))
}
