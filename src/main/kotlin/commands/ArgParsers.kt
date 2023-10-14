package commands

fun List<String>.getIndicesOrRange(modSize: Int): List<Int> {
    return when {
        first() == "all" -> (0..<modSize).toList()
        first().contains("-") -> getRange(modSize)
        else -> getIndices(modSize)
    }
}

fun List<String>.getIndices(modSize: Int): List<Int> {
    return mapNotNull { arg ->
        arg.toIntOrNull()?.also { if (it !in 0..<modSize) println("Invalid Mod index $arg") }
    }.filter { it in 0..<modSize }
}

fun List<String>.getRange(modSize: Int): List<Int> {
    val parts = first().split("-")
    val start = parts.first().toInt()
    val end = parts.last().toInt()
    return if (start < 0 || start >= end || end >= modSize) {
        println("Invalid Range $start-$end")
        listOf()
    } else {
        (start..end).toList()
    }
}