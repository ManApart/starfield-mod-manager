package commands

import toolState

fun listHelp(args: List<String>) = "List Mod details"

fun list(args: List<String>) {
//    System.out.printf()

    System.out.printf("%-10s%-10s%-22s\n", "Enabled", "Id", "Name")
    toolState.mods.forEach {
        with(it) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "Unknown"
            System.out.printf("%-10s%-10s%-22s\n", enabledCheck, idClean, name)
        }
    }
}