object DayFourteen {
    fun main() {
        part1()
        part2()
    }

    fun part1() {
        val platform = Platform(Util.getFromResources("inputs/day14.txt"))
        platform.tiltUp()
        println(platform.load())
    }

    fun part2() {
        val platform = Platform(Util.getFromResources("inputs/day14.txt"))

        // map of hash to cycle
        val cache = mutableMapOf<Int, Int>()

        var cycleLength: Int = 0
        var cycleStart: Int = 0

        for (i in 1 .. 1_000) {
            platform.cycle()

            val hash = platform.hashValue()
            if (cache[hash] != null) {
                cycleLength = i - cache[hash]!!
                cycleStart = cache[hash]!!
                break
            }
            cache[hash] = i
        }

        //println("Cycle: Start $cycleStart for $cycleLength cycles")

        val skippedCycles = cycleLength * ((1_000_000_000 - cycleStart) / cycleLength)
        //println("Skipped $skippedCycles")
        for (i in cycleStart ..< (1_000_000_000 - skippedCycles)) {
            platform.cycle()
        }

        println(platform.load())
    }

    class Platform(input: String) {
        val grid: List<MutableList<Tile>> = input.lines().filter { it.isNotBlank() }.map { line -> line.map { when(it) {
            '.' -> Tile.EMPTY
            'O' -> Tile.ROUND_ROCK
            '#' -> Tile.SQUARE_ROCK
            else -> throw IllegalArgumentException()
        } }.toMutableList() }

        fun get(x: Int, y: Int): Tile {
            return if (y in grid.indices && x in grid[y].indices)
                grid[y][x]
            else
                Tile.SQUARE_ROCK
        }

        private fun getNextUpFree(x: Int, y: Int, xOffset: Int, yOffset: Int): Pos {
            var x2 = x
            var y2 = y
            while (get(x2 + xOffset, y2 + yOffset) == Tile.EMPTY) {
                x2 += xOffset
                y2 += yOffset
            }
            return Pos(x2, y2)
        }

        private fun IntRange.possiblyReverse(indicator: Int): IntProgression {
            if (indicator <= 0) return this
            return this.reversed()
        }

        private fun tilt(xOffset: Int, yOffset: Int) {
            for (y in grid.indices.possiblyReverse(yOffset)) {
                for (x in grid[y].indices.possiblyReverse(xOffset)) {
                    if (get(x, y) == Tile.ROUND_ROCK) {
                        val (x2, y2) = getNextUpFree(x, y, xOffset, yOffset)
                        if (x != x2 || y != y2) {
                            grid[y2][x2] = Tile.ROUND_ROCK
                            grid[y][x] = Tile.EMPTY
                        }
                    }
                }
            }
        }

        fun cycle() {
            tiltUp()
            tiltLeft()
            tiltDown()
            tiltRight()
        }

        fun tiltUp() {
            tilt(0, -1)
        }

        fun tiltDown() {
            tilt(0, 1)
        }

        fun tiltLeft() {
            tilt(-1, 0)
        }

        fun tiltRight() {
            tilt(1, 0)
        }

        fun load(): Int {
            var sum = 0

            for (y in grid.indices) {
                val load = grid.size - y
                for (x in grid[y].indices) {
                    if (get(x, y) == Tile.ROUND_ROCK) {
                        sum += load
                    }
                }
            }

            return sum
        }

        fun hashValue() = grid.hashCode()

        override fun toString(): String {
            return grid.joinToString(separator = "\n") { line ->
                line.joinToString(separator = "") { tile ->
                    tile.char
                }
            }
        }
    }

    enum class Tile(val char: String) {
        EMPTY("\u001b[91m" + ". " + "\u001b[0m"),
        ROUND_ROCK("\u001b[94m" + "O " + "\u001b[0m"),
        SQUARE_ROCK("\u001b[92m" + "# " + "\u001b[0m")
    }
}