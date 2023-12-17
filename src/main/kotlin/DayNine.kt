object DayNine {
    fun main() {
        part1()
        part2()
    }

    fun part1() {
        val lines = Util.getFromResources("inputs/day9.txt").lines()
            .filter { it.isNotBlank() }
            .map { line -> line.split(" ").map { it.toLong() } }

        val sum = lines.map { line ->
            val extrapolations = mutableListOf<List<Long>>()

            var current = line
            while (!current.allZero()) {
                extrapolations.add(current)
                current = diffs(current)
            }

            extrapolations.sumOf { it.last() }
        }.sum()

        println(sum)
    }

    fun part2() {
        val lines = Util.getFromResources("inputs/day9.txt").lines()
            .filter { it.isNotBlank() }
            .map { line -> line.split(" ").map { it.toLong() } }

        val sum = lines.map { line ->
            val extrapolations = mutableListOf<List<Long>>()

            var current = line
            while (!current.allZero()) {
                extrapolations.add(current)
                current = diffs(current)
            }

            var extrapolation = 0L

            extrapolations.reversed().forEach {
                extrapolation = it.first() - extrapolation
            }

            extrapolation
        }.sum()

        println(sum)
    }

    private fun List<Long>.allZero() = this.all { it == 0L }

    fun diffs(data: List<Long>): List<Long> {
        val diffs = mutableListOf<Long>()

        for (i in 0..< data.size - 1) {
            diffs.add(data[i + 1] - data[i])
        }

        return diffs
    }
}