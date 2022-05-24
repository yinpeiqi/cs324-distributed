package myrmi.server;

import myrmi.Remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SkeletonReqHandler extends Thread {
    private Socket socket;
    private Remote obj;
    private int objectKey;

    public SkeletonReqHandler(Socket socket, Remote remoteObj, int objectKey) {
        this.socket = socket;
        this.obj = remoteObj;
        this.objectKey = objectKey;
    }

    @Override
    public void run() {
        int objectKey;
        String methodName;
        Class<?>[] argTypes;
        Object[] args;
        Object result = null;
            
        try {
            InputStream inFromClient =  this.socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inFromClient);

            methodName = (String) objectInputStream.readObject();
            objectKey = (int) objectInputStream.readObject();
            int numArgs = (int) objectInputStream.readObject();
            args = new Object[numArgs];
            argTypes = new Class<?>[numArgs];
            for (int i = 0; i < numArgs; i++) {
                argTypes[i] = (Class<?>) objectInputStream.readObject();
                args[i] = objectInputStream.readObject();
                
            }

            assert (this.objectKey == objectKey);
            try {
                // TODO: different types of method
                Class<?> clazz = this.obj.getClass();
                Method method = clazz.getMethod(methodName, argTypes);
                result = method.invoke(this.obj, args);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

            OutputStream outToClient = this.socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outToClient);

            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

            objectInputStream.close();
            objectOutputStream.close();
            this.socket.close();
        }
        catch (IOException | ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        /*TODO: implement method here
         * You need to:
         * 1. handle requests from stub, receive invocation arguments, deserialization
         * 2. get result by calling the real object, and handle different cases (non-void method, void method, method throws exception, exception in invocation process)
         * Hint: you can use an int to represent the cases: -1 invocation error, 0 exception thrown, 1 void method, 2 non-void method
         *
         *  */
    }
}
