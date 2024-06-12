package commands

import addCreation
import addModByFile
import addModById
import addModByNexusProtocol
import cyan
import urlToId

val addModDescription = """
   Add a new mod, including downloading it
   You can add mods by nexus mod manager url, by mod url, by mod id, or even by path to a local zip
   For urls and ids, you can add multiple at once, space separated
   Example nexus mod manager url: nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111
   Example mod url: https://www.nexusmods.com/starfield/mods/4183?tab=files
   Example adding a creation: add SFBGS003
""".trimIndent()

val addModUsage = """
   add <nxm://starfield/...>
   add <creation-id>
   add 4183
   add 4183 4182 4181
   add <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(addModDescription)
        firstArg.startsWith("nxm") -> addModByNexusProtocol(firstArg)
        firstArg.toIntOrNull() != null -> addModByIds(args.mapNotNull { it.toIntOrNull() })
        firstArg.startsWith("http") -> addModByUrls(args)
        firstArg.lowercase().startsWith("sfbgs") -> addCreation(firstArg, args.getOrNull(1))
        listOf("/", "./").any { firstArg.startsWith(it) } -> addModByFile(args[0], args.getOrNull(1))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun addModByUrls(urls: List<String>) {
    urls.forEach { url ->
        url.urlToId()?.let { addModById(it) }
    }
    println(cyan("Done adding"))
}

private fun addModByIds(ids: List<Int>){
    ids.forEach { addModById(it) }
    println(cyan("Done adding"))
}
