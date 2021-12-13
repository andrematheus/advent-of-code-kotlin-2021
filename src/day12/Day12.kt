package day12

import Day
import java.lang.IllegalStateException

fun day12(): Day<*, *> {
    data class Cave(val name: String) {
        val connectedTo = mutableListOf<Cave>()
        val isBig = "[A-Z]+".toRegex().matches(name)

        override fun toString(): String {
            return name
        }
    }

    fun readCaves(input: List<String>): Map<String, Cave> {
        val caves = mutableMapOf<String, Cave>()

        fun readLine(line: String) {
            val (cave1Name, cave2Name) = "^([^-]+)-(.+)$".toRegex().matchEntire(line)?.groupValues!!.drop(1)
            val cave1 = caves.computeIfAbsent(cave1Name, ::Cave)
            val cave2 = caves.computeIfAbsent(cave2Name, ::Cave)
            cave1.connectedTo.add(cave2)
            cave2.connectedTo.add(cave1)
        }

        input.forEach(::readLine)
        return caves
    }

    fun paths(input: List<String>): List<List<Cave>> {
        fun step(cave: Cave, visited: Set<Cave>, paths: List<List<Cave>>): List<List<Cave>> {
            val appendedPaths = paths.map { it + cave }

            return if (cave.name == "end") {
                appendedPaths
            } else {
                cave.connectedTo.flatMap {
                    if (!visited.contains(it) || it.isBig) {
                        step(it, visited + cave, appendedPaths)
                    } else {
                        listOf()
                    }
                }
            }
        }

        val caves = readCaves(input)
        return step(caves["start"]!!, setOf(), listOf(listOf()))
    }

    fun paths2(input: List<String>): List<List<Cave>> {
        fun step(cave: Cave, paths: List<List<Cave>>): List<List<Cave>> {
            val appendedPaths = paths.map { it + cave }
                .filter { caves ->
                    val duplicate = caves.filter { !it.isBig }
                        .groupBy { it.name }
                        .filter { it.value.size > 1 }
                    duplicate.size <= 1 && duplicate.none { it.key == "start" || it.key == "end" || it.value.size > 2 }
                }

            return if (cave.name == "end" || appendedPaths.isEmpty()) {
                appendedPaths
            } else {
                cave.connectedTo.flatMap {
                    step(it, appendedPaths)
                }
            }
        }

        val caves = readCaves(input)
        return step(caves["start"]!!, listOf(listOf()))
    }

    fun part1(input: List<String>): List<Int> {
        val problems = mutableListOf<MutableList<String>>(mutableListOf())
        for (line in input) {
            if (line == "---") {
                problems.add(mutableListOf())
            } else {
                problems.last().add(line)
            }
        }
        val solutions = problems.map { paths(it) }
        return solutions.map { it.size }
    }

    fun part2(input: List<String>): List<Int> {
        val problems = mutableListOf<MutableList<String>>(mutableListOf())
        for (line in input) {
            if (line == "---") {
                problems.add(mutableListOf())
            } else {
                problems.last().add(line)
            }
        }
        try {
            val solutions = problems.map { paths2(it) }
            return solutions.map { it.size }
        } catch (e: StackOverflowError){
            throw IllegalStateException("stack overflow")
        }
    }

    return Day(12, listOf(10, 19, 226), ::part1, listOf(36, 103, 3509), ::part2)
}
