THIS_HOME="/home/ckm/test"
LIB_HOME="$THIS_HOME/lib"

#check ckmTest.pid
if test -e ckmTest.pid; then
    PID=$(cat ckmTest.pid)
    if kill -0 $PID; then
        echo Process running with PID=$PID
        exit 1
    else
        echo Invalid pid file. Possible process was killed.
        rm -f ckmTest.pid
    fi
fi

#start jvm
nohup java \
-Xms4g \
-Xmx4g \
-classpath "\
$LIB_HOME/animal-sniffer-annotations-1.14.jar:\
$LIB_HOME/ant-1.10.3.jar:\
$LIB_HOME/ant-launcher-1.10.3.jar:\
$LIB_HOME/aopalliance-1.0.jar:\
$LIB_HOME/aspectjweaver-1.8.4.jar:\
$LIB_HOME/checker-compat-qual-2.0.0.jar:\
$LIB_HOME/commons-lang3-3.9.jar:\
$LIB_HOME/commons-logging-1.2.jar:\
$LIB_HOME/commons-text-1.8.jar:\
$LIB_HOME/error_prone_annotations-2.1.3.jar:\
$LIB_HOME/gson-2.8.6.jar:\
$LIB_HOME/guava-25.1-android.jar:\
$LIB_HOME/guice-4.2.2-no_aop.jar:\
$LIB_HOME/hamcrest-core-1.3.jar:\
$LIB_HOME/j2objc-annotations-1.1.jar:\
$LIB_HOME/javax.inject-1.jar:\
$LIB_HOME/jcommander-1.78.jar:\
$LIB_HOME/jsr305-3.0.2.jar:\
$LIB_HOME/junit-4.12.jar:\
$LIB_HOME/log4j-api-2.7.jar:\
$LIB_HOME/log4j-core-2.7.jar:\
$LIB_HOME/lucene-analyzers-common-8.7.0.jar:\
$LIB_HOME/lucene-analyzers-smartcn-8.7.0.jar:\
$LIB_HOME/lucene-core-8.7.0.jar:\
$LIB_HOME/snakeyaml-1.21.jar:\
$LIB_HOME/spring-aop-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-aspects-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-beans-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-context-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-core-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-expression-4.3.9.RELEASE.jar:\
$LIB_HOME/spring-web-4.3.9.RELEASE.jar:\
$LIB_HOME/testng-7.3.0.jar:\
$THIS_HOME/CkmTest.jar" \
com.trs.ckm.test.stability.Entrance &
echo $! > ${THIS_HOME}/ckmTest.pid
