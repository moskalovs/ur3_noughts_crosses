package com.ur.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionCatcher {

    private final ServerSocket SERVER_SOCKET;
    private final int PORT;

    public ConnectionCatcher(int port) throws IOException {
        SERVER_SOCKET = new ServerSocket(port);
        this.PORT = port;
    }

    public ConnectionPoint waitConnection() throws IOException {
        Socket socket = SERVER_SOCKET.accept();
        return new ConnectionPoint(socket);
    }

    public void close() throws IOException {
        SERVER_SOCKET.close();
    }

}