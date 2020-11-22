#!/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"
CACHE_PATH="../.cache"
MD5_FILE="$CACHE_PATH/worlds_of_minecraft_cache.zip.md5"
CACHE_FILE="$CACHE_PATH/worlds_of_minecraft_cache.zip"
VERSION_FILE="$CACHE_PATH/VERSION.txt"
WORLDS_OF_MINECRAFT_PATH="/Library/worlds_of_minecraft_cache"
CACHE_VERSION_FILE="$WORLDS_OF_MINECRAFT_PATH/VERSION.txt"

if [ ! -d $WORLDS_OF_MINECRAFT_PATH ]; then
    echo "Cache path not found."
    sudo mkdir -p "$WORLDS_OF_MINECRAFT_PATH"
fi

OWNER="$(ls -ld $WORLDS_OF_MINECRAFT_PATH | awk '{print $3}')"

if [ "$OWNER" != "$USER" ]; then
    sudo chown "$USER" "$WORLDS_OF_MINECRAFT_PATH"
fi

MD5="$(cat $MD5_FILE)"
set +e
CACHE_MD5="$(md5 -q $CACHE_FILE)"
set -e

if [ "$MD5" != "$CACHE_MD5" ]; then
    echo "
    
    
    Cache is out of date, downloading newest version. 
    This will take some time.

    To abort update, press Control + C


    "
    curl https://worlds-of-minecraft.s3-us-west-1.amazonaws.com/worlds_of_minecraft_cache.zip --output "$CACHE_FILE"
    CACHE_MD5="$(md5 -q $CACHE_FILE)"
    if [ "$MD5" != "$CACHE_MD5" ]; then
        echo "Cache file does not match, something went wrong!"
        echo "Update could not be completed."
        exit 1
    fi
fi

VERSION="$(cat $CACHE_PATH/VERSION.txt)"
set +e
CACHE_VERSION="$(cat $CACHE_VERSION_FILE)"
set -e

if [ "$VERSION" != "$CACHE_VERSION" ]; then
    echo "Installing Cache... may require your login password"
    sudo rm -rf "$WORLDS_OF_MINECRAFT_PATH/*"
    unzip "$CACHE_FILE" -d "$WORLDS_OF_MINECRAFT_PATH" > /dev/null
    set +e
    echo "Done!"
    CACHE_VERSION="$(cat $CACHE_VERSION_FILE)"
    set -e
    if [ "$VERSION" != "$CACHE_VERSION" ]; then
        echo "Cache versions did not match! Something went wrong."
        exit 1
    fi
fi 