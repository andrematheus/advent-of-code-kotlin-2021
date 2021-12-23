package day19

import Day

enum class Directions {
    X, Y, Z, XN, YN, ZN;

    fun dir(): String = when (this) {
        X -> "x"
        Y -> "y"
        Z -> "z"
        XN -> "x"
        YN -> "y"
        ZN -> "z"
    }

    fun neg(): Boolean = when (this) {
        X -> false
        Y -> false
        Z -> false
        XN -> true
        YN -> true
        ZN -> true
    }
}

data class Direction(val x: Directions, val y: Directions, val z: Directions) {
    companion object {
        val all = listOf(
            Direction(Directions.X, Directions.Y, Directions.Z),
            Direction(Directions.X, Directions.ZN, Directions.Y),
            Direction(Directions.X, Directions.YN, Directions.ZN),
            Direction(Directions.X, Directions.Z, Directions.YN),

            Direction(Directions.Z, Directions.Y, Directions.XN),
            Direction(Directions.XN, Directions.Y, Directions.ZN),
            Direction(Directions.ZN, Directions.Y, Directions.X),

            Direction(Directions.YN, Directions.X, Directions.Z),
            Direction(Directions.XN, Directions.YN, Directions.Z),
            Direction(Directions.YN, Directions.XN, Directions.Z),
        )

        val default = Direction(Directions.X, Directions.Y, Directions.Z)
    }
}

data class Pos(val x: Int, val y: Int, val z: Int) {
    fun getNamed(name: String): Int {
        return when (name) {
            "x" -> this.x
            "y" -> this.y
            "z" -> this.z
            else -> throw IllegalStateException()
        }
    }

    fun move(by: Pos): Pos {
        return Pos(
            this.x + by.x,
            this.y + by.y,
            this.z + by.z,
        )
    }

    operator fun minus(other: Pos): Pos {
        return Pos(
            this.x - other.x,
            this.y - other.y,
            this.z - other.z,
        )
    }
}

data class Reading(val pos: Pos, val direction: Direction = Direction.default) {
    fun reinterpret(to: Direction): Reading {
        val x = pos.getNamed(to.x.dir()) * (if (to.x.neg()) -1 else 1)
        val y = pos.getNamed(to.y.dir()) * (if (to.y.neg()) -1 else 1)
        val z = pos.getNamed(to.z.dir()) * (if (to.z.neg()) -1 else 1)
        return Reading(Pos(x, y, z), to)
    }

    fun orientations(): Set<Reading> {
        return Direction.all.map { this.reinterpret(it) }.toSet()
    }

    fun move(by: Pos): Reading {
        return this.copy(pos = this.pos.move(by))
    }
}

data class Scanner(
    val readings: Set<Reading>,
    val direction: Direction = Direction.default,
    val pos: Pos = Pos(0, 0, 0),
) {
    fun reinterpret(to: Direction): Scanner {
        return Scanner(readings.map { it.reinterpret(to) }.toSet(), to)
    }

    fun orientations(): List<Scanner> {
        return Direction.all.map { this.reinterpret(it) }
    }

    fun move(by: Pos): Scanner {
        return Scanner(
            readings.map { it.move(by) }.toSet(),
            pos = this.pos.move(by)
        )
    }

    fun align(to: Reading): Scanner {
        val diff = to.pos - this.readings.first().pos
        return this.move(diff)
    }

    fun hasSharedReadings(other: Scanner): Boolean {
        return this.readings.toSet().intersect(
            other.readings.toSet()
        ).also {
            if (it.size > 1) {
                println("readings: ${it.size}")
            }
        }.size >= 12
    }

    fun alignsTo(other: Scanner): Scanner? {
        val possibilities = mutableSetOf<Scanner>()
        val firstOrientation = this
        for (secondOrientation in other.orientations()) {
            val reorientation = secondOrientation.reinterpret(firstOrientation.direction)
            for (firstReading in firstOrientation.readings) {
                for (secondReading in reorientation.readings) {
                    val diff = firstReading.pos - secondReading.pos
                    val aligned = reorientation.move(diff)
                    if (firstOrientation.hasSharedReadings(aligned)) {
                        possibilities.add(aligned)
                    }
                }
            }
        }

        return possibilities.firstOrNull()
    }
}

fun readScanners(input: List<String>): MutableList<Scanner> {
    val scanners = mutableListOf<Scanner>()
    val currentReadings = mutableListOf<Reading>()

    for (line in input) {
        if (line.startsWith("--- scanner ")) {
            if (currentReadings.isNotEmpty()) {
                val scanner = Scanner(currentReadings.toSet())
                scanners.add(scanner)
                currentReadings.clear()
            }
        } else if (line != "") {
            val coords = line.split(",").map { -it.toInt() }
            val reading = Reading(Pos(coords[0], coords[1], coords[2]))
            currentReadings.add(reading)
        }
    }
    val scanner = Scanner(currentReadings.toSet())
    scanners.add(scanner)
    return scanners
}

fun day19(): Day<*, *> {
    fun part1(input: List<String>): Int {
       TODO()
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    return Day(19, 79, ::part1, null, ::part2)
}
