#!/bin/bash

cd -- "$(dirname "$BASH_SOURCE")"
git checkout basemod
git pull -Xtheirs --no-edit
find . -type f | grep ".command$" | xargs chmod +x
find . -type f | grep "gradlew" | xargs chmod +x
