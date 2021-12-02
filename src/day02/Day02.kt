package day02

import readInput

fun day02() {
    @Deprecated("Use Position")
    data class WrongPosition(val horizontal: Int, val depth: Int) {
        fun forward(by: Int) = this.copy(horizontal = horizontal + by)
        fun down(by: Int) = this.copy(depth = depth + by)
        fun up(by: Int) = this.copy(depth = depth - by)

        fun applyCommand(commandLine: String): WrongPosition {
            val commandPair = commandLine.split(" ", limit = 2).let { Pair(it[0], it[1].toInt()) }
            return when (commandPair.first) {
                "forward" -> this::forward
                "down" -> this::down
                "up" -> this::up
                else -> throw IllegalArgumentException()
            }.invoke(commandPair.second)
        }
    }

    data class Position(val aim: Int, val horizontal: Int, val depth: Int) {
        fun forward(by: Int) = this.copy(
            horizontal = horizontal + by,
            depth = depth + (aim * by)
        )

        fun down(by: Int) = this.copy(aim = aim + by)
        fun up(by: Int) = this.copy(aim = aim - by)

        fun applyCommand(commandLine: String): Position {
            val commandPair = commandLine.split(" ", limit = 2).let { Pair(it[0], it[1].toInt()) }
            return when (commandPair.first) {
                "forward" -> this::forward
                "down" -> this::down
                "up" -> this::up
                else -> throw IllegalArgumentException()
            }.invoke(commandPair.second)
        }
    }

    fun part1(input: List<String>): Int {
        val position: WrongPosition = input.fold(WrongPosition(0, 0), WrongPosition::applyCommand)
        return position.horizontal * position.depth
    }

    fun part2(input: List<String>): Int {
        val position: Position = input.fold(Position(0, 0, 0), Position::applyCommand)
        return position.horizontal * position.depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("day02/Day02")
    println(part1(input))
    println(part2(input))
}
