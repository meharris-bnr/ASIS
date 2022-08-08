@ECHO OFF
SET BINDIR=%~dp0
CD /D "%BINDIR%

SET LOCALAPPDATA=Z:\home\me\Games\mod-organizer-2-skyrimspecialedition\modorganizer2\profiles\Default
REM equivalent to /home/me/Games/mod-organizer-2-skyrimspecialedition/modorganizer2/profiles/Default

SET SP_GLOBAL_PATH_TO_DATA=Z:\home\me\.steam\debian-installation\steamapps\common\Skyrim Special Edition\Data\
REM filesystem delimiter at the end of 'Data' is important!
REM equivalent to /home/me/.steam/debian-installation/steamapps/common/Skyrim Special Edition/Data/

SET SP_GLOBAL_PATH_TO_INI=C:\users\steamuser\My Documents\My Games\Skyrim Special Edition\Skyrim.ini
REM equivalent to /home/me/.steam/debian-installation/steamapps/compatdata/489830/pfx/drive_c/users/steamuser/Documents/My Games/Skyrim Special Edition/Skyrim.ini

SET TWEAKINIS_PATH=Z:\home\me\TweakInis
REM equivalent to /home/me/TweakInis

REM -GENPATCH just runs the patch export process
"C:\java\jdk8u312-b07-jre\bin\javaw.exe" -jar -Xmx1024M -Xms1024M asis.jar -GENPATCH
