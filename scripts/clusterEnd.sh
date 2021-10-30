THIS_HOME="/home/ckm/test"
CLUSTER_PID_FILE=${THIS_HOME}/clusterTest.pid

#Checking if pid file exist
if test -e ${CLUSTER_PID_FILE}; then
    #if exists, kill process
    PID=$(cat ${CLUSTER_PID_FILE})
    kill -s 9 ${PID}
    #delete pid file
    rm -f ${CLUSTER_PID_FILE}
else
    #otherwise, exit
    echo "[" ${CLUSTER_PID_FILE} "] not exist."
    exit 1
fi
echo "Process "${PID}" is terminated"
