# Starfield Mod Manager

[![Build and Test](https://github.com/ManApart/starfield-mod-manager/actions/workflows/runTests.yml/badge.svg)](https://github.com/ManApart/starfield-mod-manager/actions/workflows/runTests.yml)

A CLI based Starfield Mod Manager for Linux. Requires some technical know-how and familiarity with the CLI, but also provides an thin slice of Vortex capabilities.

## Configuration

Get a personal API key at https://www.nexusmods.com/users/myaccount?tab=api%20access

### Dependencies

You'll need to `sudo apt install` them

- `7z` for installing from 7zip files
- `libarchive-tools` for installing rar files


### Setup Vortex Links
- Place `smm.desktop` in `~/.local/share/applications/`.
- Edit the Exec line so it points to your jar install location.
- Edit the Path line so it points at the folder of your jar
- Update `~/.local/share/applications/mimeapps.list` by adding a line `x-scheme-handler/nxm=smm.desktop`
- Make the desktop the default handler `xdg-mime default smm.desktop x-scheme-handler/nxm`

### Configure App
Run `help config` to see how to add your api key, set your game folder, set a folder for plugins.txt and optionally fetch a list of categories

## Usage

Run the app and then use `help` to see commands. Alternatively you can look at the [generated man page](manual.md).

## Examples

List Mods
```
ls
Id        Version             Load Order  Staged   Enabled  Category            Index  Name                  
3711      1.1                 3           X                 User Interface      0      interstellar - cinematic main menu edit - music
?         ?                   4           X                 ?                   1      me-music              
4183      1.0                 4           X                 Audio               2      sleepy time - less suggestive wakeup lines
968       1.1                 5           X        X        User Interface      3      orbits - a main menu replacement
106       0.1.5               6           X                 Utilities           4      starfield script extender (sfse)
658       2.0.0               7           X        X        Miscellaneous       5      baka achievement enabler (sfse)
3256      3                   8           X                 Modders Resources   6      address library for sfse plugins

```


Updating
```
update all
Updating 56 mods
Updated 0, 2, 3, 4, 5
(i: 10) mjolnir mark v can upgrade 1.1 -> 2.1
Updated 6, 7, 8, 9, 10
(i: 11) starfield geometry bridge - blender plugin can upgrade 0.9-pre-release-h1 -> 0.10-pre-release
Updated 11, 12, 13, 14, 15
Updated 16, 17, 18, 19, 20
Updated 21, 22, 23, 24, 25
Updated 26, 27, 28, 29, 30
Updated 31, 32, 33, 34, 35
(i: 39) show star names can upgrade 1.2 -> 1.3
Updated 36, 37, 38, 39, 40
(i: 42) hopetech hab spine- glass enclosure can upgrade 1.2 -> 1.3.1
(i: 44) weapon skins unlocked can upgrade 1 -> 1.2
(i: 41) show ship parts count can upgrade 1.0 -> 1.4
Updated 41, 42, 43, 44, 45
Updated 46, 47, 48, 49, 50
(i: 53) starfield extended - armor and clothing crafting can upgrade v1.0 -> v1.1
(i: 54) skill fixes can upgrade 1.0 -> 1.1
(i: 51) luma - native hdr can upgrade 1.0.0 -> 1.0.1
Updated 51, 52, 53, 54, 55
Updated 56
Done Updating

```