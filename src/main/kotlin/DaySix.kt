import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object DaySix {
    fun main() {
        part1()
        part2()
    }

    fun getInput(): List<Pair<Int, Int>> {
        val input = Util.getFromResources("inputs/day6.txt").lines()
        val times = Regex("\\d+").findAll(input[0]).map { it.value.toInt() }.toList()
        val records = Regex("\\d+").findAll(input[1]).map { it.value.toInt() }.toList()

        return times.zip(records)
    }

    fun getInput2(): Pair<Long, Long> {
        val input = Util.getFromResources("inputs/day6.txt").lines()
        val time = Regex("\\d+").find(input[0].replace(" ", ""))!!.value.toLong()
        val record = Regex("\\d+").find(input[1].replace(" ", ""))!!.value.toLong()
        return Pair(time, record)
    }

    fun part1() {
        println(getInput().map {
            val T = it.first
            val D = it.second
            println("T: $T, D: $D")
            // x = T +/- sqrt(T^2 - 4D) / 2
            val discriminantSqrt = sqrt((T * T - 4 * D).toDouble())

            val lowest = (T - discriminantSqrt) / 2
            val highest = (T + discriminantSqrt) / 2
            var total = floor(highest) - ceil(lowest) + 1
            if (lowest == ceil(lowest)) total -= 1
            if (highest == floor(highest)) total -= 1

            total.toInt()
        }.fold(1, Int::times))
    }

    fun part2() {
        val race = getInput2()
        val T = race.first
        val D = race.second
        println("T: $T, D: $D")
        // x = T +/- sqrt(T^2 - 4D) / 2
        val discriminantSqrt = sqrt((T * T - 4 * D).toDouble())

        val lowest = (T - discriminantSqrt) / 2
        val highest = (T + discriminantSqrt) / 2
        var total = floor(highest) - ceil(lowest) + 1
        if (lowest == ceil(lowest)) total -= 1
        if (highest == floor(highest)) total -= 1

        println(total.toLong())
    }
}