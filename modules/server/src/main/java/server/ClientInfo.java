package server;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientInfo implements Serializable {
    public final InetAddress ip;
    public final int port;

    public ClientInfo(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
