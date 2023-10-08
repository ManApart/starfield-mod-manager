package commands

import toolState

fun listHelp(args: List<String>) = "List Mod details"

fun list(args: List<String>) {

    System.out.printf("%-10s%-12s%-10s%-10s%-22s\n", "Index", "Load Order","Enabled", "Id", "Name")
    toolState.mods.forEachIndexed { i, mod ->
        with(mod) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "Unknown"
            System.out.printf("%-10d%-12d%-10s%-10s%-22s\n", i, loadOrder, enabledCheck, idClean, name)
        }
    }
}