package commands

import cyan
import toolData

fun findHelp() = """
    Find files
    find <search text> - list all files that have the given text in their path 
""".trimIndent()

fun find(args: List<String> = listOf()) {
    toolData.mods
        .map { mod -> mod to mod.getModFiles().map { it.path }.filter { file -> args.any { file.contains(it) } } }
        .filter { it.second.isNotEmpty() }
        .sortedBy { it.first.index }
        .forEach { (mod, matches) ->
            println("${mod.index} ${cyan(mod.name)} (${matches.size} matches)")
            matches.forEach { println("\t$it") }

        }

}