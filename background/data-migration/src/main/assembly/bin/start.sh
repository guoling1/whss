#!/bin/sh

JAVA_HOME=${java_home}

BASE_PATH=$(cd `dirname $0`; pwd)

cd $BASE_PATH; cd ..

LOGDIR="${log.dir}"

if [ ! -d "$LOGDIR" ]; then
        mkdir -p "$LOGDIR"
        if [ $? -ne 0 ]; then
                echo "Try to make dir: $LOGDIR failed, maybe privilige is not enough!!!"
                exit 1
        fi
fi

CLASS_PATH="./:config:conf:lib/*:libext/*:"

JAVA_OPTIONS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Xms512m -Xmx512m -XX:PermSize=64M -XX:MaxPermSize=256M"
JAVA_OPTIONS="$JAVA_OPTIONS -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError -XX:+DisableExplicitGC"

# main class
JAVA_OPTIONS="$JAVA_OPTIONS "

$JAVA_HOME/bin/java -classpath $CLASS_PATH $JAVA_OPTIONS

# get latested process pid
echo $! > pid
