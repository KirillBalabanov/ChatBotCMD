package com.kirillbalabanov.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class is encapsulating server.
 * Server is responsible for matching two users with each other.
 * <P>
 * Matching means that server outputs first user {@link java.net.DatagramSocket} to second user, and contrary.
 * </P>
 */
public class Server {
    ServerSocket serverSocket;
    ServerSettings serverSettings;

    /**
     * Construct server using {@link com.kirillbalabanov.server.ServerSettings} data.
     * @param backlog defines how many users can be waiting for connection in queue.
     * @throws IOException
     */
    Server(int backlog) throws IOException {
        // get local settings.
        serverSettings = ServerSettings.localSettings();
        serverSocket = new ServerSocket(serverSettings.getPort(), backlog, serverSettings.getIp());
    }

    /**
     * Method is responsible for matching users.
     * <P>
     *     Firstly server accepts two users, then collects their data such as free port and ip.
     *     Then creates two {@link java.net.DatagramSocket} with collected data and output it to the user
     *     through {@link java.io.ObjectOutputStream}.
     * </P>
     * <P>
     *     On the client side opens a connection between user and {@link java.net.DatagramSocket}.
     * </P>
     * @throws IOException
     */
    public void match() throws IOException {
        // accepting two users
        Socket user1 = serverSocket.accept();
        Socket user2 = serverSocket.accept();

        // collecting their data
        InetAddress user1IP = user1.getInetAddress();
        InetAddress user2IP = user2.getInetAddress();

        int user1Port = user1.getPort();
        int user2Port = user2.getPort();

        // creating DatagramSockets
        DatagramSocket user1DS = new DatagramSocket(user1Port, user1IP);
        DatagramSocket user2DS = new DatagramSocket(user2Port, user2IP);

        try(ObjectOutputStream ous1 = new ObjectOutputStream(user1.getOutputStream());
            ObjectOutputStream ous2 = new ObjectOutputStream(user2.getOutputStream())){

            // output DatagramSocket through ObjectOutputStream.
            ous1.writeObject(user2DS);
            ous2.writeObject(user1DS);
        }

    }

    /**
     * Export Server settings to settings\settings.txt.
     */
    public void exportSettings(String path) throws IOException {
        ServerSettings.exportSettings(serverSettings, "settings");
    }
}
