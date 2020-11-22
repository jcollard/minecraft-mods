#!/bin/bash

cd -- "$(dirname "$BASH_SOURCE")"
cd ../.cache
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`

WORLDS_OF_MINECRAFT_PATH="/Library/worlds_of_minecraft_cache"
MD5_FILE="worlds_of_minecraft_cache.zip.md5"
CACHE_FILE="worlds_of_minecraft_cache.zip"

clear

echo "██╗    ██╗ ██████╗ ██████╗ ██╗     ██████╗ ███████╗     ██████╗ ███████╗ 
██║    ██║██╔═══██╗██╔══██╗██║     ██╔══██╗██╔════╝    ██╔═══██╗██╔════╝ 
██║ █╗ ██║██║   ██║██████╔╝██║     ██║  ██║███████╗    ██║   ██║█████╗   
██║███╗██║██║   ██║██╔══██╗██║     ██║  ██║╚════██║    ██║   ██║██╔══╝   
╚███╔███╔╝╚██████╔╝██║  ██║███████╗██████╔╝███████║    ╚██████╔╝██║      
 ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═════╝ ╚══════╝     ╚═════╝ ╚═╝      
                                                                         
███╗   ███╗██╗███╗   ██╗███████╗ ██████╗██████╗  █████╗ ███████╗████████╗
████╗ ████║██║████╗  ██║██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝╚══██╔══╝
██╔████╔██║██║██╔██╗ ██║█████╗  ██║     ██████╔╝███████║█████╗     ██║   
██║╚██╔╝██║██║██║╚██╗██║██╔══╝  ██║     ██╔══██╗██╔══██║██╔══╝     ██║   
██║ ╚═╝ ██║██║██║ ╚████║███████╗╚██████╗██║  ██║██║  ██║██║        ██║   
╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝        ╚═╝   

This program attempts to rebuild the cache from scratch and may take awhile to
run as it downloads about 1.2 Gigabytes of data from various sources online. Are
you sure you want to continue?

Press enter to continue

Press Control + C to abort
"
read -p ""

# Get sudo privileges so we can install software

SUCCESS=1
while [ "$SUCCESS" != "0" ]; do
clear
    echo "
███████ ███    ██ ████████ ███████ ██████                          
██      ████   ██    ██    ██      ██   ██                         
█████   ██ ██  ██    ██    █████   ██████                          
██      ██  ██ ██    ██    ██      ██   ██                         
███████ ██   ████    ██    ███████ ██   ██                         
                                                                   
                                                                   
██████   █████  ███████ ███████ ██     ██  ██████  ██████  ██████  
██   ██ ██   ██ ██      ██      ██     ██ ██    ██ ██   ██ ██   ██ 
██████  ███████ ███████ ███████ ██  █  ██ ██    ██ ██████  ██   ██ 
██      ██   ██      ██      ██ ██ ███ ██ ██    ██ ██   ██ ██   ██ 
██      ██   ██ ███████ ███████  ███ ███   ██████  ██   ██ ██████  
                                                                   
To install the cache, you may need to enter your password. This is
the password you use to log into your computer. When you type, it
will not show up on the screen.
"
    sudo ls > /dev/null
    SUCCESS=$?
done

clear

# Check if cache exists
if [ ! -d $WORLDS_OF_MINECRAFT_PATH ]; then
    echo "Creating cache directory."
    sudo mkdir -p "$WORLDS_OF_MINECRAFT_PATH"
    sudo chown "$USER" "$WORLDS_OF_MINECRAFT_PATH"
fi

MD5="$(cat $MD5_FILE)"
set +e
CACHE_MD5="$(md5 -q $CACHE_FILE)"
set -e

if [ "$MD5" != "$CACHE_MD5" ]; then
clear
    echo "                                                          
 ____  _____ _ _ _ _____ __    _____ _____ ____  _____ _____ _____ 
|    \|     | | | |   | |  |  |     |  _  |    \|     |   | |   __|
|  |  |  |  | | | | | | |  |__|  |  |     |  |  |-   -| | | |  |  |
|____/|_____|_____|_|___|_____|_____|__|__|____/|_____|_|___|_____|
                                                                   
The cache is being downloaded. This may take awhile depending on the
speed of your internet. To abort press Control + C at anytime.
"
    curl https://worlds-of-minecraft.s3-us-west-1.amazonaws.com/worlds_of_minecraft_cache.zip --output "$CACHE_FILE"
    CACHE_MD5="$(md5 -q $CACHE_FILE)"
    if [ "$MD5" != "$CACHE_MD5" ]; then
        echo "Something went wrong!"
        echo "Update could not be completed."
        exit 1
    fi
fi

VERSION="$(cat VERSION.txt)"
set +e
CACHE_VERSION="$(cat $WORLDS_OF_MINECRAFT_PATH/VERSION.txt)"
set -e

if [ "$VERSION" != "$CACHE_VERSION" ]; then
    clear
    echo "_____ _____ _____ _____ _____ __    __    _____ _____ _____ 
|     |   | |   __|_   _|  _  |  |  |  |  |     |   | |   __|
|-   -| | | |__   | | | |     |  |__|  |__|-   -| | | |  |  |
|_____|_|___|_____| |_| |__|__|_____|_____|_____|_|___|_____|
                                                             
"
    echo "Installing Cache... this may take some time."
    unzip -o "$CACHE_FILE" -d "$WORLDS_OF_MINECRAFT_PATH" > /dev/null
    set +e
    CACHE_VERSION="$(cat $WORLDS_OF_MINECRAFT_PATH/VERSION.txt)"
    set -e
    if [ "$VERSION" != "$CACHE_VERSION" ]; then
        echo "Cache versions did not match! Something went wrong."
        exit 1
    fi
fi 

clear
echo "████████ ███████ ███████ ████████ ██ ███    ██  ██████  
   ██    ██      ██         ██    ██ ████   ██ ██       
   ██    █████   ███████    ██    ██ ██ ██  ██ ██   ███ 
   ██    ██           ██    ██    ██ ██  ██ ██ ██    ██ 
   ██    ███████ ███████    ██    ██ ██   ████  ██████  

The last step is to test if the install works! This will build Minecraft
and run it. If all goes well, Minecraft will open up for you. If it does,
you're ready to go! When you exit Minecraft it will say Build Failed. That
is normal.

If Minecraft never opens and it says Build Failed, something went wrong.

Press enter to continue

Press Control + C at anytime to abort                   
"
read -p ""

cd sample-mod

./gradlew -g "$WORLDS_OF_MINECRAFT_PATH" --offline build
cp -r build ../../basemod/build
./gradlew -g "$WORLDS_OF_MINECRAFT_PATH" --offline runClient

echo "Done"