#!/bin/bash

cd -- "$(dirname "$BASH_SOURCE")"
git checkout basemod
git pull -Xtheirs --no-edit