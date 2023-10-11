## Configuration

Get a personal API key at https://www.nexusmods.com/users/myaccount?tab=api%20access


## Setup Vortex Links
- Place `smm.desktop` in `~/.local/share/applications/`.
- Edit the exec line so it points to your jar install location.
- Updated `~/.local/share/applications/mimeapps.list` by adding a line `x-scheme-handler/nxm=smm.desktop`
- Make the desktop the default handler `xdg-mime default smm.desktop x-scheme-handler/nxm`


## Test Commands
```
add file ~/Downloads/sleepy-time
add file ~/Downloads/sleepy-time.zip
```

## Command Concept

```
enable <index>
enable 1
disable 2
enable 1 2 4
enable 1-4

add nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111
#Get primary mod file and download it
add https://www.nexusmods.com/starfield/mods/4183?tab=files
add ~/Downloads/sleepy-time.zip

# Delete mod
# Should require confirmation
rm 1

# Check for updates for all mods with an id
update

# Update a specific mod or all mods
upgrade 1
upgrade

# Change mod id
mod 1 id 123 
#Delete stage and add new file
mod 2 file ~/Downloads/sleepy-time.zip

# Move mod in index 1 to 10th in load order
order 1 10

#Place mod at top or bottom of load order
order 1 top
order 1 bottom

sort order
sort name
sort enabled


```