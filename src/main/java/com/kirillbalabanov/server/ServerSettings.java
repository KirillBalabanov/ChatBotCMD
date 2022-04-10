package com.kirillbalabanov.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Class is encapsulating ip and free port of current machine.
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

    public InetAddress getIp() {return ip;}
    public int getPort() {return port;}

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
