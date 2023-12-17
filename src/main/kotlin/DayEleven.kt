import kotlin.math.abs

object DayEleven {
    fun main() {
        val universe = Universe(Util.getFromResources("inputs/day11.txt").lines().filter { it.isNotBlank() })

        println(universe.totalPairDistance(1))
        println(universe.totalPairDistance(1_000_000 - 1))
    }

    class Universe(input: List<String>) {
        val galaxies: List<Pos>

        val emptyColumns: Set<Int>
        val emptyRows: Set<Int>

        init {
            val list = mutableListOf<Pos>()
            input.withIndex().forEach { line ->
                line.value.withIndex().forEach { char ->
                    if (char.value == '#') list.add(Pos(char.index, line.index))
                }
            }

            galaxies = list

            val columns = list.maxOf { it.first }
            val rows = list.maxOf { it.second }

            emptyRows = (0..columns).filter { col ->
                galaxies.none { it.second == col }
            }.toSet()

            emptyColumns = (0..rows).filter { row ->
                galaxies.none { it.first == row }
            }.toSet()
        }

        fun totalPairDistance(expansion: Int): Long {
            var sum = 0L
            for (a in galaxies.withIndex()) {
                for (b in galaxies.subList(a.index + 1, galaxies.size)) {
                    sum += distanceBetween(a.value, b, expansion)
                }
            }

            return sum
        }

        fun distanceBetween(a: Pos, b: Pos, expansion: Int): Int {
            val base = abs(b.second - a.second) + abs(b.first - a.first)
            val traversedRows = if (b.second > a.second) a.second..b.second else b.second..a.second
            val traversedCols = if (b.first > a.first) a.first..b.first else b.first..a.first
            val expanded = expansion * (traversedRows.intersect(emptyRows).size + traversedCols.intersect(emptyColumns).size)
            return base + expanded
        }
    }
}