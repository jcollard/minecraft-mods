#/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`
echo "Trying build with --offline mode."
./gradlew -g /Library/worlds_of_minecraft_cache --offline build