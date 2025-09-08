package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Random;

public class SensorClient {
    private static String hostname = "localhost";
    private static int port = 50000;

    public static void main(String[] args) throws IOException {
        String sensorType = "TEMP";
        Random random = new Random();

        try (Socket socket = new Socket(hostname, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                double value = switch (sensorType) {
                    case "TEMP" -> -30 + random.nextDouble() * 80; // -30 to 50
                    case "O2" -> random.nextDouble() * 6; // 0 to 6
                    case "PRESSURE" -> 650 + random.nextDouble() * 200; // 650 to 850
                    case "CO2" -> random.nextDouble() * 2; // 0 to 2
                    default -> 0;
                };

                String message = sensorType + ":" + String.format(Locale.US, "%.2f", value);
                writer.println(message); // send to server
                System.out.println("Sent: " + message); // log to console

                String response = reader.readLine();
                System.out.println("Received: " + response);

                try {
                    Thread.sleep(5000); // wait for 5 seconds before sending the next reading
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
