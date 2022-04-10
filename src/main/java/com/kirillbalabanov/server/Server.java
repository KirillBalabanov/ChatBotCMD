package com.kirillbalabanov.server;

import java.io.IOException;
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
        ServerSettings serverSettings = new ServerSettings();
        ServerSocket serverSocket = new ServerSocket(serverSettings.getPort(), backlog, serverSettings.getIp());
    }

    /**
     * Method is responsible for matching users.
     * <P>
     *     Firstly server accepts two users, then collects their data such as free port and ip.
     *     Then creates two {@link java.net.DatagramSocket} with collected data and output it to the user.
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
        InetAddress user1Ip = user1.getInetAddress();
        InetAddress user2IP = user2.getInetAddress();

    }
}
