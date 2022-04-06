package myrmi.registry;

import myrmi.Remote;
import myrmi.exception.AlreadyBoundException;
import myrmi.exception.NotBoundException;
import myrmi.exception.RemoteException;
import myrmi.server.Skeleton;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

public class RegistryImpl implements Registry {
    private final HashMap<String, Remote> bindings = new HashMap<>();

    /**
     * Construct a new RegistryImpl
     * and create a skeleton on given port
     **/
    public RegistryImpl(int port) throws RemoteException {
        Skeleton skeleton = new Skeleton(this, "127.0.0.1", port, 0);
        skeleton.start();
    }


    public Remote lookup(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: lookup(%s)\n", name);
        assert (this.bindings.containsKey(name));
        return this.bindings.get(name);
    }

    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException {
        System.out.printf("RegistryImpl: bind(%s)\n", name);
        assert (!this.bindings.containsKey(name));
        this.bindings.put(name, obj);
    }

    public void unbind(String name) throws RemoteException, NotBoundException {
        System.out.printf("RegistryImpl: unbind(%s)\n", name);
        this.bindings.remove(name);
    }

    public void rebind(String name, Remote obj) throws RemoteException {
        System.out.printf("RegistryImpl: rebind(%s)\n", name);
        this.bindings.put(name, obj);
    }

    public String[] list() throws RemoteException {
        return (String[]) this.bindings.keySet().toArray();
    }
}
