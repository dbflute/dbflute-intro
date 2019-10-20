#!/bin/sh

TAG=`git tag | tail -1f`
VERSION=`echo ${TAG} | sed s/dbflute-intro-//g`

echo "TAG      : ${TAG}"
echo "VERSION  : ${VERSION}"

git checkout ${TAG}

docker build . -t dbflute-intro --no-cache
docker tag dbflute-intro dbflute/dbflute-intro:${VERSION}
docker tag dbflute-intro dbflute/dbflute-intro:latest

docker push dbflute/dbflute-intro:${VERSION}
