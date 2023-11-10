package commands

import Mod
import save
import toolData
import java.io.File


val sortDescription = """
    Sort the list in various ways. Add reverse to invert the sort
""".trimIndent()

val sortUsage = """
    sort name
    sort name reverse
    sort id
    sort enabled
    sort category
    sort order
    sort staged
""".trimIndent()

fun sortMods(args: List<String> = listOf()) {
    val reverse = args.lastOrNull() == "reverse"
    when (args.firstOrNull()) {
        "name" -> sort(reverse) { it.name }
        "load" -> sort(reverse) { it.loadOrder }
        "order" -> sort(reverse) { it.loadOrder }
        "category" -> sort(reverse) { mod -> mod.category() ?: "zzz" }
        "enabled" -> sort(!reverse) { it.enabled }
        "id" -> sort(reverse) { it.id.toString() }
        "staged" -> sort(!reverse) { File(it.filePath).exists() }
        else -> sortDescription
    }
}

private inline fun <R : Comparable<R>> sort(reverse: Boolean, crossinline selector: (Mod) -> R) {
    if (reverse) {
        toolData.mods.sortByDescending(selector)
    } else {
        toolData.mods.sortBy(selector)
    }
    toolData.updateSorts()
    save()
    listMods()
}