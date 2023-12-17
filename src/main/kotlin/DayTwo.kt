import kotlin.math.max

object DayTwo {
    fun main() {
        part1()
        part2()
    }

    fun part1() {
        val limits = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        var sum = 0

        Util.getFromResources("inputs/day2.txt").lines().filter { it.isNotBlank() }.withIndex().forEach { game ->
            val line = game.value.substring("Game ${game.index + 1}: ".length)
            var valid = true
            line.split("; ").forEach outer@{ draw ->
                draw.split(", ").forEach { colourDraw ->
                    val split = colourDraw.split(" ")
                    val count = split[0].toInt()
                    val colour = split[1]
                    if (count > limits[colour]!!) {
                        valid = false
                        return@outer
                    }
                }
            }
            if (valid) sum += (game.index + 1)
        }

        println(sum)
    }

    fun part2() {
        var sum = 0

        Util.getFromResources("inputs/day2.txt").lines().filter { it.isNotBlank() }.withIndex().forEach { game ->
            val maxSeen = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )

            val line = game.value.substring("Game ${game.index + 1}: ".length)

            line.split("; ").forEach outer@{ draw ->
                draw.split(", ").forEach { colourDraw ->
                    val split = colourDraw.split(" ")
                    val count = split[0].toInt()
                    val colour = split[1]
                    maxSeen[colour] = max(maxSeen[colour]!!, count)
                }
            }

            sum += maxSeen.values.fold(1, Int::times)
        }

        println(sum)
    }
}