package commands

import toolState

fun refreshHelp(args: List<String> = listOf()) = """
    refresh <mod index>
    refresh 1 2 4
    refresh 1-3
    refresh all - For all mods with ids, attempt to redownload (or grab the file from the downloads folder if it exists) and restage.
""".trimIndent()

fun refresh(args: List<String>) {
    if (args.isEmpty()) {
        println(refreshHelp())
    } else {
        args.getIndicesOrRange(toolState.mods.size)
            .mapNotNull { toolState.byIndex(it) }
            .filter { it.id != null }
            .also { println("Refreshing ${it.size} mods") }
            .forEach { addModById(it.id!!, it.fileId) }
        println("Done Refreshing")
    }
}