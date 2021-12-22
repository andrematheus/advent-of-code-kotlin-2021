package day18

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.*

internal class SnailTest {
    @Test
    internal fun shouldParseAndAdd() {
        assertEquals(
            Snail.fromString("[[1,2],[[3,4],5]]"),
            Snail.fromString("[1,2]") + Snail.fromString("[[3,4],5]")
        )
    }

    @Test
    internal fun shouldParseAndAddSecondExample() {
        assertEquals(
            Snail.fromString("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"),
            Snail.fromString("[[[[4,3],4],4],[7,[[8,4],9]]]") + Snail.fromString("[1,1]")
        )
    }

    @ParameterizedTest
    @MethodSource("listsToAdd")
    internal fun shouldSumFirstExampleList(numbers: List<Snail>, expected: Snail) {
        assertEquals(
            expected,
            numbers.reduce(Snail::plus)
        )
    }

    @ParameterizedTest
    @MethodSource("snailsToExplode")
    internal fun explodeTests(source: String, expected: String) {
        val sourceSnail = Snail.fromString(source)
        val expectedSnail = Snail.fromString(expected)
        val idx = sourceSnail.indexLeftMostNestedInsideFourPairs()!!
        sourceSnail.explodeLeftMostNestedInsideFourPairs(idx)
        assertEquals(expectedSnail, sourceSnail)
    }

    @ParameterizedTest
    @MethodSource("magnitudeExamples")
    internal fun magnitudeTests(source: Snail.SnailPair, expected: Int) {
        assertEquals(expected, source.magnitude())
    }

    companion object {
        @JvmStatic
        fun snailsToExplode(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"),
                Arguments.arguments("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]"),
                Arguments.arguments("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"),
                Arguments.arguments("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
                Arguments.arguments("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
            )
        }

        @JvmStatic
        fun listsToAdd(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[1,1]"),
                        Snail.fromString("[2,2]"),
                        Snail.fromString("[3,3]"),
                        Snail.fromString("[4,4]"),
                    ),
                    Snail.fromString("[[[[1,1],[2,2]],[3,3]],[4,4]]"),
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[1,1]"),
                        Snail.fromString("[2,2]"),
                        Snail.fromString("[3,3]"),
                        Snail.fromString("[4,4]"),
                        Snail.fromString("[5,5]"),
                    ),
                    Snail.fromString("[[[[3,0],[5,3]],[4,4]],[5,5]]"),
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[1,1]"),
                        Snail.fromString("[2,2]"),
                        Snail.fromString("[3,3]"),
                        Snail.fromString("[4,4]"),
                        Snail.fromString("[5,5]"),
                        Snail.fromString("[6,6]"),
                    ),
                    Snail.fromString("[[[[5,0],[7,4]],[5,5]],[6,6]]"),
                ),

                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"),
                        Snail.fromString("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]")
                    ),
                    Snail.fromString("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]")
                ),

                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"),
                        Snail.fromString("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]")
                    ),
                    Snail.fromString("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]")
                ),

                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]"),
                        Snail.fromString("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]")
                    ),
                    Snail.fromString("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]"),
                        Snail.fromString("[7,[5,[[3,8],[1,4]]]]")
                    ),
                    Snail.fromString("[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]"),
                        Snail.fromString("[[2,[2,2]],[8,[8,1]]]")
                    ),
                    Snail.fromString("[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]"),
                        Snail.fromString("[2,9]")
                    ),
                    Snail.fromString("[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]"),
                        Snail.fromString("[1,[[[9,3],9],[[9,0],[0,7]]]]")
                    ),
                    Snail.fromString("[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]"),
                        Snail.fromString("[[[5,[7,4]],7],1]")
                    ),
                    Snail.fromString("[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]"),
                        Snail.fromString("[[[[4,2],2],6],[8,7]]")
                    ),
                    Snail.fromString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
                ),
                Arguments.arguments(
                    listOf(
                        Snail.fromString("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"),
                        Snail.fromString("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"),
                        Snail.fromString("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]"),
                        Snail.fromString("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]"),
                        Snail.fromString("[7,[5,[[3,8],[1,4]]]]"),
                        Snail.fromString("[[2,[2,2]],[8,[8,1]]]"),
                        Snail.fromString("[2,9]"),
                        Snail.fromString("[1,[[[9,3],9],[[9,0],[0,7]]]]"),
                        Snail.fromString("[[[5,[7,4]],7],1]"),
                        Snail.fromString("[[[[4,2],2],6],[8,7]]")
                    ),
                    Snail.fromString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"),
                ),
            )
        }

        @JvmStatic
        fun magnitudeExamples(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments(Snail.fromString("[[1,2],[[3,4],5]]").parsePairs(), 143),
                Arguments.arguments(Snail.fromString("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").parsePairs(), 1384),
                Arguments.arguments(Snail.fromString("[[[[1,1],[2,2]],[3,3]],[4,4]]").parsePairs(), 445),
                Arguments.arguments(Snail.fromString("[[[[3,0],[5,3]],[4,4]],[5,5]]").parsePairs(), 791),
                Arguments.arguments(Snail.fromString("[[[[5,0],[7,4]],[5,5]],[6,6]]").parsePairs(), 1137),
                Arguments.arguments(Snail.fromString("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").parsePairs(), 3488),
            )
        }
    }

    @Test
    internal fun `parse snail`() {
        val snailInput = "[1,2]"
        assertEquals(
            Snail.of(
                listOf(
                    Snail.SnailToken.Open,
                    Snail.SnailToken.Number(1),
                    Snail.SnailToken.Number(2),
                    Snail.SnailToken.Close,
                )
            ),
            Snail.fromString(snailInput)
        )
    }
}