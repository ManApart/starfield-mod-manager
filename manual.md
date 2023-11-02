
# Manual

Command | Description | Aliases | Usage
--- | --- | --- | ---
add |Add a new mod | |add nexus nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111 <br/>add https://www.nexusmods.com/starfield/mods/4183?tab=files <br/>add 4183 <br/>add 4183 4182 4181 - Add multiple by id <br/>add <path-to-mod-zip> <name-of-mod>*
config |Edit Configuration | |config game-path <path-to-folder> - Sets the path to the folder under steam containing the starfield Data folder and exe <br/>config ini-path <path-to-folder> - Sets the path to the folder under your documents that contains StarfieldCustom.ini and eventually Plugins.txt. Needed for updating mod load order <br/>config api-key <key-from-nexus> <br/>config verbose <true/false> - get additional output (for debugging) <br/>config use-my-docs <true/false> - deploy mod files under Data to my documents instead of the game folder. (Defaults to false)  <br/>config categories - download category names from nexus <br/>If your paths have spaces, make sure to quote them
enable |Enable Mod | |enable <mod index> <br/>disable <mod index> <br/>enable 1 2 4 <br/>enable 1-4 <br/>disable all
disable |Enable Mod | |enable <mod index> <br/>disable <mod index> <br/>enable 1 2 4 <br/>enable 1-4 <br/>disable all
endorse |Endorse Mod | |endorse <mod index> - endorse a mod on nexus <br/>abstain <mod index> <br/>endorse 1 2 4 <br/>endorse 1-4
abstain |Abstain from endorsing Mod | |endorse <mod index> - endorse a mod on nexus <br/>abstain <mod index> <br/>endorse 1 2 4 <br/>endorse 1-4
deploy |Deploy enabled mods | |deploy - Applies all mods to the game folder by creating the appropriate symlinks <br/>deploy dryrun - Per your load order view how files will be deployed
help |Explain commands | |
fetch |Fetch Mod Data | |fetch <mod id> - Add mod metadata without downloading files <br/>fetch 111 222 333 - Fetch multiple <br/>Useful for adding NEW mods. To check for updates on existing mods, see update
list |List Mods |ls |List Mod details <br/>list 10 30 - List 30 mods, starting with the 10th mod
detail |View all details of mod | |detail <mod id> - View all mod detail
order |Change Load Order | |order 1 first <br/>order 1 last <br/>order 1 sooner 5 <br/>order 1 later <br/>order 1 set 4 - sets to exactly this order in the load. Any mods with a higher number for load order have their number increased <br/>Mods with a higher load order are loaded later, and override mods loaded earlier. Given mod A has an order 5 and mod B has an order of 1, then A will load AFTER B, and A's files will be used instead of B's in any file conflicts. 
open |Open mod on web | |open <mod index> - open on nexus <br/>local <mod index> - open local folder <br/>open 1 2 4 <br/>open 1-4
local |Open local mod folder | |open <mod index> - open on nexus <br/>local <mod index> - open local folder <br/>open 1 2 4 <br/>open 1-4
purge |Purge all sym links | |purge - delete all symlinks and rename override files <br/>purge dryrun - view what a purge would do without doing it
mod |Update a mod | |mod <mod index> id 123 - Update mod's id <br/>mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip <br/>mod <mod index> name <new name> - renames a mod without changing file paths <br/>rename <mod index> <new name>
profile |Create and use local mod lists | |profile list <br/>profile save <name> - create a new profile  <br/>profile save <index> - save to an existing profile <br/>profile view <index> <br/>profile load <index>
rename |Rename a mod |mv |mod <mod index> id 123 - Update mod's id <br/>mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip <br/>mod <mod index> name <new name> - renames a mod without changing file paths <br/>rename <mod index> <new name>
refresh |Refresh mods by id | |refresh <mod index> <br/>refresh 1 2 4 <br/>refresh 1-3 <br/>refresh all - For all mods with ids, attempt to redownload (or grab the file from the downloads folder if it exists) and restage. <br/>refresh empty - Refresh any files without staged data <br/>refresh staged - Refresh only files that are staged <br/>If you're looking to upgrade to a new version, see update and upgrade
update |Check for newer versions | |update - fetches latest metadata for mods, including new versions and endorsement data <br/>update <mod index> <br/>update 1 2 4 <br/>update 1-3 <br/>Useful for checking for updates existing mods. To check add new mods, see fetch or add. <br/>To download updates, see upgrade
upgrade |Upgrade to newer versions | |upgrade <mod index> <br/>upgrade 1 2 4 <br/>upgrade 1-3 <br/>upgrade all - For all mods with newer versions, attempt to stage the latest version. <br/>If you want to check for new versions, see update <br/>If you're looking to just redownload or restage a file at the current version, see refresh
remove |Delete a mod |rm |remove <mod index> <br/>rm <mod index>
search |Search Mods |grep, awk |Search for mods and list them once <br/>To apply a filter to future lists, see filter <br/>search <search text> - search the given text (name or category)  <br/>search 123 - show matching ids <br/>search enabled - show only enabled mods <br/>search disabled <br/>search staged <br/>search unstaged <br/>search missing - show missing ids
filter |Apply a filter to Mods | |filter mods that contain the given text (name or category) <br/>Changes persist across future ls until filter clear or filter all is called <br/>To do a one time search, see search <br/>filter <search text> -  <br/>filter 123 - show matching ids <br/>filter enabled - show only enabled mods <br/>filter disabled <br/>filter staged <br/>filter unstaged <br/>filter missing - show missing ids
sort |Sort Mods | |sort the list in various ways. Add reverse to invert the sort <br/>sort name <br/>sort id <br/>sort enabled <br/>sort category <br/>sort order <br/>sort staged
validate |List issues with mods | |validate <br/>validate <mod index> <br/>validate 1 2 4 <br/>validate 1-3
start |Launch Starfield |game |start - run the steam game
exit |Exit Program | |Exit the process
    