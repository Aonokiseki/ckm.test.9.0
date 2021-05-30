THIS_HOME="/home/ckm/test"

list="\
${THIS_HOME}/logs/* \
${THIS_HOME}/nohup.out \
${THIS_HOME}/testdata/output/clu/* \
${THIS_HOME}/*.nmon \
${THIS_HOME}/test-output \
${THIS_HOME}/jstacks/*"

#clean files
for f in $list; do
    if test -e "$f"; then
        echo "delete [" ${f} "]"
        rm "$f" -rf
    fi
done
echo "OK"
