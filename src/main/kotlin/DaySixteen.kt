import DaySixteen.Direction.*

object DaySixteen {
    fun main() {
        val map = Util.getFromResources("inputs/day16.txt").lines().map { it.toList() }

        println(countEnergized(map, Pair(RIGHT, Pos(0, 0))))

        val height = map.size
        val width = map[0].size

        println(listOf(
            (0 ..< width).map { Pair(DOWN, Pos(it, 0)) },
            (0 ..< height).map { Pair(RIGHT, Pos(0, it)) },
            (0 ..< width).map { Pair(UP, Pos(it, height - 1)) },
            (0 ..< height).map { Pair(LEFT, Pos(width - 1, it)) },
        ).flatten().maxOf { countEnergized(map, it) })
    }

    private val dirMap = mapOf(
        '.' to Direction.entries.associateWith { listOf(it) },
        '|' to mapOf(
            RIGHT to listOf(UP, DOWN),
            LEFT to listOf(UP, DOWN),
            UP to listOf(UP),
            DOWN to listOf(DOWN)
        ),
        '-' to mapOf(
            RIGHT to listOf(RIGHT),
            LEFT to listOf(LEFT),
            UP to listOf(LEFT, RIGHT),
            DOWN to listOf(LEFT, RIGHT)
        ),
        '/' to mapOf(
            RIGHT to listOf(UP),
            UP to listOf(RIGHT),
            LEFT to listOf(DOWN),
            DOWN to listOf(LEFT)
        ),
        '\\' to mapOf(
            RIGHT to listOf(DOWN),
            DOWN to listOf(RIGHT),
            LEFT to listOf(UP),
            UP to listOf(LEFT)
        )
    )

    private fun countEnergized(map: List<List<Char>>, start: Pair<Direction, Pos>): Int {
        val todo = mutableListOf(start)
        val touched = mutableSetOf<Pair<Direction, Pos>>()

        while (todo.isNotEmpty()) {
            val next = todo.removeFirst()
            if (touched.contains(next)) continue
            val symbol = map.get(next.second) ?: continue
            touched.add(next)
            dirMap[symbol]!![next.first]!!.forEach {
                todo.add(Pair(it, next.second.move(it)))
            }
        }

        return touched.map { it.second }.toSet().size
    }

    private fun <T> List<List<T>>.get(x: Int, y: Int): T? {
        return if (y in this.indices && x in this[y].indices) {
            this[y][x]
        } else {
            null
        }
    }

    private fun Pos.move(dir: Direction): Pos {
        return when(dir) {
            UP -> Pos(this.first, this.second - 1)
            RIGHT -> Pos(this.first + 1, this.second)
            DOWN -> Pos(this.first, this.second + 1)
            LEFT -> Pos(this.first - 1, this.second)
        }
    }

    private fun <T> List<List<T>>.get(pos: Pos): T? {
        return this.get(pos.first, pos.second)
    }

    enum class Direction {
        RIGHT, DOWN, LEFT, UP
    }
}