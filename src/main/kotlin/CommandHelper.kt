import commands.getIndicesOrRange
import java.io.File

fun doCommand(args: List<String>, execute: List<Mod>.() -> Unit) {
    when {
        args.isEmpty() -> toolData.mods.execute()
        args.first() == "empty" -> executeFiltered(execute) { !File(it.filePath).exists() }
        args.first() == "staged" -> executeFiltered(execute) { File(it.filePath).exists() }
        args.first() == "enabled" -> executeFiltered(execute) { it.enabled }
        args.first() == "disabled" -> executeFiltered(execute) { !it.enabled }
        else -> {
            args.getIndicesOrRange(toolData.mods.size)
                .mapNotNull { toolData.byIndex(it) }
                .execute()
        }
    }
}

private fun executeFiltered(execute: List<Mod>.() -> Unit, filter: (Mod) -> Boolean) {
    toolData.mods.filter(filter).execute()
}
