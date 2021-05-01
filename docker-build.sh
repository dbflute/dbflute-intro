#!/bin/sh

set -e -o pipefail

# build docker image
docker build . -t dbflute-intro --no-cache
