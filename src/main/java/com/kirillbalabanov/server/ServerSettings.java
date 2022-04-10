package com.kirillbalabanov.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Class is responsible for creating txt file with server IP and port.
 */
public class ServerSettings {
    private InetAddress ip;
    private int port;

    /**
     * Construct class encapsulating free port and ip of current machine
     * @throws IOException
     */
    public ServerSettings() throws IOException{
        this.ip = InetAddress.getLocalHost();
        this.port = getFreePort();
    }

    /**
     * Method is creating txt file containing IP and port of the server.
     * @param path path to file.
     * @param fileName name of file.
     */
    public void exportSettings(String path, String fileName) {


    }

    /**
     * Method is creating txt file containing IP and port of the server with default fileName = 'settings'.
     * @param path path to file
     */
    public void exportSettings(String path) {
        exportSettings(path, "settings");
    }

    /**
     * Method defines free port and returns it.
     * @return free port
     * @throws IOException
     */
    private int getFreePort() throws IOException {
        try(ServerSocket ss = new ServerSocket(0)) {
            return ss.getLocalPort();
        }
    }
}
