package commands

import fetchModInfo
import urlToId

fun fetchHelp(args: List<String> = listOf()) = """
   fetch <mod id> - Add mod metadata without downloading files
   fetch 111 222 333 - Fetch multiple
""".trimIndent()

fun fetchMod(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(addModHelp())
        firstArg.toIntOrNull() != null -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
        firstArg.startsWith("http") -> addModByUrls(args)
        else -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
    }
}

private fun fetchModsById(ids: List<Int>) =
    ids.forEach { fetchModInfo(it)?.let { mod -> println("Fetched info for ${mod.name}") } }

private fun addModByUrls(urls: List<String>) {
    urls.forEach { url ->
        url.urlToId()?.let { fetchModInfo(it) }?.let { println("Fetched info for ${it.name}") }
    }
}
