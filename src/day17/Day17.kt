package day17

import Day
import kotlin.math.*

fun day17(): Day<*, *> {
    data class TargetArea(val xrange: IntRange, val yrange: IntRange) {
        val farthest: Int
            get() = max(xrange.first, xrange.last)

        val bottom: Int
            get() = min(yrange.first, yrange.last)
    }
    data class Probe(var position: Pair<Int, Int>, var velocities: Pair<Int, Int>)

    fun solveProbe(targetArea: TargetArea, velocitities: Pair<Int, Int>): Int? {
        var iterations = 0
        val probe = Probe(Pair(0, 0), velocitities)
        var maxY = 0
        var passedThrough = false

        while (true) {
            probe.position = probe.position.copy(
                first = probe.position.first + probe.velocities.first,
                second = probe.position.second + probe.velocities.second,
            )
            probe.velocities = probe.velocities.copy(
                first = when {
                    probe.velocities.first > 0 -> probe.velocities.first - 1
                    probe.velocities.first < 0 -> probe.velocities.first + 1
                    else -> 0
                },
                second = probe.velocities.second - 1
            )

            if (targetArea.xrange.contains(probe.position.first)
                && targetArea.yrange.contains(probe.position.second)
            ) {
                passedThrough = true
            }
            if (probe.position.second > maxY
            ) {
                maxY = probe.position.second
            }
            if (probe.position.first > targetArea.farthest ||
                probe.position.second < targetArea.bottom
            ) {
                if (passedThrough) {
                    return maxY
                } else {
                    return null
                }
            }
            iterations++
        }
    }

    fun parseInput(input: List<String>): Pair<TargetArea, Int> {
        val inputRegex = "target area: x=([-]?[0-9]+)..([-]?[0-9]+), y=([-]?[0-9]+)..([-]?[0-9]+)"
        val parameters = inputRegex.toRegex().matchEntire(input[0])!!.groups.drop(1).map { it!!.value.toInt() }
            .toMutableList()
        if (parameters[1] < parameters[0]) {
            val tmp = parameters[0]
            parameters[0] = parameters[1]
            parameters[1] = tmp
        }
        if (parameters[3] < parameters[2]) {
            val tmp = parameters[2]
            parameters[2] = parameters[3]
            parameters[3] = tmp
        }
        val targetArea = TargetArea(
            parameters[0]..parameters[1],
            parameters[2]..parameters[3]
        )

        val ampli = max(
            abs(parameters[2]),
            abs(parameters[3])
        )
        return Pair(targetArea, ampli)
    }

    fun part1(input: List<String>): Int {
        var overallMaxY = 0
        val (targetArea, ampli) = parseInput(input)

        for (x in 0..targetArea.xrange.last) {
            for (y in (-1 * ampli)..ampli) {
                solveProbe(targetArea, Pair(x, y)).let {
                    if (it != null && it > overallMaxY) {
                        overallMaxY = it
                    }
                }
            }
        }

        return overallMaxY
    }

    fun part2(input: List<String>): Int {
        val (targetArea, ampli) = parseInput(input)
        val solutions = mutableListOf<Pair<Int, Int>>()

        for (x in 0..targetArea.xrange.last) {
            for (y in (-1 * ampli)..ampli) {
                solveProbe(targetArea, Pair(x, y)).let {
                    if (it != null) {
                        solutions.add(Pair(x, y))
                    }
                }
            }
        }

        return solutions.size
    }

    return Day(17, 45, ::part1, 112, ::part2)
}
