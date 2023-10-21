package commands

import Mod
import save
import toolConfig
import toolData
import java.io.File


fun sortHelp() = """
    sort the list in various ways. Add reverse to invert the sort
    sort name
    sort id
    sort enabled
    sort category
    sort order
    sort staged
""".trimIndent()

fun sortMods(args: List<String> = listOf()) {
    val reverse = args.lastOrNull() == "reverse"
    val sortType = args.firstOrNull()
    when (sortType) {
        "name" -> sort(reverse) { it.name }
        "load" -> sort(reverse) { it.loadOrder }
        "order" -> sort(reverse) { it.loadOrder }
        "category" -> sort(reverse) { mod -> mod.categoryId?.let { toolConfig.categories[it] } ?: "zzz" }
        "enabled" -> sort(!reverse) { it.enabled }
        "id" -> sort(reverse) { it.id.toString() }
        "staged" -> sort(!reverse) { File(it.filePath).exists() }
        else -> sortHelp()
    }
}

private inline fun <R : Comparable<R>> sort(reverse: Boolean, crossinline selector: (Mod) -> R) {
    if (reverse) {
        toolData.mods.sortByDescending(selector)
    } else {
        toolData.mods.sortBy(selector)
    }
    save()
    listMods()
}