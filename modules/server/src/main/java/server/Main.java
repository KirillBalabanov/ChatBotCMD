package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");
            Server server = Server.startAndExportSettings(50);
            System.out.println("Server created successfully.\n" +
                    "Server settings exported to settings/settings.txt");
            System.out.println("Print 'stop' to stop the server properly.");
            System.out.println("Print 'terminate' to terminate the server immediately.");
            new Thread(() -> {
                try{
                    try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                        while(true) {
                            try{
                                switch (br.readLine()) {
                                    case "stop" -> {
                                        System.out.println("Waiting for server to close.");
                                        server.waitClose();
                                        return;
                                    }
                                    case "terminate" -> {
                                        System.out.println("Terminating server...");
                                        server.terminate();
                                        return;
                                    }
                                    default -> System.out.println("Invalid option.");
                                }
                            } catch (Exception e) {e.printStackTrace();}
                        }
                    }
                } catch (IOException ignore) {}
            }).start();

            // loop match
            while(true) {
                try {
                    server.matchTwoUsersAndSwapObjects();
                    if(server.isClosed()) {
                        System.out.println("Server closed successfully.");
                        System.exit(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Server terminated.");
                    System.exit(1);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
