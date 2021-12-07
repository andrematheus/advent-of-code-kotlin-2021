import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Day<T, U>(
    val number: Int,
    val part1Expected: T,
    val part1: (List<String>) -> T,

    val part2Expected: U?,
    val part2: ((List<String>) -> U)?,
) {
    fun run() {
        val numberStr = number.toString().padStart(2, '0')
        val testInput = readInput("day$numberStr/Day${numberStr}_test")
        val part1TestResult = part1(testInput)
        println("day $numberStr part1 test: $part1TestResult")
        check(part1TestResult == part1Expected)

        val input = readInput("day$numberStr/Day$numberStr")
        println(part1(input))

        if (part2Expected != null && part2 != null) {
            val part2TestResult = part2.invoke(testInput)
            println("day $numberStr part2 test: $part2TestResult")
            check(part2TestResult == part2Expected)
            println(part2.invoke(input))
        }
    }
}
