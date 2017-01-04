#!/bin/sh

BASE_PATH=$(cd `dirname $0`; pwd)

cd $BASE_PATH;

mvn clean install -f base-pom.xml
mvn clean install -f 3th-pom.xml
mvn clean install
