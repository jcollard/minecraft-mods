#!/bin/bash
set -e
cd -- "$(dirname "$BASH_SOURCE")"
git add .
echo "Creating a backup before updating just in case!"
set +e
git commit -m "Making backup commit before running update" > /dev/null
set -e
git pull -Xtheirs --no-edit

source utilities/update-cache.command

echo "Update complete!"