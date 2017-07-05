#!/bin/bash

error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}

BASE_PATH=$(cd `dirname $0`; pwd)
MASTER="zhangyulong@106.3.36.87"
#BRAVE="root@123.57.22.217"

cd ${BASE_PATH};
cd ..

mvn clean install -Pproduction -f background/chronos/pom.xml

[ $? -ne 0 ] && error_exit "拷贝jar包至服务器错误"

scp background/chronos/target/hss-chronos-1.0.0-SNAPSHOT-assembly.zip ${MASTER}:~/

[ $? -ne 0 ] && error_exit "拷贝jar包至主服务器错误"

#scp background/chronos/target/xjd-chronos-1.0.0-SNAPSHOT-assembly.tar.gz ${BRAVE}:~/
#
#[ $? -ne 0 ] && error_exit "拷贝jar包至备服务器错误"

#ssh -t ${SERVER} "/bin/bash /home/webmaster/deployMobile.sh"

#[ $? -ne 0 ] && error_exit "服务器deploy错误"
