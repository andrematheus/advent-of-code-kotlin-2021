package day04

import Day

class Board(private val numbers: List<List<Int>>) {
    sealed class Group {
        data class Row(val index: Int) : Group()
        data class Column(val index: Int) : Group()
    }

    val score: Int
        get() {
            var sumOfNonMarkedNumbers = 0
            this.numbers.forEachIndexed { row, columns ->
                columns.forEachIndexed { column, number ->
                    if (!marks[row][column]) {
                        sumOfNonMarkedNumbers += number
                    }
                }
            }
            return sumOfNonMarkedNumbers * (winnerDraw ?: 0)
        }

    private val marks: MutableList<MutableList<Boolean>> = MutableList(5) { _ -> MutableList(5) { _ -> false } }
    private var winnerGroup: Group? = null
    private var winnerDraw: Int? = null

    val won: Boolean
        get()= this.winnerGroup != null

    fun update(draw: Int) {
        numbers.forEachIndexed { row, sublist ->
            sublist.forEachIndexed { col, n ->
                if (n == draw) {
                    markAndCheck(row, col)
                    if (won) {
                        winnerDraw = draw
                    }
                }
            }
        }
    }

    private fun markAndCheck(row: Int, col: Int) {
        marks[row][col] = true
        checkRow(row)
        checkCol(col)
    }

    private fun checkRow(index: Int) {
        val group = Group.Row(index)
        val marks = getMarks(group)
        if (marks.all { it }) {
            winnerGroup = group
        }
    }

    private fun checkCol(index: Int) {
        val group = Group.Column(index)
        val marks = getMarks(group)
        if (marks.all { it }) {
            winnerGroup = group
        }
    }

    private fun getMarks(group: Group): List<Boolean> {
        return when (group) {
            is Group.Row -> this.marks.get(group.index)
            is Group.Column -> this.marks.map { it.get(group.index) }
        }
    }
}

fun day04(): Day<Int, Int> {
    fun readDraws(line: String): List<Int> {
        return line
            .split(",")
            .map(String::toInt)
    }

    fun readBoard(lines: List<String>): Board {
        assert(lines.size == 5)
        return lines.map {
            it.trim().split("\\s+".toRegex())
                .map(String::toInt)
        }.let {
            Board(it)
        }
    }

    fun part1(input: List<String>): Int {
        val draws = readDraws(input[0])
        val boards = input.drop(1)
            .chunked(6)
            .map {
                readBoard(it.drop(1))
            }
        for (draw in draws) {
            boards.map { it.update(draw) }
            for (board in boards) {
                if (board.won) {
                    return board.score
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val draws = readDraws(input[0])
        val boards: MutableList<Board> = input.drop(1)
            .chunked(6)
            .map {
                readBoard(it.drop(1))
            }.toMutableList()
        var lastWinner: Board? = null

        for (draw in draws) {
            boards.map { it.update(draw) }
            boards
                .filter { it.won }
                .map {
                    lastWinner = it
                    boards.remove(it)
                }
        }

        return lastWinner?.score ?: 0
    }

    return Day(4, 4512, ::part1, 1924, ::part2)
}
