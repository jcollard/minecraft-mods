#/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`

function error_cache_out_of_date {
    clear
    echo "


    
    ********************************************
    * Your cache is out of date and needs to   *
    * be updated. The download is ~800 MB      *
    *                                          *
    * Press enter to download and install the  *
    * cache.                                   *
    *                                          *
    * Press Control + C now to stop building.  *
    ********************************************



    "
    read -p ""
    source ../update.command
    exit 0
}

MD5="$(cat ../.cache/worlds_of_minecraft_cache.zip.md5)"
set +e
CACHE_MD5="$(md5 -q ../.cache/worlds_of_minecraft_cache.zip)"
set -e

if [ "$MD5" != "$CACHE_MD5" ]; then
    error_cache_out_of_date
fi

./gradlew -g /Library/worlds_of_minecraft_cache --offline buildResources
