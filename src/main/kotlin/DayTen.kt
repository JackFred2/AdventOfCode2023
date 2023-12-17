object DayTen {
    fun main() {
        val grid = Grid(Util.getFromResources("inputs/day10.txt").lines().filter { it.isNotBlank() })

        println(grid)
        println(grid.loop.size / 2)
        println(grid.interior.size)
    }

    enum class Direction(val x: Int, val y: Int) {
        North(0, -1),
        West(-1, 0),
        South(0, 1),
        East(1, 0);

        fun opposite() = when(this) {
            North -> South
            East -> West
            South -> North
            West -> East
        }

        fun clockwise() = when(this) {
            North -> East
            East -> South
            South -> West
            West -> North
        }

        fun anticlockwise() = when(this) {
            North -> West
            East -> North
            South -> East
            West -> South
        }
    }

    val cleanPrintMap = mapOf(
        '-' to '═',
        '|' to '║',
        'F' to '╔',
        '7' to '╗',
        'L' to '╚',
        'J' to '╝',
        '.' to '.'
    )

    val pipeSegments = mapOf(
        '|' to setOf(Direction.North, Direction.South),
        '-' to setOf(Direction.West, Direction.East),
        'L' to setOf(Direction.North, Direction.East),
        'F' to setOf(Direction.South, Direction.East),
        'J' to setOf(Direction.North, Direction.West),
        '7' to setOf(Direction.South, Direction.West)
    )

    fun List<List<Char>>.get(x: Int, y: Int): Char? {
        return if (y in indices && x in this[y].indices) this[y][x] else null
    }

    fun List<List<Char>>.get(pos: Pos) = get(pos.first, pos.second)

    fun Pair<Int, Int>.offset(dir: Direction) = Pair(this.first + dir.x, this.second + dir.y)

    class Grid(lines: List<String>) {
        val interior: Set<Pos>
        val map: List<List<Char>>
        val animalPos: Pos
        val width: Int
        val height: Int

        val loop: List<Pos>

        init {
            val map = lines.map { it.toMutableList() }.toMutableList()
            width = map[0].size
            height = map.size

            // remove animal from grid
            val animalLine = map.withIndex().find { it.value.contains('S') }!!
            animalPos = Pair(animalLine.value.indexOf('S'), animalLine.index)

            // replace old animal tile with correct pipe
            val surrounding = Direction.entries.filter { dir ->
                pipeSegments[map.get(animalPos.offset(dir))]?.contains(dir.opposite()) ?: false
            }.toSet()

            val matchingPipe = pipeSegments.entries.first {
                it.value == surrounding
            }

            map[animalPos.second][animalPos.first] = matchingPipe.key

            this.map = map

            // generate loop
            val loopTiles = mutableListOf<Pos>()
            var pos = animalPos
            val startingDirection = pipeSegments[map.get(animalPos)]!!.first()
            var direction = startingDirection

            // count turns to determine interior side; interior will always have 4 more than non
            var anticlockwiseTurns = 0
            var clockwiseTurns = 0

            do {
                loopTiles.add(pos)
                pos = pos.offset(direction)
                val oldDir = direction
                direction = pipeSegments[map.get(pos)]!!.first { it != direction.opposite() }

                if (direction == oldDir.clockwise()) {
                    clockwiseTurns++
                } else if (direction == oldDir.anticlockwise()) {
                    anticlockwiseTurns++
                }
            } while (pos != animalPos)

            loop = loopTiles

            val interiorSide = if (clockwiseTurns > anticlockwiseTurns) Direction::clockwise else Direction::anticlockwise
            val interiorCandidates = mutableSetOf<Pos>()

            direction = startingDirection

            // collect initial interior tiles
            do {
                pos = pos.offset(direction)
                interiorCandidates.add(pos.offset(interiorSide.invoke(direction)))
                direction = pipeSegments[map.get(pos)]!!.first { it != direction.opposite() }
                interiorCandidates.add(pos.offset(interiorSide.invoke(direction)))
            } while (pos != animalPos)

            val interiorTouching = interiorCandidates.filter { !loopTiles.contains(it) }.toMutableSet()
            val interior = mutableSetOf<Pos>()

            //flood fill rest
            val toCheck = interiorTouching.toMutableList()
            while (toCheck.isNotEmpty()) {
                val checked = toCheck.removeLast()
                if (interior.contains(checked) || loop.contains(checked)) continue
                interior.add(checked)
                Direction.entries.forEach { toCheck.add(checked.offset(it)) }
            }

            this.interior = interior
        }

        override fun toString(): String {
            val green = "\u001b[92m"
            val red = "\u001b[91m"
            val blue = "\u001b[94m"
            val gray = "\u001b[37m"
            val reset = "\u001b[0m"
            return "Animal Pos: $animalPos\n" + map.withIndex().joinToString(separator = "\n") { line ->
                line.value.withIndex().joinToString(separator = "") { char ->
                    val pos = Pos(char.index, line.index)
                    val result = cleanPrintMap[char.value] ?: char.value
                    if (pos == animalPos) {
                        red + result + reset
                    } else if (interior.contains(pos)) {
                        blue + result + reset
                    } else if (loop.contains(pos)) {
                        green + result + reset
                    } else {
                        gray + result + reset
                    }
                }
            }
        }
    }
}