#!/bin/sh

BASE_PATH=$(cd `dirname $0`; pwd)

cd $BASE_PATH; cd ..

if [ -e pid ]; then
    kill `cat pid`
    rm -f pid
fi

#强制杀掉
if [[ `ps -ef | grep java |grep xjd.chronos |awk '{print $2}'` ]]; then
    ps -ef | grep java |grep xjd.chronos |awk '{print $2}' |xargs -I {} kill -9 {}
fi
