package day11

import Day

fun day11(): Day<*, *> {
    class OctopusesModel(input: List<String>) {
        val octopuses = input.map { it.map { c -> c.toString().toInt() }.toMutableList() }.toMutableList()
        var flashes = 0
        val flashingInStep = mutableListOf<Pair<Int, Int>>()
        var step = 0

        fun flash(row: Int, column: Int) {
            flashes++
            for (coord in listOf(
                Pair(row - 1, column - 1),
                Pair(row - 1, column),
                Pair(row - 1, column + 1),

                Pair(row, column - 1),
                Pair(row, column + 1),

                Pair(row + 1, column - 1),
                Pair(row + 1, column),
                Pair(row + 1, column + 1),
            )) {
                octopuses.getOrNull(coord.first)?.also {
                    if (coord.second >= 0 && coord.second <= it.lastIndex) {
                        it[coord.second] = it[coord.second].inc()
                    }
                }
            }
            octopuses[row][column] = 0
            flashingInStep.add(Pair(row, column))
        }

        fun loop(until: OctopusesModel.() -> Boolean) {
            while(this.until()) {
                step++
                flashingInStep.clear()
                octopuses.forEach { row -> row.forEachIndexed { i, _ -> row[i]++ } }
                while (octopuses.flatten().any { it > 9 }) {
                    octopuses.forEachIndexed { row, l ->
                        l.forEachIndexed { column, n ->
                            if (n > 9) {
                                flash(row, column)
                            }
                        }
                    }
                }
                flashingInStep.forEach {
                    octopuses[it.first][it.second] = 0
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val problem = OctopusesModel(input)
        problem.loop { step < 100 }
        return problem.flashes
    }

    fun part2(input: List<String>): Int {
        val problem = OctopusesModel(input)
        problem.loop { flashingInStep.size < 100 }
        return problem.step
    }

    return Day(11, 1656, ::part1, 195, ::part2)
}
