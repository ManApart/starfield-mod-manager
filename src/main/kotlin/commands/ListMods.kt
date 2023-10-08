package commands

import toolState

fun listHelp(args: List<String>) = "List Mod details"

fun list(args: List<String>) {
    System.out.printf("%-7s%-9s%-12s%-10s%-22s\n", "Index", "Enabled", "Load Order", "Id", "Name")
    toolState.mods.forEachIndexed { i, mod ->
        with(mod) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "Unknown"
            System.out.printf("%-7d%-9s%-12d%-10s%-22s\n", i, enabledCheck, loadOrder, idClean, name)
        }
    }
}