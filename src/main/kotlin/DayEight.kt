import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

object DayEight {
    fun main() {
        //part1()
        part2()
    }

    val nodeRegex = Regex("(?<id>[0-9A-Z]{3}) = \\((?<left>[0-9A-Z]{3}), (?<right>[0-9A-Z]{3})\\)")

    fun part1() {
        val (instructions, nodeMap) = load()

        var current = nodeMap["AAA"]!!
        val target = nodeMap["ZZZ"]!!
        var currentInstructionIndex = 0
        var steps = 0

        while (current != target && steps < 10000000) {
            var instruction = instructions[currentInstructionIndex++]
            if (currentInstructionIndex >= instructions.length) currentInstructionIndex = 0

            if (instruction == 'L') current = current.left
            else current = current.right

            steps++
        }

        println(steps)
    }

    fun part2() {
        val (instructions, nodeMap) = load()

        val starts = nodeMap.values.filter { it.isStart() }

        val loopSizes = starts.map { node ->
            var current = node
            var steps = 0
            var currentInstructionIndex = 0

            while (!current.isEnd()) {
                steps++

                var instruction = instructions[currentInstructionIndex++]
                if (currentInstructionIndex >= instructions.length) currentInstructionIndex = 0
                if (instruction == 'L') current = current.left
                else current = current.right
            }

            val loopStart = steps
            val loopStartNode = current
            val loopStartInstruction = currentInstructionIndex

            println("loop start: $loopStartNode at step $loopStart")

            do {
                steps++

                var instruction = instructions[currentInstructionIndex++]
                if (currentInstructionIndex >= instructions.length) currentInstructionIndex = 0
                if (instruction == 'L') current = current.left
                else current = current.right

                //println(current)
            } while (!(loopStartNode == current && loopStartInstruction == currentInstructionIndex))

            println("Loop ended at step $steps: size ${steps - loopStart}")

            steps - loopStart
        }

        println(loopSizes)

        val maxFactors = mutableMapOf<Int, Int>()

        loopSizes.map { factorize(it) }.flatMap { it.entries }.forEach {
            maxFactors[it.key] = maxFactors[it.key]?.let { facCount -> max(facCount, it.value) } ?: it.value
        }

        val factors = mutableListOf<Int>()

        maxFactors.forEach {
            for (i in 0..<it.value) factors.add(it.key)
        }

        println(factors.fold(1, Long::times))
    }

    fun factorize(i: Int): Map<Int, Int> {
        var total = i
        var factors = mutableMapOf<Int, Int>()
        for (factor in 2..floor(sqrt(i.toDouble())).toInt()) {
            while (total % factor == 0) {
                factors[factor] = factors[factor]?.plus(1) ?: 1
                factors[total / factor] = factors[total / factor]?.plus(1) ?: 1
                total /= factor
            }
        }

        return factors
    }

    fun load(): Pair<String, Map<String, Node>> {
        val lines = Util.getFromResources("inputs/day8.txt").lines().filter { it.isNotBlank() }
        val instructions = lines[0]

        val nodeMap = mutableMapOf<String, Node>()

        lines.subList(1, lines.size).map { nodeRegex.find(it)!! }.forEach {
            val id = it.group("id")
            nodeMap[id] = Node(nodeMap, id, it.group("left"), it.group("right"))
        }

        return Pair(instructions, nodeMap)
    }

    data class Ghost(var currentNode: Node) {
        val visited = mutableMapOf<Node, Int>()

        fun hasLooped(): Boolean {
            return visited.contains(currentNode)
        }
    }

    class Node(nodeMap: Map<String, Node>, val id: String, left: String, right: String) {
        val left: Node by lazy { nodeMap[left]!! }
        val right: Node by lazy { nodeMap[right]!! }

        override fun toString(): String {
            return "(Node $id (left=${left.id}, right=${right.id}))"
        }

        fun isStart() = id.endsWith('A')
        fun isEnd() = id.endsWith('Z')
    }
}