package server;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientInfo implements Serializable {
    private final int port;
    private final InetAddress ip;
    private final String username;
    private boolean isHost;

    public ClientInfo(InetAddress ip, int port, String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.isHost = false;
    }

    public void setHost() { this.isHost = true; }
    public boolean isHost() { return isHost; }
    public String getUserName() { return this.username; }
    public InetAddress getIp() { return this.ip; }
    public int getPort() { return this.port; }
}
