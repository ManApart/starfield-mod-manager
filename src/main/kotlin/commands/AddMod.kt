package commands

import addModByFile
import addModById
import addModByNexusProtocol
import urlToId

fun addModHelp() = """
   add nexus nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111
   add https://www.nexusmods.com/starfield/mods/4183?tab=files
   add 4183
   add 4183 4182 4181 - Add multiple by id
   add <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(addModHelp())
        firstArg.startsWith("nxm") -> addModByNexusProtocol(firstArg)
        firstArg.toIntOrNull() != null -> addModByIds(args.mapNotNull { it.toIntOrNull() })
        firstArg.startsWith("http") -> addModByUrls(args)
        listOf("/", "./").any { firstArg.startsWith(it) } -> addModByFile(args[0], args.getOrNull(1))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun addModByUrls(urls: List<String>) {
    urls.forEach { url ->
        url.urlToId()?.let { addModById(it) }
    }
    println("Done adding")
}

private fun addModByIds(ids: List<Int>){
    ids.forEach { addModById(it) }
    println("Done adding")
}
