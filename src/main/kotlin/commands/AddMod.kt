package commands

import addModByFile
import addModById
import addModByNexusProtocol

fun addModHelp(args: List<String> = listOf()) = """
   add nexus nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111
   add url https://www.nexusmods.com/starfield/mods/4183?tab=files
   add id 4183
   add id 4183 4182 4181 - Add multiple by id
   add file <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val subCommand = args.firstOrNull()
    when {
        args.size < 2 -> println(addModHelp())
        subCommand == "nexus" -> addModByNexusProtocol(args[1])
        subCommand == "id" && args.size > 2 -> addModByIds(args.drop(1).map { it.toInt() })
        subCommand == "id" -> addModById(args[1].toInt())
        subCommand == "url" -> addModByUrl(args[1])
        subCommand == "file" -> addModByFile(args[1], args.getOrNull(2))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun addModByUrl(url: String) {
    url.replace("https://www.nexusmods.com/starfield/mods/", "").let { idPart ->
        val end = idPart.indexOf("?").takeIf { it > 0 } ?: idPart.length
        idPart.substring(0, end)
    }.toIntOrNull()?.let { addModById(it) } ?: println("Could not find id in $url")
}

private fun addModByIds(ids: List<Int>) = ids.forEach { addModById(it) }
