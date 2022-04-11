package server;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientInfo implements Serializable {
    public final InetAddress ip;
    public final int port;
    public final String name;
    public boolean isHost;

    public ClientInfo(InetAddress ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.isHost = false;
    }

    public void setHost() { isHost = true; }
}
