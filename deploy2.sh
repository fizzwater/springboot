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
FHOME=/home/master/deploy
THOME=/usr/local/tomcat-server
################################# main ######################################
main(){

#               source /home/master/.bash_profile

                if [ $PROJECT_NAME = "recruit-gateway" ]; then
                        GIT_COMMOND="https:qinchao:qc3570650@//git.lug.ustc.edu.cn/rcr/recruit-gateway.git"
                        MVN_COMMOND="-Pdev"
                        deploy
                else
                        echo "EXE_COMMOND error!"
                fi
}

################################Update source code & package####################
packageRcr(){
        cd $FHOME
        git clone   $GIT_COMMOND
        cd $FHOME/$PROJECT_NAME
        mvn clean package -Dmaven.test.skip=true $MVN_COMMOND
        echo "finished package"

}

##################################deploy app########################################
deploy(){
        packageRcr
}

main