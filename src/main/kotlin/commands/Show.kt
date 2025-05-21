package commands

import jsonMapper
import kotlinx.serialization.encodeToString
import toolData
import yellow

val showDescription = """
    Show all of a mods detail
""".trimIndent()

val showUsage = """
    show <index>
""".trimIndent()

fun show(command: String, args: List<String>) {
    val mod = args.firstOrNull()?.toIntOrNull()?.let { toolData.byIndex(it) }
    if (mod == null){
        println(yellow("Unable to find mod for ${args.joinToString(" ")}"))
    } else {
        println("Category: "+ mod.category())
        println(jsonMapper.encodeToString(mod))
    }
}
