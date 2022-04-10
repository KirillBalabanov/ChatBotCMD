package com.kirillbalabanov.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.kirillbalabanov.server.ServerSettings;

/**
 * Class is implementing client of {@link com.kirillbalabanov.server.Server ChatBotServer}
 *
 */
public class Client {
    private DatagramSocket datagramSocket;
    private String userName;
    private int port;
    private InetAddress userIP;
    private Socket serverSocket;

    Client(String userName) {
        this.userName = userName;
        serverSocket = new Socket(Serv)
    }

    /**
     * Method
     */
    public void findUser() {

    }

}
