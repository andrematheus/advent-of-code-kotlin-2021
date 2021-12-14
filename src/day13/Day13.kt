package day13

import Day

enum class Direction {
    Vertical, Horizontal
}

data class Dot(val x: Int, val y: Int)
data class Fold(val coord: Int, val direction: Direction)

fun Set<Dot>.display() {
    val maxX = this.maxOf { it.x }
    val maxY = this.maxOf { it.y }

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            if (this.contains(Dot(x, y))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}

fun day13(): Day<*, *> {
    class Dots(var dots: MutableSet<Dot> = mutableSetOf()) {

        fun mark(dot: Dot) {
            dots.add(dot)
        }

        fun fold(at: Fold) {
            this.dots = dots.filter { !atFold(it, at) }
                .map {
                    if (afterFold(it, at)) {

                        val m = mirror(it, at)
                        m
                    } else {
                        it
                    }
                }.toMutableSet()
        }

        fun atFold(dot: Dot, fold: Fold): Boolean {
            return when (fold.direction) {
                Direction.Vertical -> dot.x == fold.coord
                Direction.Horizontal -> dot.y == fold.coord
            }
        }

        fun afterFold(dot: Dot, fold: Fold): Boolean {
            return when (fold.direction) {
                Direction.Vertical -> dot.x > fold.coord
                Direction.Horizontal -> dot.y > fold.coord
            }
        }

        fun mirror(dot: Dot, fold: Fold): Dot {
            return when (fold.direction) {
                Direction.Vertical -> {
                    val distance = dot.x - fold.coord
                    dot.copy(x = fold.coord - distance)
                }
                Direction.Horizontal -> {
                    val distance = dot.y - fold.coord
                    dot.copy(y = fold.coord - distance)
                }
            }
        }
    }

    fun doTheFolds(input: List<String>, howMany: Int? = null): Dots {
        val (dotDescriptions, folds) = input.indexOf("").let {
            Pair(
                input.subList(0, it),
                input.subList(it + 1, input.lastIndex + 1)
            )
        }
        val dots = Dots()

        dotDescriptions.map { dd ->
            dd.split(",")
                .map(String::toInt)
                .let { Dot(it[0], it[1]) }
                .also { dots.mark(it) }
        }
        folds.map {
            it.split("=")
                .let {
                    Fold(
                        it[1].toInt(),
                        if (it[0].last() == 'y') {
                            Direction.Horizontal
                        } else {
                            Direction.Vertical
                        }
                    )
                }
        }.let { if (howMany == null) it else it.take(howMany) }.forEach {
            dots.fold(it)
        }
        return dots
    }

    fun part1(input: List<String>): Int {
        val dots = doTheFolds(input, 1)
        return dots.dots.size
    }

    fun part2(input: List<String>) {
        val dots = doTheFolds(input)
        dots.dots.display()
    }

    return Day(13, 17, ::part1, Unit, ::part2)
}
