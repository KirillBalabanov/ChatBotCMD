package server;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {

            System.out.println("Starting server...");
            Server server = Server.createExportAndStart(50);
            System.out.println("Server created successfully.\n" +
                    "Server settings exported to settings/settings.txt");

            // loop match
            while(true) {
                try {
                    server.match();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch(IOException e) {
            System.out.println(e.toString());
        }


    }
}
