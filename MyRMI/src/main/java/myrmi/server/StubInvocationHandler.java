package myrmi.server;

import myrmi.Remote;
import myrmi.exception.RemoteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class StubInvocationHandler implements InvocationHandler, Serializable {
    private String host;
    private int port;
    private int objectKey;

    public StubInvocationHandler(String host, int port, int objectKey) {
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        System.out.printf("Stub created to %s:%d, object key = %d\n", host, port, objectKey);
    }

    public StubInvocationHandler(RemoteObjectRef ref) {
        this(ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws RemoteException, IOException, ClassNotFoundException, Throwable {
        Object result = null;
        try {
            Socket client = new Socket(this.host, this.port);
            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outToServer);

            objectOutputStream.writeObject(method.getName());
            objectOutputStream.writeObject(objectKey);
            objectOutputStream.writeObject(args.length);
            for (int i = 0; i < args.length; i++) {
                Class<?> clazz = args[i].getClass();
                for (Class<?> j: clazz.getInterfaces()) {
                    if (Remote.class.isAssignableFrom(j)) {
                        clazz = Remote.class;
                    }
                }
                objectOutputStream.writeObject(clazz);
                objectOutputStream.writeObject(args[i]);
            }
            objectOutputStream.flush();

            InputStream inFromServer = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inFromServer);
            result = objectInputStream.readObject();

            objectOutputStream.close();
            objectInputStream.close();
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
