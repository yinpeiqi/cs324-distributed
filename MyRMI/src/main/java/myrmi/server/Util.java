package myrmi.server;

import myrmi.Remote;
import myrmi.registry.Registry;

import java.lang.reflect.Proxy;

public class Util {


    public static Remote createStub(RemoteObjectRef ref) {
        StubInvocationHandler invocationHandler = new StubInvocationHandler(ref);
        if (ref.getInterfaceName().equals("myrmi.registry.Registry")) {
            return (Registry) Proxy.newProxyInstance(
                Registry.class.getClassLoader(),
                new Class<?>[]{Registry.class},
                invocationHandler);
        }
        else {
            return (Remote) Proxy.newProxyInstance(
                UnicastRemoteObject.class.getClassLoader(),
                UnicastRemoteObject.class.getInterfaces(),
                invocationHandler);
        }
        //TODO: finish here, instantiate an StubInvocationHandler for ref and then return a stub
    }

}
