package day02

import Day

fun day02(): Day<Int, Int> {
    fun part1(input: List<String>): Int {
        data class Position(val horizontal: Int, val depth: Int)

        val position = input.map { line ->
            line.split(" ", limit = 2)
                .let { coords -> Pair(coords[0], coords[1].toInt()) }
        }.fold(Position(0, 0)) { (horizontal, depth), (command, by) ->
            when (command) {
                "forward" -> Position(horizontal + by, depth)
                "down" -> Position(horizontal, depth + by)
                "up" -> Position(horizontal, depth - by)
                else -> throw IllegalArgumentException()
            }
        }
        return position.horizontal * position.depth
    }

    fun part2(input: List<String>): Int {
        data class Position(val aim: Int, val horizontal: Int, val depth: Int)

        val position = input.map { line ->
            line.split(" ", limit = 2)
                .let { coords -> Pair(coords[0], coords[1].toInt()) }
        }.fold(Position(0, 0, 0)) { acc, (command, by) ->
            when (command) {
                "forward" -> acc.copy(
                    horizontal = acc.horizontal + by,
                    depth = acc.depth + (acc.aim * by)
                )
                "down" -> acc.copy(aim = acc.aim + by)
                "up" -> acc.copy(aim = acc.aim - by)
                else -> throw IllegalArgumentException()
            }
        }

        return position.horizontal * position.depth
    }

    return Day(2, 150, ::part1, 900, ::part2)
}
