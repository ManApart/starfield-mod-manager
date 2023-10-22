package commands

import Mod
import Profile
import save
import toolConfig
import toolData

fun profileHelp() = """
    profile list
    profile save <name> - create a new profile 
    profile save <index> - save to an existing profile
    profile view <index>
    profile load <index>
""".trimIndent()

//display like mod and operate off indexes
fun profile(args: List<String> = listOf()) {
    val subCommand = args.firstOrNull()
    val index = args.lastOrNull()?.toIntOrNull()
    when {
        subCommand == null || subCommand == "list" -> viewProfiles()
        subCommand == "view" && index != null -> viewProfile(index)
        subCommand == "load" && index != null -> loadProfile(index)
        subCommand == "save" && index != null -> saveProfile(index)
        subCommand == "save" && args.size > 1 -> saveProfile(args.drop(1).joinToString(" "))
        else -> println(profileHelp())
    }
}

private fun viewProfiles() {

}

private fun viewProfile(i: Int) {

}

private fun loadProfile(i: Int) {

}

private fun saveProfile(i: Int) {
    toolData.profileByIndex(i)?.let { profile ->
        //save requires confirmation and shows diff
        val newIds = enabledIds()
        val newPaths = enabledPaths()
        
    }
}

private fun saveProfile(name: String) {
    if (toolData.profiles.any { it.name == "name" }) {
        println("Profile already exists with name $name.")
        println("To save to this profile, use the index reference.")
        return
    }
    toolData.profiles.add(Profile(name, enabledIds(), enabledPaths()))
    save()
    println("Saved")
}


private fun enabledIds() = toolData.mods.filter { it.enabled }.mapNotNull { it.id }

private fun enabledPaths() = toolData.mods.filter { it.enabled && it.id == null }.map { it.filePath }