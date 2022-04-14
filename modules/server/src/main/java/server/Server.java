package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class is encapsulating server.
 * server.Server is responsible for matching two users with each other.
 * <P>
 * Matching means that server inputs objects from two users and then outputs user1 object to user2, and contrary.
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
     * </P>
     */
    public void matchTwoUsersAndSwapObjects() throws IOException, ClassNotFoundException {
        // accepting two users
        Socket user1 = serverSocket.accept();
        Socket user2 = serverSocket.accept();

        try(ObjectOutputStream ous1 = new ObjectOutputStream(user1.getOutputStream());
            ObjectOutputStream ous2 = new ObjectOutputStream(user2.getOutputStream());
            ObjectInputStream ois1 = new ObjectInputStream(user1.getInputStream());
            ObjectInputStream ois2 = new ObjectInputStream(user2.getInputStream());){

            // get Objects from users
            Object user1Obj = ois1.readObject();
            Object user2Obj = ois2.readObject();;

            // output objects to users
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
}
