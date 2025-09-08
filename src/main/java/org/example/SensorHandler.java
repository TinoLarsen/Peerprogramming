package org.example;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class SensorHandler implements Runnable {

    private Socket client;

    public SensorHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                BufferedWriter log = new BufferedWriter(new FileWriter("mars.log", true))
                ){
            String line;
            while ((line = in.readLine())!= null){
                System.out.println("Modtaget: " + line);
                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                String type = parts[0];
                double value = Double.parseDouble(parts[1]);

                boolean alarm = checkThresholds(type,value);

                String logLine = "[" + LocalDateTime.now() + "] " + type + " " + value;
                if (alarm) logLine += "-> ALARM!";
                log.write(logLine);
                log.newLine();
                log.flush();

                if (alarm){
                    out.println("ALARM" + type + " out of range: (" + value + ")" );
                } else {
                    out.println("OK");
                }
            }
        }catch (IOException | NumberFormatException e){
            e.printStackTrace();
        }
    }

    private boolean checkThresholds (String type, double value) {
        return switch (type) {
            case "TEMP" -> value < -20 || value > 50;
            case "O2" -> value < 0 || value > 5;
            case "PRESSURE" -> value < 700 || value > 800;
            case "CO2" -> value < 0 || value > 1;
            default -> false;
        };
    }
}
