package day18

import Day

data class Snail(val tokens: MutableList<SnailToken>) {
    sealed class SnailToken {
        data class Number(val value: Int) : SnailToken() {
            operator fun plus(other: Number): Number {
                return Number(value + other.value)
            }

            override fun toString(): String {
                return "${this.value.toString()},"
            }
        }

        object Open : SnailToken()
        object Close : SnailToken()


        override fun toString(): String {
            return when (this) {
                Close -> "]"
                is Number -> "${this.value},"
                Open -> "["
            }
        }
    }

    sealed class SnailPair {
        data class Number(val value: Int) : SnailPair()
        data class Pair(val left: SnailPair, val right: SnailPair) : SnailPair()

        fun magnitude(): Int {
            when (this) {
                is Number -> return this.value
                is Pair -> return (this.left.magnitude() * 3) + (this.right.magnitude() * 2)
            }
        }
    }

    fun parsePairs(): SnailPair {
        var idx = 0
        fun step(): SnailPair {
            val buffer = mutableListOf<SnailPair>()
            while (idx < tokens.lastIndex) {
                val tk = tokens[idx]
                when (tk) {
                    SnailToken.Open -> {
                        idx++
                        buffer.add(step())
                    }
                    SnailToken.Close -> {
                        idx++
                        return when (buffer.size) {
                            1 -> buffer[0]
                            2 -> SnailPair.Pair(buffer[0], buffer[1])
                            else -> throw IllegalStateException()
                        }
                    }
                    is SnailToken.Number -> {
                        buffer.add(SnailPair.Number(tk.value))
                        idx++
                    }
                }
            }
            return when (buffer.size) {
                1 -> buffer[0]
                2 -> SnailPair.Pair(buffer[0], buffer[1])
                else -> throw IllegalStateException()
            }
        }
        return step()
    }

    fun reduce() {
        while (true) {
            val idx = indexLeftMostNestedInsideFourPairs()
            if (idx != null) {
                explodeLeftMostNestedInsideFourPairs(idx)
            } else {
                val idx = indexOfNumberGreaterOrEqualThanTen()
                if (idx != null) {
                    split(idx)
                } else {
                    break
                }
            }
        }
    }

    fun indexLeftMostNestedInsideFourPairs(): Int? {
        var depth = 0
        tokens.forEachIndexed { idx, tk ->
            when (tk) {
                SnailToken.Open -> depth++
                SnailToken.Close -> depth--
                else -> {}
            }
            if (depth > 4
                && (idx + 2) <= tokens.lastIndex
                && tokens[idx + 1] is SnailToken.Number
                && tokens[idx + 2] is SnailToken.Number
            ) {
                return idx
            }
        }
        return null
    }

    private fun indexOfNumberGreaterOrEqualThanTen(): Int? {
        return tokens.indexOfFirst {
            when (it) {
                is SnailToken.Number -> it.value >= 10
                else -> false
            }
        }.let {
            if (it == -1) {
                null
            } else {
                it
            }
        }
    }

    fun explodeLeftMostNestedInsideFourPairs(idx: Int) {
        fun leftNumberOfIndex(idx: Int): Int? {
            for (i in idx downTo 0) {
                if (tokens[i] is SnailToken.Number) {
                    return i
                }
            }
            return null
        }

        fun rightNumberOfIndex(idx: Int): Int? {
            for (i in idx + 3..tokens.lastIndex) {
                if (tokens[i] is SnailToken.Number) {
                    return i
                }
            }
            return null
        }

        idx.let {
            val explodingPair = Pair(
                tokens[it + 1] as SnailToken.Number,
                tokens[it + 2] as SnailToken.Number
            )

            leftNumberOfIndex(it)?.let {
                tokens[it] = tokens[it] as SnailToken.Number + explodingPair.first
            }

            rightNumberOfIndex(it)?.let {
                tokens[it] = tokens[it] as SnailToken.Number + explodingPair.second
            }

            tokens[it] = SnailToken.Number(0)
            tokens.removeAt(it + 3)
            tokens.removeAt(it + 2)
            tokens.removeAt(it + 1)
        }
    }

    fun split(idx: Int) {
        val n = tokens[idx] as SnailToken.Number
        tokens.removeAt(idx)
        tokens.addAll(
            idx, listOf(
                SnailToken.Open,
                SnailToken.Number(n.value / 2),
                SnailToken.Number(if (n.value % 2 == 1) n.value / 2 + 1 else n.value / 2),
                SnailToken.Close,
            )
        )
    }

    operator fun plus(snail: Snail): Snail {
        val tokens = mutableListOf<SnailToken>()
        tokens.add(SnailToken.Open)
        tokens.addAll(this.tokens)
        tokens.addAll(snail.tokens)
        tokens.add(SnailToken.Close)
        return Snail(
            tokens
        ).also {
            it.reduce()
        }
    }

    override fun toString(): String {
        return "Snail<${tokens.map { it.toString() }.joinToString("")}>"
    }

    companion object {
        fun of(tokens: List<SnailToken>): Snail {
            return Snail(tokens.toMutableList())
        }

        fun fromString(input: String): Snail {
            val tokens = mutableListOf<SnailToken>()
            var buffer = ""

            for (c in input) {
                if (!c.isDigit() && buffer.isNotEmpty()) {
                    tokens.add(SnailToken.Number(buffer.toInt()))
                    buffer = ""
                }
                when (c) {
                    '[' -> tokens.add(SnailToken.Open)
                    ']' -> tokens.add(SnailToken.Close)
                    ',' -> {}
                    else -> buffer += c
                }
            }
            return Snail(tokens)
        }
    }
}

fun day18(): Day<*, *> {
    fun part1(input: List<String>): Int {
        return input.map(Snail::fromString)
            .reduce(Snail::plus)
            .parsePairs().magnitude()
    }

    fun part2(input: List<String>): Int {
        val snails = input.map(Snail::fromString)
        return snails.flatMap { snail ->
            snails.map {
                Pair(
                    Snail(snail.tokens.toList().toMutableList()),
                    Snail(it.tokens.toList().toMutableList())
                )
            }

        }
            .filter { it.first != it.second }
            .mapIndexed { idx, it ->
                it.first + it.second
            }
            .mapIndexed { idx, it ->
                it.parsePairs()
            }
            .maxByOrNull { it.magnitude() }!!.magnitude()
    }

    return Day(18, 4140, ::part1, 3993, ::part2)
}
