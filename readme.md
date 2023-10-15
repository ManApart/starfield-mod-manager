## Configuration

Get a personal API key at https://www.nexusmods.com/users/myaccount?tab=api%20access

## Dependencies

You'll need to sudo apt install them

- `7z` for installing from 7zip files
- `libarchive-tools` for installing rar files


## Setup Vortex Links
- Place `smm.desktop` in `~/.local/share/applications/`.
- Edit the exec line so it points to your jar install location.
- Updated `~/.local/share/applications/mimeapps.list` by adding a line `x-scheme-handler/nxm=smm.desktop`
- Make the desktop the default handler `xdg-mime default smm.desktop x-scheme-handler/nxm`


## Test Commands
```
add ~/Downloads/sleepy-time
add ~/Downloads/sleepy-time.zip
add ~/Downloads/starfield-script-extender-(sfse).7z
```

## Command Concept

```
# Check for updates for all mods with an id
update

# Update a specific mod or all mods
upgrade 1
upgrade

sort order
sort name
sort enabled

```