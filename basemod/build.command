#/bin/bash
set -e
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`
./gradlew -g ~/.worlds_of_minecraft_cache --offline buildResources