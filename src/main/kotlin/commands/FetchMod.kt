package commands

import cyan
import fetchModInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import toolConfig
import urlToId

val fetchDescription = """
   Add mod metadata without downloading files
   Useful for adding NEW mods. To check for updates on existing mods, see update
""".trimIndent()

val fetchUsage = """
   fetch <mod id>
   fetch 111 222 333
""".trimIndent()

fun fetchMod(command: String, args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(fetchDescription)
        firstArg.toIntOrNull() != null -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
        firstArg.startsWith("http") -> addModByUrls(args)
        else -> fetchModsById(args.mapNotNull { it.toIntOrNull() })
    }
}

fun fetchModsById(ids: List<Int>) {
    ids.chunked(toolConfig.chunkSize).forEach { chunk ->
        runBlocking {
            chunk.map { id ->
                async {
                    fetchModInfo(id)?.let { mod -> println("Fetched info for ${mod.id} ${mod.name}") }
                }
            }.awaitAll()
        }
    }
    println(cyan("Done Fetching"))
}

private fun addModByUrls(urls: List<String>) {
    urls.chunked(toolConfig.chunkSize).forEach { chunk ->
        runBlocking {
            chunk.map {url ->
                async {
                        url.urlToId()?.let { fetchModInfo(it) }?.let { println("Fetched info for ${it.id} ${it.name}") }
                    }
                }.awaitAll()
            }
        }
        println(cyan("Done fetching"))
    }
