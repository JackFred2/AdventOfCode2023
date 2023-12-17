object DayFifteen {
    val instructionRegex = Regex("(?<label>[a-z]+)(?<instruction>-|=\\d+)")

    fun main() {
        println(Util.getFromResources("inputs/day15.txt")
                .trim()
                .split(",")
                .sumOf { hash(it) })

        val boxes = (0 ..< 256).associateWith { Box(it) }

        instructionRegex.findAll(Util.getFromResources("inputs/day15.txt").trim()).forEach {
            val label = it.group("label")
            val box = boxes[hash(label)]!!
            val instruction = it.group("instruction")
            if (instruction == "-") {
                box.lenses.removeIf { lens -> lens.label == label }
            } else { // =\d+
                val focalLength = instruction.substring(1).toInt()
                val newLens = Lens(label, focalLength)
                val existingIndex = box.lenses.indexOfFirst { lens -> lens.label == label }
                if (existingIndex != -1) {
                    box.lenses[existingIndex] = newLens
                } else {
                    box.lenses.add(newLens)
                }
            }
        }

        //boxes.values.filter { it.lenses.isNotEmpty() }.forEach { println(it) }

        println(boxes.values.sumOf { it.focusPower() })
    }

    fun hash(s: String): Int {
        return s.fold(0) { accum: Int, c: Char -> ((accum + c.code) * 17) % 256 }
    }

    class Box(val id: Int) {
        val lenses = mutableListOf<Lens>()

        fun focusPower(): Int {
            return (id + 1) * lenses.withIndex().fold(0) { accum: Int, (index: Int, lens: Lens) ->
                accum + (index + 1) * lens.focalLength
            }
        }

        override fun toString(): String {
            return "Box $id: ${lenses.joinToString(separator = " ")}"
        }
    }

    data class Lens(val label: String, val focalLength: Int) {
        override fun toString(): String {
            return "[$label $focalLength]"
        }
    }
}