package commands

import Mod
import toolData
import java.io.File

fun searchHelp() = """
    Search for mods and list them once
    To apply a filter to future lists, see filter
    search <search text> - search the given text (name or category) 
    search 123 - show matching ids
    search enabled - show only enabled mods
    search disabled
    search staged
    search unstaged
    search missing - show missing ids
""".trimIndent()

fun searchMods(args: List<String> = listOf()) {
    searchMods(false, args)
}

fun searchMods(persist: Boolean, args: List<String> = listOf()) {
    if (args.firstOrNull() == "all" || args.firstOrNull() == "clear"){
        toolData.mods.forEach { it.show = true }
        listMods()
        return
    }
    val enabled = when {
        args.contains("enabled") -> true
        args.contains("disabled") -> false
        else -> null
    }
    val staged = when {
        args.contains("staged") -> true
        args.contains("unstaged") -> false
        else -> null
    }
    val missing = when {
        args.contains("missing") -> true
        else -> null
    }
    val id = args.firstOrNull { it.toIntOrNull() != null }
    val flagList = listOf("enabled", "disabled", "staged", "unstaged")
    val search = args.filter { !flagList.contains(it) && it.toIntOrNull() == null }.joinToString(" ").lowercase()

    val mods = toolData.mods.map { mod ->
        val displayed = mod.isDisplayed(enabled, staged, missing, id, search)
        if (persist) mod.show = displayed
        mod to displayed
    }
    display(mods)
}

private fun Mod.isDisplayed(enabled: Boolean?, staged: Boolean?, missing: Boolean?, id: String?, search: String): Boolean {
    return (enabled != null && enabled == this.enabled) ||
            (staged != null && staged == File(filePath).exists()) ||
            (missing != null && missing == (this.id == null)) ||
            (search.isNotBlank() && name.contains(search)) ||
            (search.isNotBlank() && category()?.lowercase()?.contains(search) ?: false) ||
            (id != null && this.id?.toString()?.contains(id) ?: false)
}