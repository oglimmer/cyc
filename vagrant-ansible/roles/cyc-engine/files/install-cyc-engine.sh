#!/bin/sh

CYC_ENGINE_CONTAINER=/usr/local/cyc-engine-container

for file in `ls -1 $CYC_ENGINE_CONTAINER | grep cyc | sort -r| awk '{gsub(/\/.*\/|cyc/,"",$1); printf("%03d\n",++$1);}'`
do
  VERSION=$file
  break
done

if [ -z "$VERSION" ]; then
  VERSION=001
fi

mkdir -p $CYC_ENGINE_CONTAINER/tmp-deploy
cp /tmp/engine-jar-with-dependencies.jar $CYC_ENGINE_CONTAINER/tmp-deploy
mv $CYC_ENGINE_CONTAINER/tmp-deploy $CYC_ENGINE_CONTAINER/cyc$VERSION

