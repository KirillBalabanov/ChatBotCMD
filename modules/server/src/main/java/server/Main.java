package server;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {

            System.out.println("Starting server...");
            Server server = Server.startAndExportSettings(50);
            System.out.println("Server created successfully.\n" +
                    "Server settings exported to settings/settings.txt");

            // loop match
            while(true) {
                try {
                    server.matchTwoUsersAndSwapObjects();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

        } catch(Exception e) {
            System.out.println(e.toString());
        }


    }
}
