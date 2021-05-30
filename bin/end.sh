THIS_HOME="/home/ckm/test"
CKM_PID_FILE=${THIS_HOME}/ckmTest.pid

#Checking if pid file exist
if test -e ${CKM_PID_FILE}; then
    #if exists, kill process
    PID=$(cat ${CKM_PID_FILE})
    kill -s 9 ${PID}
    #delete pid file
    rm -f ${CKM_PID_FILE}
else
    #otherwise, exit
    echo "[" ${CKM_PID_FILE} "] not exist."
    exit 1
fi
echo "Process "${PID}" is terminated"
