# How To Start
2. Start a rmi registry
```
java -classpath target/classes -Djava.rmi.server.codebase=file:target/classes myrmi.registry.Main &
```
3. Start a server
```
java -classpath target/classes/ myrmi.test.Main &
```
4. Start a client
```
java -classpath target/classes/ myrmi.test.Client
```
