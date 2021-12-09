import day01.day01
import day02.day02
import day03.day03
import day04.day04
import day05.day05
import day06.day06
import day07.day07
import day08.day08
import day09.day09

fun main() {
    for (day in listOf<Day<*, *>>(
        day01(),
        day02(),
        day03(),
        day04(),
        day05(),
        day06(),
        day07(),
        day08(),
        day09(),
    )) {
        day.run()
    }
}