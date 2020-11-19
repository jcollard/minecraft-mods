#!/bin/bash
set -e
cd -- "$(dirname "$BASH_SOURCE")"
git add .
echo "Creating a backup before updating just in case!"
git commit -m "Making backup commit before running update" > /dev/null
git pull -Xtheirs --no-edit
echo "Update complete!"