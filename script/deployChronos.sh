#!/bin/bash

error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}

BASE_PATH=$(cd `dirname $0`; pwd)
SERVER="zhangyulong@106.3.36.87"

cd ${BASE_PATH};
cd ..

mvn clean install -Ptest -f background/chronos/pom.xml

[ $? -ne 0 ] && error_exit "编译错误"

scp background/chronos/target/hss-chronos-1.0.0-SNAPSHOT-assembly.zip ${SERVER}:~/

[ $? -ne 0 ] && error_exit "拷贝jar包至服务器错误"

#ssh -t ${SERVER} "/bin/bash /home/webmaster/deployAdmin.sh"

#[ $? -ne 0 ] && error_exit "服务器deploy错误"
