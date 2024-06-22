package commands

import Mod
import confirm
import cyan
import red
import save
import toolData
import yellow

enum class Tag(val tag: String) {
    CREATION("Creation"),
    EXTERNAL("External"),
}

val tagDescription = """
Tools for managing external plugins
external ls - lists unmanaged plugins by examining your game data folder
external add - adds a single plugin by its id (like SFBGS003). Also see add mod
external add all - Attempts to add _all_ unmanaged plugins found in the data folder

""".trimIndent()

val tagUsage = """
   tag 1 add essential
   tag 1 rm essential
   tag 1 rm 0
""".trimIndent()

fun tag(args: List<String>) {
    val i = args.firstOrNull()?.toIntOrNull()
    val mod = i?.let { toolData.byIndex(it) }
    val command = args.getOrNull(1)?.replace("rm", "remove")
    val tagArg = args.getOrNull(2)
    when {
        args.isEmpty() -> println(tagDescription)
        mod == null -> println("Must provide the index of a valid mod to update")
        command == "add" -> addTag(mod, tagArg)
        command == "remove" && tagArg?.toIntOrNull() != null -> removeTag(mod, tagArg.toInt())
        command == "remove" && tagArg != null -> removeTag(mod, tagArg)

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun addTag(mod: Mod, tag: String?) {
    if (tag == null) {
        println("No tag value found to add")
        return
    }
    mod.tags.add(tag)
    save()
}

private fun removeTag(mod: Mod, tagId: Int) = removeTag(mod, mod.tags.elementAt(tagId))

private fun removeTag(mod: Mod, tag: String) {
    if (!mod.tags.contains(tag)) {
        println(red("Tag ") + cyan("'$tag'") + red(" doesn't exist in ") + cyan("'${mod.tags.joinToString(", ")}'") + ". (Command is case sensitive.)")
        return
    }
    confirm(false, yellow("Remove tag $tag? ")) {
        mod.tags.remove(tag)
        save()
    }
}
