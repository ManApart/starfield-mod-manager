package commands

import Mod
import toolData
import java.io.File

val searchDescription = """
    Search for mods and list them once
    To apply a filter to future lists, see filter
    Search the given text (name or category), by ids, by mods missing ids, and by status  
""".trimIndent()

val searchModsUsage = """
    search <search text> 
    search <mod id>
    search enabled
    search disabled
    search staged
    search unstaged
    search endorsed
    search unendorsed
    search abstained
    search missing
""".trimIndent()

fun searchMods(args: List<String> = listOf()) {
    searchMods(false, args)
}

fun searchMods(persist: Boolean, args: List<String> = listOf()) {
    if (args.firstOrNull() == "all" || args.firstOrNull() == "clear") {
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
    val endorsed = when {
        args.contains("endorsed") -> true
        args.contains("abstained") -> false
        else -> null
    }
    val unendorsed = args.contains("unendorsed")
    val missing = when {
        args.contains("missing") -> true
        else -> null
    }
    val id = args.firstOrNull { it.toIntOrNull() != null }
    val flagList = listOf("enabled", "disabled", "staged", "unstaged", "endorsed", "abstained", "unendorsed")
    val search = args.filter { !flagList.contains(it) && it.toIntOrNull() == null }.joinToString(" ").lowercase()

    val mods = toolData.mods.map { mod ->
        val displayed = mod.isDisplayed(enabled, staged, missing, id, endorsed, unendorsed, search)
        if (persist) mod.show = displayed
        mod to displayed
    }
    display(mods)
}

private fun Mod.isDisplayed(
    enabled: Boolean?,
    staged: Boolean?,
    missing: Boolean?,
    id: String?,
    endorsed: Boolean?,
    unendorsed: Boolean,
    search: String
): Boolean {
    return (enabled != null && enabled == this.enabled) ||
            (staged != null && staged == File(filePath).exists()) ||
            (missing != null && missing == (this.id == null)) ||
            (search.isNotBlank() && name.contains(search)) ||
            (search.isNotBlank() && category()?.lowercase()?.contains(search) ?: false) ||
            (id != null && this.id?.toString()?.contains(id) ?: false) ||
            (endorsed != null && endorsed == this.endorsed) || (unendorsed && this.endorsed == null)
}