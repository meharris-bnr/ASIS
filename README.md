Automatic Spells, Increased Spawns, AI, all the works built from the core library of SkyProc for Skyrim.

---------

Current issues:
* NPC records lose there Full and Short names when they are copied across, shouldn't be too hard to fix.
* SSEEdit shows a bunch of missing tints when checking for errors.

Environment variables:

All run configurations require at least two environment variables:

LOCALAPPDATA, which defines the path to 'plugins.txt', which should be managed by your mod manager (i.e., MO2)

and SP_GLOBAL_PATH_TO_INI, which defines the path to Skyrim.ini.

Examples (from my Linux host using MO2 w/default profile):

LOCALAPPDATA=/home/me/Games/mod-organizer-2-skyrimspecialedition/modorganizer2/profiles/Default

SP_GLOBAL_PATH_TO_INI=/home/me/.steam/debian-installation/steamapps/compatdata/489830/pfx/drive_c/users/steamuser/Documents/My Games/Skyrim Special Edition/Skyrim.ini

ASIS also requires TWEAKINI_PATH, which should point to a folder containing TweakIniLoadExclusions.txt and/or TweakIniLoadOrder.txt 

Configuration properties (application.properties):

The following configuration properties are required:

sp.global.path.to.data, which defines the path to the SSE 'Data' folder.

Example:

sp.global.path.to.data=/home/me/.steam/debian-installation/steamapps/common/Skyrim Special Edition/Data/
