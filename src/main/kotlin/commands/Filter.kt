package commands

fun filterHelp() = """
    filter mods that contain the given text (name or category)
    Changes persist across future ls until filter clear or filter all is called
    To do a one time search, see search
    filter <search text> - 
    filter 123 - show matching ids
    filter enabled - show only enabled mods
    filter disabled
    filter staged
    filter unstaged
    filter missing - show missing ids
""".trimIndent()

fun filterMods(args: List<String> = listOf()) {
    searchMods(true, args)
}
