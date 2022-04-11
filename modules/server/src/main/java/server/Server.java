package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class is encapsulating server.
 * server.Server is responsible for matching two users with each other.
 * <P>
 * Matching means that server outputs first user {@link java.net.DatagramSocket} to second user, and contrary.
 * </P>
 */
public class Server {
    private final ServerSocket serverSocket;
    private final ServerSettings serverSettings;

    /**
     * Construct server using {@link ServerSettings} data.
     * @param backlog defines how many users can be waiting for connection in queue.
     */
    public Server(int backlog) throws IOException {
        // get local settings.
        serverSettings = ServerSettings.localSettings();
        serverSocket = new ServerSocket(serverSettings.getPort(), backlog, serverSettings.getIp());
    }

    /**
     * Method is responsible for matching users.
     * <P>
     *     Firstly server accepts two users, then collects their data such as free port and ip.
     *     Then outputs user1 {@link server.ClientInfo} to user2, and contrary.
     * </P>
     * <P>
     *     On the client side opens a connection between users.
     * </P>
     */
    public void match() throws IOException {
        // accepting two users
        Socket user1 = serverSocket.accept();
        Socket user2 = serverSocket.accept();

        // collecting their data
        ClientInfo clientInfo1 = new ClientInfo(user1.getInetAddress(), user1.getPort());
        ClientInfo clientInfo2 = new ClientInfo(user2.getInetAddress(), user2.getPort());

        try(ObjectOutputStream ous1 = new ObjectOutputStream(user1.getOutputStream());
            ObjectOutputStream ous2 = new ObjectOutputStream(user2.getOutputStream())){

            // output InetAddress and Integer through ObjectOutputStream.
            ous1.writeObject(clientInfo2);
            ous2.writeObject(clientInfo1);
        }
    }

    public static Server startAndExportSettings(int backlog) throws IOException {
        Server server = new Server(backlog);
        server.exportSettings();
        return server;
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    /**
     * Export server settings to settings\settings.txt
     */
    public void exportSettings() throws IOException {
        ServerSettings.exportSettings(serverSettings, "settings/settings.txt");
    }
}
