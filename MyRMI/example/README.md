# How To Start
1. Compile the files
```
javac -d target/ foo/Client.java foo/Foo.java foo/IFoo.java foo/Main.java
```
2. Start a rmi registry
```
rmiregistry &
```
3. Start a server
```
java -classpath target/ -Djava.rmi.server.codebase=file:target/ example.foo.Main &
```
4. Start a client
```
java -classpath target/ example.foo.Client
```
