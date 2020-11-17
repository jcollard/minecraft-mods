#/bin/bash
cd -- "$(dirname "$BASH_SOURCE")"
./gradlew genEclipseRuns
./gradlew eclipse