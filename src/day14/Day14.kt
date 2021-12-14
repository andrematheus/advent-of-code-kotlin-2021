package day14

import Day

fun day14(): Day<*, *> {
    fun findPolymer(input: List<String>, steps: Int): Long {
        val start = input[0].windowed(2, partialWindows = true)
            .toMutableList()
        val rules = input.drop(2).map {
            it.split("->")
                .map { it.trim() }
        }
            .map {
                it.first() to it[1]
            }
            .toMap()

        val memory = mutableMapOf<Pair<String, Int>, Map<Char, Long>>()
        var actualSteps = 0

        fun step(pair: String, currStep: Int, untilStep: Int, charsWithFreq: MutableMap<Char, Long>): Map<Char, Long> {
            actualSteps++
            if (memory.containsKey(Pair(pair, untilStep - currStep))) {
                val m = memory[Pair(pair, untilStep - currStep)]!!
                m.forEach {
                    charsWithFreq[it.key] = charsWithFreq.getOrDefault(it.key, 0L) + it.value
                }
                return m
            } else if (pair.length == 1 || !rules.containsKey(pair) || currStep == untilStep) {
                charsWithFreq[pair[0]] = charsWithFreq.getOrDefault(pair[0], 0L) + 1L
                return mapOf()
            } else {
                val charToAdd = rules[pair]!!
                val m = mutableMapOf<Char, Long>()
                step(pair[0] + charToAdd, currStep + 1, untilStep, m)
                step(charToAdd + pair[1], currStep + 1, untilStep, m)
                memory.put(Pair(pair, untilStep - currStep), m)
                m.forEach {
                    charsWithFreq[it.key] = charsWithFreq.getOrDefault(it.key, 0L) + it.value
                }
                return m
            }
        }

        val charsWithFreq = mutableMapOf<Char, Long>()

        for (s in start) {
            step(s, 0, steps, charsWithFreq)
        }

        val max = charsWithFreq.maxOf { it.value }
        val min = charsWithFreq.minOf { it.value }
        println("Finished in $actualSteps")
        return max - min
    }

    fun part1(input: List<String>): Long {
        return findPolymer(input, 10)
    }

    fun part2(input: List<String>): Long {
        return findPolymer(input, 40)
    }

    return Day(14, 1588, ::part1, 2188189693529, ::part2)
}
