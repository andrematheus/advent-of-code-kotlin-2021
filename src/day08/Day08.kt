package day08

import Day

fun day08(): Day<*, *> {
    // segments: left = l, top = t, middle = m, bottom = b, right = r
    // t, tl, tr, m, bl, br, b
    val segmentsBySignal = mapOf(
        0 to listOf("t", "tr", "tl", "bl", "br", "b"),
        1 to listOf("tr", "br"),
        2 to listOf("t", "tr", "m", "bl", "b"),
        3 to listOf("t", "tr", "m", "br", "b"),
        4 to listOf("tl", "tr", "m", "br"),
        5 to listOf("t", "tl", "m", "br", "b"),
        6 to listOf("t", "tl", "m", "bl", "br", "b"),
        7 to listOf("t", "tr", "br"),
        8 to listOf("t", "tl", "tr", "m", "bl", "br", "b"),
        9 to listOf("t", "tl", "tr", "m", "br", "b")
    )
    val numbersBySegmentSet = segmentsBySignal.entries.associateBy({ it.value.toSet() }, { it.key })

    val letters: Array<Char> = "abcdefg".toCharArray().toTypedArray()

    val allSegments = setOf("t", "tl", "tr", "m", "bl", "br", "b")

    fun part1(input: List<String>): Int {
        return input.flatMap {
            it
                .split("|")[1]
                .trim()
                .split(" ")
        }
            .count { listOf(2, 4, 3, 7).contains(it.length) }
    }

    fun deductCode(signals: List<String>): Map<String, Set<Char>> {
        var code: Map<String, Set<Char>> = allSegments.associateWith { setOf(*letters) }

        fun applyConstraint(
            startingPossibleCodes: Map<String, Set<Char>>,
            signal: String,
            segments: List<String>
        ): Map<String, Set<Char>> {
            val possibleCodes = startingPossibleCodes.toMutableMap()
            val signalSet = signal.toSet()

            for (segment in segments) {
                possibleCodes[segment] = possibleCodes[segment]!!.intersect(signalSet)
            }

            for (otherSegment in allSegments.minus(segments.toSet())) {
                possibleCodes[otherSegment] = possibleCodes[otherSegment]!!.minus(signalSet)
            }
            return possibleCodes.toMap()
        }

        fun step(
            startingCode: Map<String, Set<Char>>,
            signals: List<Pair<String, Map<Int, List<String>>>>
        ): Map<String, Set<Char>>? {
            if (signals.isEmpty()) {
                return startingCode
            }
            val signal = signals.first()
            val possibleSegments = signal.second

            if (possibleSegments.isEmpty()) {
                return null
            } else {
                val result = possibleSegments.map { possibleSegmentList ->
                    applyConstraint(
                        startingCode,
                        signal.first,
                        possibleSegmentList.value
                    )
                }.filter {
                    it.all { entry -> entry.value.isNotEmpty() }
                }.map {
                    step(it, signals.drop(1))
                }

                return if (result.isEmpty()) {
                    null
                } else if (result.size > 1) {
                    throw IllegalStateException("Many solutions")
                } else {
                    result.first()
                }
            }
        }

        val signalsWithPossibleSegments =
            signals.associateWith { signal -> segmentsBySignal.filter { it.value.size == signal.length } }
                .toList()
                .sortedBy { it.first.length }

        code = step(code, signalsWithPossibleSegments)!!
        if (code.any { it.value.size != 1 }) {
            throw IllegalStateException("Invalid solution")
        }

        return code
    }

    fun decode(code: String, key: Map<String, Set<Char>>): Int {
        val inversed = key.entries.associateBy({ it.value.first() }, { it.key })

        fun decodeNumber(numberCode: String): Int {
            val segments = numberCode.map { inversed[it] }.toSet()
            return numbersBySegmentSet[segments]!!
        }

        return code.split(" ").map { decodeNumber(it) }.joinToString("").toInt()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split("|")
                .map { it.trim() }
                .let {
                    val (signals, output) = it
                    val key = deductCode(signals.split(" "))
                    decode(output, key)
                }
        }.sum()
    }

    return Day(8, 26, ::part1, 61229, ::part2)
}
