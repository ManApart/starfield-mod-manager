package commands

import fetchModInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import toolConfig
import urlToId

fun fetchHelp() = """
   fetch <mod id> - Add mod metadata without downloading files
   fetch 111 222 333 - Fetch multiple
   Useful for adding NEW mods. To check for updates on existing mods, see update
""".trimIndent()

fun fetchMod(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(fetchHelp())
        firstArg.toIntOrNull() != null -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
        firstArg.startsWith("http") -> addModByUrls(args)
        else -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
    }
}

private fun fetchModsById(ids: List<Int>) {
    ids.chunked(toolConfig.chunkSize).forEach { chunk ->
        runBlocking {
            chunk.map { id ->
                async {
                    fetchModInfo(id)?.let { mod -> println("Fetched info for ${mod.name}") }
                }
            }.awaitAll()
        }
    }
    println("Done Fetching")
}

private fun addModByUrls(urls: List<String>) {
    urls.chunked(toolConfig.chunkSize).forEach { chunk ->
        runBlocking {
            chunk.map {url ->
                async {
                        url.urlToId()?.let { fetchModInfo(it) }?.let { println("Fetched info for ${it.name}") }
                    }
                }.awaitAll()
            }
        }
        println("Done fetching")
    }
