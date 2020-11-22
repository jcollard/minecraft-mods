#!/bin/bash

set +e
cd -- "$(dirname "$BASH_SOURCE")"
git config user.name "Local User"
git config user.email local@user.com
git add .
echo "Creating a backup before updating just in case!"
git commit -m "Making backup commit before running update" > /dev/null
git pull -Xtheirs --no-edit

source utilities/check-cache.command

echo "Update complete!"