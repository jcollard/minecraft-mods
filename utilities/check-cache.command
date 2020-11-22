#!/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"

function error_cache_out_of_date {
    clear
    echo "██╗   ██╗██████╗ ██████╗  █████╗ ████████╗███████╗
██║   ██║██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝██╔════╝
██║   ██║██████╔╝██║  ██║███████║   ██║   █████╗  
██║   ██║██╔═══╝ ██║  ██║██╔══██║   ██║   ██╔══╝  
╚██████╔╝██║     ██████╔╝██║  ██║   ██║   ███████╗
 ╚═════╝ ╚═╝     ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝

 Your cache is out of date and you need to run the install_cache.command
 to get the latest cache.

 Press enter to update now.

 Press Control + C to abort
    "
    read -p ""
    source install_cache.command
    exit 0
}

MD5="$(cat ../.cache/worlds_of_minecraft_cache.zip.md5)"
set +e
CACHE_MD5="$(md5 -q ../.cache/worlds_of_minecraft_cache.zip)"
set -e

if [ "$MD5" != "$CACHE_MD5" ]; then
    error_cache_out_of_date
fi