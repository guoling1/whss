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

mvn clean install -Pproduction -f webapp/hsy/pom.xml

[ $? -ne 0 ] && error_exit "编译错误"

scp webapp/hss/target/hss-mobile-1.0.0-SNAPSHOT.war ${MASTER}:~/

[ $? -ne 0 ] && error_exit "拷贝war包至主服务器错误"

#scp webapp/shouqianbao/target/hss-shouqianbao-1.0.0-SNAPSHOT.war ${BRAVE}:~/
#
#[ $? -ne 0 ] && error_exit "拷贝war包至备服务器错误"

#ssh -t ${SERVER} "/bin/bash /home/webmaster/deployshouqianbao.sh"

#[ $? -ne 0 ] && error_exit "服务器deploy错误"
