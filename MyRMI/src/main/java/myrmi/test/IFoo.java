package myrmi.test;

import myrmi.Remote;
import myrmi.exception.RemoteException;

public interface IFoo extends Remote {
    public String getMessage() throws RemoteException;
}
