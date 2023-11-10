package commands

val filterDescription = """
    Filter mods that contain the given text (name or category) or status, including missing ids
    Changes persist across future ls until filter clear or filter all is called
    To do a one time search, see search
""".trimIndent()

val filterUsage = """
    filter <search text> - 
    filter 123 
    filter enabled
    filter disabled
    filter staged
    filter unstaged
    filter endorsed
    filter unendorsed
    filter abstained
    filter missing - show missing ids
""".trimIndent()

fun filterMods(args: List<String> = listOf()) {
    searchMods(true, args)
}
