package org.example;

import java.net.Socket;

public class ServerCLientHandler extends Thread {

    private final Socket socket;

    public ServerCLientHandler(Socket socket) {
        super ("sensor" + socket.getPort());
        this.socket = socket;
    }






}
