package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private static final int port = 50000;
    private static final int threads = 5;

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("ny sensor connected " + clientSocket.getInetAddress());
                pool.execute(new SensorHandler(clientSocket));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}