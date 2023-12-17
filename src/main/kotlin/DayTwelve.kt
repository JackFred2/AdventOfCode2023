object DayTwelve {
    fun main() {
        part1()
    }

    fun part1() {
        val unsolved = Util.getFromResources("inputs/day12.txt").lines().filter { it.isNotBlank() }.map { Row(it) }

        val solved = unsolved.map { tryPlace(it.str, it.damagedGroupSizes) }.sum()

        println(solved)
    }

    fun tryPlace(working: String, damagedSizes: List<Int>): Int {
        val nextWildcard = working.indexOf('?')

        if (nextWildcard == -1) {
            val valid = working.split(".").filter { it.isNotBlank() }.map { it.length } == damagedSizes
            return if (valid) 1 else 0
        } else {
            return tryPlace(working.replaceFirst('?', '#'), damagedSizes) + tryPlace(working.replaceFirst('?', '.'), damagedSizes)
        }
    }

    class Row(line: String) {
        val str: String
        val damagedGroupSizes: List<Int>

        init {
            val split = line.split(" ")
            this.str = split[0]
            damagedGroupSizes = split[1].split(",").map { it.toInt() }
        }
    }
}