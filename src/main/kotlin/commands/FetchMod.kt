package commands

import fetchModInfo

fun fetchHelp(args: List<String> = listOf()) = """
   fetch <mod id> - Add mod metadata without downloading files
   fetch 111 222 333 - Fetch multiple
""".trimIndent()

fun fetchMod(args: List<String>) {
    when {
        args.isEmpty() -> println(addModHelp())
        else -> args.mapNotNull { it.toIntOrNull() }.forEach {
            fetchModInfo(it)?.let { mod -> println("Fetched info for ${mod.name}") }
        }
    }
}


