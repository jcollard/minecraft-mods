#/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"
export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_261`

set +e
./gradlew -g /Library/worlds_of_minecraft_cache --offline buildResources
FAIL=$?
set -e
if [ "$FAIL" != "0" ]; then
    source ../utilities/check-cache.command
fi