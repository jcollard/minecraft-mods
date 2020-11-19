#/bin/bash
set +x
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`
echo "Trying build with --offline mode."
./gradlew -g ~/.worlds_of_minecraft_cache --offline build
if [ "$?" != "0" ]; then
    clear
    echo "


    
    ********************************************
    * Build failed with offline mode.          *
    *                                          *
    * Press enter to try again with offline    *
    * mode disabled.                           *
    *                                          *
    * Press Control + C now to stop building.  *
    ********************************************



    "
    read -p ""
    ./gradlew -g ~/.worlds_of_minecraft_cache build
fi