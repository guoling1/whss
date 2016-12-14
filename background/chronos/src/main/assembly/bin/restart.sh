#!/bin/sh
BASE_PATH=$(cd `dirname $0`; pwd)

cd ${BASE_PATH};
sh stop.sh

cd ${BASE_PATH};
sh start.sh
