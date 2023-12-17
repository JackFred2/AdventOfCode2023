object DayOne {

    fun main() {
        part1()
        part2()
    }

    fun part1() {
        println(Util.getFromResources("inputs/day1.txt").lines()
            .filter { it.isNotEmpty() }
            .map { it.replace(Regex("[^\\d]"), "") }
            .sumOf { "${it.first()}${it.last()}".toInt() })
    }

    val textToDigit = mapOf(
        Pair("one", "1"),
        Pair("two", "2"),
        Pair("three", "3"),
        Pair("four", "4"),
        Pair("five", "5"),
        Pair("six", "6"),
        Pair("seven", "7"),
        Pair("eight", "8"),
        Pair("nine", "9"),
    )

    val textToDigitReversed = textToDigit.map { it.key.reversed() to it.value }.toMap()

    fun part2() {
        //var count = 1
        val result = Util.getFromResources("inputs/day1.txt").lines()
            .filter { it.isNotEmpty() }
            .map { line: String ->
                var lowestIndex = Int.MAX_VALUE
                var lowestIndexText: Map.Entry<String, String>? = null
                textToDigit.forEach {
                    val digitIndex = line.indexOfAny("123456789".toCharArray())
                    val index = line.indexOf(it.key)
                    if (index != -1 && index < lowestIndex && (digitIndex == -1 || index < digitIndex)) {
                        lowestIndex = index
                        lowestIndexText = it
                    }
                }
                if (lowestIndexText != null) return@map line.replaceFirst(
                    lowestIndexText!!.key,
                    lowestIndexText!!.value
                )

                return@map line
            }.map { line: String ->
                val reversed = line.reversed()
                var lowestIndex = Int.MAX_VALUE
                var lowestIndexText: Map.Entry<String, String>? = null
                textToDigitReversed.forEach {
                    val digitIndex = reversed.indexOfAny("123456789".toCharArray())
                    val index = reversed.indexOf(it.key)
                    if (index != -1 && index < lowestIndex && (digitIndex == -1 || index < digitIndex)) {
                        lowestIndex = index
                        lowestIndexText = it
                    }
                }
                if (lowestIndexText != null) return@map reversed.replaceFirst(
                    lowestIndexText!!.key,
                    lowestIndexText!!.value
                ).reversed()

                return@map reversed.reversed()
            }
            .map { it.replace(Regex("[^\\d]"), "") }
            .sumOf {
                val sum = "${it.first()}${it.last()}".toInt()
                //println("(${count++}) $it: $sum")
                sum
            }

        println(result)
    }
}