cd /root/cs324-distributed/MyRMI/
if [ "$RUN_COMPONENT" == "SERVER" ]; then
echo "Run server!"
java -classpath target/classes/ myrmi.test.Main &
elif [ "$RUN_COMPONENT" == "REGISTRY" ]; then
echo "Run registry!"
java -classpath target/classes -Djava.rmi.server.codebase=file:target/classes myrmi.registry.Main &
elif [ "$RUN_COMPONENT" == "CLIENT" ]; then
echo "Run client!"
java -classpath target/classes/ myrmi.test.Client
else
echo "Error: No component specified!"
fi