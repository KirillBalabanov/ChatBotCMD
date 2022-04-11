package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

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
     *     Firstly server accepts two users, then inputs an object of each user.
     *     User1 object outputs to user2, anc contrary.
     *     Semaphore outputs to every user to sync their connection after their close connection with server.
     * </P>
     * <P>
     *     On the client side opens a 'connection' between users through {@link java.net.DatagramSocket}
     * </P>
     */
    public void match() throws IOException, ClassNotFoundException {
        // accepting two users
        Socket user1 = serverSocket.accept();
        Socket user2 = serverSocket.accept();

        try(ObjectOutputStream ous1 = new ObjectOutputStream(user1.getOutputStream());
            ObjectOutputStream ous2 = new ObjectOutputStream(user2.getOutputStream());
            ObjectInputStream ois1 = new ObjectInputStream(user1.getInputStream());
            ObjectInputStream ois2 = new ObjectInputStream(user2.getInputStream());){

            // get Objects from users
            ClientInfo user1Obj = (ClientInfo) ois1.readObject();
            ClientInfo user2Obj = (ClientInfo) ois2.readObject();;

            // output objects to users, setting user1 - host.
            user1Obj.setHost();
            ous1.writeObject(user2Obj);
            ous2.writeObject(user1Obj);

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
