#!/bin/sh

set -e -o pipefail

# validate args
TAG=${1}
if [ -z ${TAG} ]; then
  echo "specify tag as args[1]."
  exit 1
fi

# add tag and push docker hub
docker tag dbflute-intro dbflute/dbflute-intro:${TAG}
docker tag dbflute-intro dbflute/dbflute-intro:latest
docker push dbflute/dbflute-intro:${TAG}
docker push dbflute/dbflute-intro:latest
