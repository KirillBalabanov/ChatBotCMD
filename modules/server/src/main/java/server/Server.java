package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Class is encapsulating server.
 * server.Server is responsible for matching two users with each other.
 * <P>
 * Matching means that server inputs {@link server.ClientInfo} from two users and then outputs user1 object to user2, and contrary.
 * </P>
 */
public class Server {
    private final ServerSocket serverSocket;
    private final ServerSettings serverSettings;
    private boolean isClosed;
    private final Semaphore semaphore;
    /**
     * Construct server using {@link ServerSettings} data.
     * @param backlog defines how many users can be waiting for connection in queue.
     */
    public Server(int backlog) throws IOException {
        semaphore = new Semaphore(1);
        // get local settings.
        serverSettings = ServerSettings.localSettings();
        serverSocket = new ServerSocket(serverSettings.getPort(), backlog, serverSettings.getIp());
    }

    /**
     * Method is responsible for matching users.
     * <P>
     *     Firstly server accepts two users, then inputs an {@link server.ClientInfo} instance of each user.
     *     User1 object outputs to user2, anc contrary.
     * </P>
     * If no users are in waiting queue, and method waitClose was invoked - <b>method returns immediately.</b>
     */
    public void matchTwoUsersAndSwapObjects() throws IOException, ClassNotFoundException, InterruptedException {
        // accepting two users
        Socket user1;
        try{
            user1 = serverSocket.accept();
        } catch (IOException ignore) {
            // if no users are waiting for match, and server is closed - return from this method.
            return;
        }
        // get block if first user is in waiting queue.
        semaphore.acquire();
        Socket user2 = serverSocket.accept();

        try(ObjectOutputStream ous1 = new ObjectOutputStream(user1.getOutputStream());
            ObjectOutputStream ous2 = new ObjectOutputStream(user2.getOutputStream());
            ObjectInputStream ois1 = new ObjectInputStream(user1.getInputStream());
            ObjectInputStream ois2 = new ObjectInputStream(user2.getInputStream())){

            // get Objects from users
            ClientInfo user1Obj = (ClientInfo) ois1.readObject();
            ClientInfo user2Obj = (ClientInfo) ois2.readObject();

            // set user1 - host.
            user1Obj.setHost();

            // output objects to users
            ous1.writeObject(user2Obj);
            ous2.writeObject(user1Obj);

        }
        semaphore.release();
    }

    public static Server startAndExportSettings(int backlog) throws IOException {
        Server server = new Server(backlog);
        server.exportSettings();
        return server;
    }

    /**
     * Terminates the server forcefully.
     */
    public void terminate() throws IOException {
        close();
    }

    /**
     * If any user is waiting for server reply, waitClose would wait up until the reply would be sent,
     * and then server closes.
     * <p>In other case if no users are waiting for server reply, server would close immediately.</p>
     */
    public void waitClose() throws IOException, InterruptedException {
        // waiting for block
        semaphore.acquire();
        close();
        semaphore.release();
    }

    private void close() throws IOException {
        isClosed = true;
        serverSocket.close();
    }

    /**
     * Export server settings to default path settings/settings.txt.
     */
    public void exportSettings() throws IOException {
        ServerSettings.exportSettings(this.serverSettings, "settings/settings.txt");
    }

    /**
     * Export server settings to given path.
     */
    public void exportSettings(String path) throws IOException {
        ServerSettings.exportSettings(this.serverSettings, path);
    }

    public boolean isClosed() { return this.isClosed; }
}
