package commands.view

import cyan
import toolData

val findDescription = """
    Find all files that have the given text in their path 
""".trimIndent()
val findUsage = """
    Find files
    find <search text> 
""".trimIndent()

fun find(command: String, args: List<String> = listOf()) {
    toolData.mods
        .map { mod -> mod to mod.getModFiles().map { it.path }.filter { file -> args.any { file.contains(it) } } }
        .filter { it.second.isNotEmpty() }
        .sortedBy { it.first.index }
        .forEach { (mod, matches) ->
            println("${mod.index} ${cyan(mod.name)} (${matches.size} matches)")
            matches.forEach { println("\t$it") }

        }

}
