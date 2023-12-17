object DaySeven {
    fun main() {
        part1()
        part2()
    }

    val cardRank = "AKQJT98765432".toList()
    val cardRank2 = "AKQT98765432J".toList()
    val regex = Regex("(?<hand>\\w{5}) (?<bid>\\d+)")

    fun part1() {
        val cards = Util.getFromResources("inputs/day7.txt").lines().filter { it.isNotBlank() }
            .map { regex.find(it)!! }
            .map { it.group("hand") to it.group("bid").toInt() }
            .toList()

        val hands = cards.map { Hand(it.first) to it.second }.sortedBy { it.first }.reversed()

        println(hands.withIndex().sumOf { (it.index + 1) * it.value.second })
    }

    fun part2() {
        val cards = Util.getFromResources("inputs/day7.txt").lines().filter { it.isNotBlank() }
            .map { regex.find(it)!! }
            .map { it.group("hand") to it.group("bid").toInt() }
            .toList()

        val hands = cards.map { Hand2(it.first) to it.second }.sortedBy { it.first }.reversed()

        println(hands.joinToString(separator = "\n"))

        println(hands.withIndex().sumOf { (it.index + 1) * it.value.second })
    }

    data class Hand(val cards: String) : Comparable<Hand> {
        private val type: HandType

        init {
            val cardCounts = cards.groupBy { it }.map { it.key to it.value.size }.sortedBy { cardRank.indexOf(it.first) }.toMap()
            val maxCardCount = cardCounts.values.max()

            type = when(cardCounts.size) {
                1 -> HandType.FiveKind
                2 -> if (maxCardCount == 4) HandType.FourKind else HandType.FullHouse
                3 -> if (maxCardCount == 3) HandType.ThreeKind else HandType.TwoPair
                4 -> HandType.OnePair
                5 -> HandType.HighCard
                else -> throw IllegalArgumentException("too large hand")
            }
        }

        override fun compareTo(other: Hand): Int {
            val rankComp = this.type.rank - other.type.rank
            if (rankComp != 0) return rankComp
            this.cards.zip(other.cards).forEach {
                val cardComp = cardRank.indexOf(it.first) - cardRank.indexOf(it.second)
                if (cardComp != 0) return cardComp
            }
            return 0
        }
    }

    data class Hand2(val cards: String) : Comparable<Hand2> {
        private val type: HandType

        init {
            val cardCounts = cards.groupBy { it }.map { it.key to it.value.size }.sortedBy { cardRank2.indexOf(it.first) }.toMap()
            val jokerAwareCounts = cardCounts['J']?.let { jokerCount ->
                val copy = cardCounts.filter { it.key != 'J' }.toMutableMap()

                // used better example input to help with this
                // https://www.reddit.com/r/adventofcode/comments/18cr4xr/2023_day_7_better_example_input_not_a_spoiler/
                if (copy.isEmpty()) return@let mapOf('A' to 5)
                val max = copy.values.max()
                val improvedKey = copy.entries.filter { it.value == max }.toList().firstOrNull()?.key ?: 'A'

                copy[improvedKey] = copy[improvedKey]?.plus(jokerCount) ?: jokerCount
                copy
            } ?: cardCounts
            val maxCardCount = jokerAwareCounts.values.max()

            type = when(jokerAwareCounts.size) {
                1 -> HandType.FiveKind
                2 -> if (maxCardCount == 4) HandType.FourKind else HandType.FullHouse
                3 -> if (maxCardCount == 3) HandType.ThreeKind else HandType.TwoPair
                4 -> HandType.OnePair
                5 -> HandType.HighCard
                else -> throw IllegalArgumentException("too large hand")
            }
        }

        override fun toString(): String {
            return "(Hand2(cards=$cards,type=$type)"
        }

        override fun compareTo(other: Hand2): Int {
            val rankComp = this.type.rank - other.type.rank
            if (rankComp != 0) return rankComp
            this.cards.zip(other.cards).forEach {
                val cardComp = cardRank2.indexOf(it.first) - cardRank2.indexOf(it.second)
                if (cardComp != 0) return cardComp
            }
            return 0
        }
    }

    enum class HandType(val rank: Int) {
        FiveKind(1),
        FourKind(2),
        FullHouse(3),
        ThreeKind(4),
        TwoPair(5),
        OnePair(6),
        HighCard(7),
    }
}