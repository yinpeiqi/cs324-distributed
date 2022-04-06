package myrmi.test;

import myrmi.exception.RemoteException;
import myrmi.server.UnicastRemoteObject;

public class Foo extends UnicastRemoteObject implements IFoo{
    private String message;
    private static final long serialVersionUID = -5655647669552931408L;

    protected Foo(String message) throws RemoteException {
        this.message = message;
    }

    public String getMessage() throws RemoteException {
        return this.message;
    }
}
