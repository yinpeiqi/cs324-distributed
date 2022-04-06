package myrmi.server;

import myrmi.Remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Skeleton extends Thread {
    static final int BACKLOG = 5;
    private Remote remoteObj;

    private String host;
    private int port;
    private int objectKey;

    public int getPort() {
        return port;
    }

    public Skeleton(Remote remoteObj, RemoteObjectRef ref) {
        this(remoteObj, ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    public Skeleton(Remote remoteObj, String host, int port, int objectKey) {
        super();
        this.remoteObj = remoteObj;
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        this.setDaemon(false);
    }

    @Override
    public void run() {
        try {
            // TODO: how to listen to different host?
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server is listening on port " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client " + socket.getInetAddress() + ":" + socket.getLocalPort() + " has connected to the server.");

                SkeletonReqHandler reqHandler = new SkeletonReqHandler(socket, this.remoteObj, this.objectKey);
                reqHandler.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
