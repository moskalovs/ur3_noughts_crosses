package com.ur.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionPoint {

    private final Socket SOCKET;
    private final DataInputStream IN;
    private final DataOutputStream OUT;

    ConnectionPoint(Socket socket) throws IOException {
        this.IN = new DataInputStream(socket.getInputStream());
        this.OUT = new DataOutputStream(socket.getOutputStream());
        this.SOCKET = socket;
    }

    public byte[] waitData(int countOfBytes) throws IOException {
        byte[] receivedData = new byte[countOfBytes];
        IN.readFully(receivedData, 0, countOfBytes);
        return receivedData;
    }

    public void send(byte[] data) throws IOException {
        OUT.write(data);
        OUT.flush();
    }

    public void sendWithCheck(byte[] data) throws IOException {
        OUT.write(data);
        OUT.flush();
    }

    public void close() throws IOException {
        IN.close();
        OUT.close();
        SOCKET.close();
    }

}
