package com.kirillbalabanov.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    private ServerSettings() throws IOException{
        this.ip = InetAddress.getLocalHost();
        this.port = getFreePort();
    }

    private ServerSettings(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Method is exporting settings of {@link com.kirillbalabanov.server.ServerSettings} to file.
     * @param serverSettings {@link com.kirillbalabanov.server.ServerSettings} class, encapsulating ip and port.
     * @param path path to file.
     * @param fileName file name.
     * @throws IOException
     */
    public static void exportSettings(ServerSettings serverSettings, String path, String fileName) throws IOException{
        try(FileWriter fileWriter = new FileWriter(path + "\\" + fileName)) {
            fileWriter.write(serverSettings.ip.toString() + "\n");
            fileWriter.write(serverSettings.port);
        }
    }

    /**
     * Method is exporting settings of {@link com.kirillbalabanov.server.ServerSettings} to file <b>settings</b>
     * with given path.
     * @param serverSettings {@link com.kirillbalabanov.server.ServerSettings} class, encapsulating ip and port.
     * @param path path to file.
     * @throws IOException
     */
    public static void exportSettings(ServerSettings serverSettings, String path) throws IOException {
        exportSettings(serverSettings, path, "settings");
    }


    public static ServerSettings importSettings(String path, String fileName) throws IOException {
        try(FileReader fileReader = new FileReader(path + "\\" + fileName)) {
            ServerSettings serverSettings = new ServerSettings();
        }
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
