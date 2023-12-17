object DayFour {
    fun main() {
        part1()
        part2()
    }

    fun part1() {
        val regex = Regex("Card +(?<id>\\d+): (?<winning>[ 0-9]+) \\| (?<yours>[ 0-9]+)")

        var sum = 0
        for (line in Util.getFromResources("inputs/day4.txt").lines()
            .filter { it.isNotBlank() }
            .mapNotNull { regex.find(it) }) {
            val winning = line.groups["winning"]!!.value.split(Regex(" +")).filter { it.isNotBlank() }.toSet()
            val yours = line.groups["yours"]!!.value.split(Regex(" +")).filter { it.isNotBlank() }

            val matching = yours.filter { winning.contains(it) }
            val value = if (matching.isNotEmpty()) 1 shl (matching.size - 1) else 0

            // println("Card ${line.groups["id"]!!.value.padStart(3)}: ${winning.map { it.padStart(2) }.joinToString(" ")} | ${yours.map { it.padStart(2) }.joinToString(" ")}")

            if (matching.isNotEmpty()) sum += value
        }

        println(sum)
    }

    fun part2() {
        val regex = Regex("Card +(?<id>\\d+): (?<winning>[ 0-9]+) \\| (?<yours>[ 0-9]+)")
        val input = Util.getFromResources("inputs/day4.txt").lines()
            .filter { it.isNotBlank() }
            .mapNotNull { regex.find(it) }

        val counts = input.indices.associateWith { 1 }.toMutableMap()

        for (line in input) {
            val id = line.groups["id"]!!.value.toInt() - 1
            val winning = line.groups["winning"]!!.value.split(Regex(" +")).filter { it.isNotBlank() }.toSet()
            val yours = line.groups["yours"]!!.value.split(Regex(" +")).filter { it.isNotBlank() }

            val factor = counts[id]!!
            val matching = yours.filter { winning.contains(it) }.size

            for (i in (id + 1)..<(id + 1 + matching)) {
                if (counts[i] != null) counts[i] = counts[i]!! + factor
            }
        }

        println(counts.values.sum())
    }
}