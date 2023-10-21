
# Manual

Command | Description | Aliases | Usage
--- | --- | --- | ---
add |Add a new mod | |add nexus nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111 <br/>add https://www.nexusmods.com/starfield/mods/4183?tab=files <br/>add 4183 <br/>add 4183 4182 4181 - Add multiple by id <br/>add <path-to-mod-zip> <name-of-mod>*
config |Edit Configuration | |config game-path <path-to-folder> - Sets the path to the folder under steam containing the starfield Data folder and exe <br/>config api-key <key-from-nexus>
enable |Enable Mod | |enable <mod index> <br/>disable <mod index> <br/>enable 1 2 4 <br/>enable 1-4 <br/>disable all
disable |Enable Mod | |enable <mod index> <br/>disable <mod index> <br/>enable 1 2 4 <br/>enable 1-4 <br/>disable all
deploy |Deploy enabled mods | |deploy <br/>Applies all mods to the game folder by creating the appropriate symlinks
help |Explain commands | |
fetch |Fetch Mod Data | |fetch <mod id> - Add mod metadata without downloading files <br/>fetch 111 222 333 - Fetch multiple <br/>Useful for adding NEW mods. To check for updates on existing mods, see update
list |List Mods |ls |List Mod details
detail |View all details of mod | |detail <mod id> - View all mod detail
order |Change Load Order | |order 1 first <br/>order 1 last <br/>order 1 sooner 5 <br/>order 1 later <br/>order 1 set 4 - sets to exactly this order in the load, bumping later mods down
open |Open mod on web | |open <mod index> - open on nexus <br/>local <mod index> - open local folder <br/>open 1 2 4 <br/>open 1-4
local |Open local mod folder | |open <mod index> - open on nexus <br/>local <mod index> - open local folder <br/>open 1 2 4 <br/>open 1-4
purge |Purge all sym links | |TODO
mod |Update a mod | |mod <mod index> id 123 - Update mod's id <br/>mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip <br/>mod <mod index> name <new name> - renames a mod without changing file paths <br/>rename <mod index> <new name>
rename |Rename a mod |mv |mod <mod index> id 123 - Update mod's id <br/>mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip <br/>mod <mod index> name <new name> - renames a mod without changing file paths <br/>rename <mod index> <new name>
refresh |Refresh mods by id | |refresh <mod index> <br/>refresh 1 2 4 <br/>refresh 1-3 <br/>refresh all - For all mods with ids, attempt to redownload (or grab the file from the downloads folder if it exists) and restage. <br/>refresh empty - Refresh any files without staged data <br/>If you're looking to upgrade to a new version, see update and upgrade
update |Check for newer versions | |update <mod index> <br/>update 1 2 4 <br/>update 1-3 <br/>update all <br/>Useful for checking for updates existing mods. To check add new mods, see fetch or add. <br/>To download updates, see upgrade
upgrade |Upgrade to newer versions | |upgrade <mod index> <br/>upgrade 1 2 4 <br/>upgrade 1-3 <br/>upgrade all - For all mods with newer versions, attempt to stage the latest version. <br/>If you want to check for new versions, see update <br/>If you're looking to just redownload or restage a file at the current version, see refresh
remove |Delete a mod |rm |remove <mod index> <br/>rm <mod index>
search |Search Mods |grep, awk |search <search text> - filter mods that contain the given text <br/>search 123 - show matching ids <br/>search enabled - show only enabled mods <br/>search disabled <br/>search staged <br/>search unstaged <br/>search missing - show missing ids
validate |List issues with mods | |validate <mod index> <br/>validate 1 2 4 <br/>validate 1-3 <br/>validate all
exit |Exit Program | |Exit the process
    