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

mvn clean install -Ptest -f webapp/hsy/pom.xml

[ $? -ne 0 ] && error_exit "编译错误"

scp webapp/hss/target/hss-mobile-1.0.0-SNAPSHOT.war ${SERVER}:~/

[ $? -ne 0 ] && error_exit "拷贝war包至服务器错误"

#ssh -t ${SERVER} "/bin/bash /home/webmaster/deployshouqianbao.sh"

#[ $? -ne 0 ] && error_exit "服务器deploy错误"
