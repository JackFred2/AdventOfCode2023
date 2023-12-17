object DayFive {
    val titleRegex = Regex("(?<source>[a-z]+)-to-(?<dest>[a-z]+) map:")
    val rangeRegex = Regex("(?<deststart>\\d+) (?<sourcestart>\\d+) (?<length>\\d+)")

    fun main() {
        part1()
        part2()
    }

    fun getMap(input: List<String>): List<Map> {
        return input.subList(2, input.size).joinToString(separator = "\n").split("\n\n").map {
            val lines = it.lines().filter { it.isNotBlank() }
            val title = titleRegex.matchEntire(lines[0])!!
            val source = title.groups["source"]!!.value
            val dest = title.groups["dest"]!!.value

            val ranges = lines.subList(1, lines.size).map { line -> Range(line) }

            Map(source, dest, ranges)
        }
    }

    fun part1() {
        val input = Util.getFromResources("inputs/day5.txt").lines()
        val seeds = input[0].substring("seeds: ".length).split(" ").map { it.toLong() }
        val maps = getMap(input)

        val order = listOf("seed", "soil", "fertilizer", "water", "light", "temperature", "humidity")

        val resultLocations = seeds.associateWith {
            var current = it

            order.forEach { category ->
                current = maps.first { map -> map.source == category }.change(current)
            }

            current
        }

        println(resultLocations.values.min())
    }

    fun part2() {
        val input = Util.getFromResources("inputs/day5.txt").lines()
        val seedsString = input[0].substring("seeds: ".length).split(" ")
        val seedRanges = seedsString.withIndex().partition { it.index % 2 == 0 }.let { it.first.zip(it.second) }.map { it.first.value.toLong() ..<(it.first.value.toLong() + it.second.value.toLong()) }
        val maps = getMap(input)

        val order = listOf("seed", "soil", "fertilizer", "water", "light", "temperature", "humidity")

        seedRanges.forEach {
            var list = mutableListOf(it)

            order.map { maps.first { map -> map.source == it } }.forEach { map ->
                list = list.flatMap { range ->
                    val result = mutableListOf<LongRange>()

                    val intersect = map

                    return@flatMap result
                }.toMutableList()
            }
        }
    }

    class Map(val source: String, val dest: String, val ranges: List<Range>) {
        fun change(value: Long): Long {
            this.ranges.forEach {
                if (it.sourceRange.contains(value)) return@change it.change(value)
            }
            return value
        }

        override fun toString(): String {
            return "<$source -> $dest [$ranges]>"
        }
    }

    class Range(line: String) {
        val sourceRange: LongRange
        private val destStart: Long

        init {
            val match = rangeRegex.matchEntire(line)!!
            val start = match.groups["sourcestart"]!!.value.toLong()
            val dest = match.groups["deststart"]!!.value.toLong()
            val length = match.groups["length"]!!.value.toLong()
            sourceRange = start ..< start + length
            destStart = dest
        }

        fun change(value: Long): Long {
            return if (sourceRange.contains(value)) {
                value - sourceRange.first + destStart
            } else {
                value
            }
        }

        override fun toString(): String {
            return "(${sourceRange} -> ${destStart..(destStart + (sourceRange.last - sourceRange.first))})"
        }
    }
}