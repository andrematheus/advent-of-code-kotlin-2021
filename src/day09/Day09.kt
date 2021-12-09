package day09

import Day

fun day09(): Day<*, *> {
    fun lowPoints(floorMap: List<List<Int>>): List<Pair<Pair<Int, Int>, Int>> {
        val extendedFloorMap = (listOf(null) + floorMap + listOf(null))

        fun lowPointsAtSlice(startingIndex: Int, floorMapSlice: List<List<Int>?>): List<Pair<Pair<Int, Int>, Int>> {
            val (prev, curr, nex) = floorMapSlice
            return curr?.mapIndexed { idx, it ->
                val neighbours = listOf(
                    // top left
                    prev?.getOrNull(idx - 1),
                    // top
                    prev?.getOrNull(idx),
                    // top right
                    prev?.getOrNull(idx + 1),
                    // left
                    curr.getOrNull(idx - 1),
                    // right
                    curr.getOrNull(idx + 1),
                    // bottom left
                    nex?.getOrNull(idx - 1),
                    // bottom
                    nex?.getOrNull(idx),
                    // bottom right
                    nex?.getOrNull(idx + 1),
                ).filterNotNull()
                val isBottom = neighbours.all { neighbour -> neighbour > it }

                if (isBottom) {
                    Pair(Pair(startingIndex, idx), it)
                } else {
                    null
                }
            }?.filterNotNull() ?: listOf()
        }

        return extendedFloorMap.windowed(3, partialWindows = false)
            .withIndex()
            .flatMap { lowPointsAtSlice(it.index, it.value) }
    }

    fun part1(input: List<String>): Int {
        fun riskLevels(lowPoints: List<Int>): List<Int> {
            return lowPoints.map { it + 1 }
        }

        val floorMap = input.map { it.map { c -> c.toString().toInt() } }
        return riskLevels(lowPoints(floorMap).map { it.second }).sum()
    }

    fun part2(input: List<String>): Int {
        val floorMap = input.map { it.map { c -> c.toString().toInt() } }

        fun findBasin(lowPoint: Pair<Pair<Int, Int>, Int>): List<Pair<Pair<Int, Int>, Int>> {
            val neighboursPartOfBasin = listOf(
                // top
                Pair(
                    Pair(lowPoint.first.first - 1, lowPoint.first.second),
                    floorMap.getOrNull(lowPoint.first.first - 1)?.getOrNull(lowPoint.first.second),
                ),
                // left
                Pair(
                    Pair(lowPoint.first.first, lowPoint.first.second - 1),
                    floorMap.getOrNull(lowPoint.first.first)?.getOrNull(lowPoint.first.second - 1),
                ),
                // right
                Pair(
                    Pair(lowPoint.first.first, lowPoint.first.second + 1),
                    floorMap.getOrNull(lowPoint.first.first)?.getOrNull(lowPoint.first.second + 1),
                ),
                // bottom
                Pair(
                    Pair(lowPoint.first.first + 1, lowPoint.first.second),
                    floorMap.getOrNull(lowPoint.first.first + 1)?.getOrNull(lowPoint.first.second),
                ),
            )
                .filter { it.second != null }
                .filter { it.second != 9 && it.second!! > lowPoint.second }
            return (listOf(lowPoint) + neighboursPartOfBasin.flatMap { findBasin(it as Pair<Pair<Int, Int>, Int>) }).distinct()
        }

        val lowPoints = lowPoints(floorMap)
        return lowPoints.map { findBasin(it) }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .reduce(Int::times)
    }

    return Day(9, 15, ::part1, 1134, ::part2)
}
