package myrmi.server;

import myrmi.Remote;
import myrmi.exception.RemoteException;


public class UnicastRemoteObject implements Remote, java.io.Serializable {
    int port;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int port) throws RemoteException {
        this.port = port;
        exportObject(this, port);
    }

    public static Remote exportObject(Remote obj) throws RemoteException {
        return exportObject(obj, 0);
    }

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj, "127.0.0.1", port);
    }

    /**
     * 1. create a skeleton of the given object ``obj'' and bind with the address ``host:port''
     * 2. return a stub of the object ( Util.createStub() )
     **/
    static int totalObject = 0;
    public static Remote exportObject(Remote obj, String host, int port) throws RemoteException {
        int objectKey = totalObject;
        totalObject++;
        RemoteObjectRef registryRef = new RemoteObjectRef(host, port, objectKey, "myrmi.server.UnicastRemoteObject");
        Skeleton skeleton = new Skeleton(obj, registryRef);
        skeleton.start();

        return Util.createStub(registryRef);
    }
}
