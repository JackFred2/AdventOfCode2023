object Util {
    fun getFromResources(filename: String) = Util::class.java.getResource(filename)!!.readText()
}

fun MatchResult.group(group: String) = this.groups[group]!!.value

typealias Pos = Pair<Int, Int>