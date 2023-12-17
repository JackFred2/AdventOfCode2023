

sealed interface Element

object Empty : Element {
    override fun toString() = ".   "
}
class Symbol(val symbol: Char) : Element {
    override fun toString() = "$symbol   "
}
class Number(val number: Int) : Element {
    override fun toString() = number.toString().padEnd(4, ' ')
}

object DayThree {
    fun main() {
        part1()
        part2()
    }

    fun part1() {
        val map = PartMap(Util.getFromResources("inputs/day3.txt"))

        val valid = mutableSetOf<Number>()

        for (y in 0 ..< map.height) {
            for (x in 0..< map.width) {
                val element = map.get(x, y)
                if (element is Number && !valid.contains(element)) {
                    if (map.isValid(x, y)) {
                        valid.add(element)
                    }
                }
            }
        }

        //println(map)
        println(valid.sumOf { it.number })
    }

    fun part2() {
        val map = PartMap(Util.getFromResources("inputs/day3.txt"))

        var sum = 0

        for (y in 0 ..< map.height) {
            for (x in 0..< map.width) {
                val element = map.get(x, y)
                if (element is Symbol && element.symbol == '*') {
                    val surroundingNumbers = mutableSetOf<Number>()

                    for (y2 in -1..1) {
                        for (x2 in -1..1) {
                            val bordered = map.get(x + x2, y + y2)

                            if (bordered is Number && !surroundingNumbers.contains(bordered)) {
                                surroundingNumbers.add(bordered)
                            }
                        }
                    }

                    if (surroundingNumbers.size == 2) {
                        sum += surroundingNumbers.map { n -> n.number }.fold(1, Int::times)
                    }
                }
            }
        }

        println(sum)
    }
}

class PartMap(input: String) {
    val data: List<List<Element>>
    val width: Int
    val height: Int

    fun get(x: Int, y: Int): Element {
        if (x in 0..<width && y in 0..<height)
            return data[y][x]
        return Empty
    }

    fun isValid(x: Int, y: Int): Boolean {
        for (x2 in -1..1) {
            for (y2 in -1..1) {
                if (get(x + x2, y + y2) is Symbol) return true
            }
        }

        return false
    }

    init {
        data = mutableListOf()

        input.lines().filter { it.isNotBlank() }.forEach {
            val line = mutableListOf<Element>()

            var index = 0
            while (index < it.length) {
                val char = it[index]
                when {
                    char == '.' -> {
                        line.add(Empty)
                    }
                    char.isDigit() -> {
                        var num = char.toString()

                        while (index < it.length - 1 && it[index + 1].isDigit()) {
                            num += it[index + 1]
                            index++
                        }

                        val number = Number(num.toInt())

                        for (i in num.indices) {
                            line.add(number)
                        }
                    }
                    else -> {
                        line.add(Symbol(char))
                    }
                }
                index++
            }

            data.add(line)
        }

        width = data[0].size
        height = data.size
    }

    override fun toString(): String {
        return data.map { line -> line.joinToString(transform = Element::toString, separator = "")  }.joinToString("\n")
    }
}