#/bin/bash
set -e
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`
echo "Trying build with --offline mode."
./gradlew -g ~/.worlds_of_minecraft_cache --offline buildResources
if [ "$?" != "0" ]; then
    echo "Build failed with offline mode. Trying again with offline mode disabled."
    ./gradlew -g ~/.worlds_of_minecraft_cache buildResources
fi