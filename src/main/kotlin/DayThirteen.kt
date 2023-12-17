import java.sql.Ref
import kotlin.math.min

object DayThirteen {
    fun main() {
        val patterns = Util.getFromResources("inputs/day13.txt").split("\n\n", "\r\n\r\n").map { it.lines() }

        val reflections = patterns.map { pattern ->
            val width = pattern[0].length
            val height = pattern.size

            // try horizontal

            // println(pattern.joinToString(separator = "\n"))
            for (x in 1 ..< width) {
                if (reflectsHorizontal(x, pattern)) {
                    // println("Reflects at col $x")
                    return@map pattern to Reflection(ReflectionType.HORIZONTAL, x)
                }
            }
            for (y in 1 ..< height) {
                if (reflectsVertical(y, pattern)) {
                    // println("Reflects at row $y")
                    return@map pattern to Reflection(ReflectionType.VERTICAL, y)
                }
            }

            return@map pattern to Reflection(ReflectionType.HORIZONTAL, -1)
        }.filter { it.second.index != -1 }.toMap()

        println(reflections.values.sumOf { it.value() })

        val altered = reflections.entries.map { (pattern, reflection) ->
            val width = pattern[0].length
            val height = pattern.size

            pattern.withIndex().forEach { (lineNumber, line) ->
                line.indices.forEach { charToChange ->
                    val mutated = pattern.toMutableList()
                    val replacement = if (line[charToChange] == '#') "." else "#"
                    mutated[lineNumber] = line.replaceRange(charToChange, charToChange + 1, replacement)

                    for (x in 1 ..< width) {
                        if (reflectsHorizontal(x, mutated)) {
                            val alteredReflection = Reflection(ReflectionType.HORIZONTAL, x)
                            if (alteredReflection != reflection) {
                                return@map alteredReflection
                            }
                        }
                    }
                    for (y in 1 ..< height) {
                        if (reflectsVertical(y, mutated)) {
                            val alteredReflection = Reflection(ReflectionType.VERTICAL, y)
                            if (alteredReflection != reflection) {
                                return@map alteredReflection
                            }
                        }
                    }
                }
            }
            return@map Reflection(ReflectionType.HORIZONTAL, -1)

        }.filter { it.index != -1 }

        println(altered.sumOf { it.value() })
    }

    fun reflectsHorizontal(endCol: Int, pattern: List<String>): Boolean {
        val width = pattern[0].length
        val numberToCheckEachSide = min(endCol, width - endCol)

        for (row in pattern) {
            val left = row.substring(endCol - numberToCheckEachSide, endCol)
            val right = row.substring(endCol, endCol + numberToCheckEachSide)
            if (left.reversed() != right) return false
        }

        return true
    }

    fun reflectsVertical(endRow: Int, pattern: List<String>): Boolean {
        val height = pattern.size
        val numberToCheckEachSide = min(endRow, height - endRow)

        val up = pattern.subList(endRow - numberToCheckEachSide, endRow)
        val down = pattern.subList(endRow, endRow + numberToCheckEachSide)

        return up.reversed() == down
    }

    enum class ReflectionType {
        VERTICAL,
        HORIZONTAL
    }

    data class Reflection(val reflectionType: ReflectionType, val index: Int) {
        fun value(): Int = if (reflectionType == ReflectionType.HORIZONTAL) index else 100 * index
    }
}