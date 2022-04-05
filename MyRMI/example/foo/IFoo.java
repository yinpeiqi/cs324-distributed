package example.foo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFoo extends Remote {
    public String getMessage() throws RemoteException;
}
