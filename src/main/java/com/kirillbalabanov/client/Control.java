package com.kirillbalabanov.client;

import com.kirillbalabanov.client.exceptions.InvalidNameException;
import com.kirillbalabanov.server.ServerSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import com.kirillbalabanov.client.Properties;

/**
 * Class is responsible for controlling program execution.
 */
public class Control {
    static boolean talking = false;
    /**
     * Method is responsible for connecting to server and get matched user's {@link java.net.DatagramSocket}.
     */
    public static DatagramSocket findUser(Client client) throws IOException {
        ServerSettings serverSettings = ServerSettings.importSettings("\\\\settings\\settings.txt");
        // connect to server
        Socket serverSocket = new Socket(serverSettings.getIp(), serverSettings.getPort());

        // setting port that would be freed to client
        client.setCurrentPort(serverSocket.getLocalPort());

        DatagramSocket matchedSocket = null;

        // get matched DatagramSocket
        try(ObjectInputStream objectInputStream = new ObjectInputStream(serverSocket.getInputStream())) {
            matchedSocket = (DatagramSocket) objectInputStream.readObject();
        } catch (ClassNotFoundException ignore) {

        }
        return matchedSocket;
    }

    public static void talk(Client client, DatagramSocket talkerSocket) {

        talking = true;

        DatagramSocket usersSocket = null;

        // receive buffer
        int bufferSize = Properties.datagramPacketSize;
        byte[] buffer = new byte[bufferSize];
        DatagramPacket receivePackage = new DatagramPacket(buffer, bufferSize);

        try{
            // send user's name
            usersSocket = new DatagramSocket(client.getCurrentPort());
            sendPackage(usersSocket, talkerSocket, client.getUserName().getBytes());

            // receive talker's name
            talkerSocket.receive(receivePackage);

        } catch (IOException e) {
            System.out.println(e.toString());
        }

        String talkerName = new String(receivePackage.getData(), 0, receivePackage.getLength());


        // Start output stream for receiving Datagram Packages and printing them to Console.
        new Thread("Talker Thread") {
            public void run() {
                try {
                    while(talking) {
                        talkerSocket.receive(receivePackage);
                        String receivedString = new String(receivePackage.getData(), 0, receivePackage.getLength());

                        if(receivedString.equals(Properties.endTalkStr)) {
                            talking = false;
                            Properties.printConversationEnd(talkerName);
                            // enter to end input stream which is waiting for line.
                            System.out.println("Press enter to continue.");
                            return;
                        }

                        System.out.printf("--- %s: %s", talkerName, receivedString);
                    }
                } catch (IOException e) {
                    System.out.println(e.toString());
                    return;
                }

            }
        }.start();


        // Read from console and send to talker.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(talking) {
            try{
                String line = br.readLine();

                if(!talking) break;

                sendPackage(usersSocket, talkerSocket, line.getBytes());

                if(line.equals(Properties.endTalkStr)) {
                    Properties.printConversationEnd(talkerName);
                    talking = false;
                    break;
                }

            } catch(IOException e){
                System.out.println(e.toString());
            }

        }

    }

    private static void sendPackage(DatagramSocket usersSocket, DatagramSocket talkerSocket, byte[] bytes) throws IOException {
        byte[] buffer = new byte[Properties.datagramPacketSize];
        int counter = 0;
        for(int i = 0; i < bytes.length; i++) {
            if(counter == Properties.datagramPacketSize){
                usersSocket.send(new DatagramPacket(buffer, counter, talkerSocket.getInetAddress(), talkerSocket.getPort()));
                counter = 0;
            }
            buffer[i] = bytes[i];
            counter++;
        }
        usersSocket.send(new DatagramPacket(buffer, counter, talkerSocket.getInetAddress(), talkerSocket.getPort()));
    }
}