cd /root/cs324-distributed/HW4/
if [ "$RUN_COMPONENT" == "SERVER" ]; then
echo "Run server!"
java -classpath target/ demo.Server &
elif [ "$RUN_COMPONENT" == "REGISTRY" ]; then
echo "Run registry!"
java -classpath target/ -Djava.rmi.server.codebase=file:target demo.Registry &
elif [ "$RUN_COMPONENT" == "CLIENT" ]; then
echo "Run client!"
java -classpath target/ demo.Client 80000 .065 15
else
echo "Error: No component specified!"
fi