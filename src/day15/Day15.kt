package day15

import Day
import java.util.*

fun day15(): Day<*, *> {
    fun step(cave: List<List<Int>>): Int {
        val distsFromOrigin = MutableList(cave.size) { _ -> MutableList<Int?>(cave.size) { _ -> null } }

        val next = PriorityQueue<Pair<Int, Pair<Int, Int>>>() { n1, n2 ->
            n1.first.compareTo(n2.first)
        }
        val visited = mutableSetOf<Pair<Int, Int>>()
        next.add(Pair(0, Pair(0, 0)))
        distsFromOrigin[0][0] = 0

        while (next.isNotEmpty()) {
            val (dist, coords) = next.poll()
            val currDist = distsFromOrigin[coords.first][coords.second]
            if (currDist == null || dist < currDist) {
                distsFromOrigin[coords.first][coords.second] = dist
            }
            if (visited.contains(coords)) {
                continue
            }
            listOf(
                Pair(coords.first - 1, coords.second),
                Pair(coords.first + 1, coords.second),
                Pair(coords.first, coords.second - 1),
                Pair(coords.first, coords.second + 1),
            ).filter { it.first >= 0 && it.second >= 0 && it.first < cave.size && it.second < cave.size }
                .forEach {
                    if (!visited.contains(it)) {
                        val distToIt = cave[it.first][it.second] + dist
                        next.offer(Pair(distToIt, it))
                    }
                }
            visited.add(coords)
        }

        return distsFromOrigin.last().last()!!
    }

    fun part1(input: List<String>): Int {
        val cave = input.map { line -> line.map { it.toString().toInt() } }
        return step(cave)
    }

    fun part2(input: List<String>): Int {
        fun Int.wrappingInc(): Int {
            val res = this.inc()
            return if (res == 10) {
                1
            } else {
                res
            }
        }

        fun expand(cave: List<List<Int>>): List<List<Int>> {
            val caveSize = cave.size
            val expandedCaves = MutableList(caveSize * 5) { _ -> MutableList(caveSize * 5) { _ -> 0 } }

            fun incCave(from: Pair<Int, Int>, to: Pair<Int, Int>) {
                for (row in from.first until from.first + caveSize) {
                    for (column in from.second until from.second + caveSize) {
                        expandedCaves[to.first + (row - from.first)][to.second + (column - from.second)] =
                            expandedCaves[row][column].wrappingInc()
                    }
                }
            }

            for (row in 0 until caveSize) {
                for (column in 0 until caveSize) {
                    expandedCaves[row][column] = cave[row][column]
                }
            }

            for (caveRow in 0 until 5) {
                val row = caveRow * caveSize
                if (row - caveSize >= 0) {
                    incCave(Pair(row - caveSize, 0), Pair(row, 0))
                }
                for (caveColumn in 0 until 4) {
                    val column = caveColumn * caveSize
                    incCave(Pair(row, column), Pair(row, column + caveSize))
                }
            }
            return expandedCaves
        }

        val cave = expand(input.map { line -> line.map { it.toString().toInt() } })

        return step(cave)
    }

    return Day(15, 40, ::part1, 315, ::part2)
}
