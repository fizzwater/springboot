#!/bin/bash

#./deploy.sh opsmanager
#./deploy.sh monitor-service
#./deploy.sh monitor-ui
################### Input parameter for user ###################################

PROJECT_NAME=$1
GIT_COMMOND=
MVN_COMMOND=
WAR_FILE=
WAR_NAME=
TOMCAT_NAME=
FHOME=/root/ops
THOME=/usr/local/tomcat-server
################################# main ######################################
main(){

#               source /home/master/.bash_profile

                if [ $PROJECT_NAME = "monitor-service" ]; then
                        GIT_COMMOND="http://fizzwater:pwd@git.yoho.cn/ops/monitor-service.git"
                        MVN_COMMOND="-Plocal"
                        WAR_FILE=$FHOME/monitor-service/monitor-service-web/target
                        WAR_NAME=monitor
                        TOMCAT_NAME=apache-tomcat-7.0.81-server
                        deploy
                elif [ $PROJECT_NAME = "monitor-ui" ]; then
                        GIT_COMMOND="http://fizzwater:pwd@git.yoho.cn/ops/monitor-ui.git"
                        MVN_COMMOND="-Plocal"
                        WAR_FILE=$FHOME/monitor-ui/monitor-ui-web/target
                        WAR_NAME=ROOT
                        TOMCAT_NAME=apache-tomcat-7.0.81-ui
                        deploy
                elif [ $PROJECT_NAME = "opsmanager" ]; then
                    GIT_COMMOND="http://fizzwater:pwd@git.yoho.cn/ops/opsmanager.git"
                        MVN_COMMOND="-Ptest"
                        WAR_FILE=$FHOME/opsmanager/yoho-opsmanager-web/target
                        WAR_NAME=ROOT
                        TOMCAT_NAME=tomcat_dev_ops_com
                        deploy
                else
                        echo "EXE_COMMOND error!"
                fi
}

################################Update source code & package####################
package(){
        cd $FHOME
    rm -rf  $FHOME/$PROJECT_NAME
        /usr/local/bin/git clone   $GIT_COMMOND
        cd $FHOME/$PROJECT_NAME
        /usr/local/maven3/bin/mvn clean package -Dmaven.test.skip=true $MVN_COMMOND

}

reloadtomcat(){
        cd $THOME/$TOMCAT_NAME/webapps
    rm -rf  $WAR_NAME*

        keys=`(ps -ef |grep $TOMCAT_NAME  |grep -v "grep") | awk '{print $2}'`
                for key in ${keys[*]}
                        do
                                kill -9 $key
                        done

        cp $WAR_FILE/$WAR_NAME.war ./
        cd $THOME/$TOMCAT_NAME/bin
        ./startup.sh

}

##################################deploy app########################################
deploy(){

        package
        reloadtomcat
}

main