#!/bin/sh

BASE_PATH=$(cd `dirname $0`; pwd)

cd $BASE_PATH; cd ..

kill `cat pid`

rm -f pid
