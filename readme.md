## Configuration

Get a personal API key at https://www.nexusmods.com/users/myaccount?tab=api%20access

## Dependencies

You'll need to sudo apt install them

- `7z` for installing from 7zip files
- `libarchive-tools` for installing rar files


## Setup Vortex Links
- Place `smm.desktop` in `~/.local/share/applications/`.
- Edit the Exec line so it points to your jar install location.
- Edit the Path line so it potins at the folder of your jar
- Updated `~/.local/share/applications/mimeapps.list` by adding a line `x-scheme-handler/nxm=smm.desktop`
- Make the desktop the default handler `xdg-mime default smm.desktop x-scheme-handler/nxm`

## Usage

Run the app and then use `help` to see commands. Alternatively you can look at the [generated man page](manual.md).

## Test Commands
```
add ~/Downloads/starfield-mods/sleepy-time
add ~/Downloads/starfield-mods/sleepy-time.zip
add ~/Downloads/starfield-mods/starfield-script-extender-(sfse).7z
mod 1 file ~/Downloads/starfield-mods/interstellar---cinematic-main-menu-edit---music.rar
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