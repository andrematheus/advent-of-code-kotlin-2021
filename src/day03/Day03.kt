package day03

import Day

fun day03(): Day<Int, Int> {
    fun part1(input: List<String>): Int {
        assert(input.isNotEmpty())
        val countsOfOnes = MutableList(input[0].length) { i -> 0 }

        for (line in input) {
            line.onEachIndexed { index, c ->
                if (c == '1') {
                    countsOfOnes[index]++
                }
            }
        }

        val gammaRateBuilder = StringBuilder()
        val epsilonRateBuilder = StringBuilder()

        for (count in countsOfOnes) {
            if (count > input.size / 2) {
                gammaRateBuilder.append('1')
                epsilonRateBuilder.append('0')
            } else {
                gammaRateBuilder.append('0')
                epsilonRateBuilder.append('1')
            }
        }

        val gammaRate = Integer.parseInt(gammaRateBuilder.toString(), 2)
        val epsilonRate = Integer.parseInt(epsilonRateBuilder.toString(), 2)
        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        var candidates = input
        var oxygenGeneratorRatingString: String? = null

        for (i in 0..input[0].length) {
            val result = candidates.partition { it[i] == '1' }
            candidates = if (result.first.size >= result.second.size) {
                result.first
            } else {
                result.second
            }
            if (candidates.size == 1) {
                oxygenGeneratorRatingString = candidates[0]
                break
            }
        }

        candidates = input
        var co2ScrubberRatingString: String? = null

        for (i in 0..input[0].length) {
            val result = candidates.partition { it[i] == '1' }
            candidates = if (result.first.size >= result.second.size) {
                result.second
            } else {
                result.first
            }
            if (candidates.size == 1) {
                co2ScrubberRatingString = candidates[0]
                break
            }
        }

        if (oxygenGeneratorRatingString != null && co2ScrubberRatingString != null) {
            val oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatingString, 2)
            val co2ScrubberRating = Integer.parseInt(co2ScrubberRatingString, 2)
            return oxygenGeneratorRating * co2ScrubberRating
        } else {
            throw IllegalArgumentException()
        }
    }

    return Day(3, 198, ::part1, 230, ::part2)
}
