# Starfield Mod Manager

[![Build and Test](https://github.com/ManApart/starfield-mod-manager/actions/workflows/runTests.yml/badge.svg)](https://github.com/ManApart/starfield-mod-manager/actions/workflows/runTests.yml)

A CLI based Starfield Mod Manager for Linux. Requires some technical know-how and familiarity with the CLI, but also provides a thin slice of Vortex capabilities.

Currently only fully supports premium members. Non Premium members should be able to add files by zip folder or nexus "download with mod manager links", but likely won't be able to download mods by pasting in a url or id. 

## Features

Most actions can do something
- To one mod
- To a list of mods 
- To a range of mods
- To all mods

Add Mods
- By clicking the "Vortex" download button
- By id
- By url
- By local zip file

Open a Mod
- Locally
- On Nexus

Fetch new metadata and download updates
- Add a mod without downloading it
- Refresh existing mods
- Check for updates
- Download all updated files
- Redownload or restage files

Enable and Disable Mods
- Change load order
- Deploy Dry run to see which files will be applied
- Save local mod lists (profiles)
- Purge all symlinks

Endorse Mods
- Endorse or Abstain from mods

Search Mods
- Use filter to apply search every time
- By name 
- By category
- By enabled
- By Staged
- etc

Sort Mods
- By id
- By name
- By enabled
- By Category
- And more

Validate Mods
- Find mods with improper staging or other issues

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

Run the app and then use `help` to see commands. Alternatively you can look at the [generated man page](manual.md). You can also look through [examples](examples.md).

To reduce typing, most commands take the index of the mod, instead of mod id or name. This means the index of a mod can change as mods are added, deleted or sorted. Listing mods will always show their indices, and filtering will retain the index.


## Alternatives

Don't like this mod manager? Try another one:
- [Ammo](https://github.com/cyberrumor/ammo)
- [Lamp](https://github.com/CHollingworth/Lamp)
- [Starfield Mod Loader](https://github.com/lVlyke/starfield-mod-loader)