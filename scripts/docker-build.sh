#!/bin/sh

set -e -o pipefail

# validate args
TAG=${1}
if [ -z ${TAG} ]; then
  echo "specify tag as args[1]."
  exit 1
fi

# display tag and version
VERSION=`echo ${TAG} | sed s/dbflute-intro-//g`
echo "TAG      : ${TAG}"
echo "VERSION  : ${VERSION}"

# move working dir
PROJECT_ROOT="$(cd $(dirname "$0") && pwd)/../"
cd $PROJECT_ROOT

# build docker image
cp build/libs/dbflute-intro.jar docker/dbflute-intro
echo ${VERSION} > docker/dbflute-intro/version.txt
docker build docker/dbflute-intro -f docker/dbflute-intro/Dockerfile -t dbflute-intro --no-cache
rm docker/dbflute-intro/dbflute-intro.jar
rm docker/dbflute-intro/version.txt

# add tag and push docker hub
docker tag dbflute-intro dbflute/dbflute-intro:${VERSION}
docker tag dbflute-intro dbflute/dbflute-intro:latest
docker push dbflute/dbflute-intro:${VERSION}
docker push dbflute/dbflute-intro:latest
