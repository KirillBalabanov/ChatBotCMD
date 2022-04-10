package client;

import server.ClientInfo;
import server.ServerSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.*;

/**
 * Class is responsible for controlling program execution.
 */
public class ClientControl {
    private boolean talking;
    private boolean searching;
    private final Client client;
    private ClientInfo matchedClientInfo;

    ClientControl(Client client) {
        this.client = client;
        talking = searching = false;
        matchedClientInfo = null;
    }

    /**
     * Method is responsible for connecting to server and get matched user's {@link java.net.DatagramSocket}.
     */
    public boolean findUser() {
        searching = true;
        try{
            ServerSettings serverSettings = ServerSettings.importSettings("settings/settings.txt");
            // connect to server
            Socket serverSocket = new Socket(serverSettings.getIp(), serverSettings.getPort());

            // setting port that would be freed to client
            client.setCurrentPort(serverSocket.getLocalPort());

            // get matched ClientInfo
            try(ObjectInputStream objectInputStream = new ObjectInputStream(serverSocket.getInputStream())) {
                matchedClientInfo = (ClientInfo) objectInputStream.readObject();
            }
        } catch (Exception ignore) {return false; }
        finally {
            searching = false;
        }
        return true;
    }

    public void talk() {

        talking = true;

        try(DatagramSocket usersSocket = new DatagramSocket(client.getCurrentPort())) {

            // receive buffer
            int bufferSize = Properties.datagramPacketSize;
            byte[] buffer = new byte[bufferSize];
            DatagramPacket receivePackage = new DatagramPacket(buffer, bufferSize);

            try{

                // send user's name
                sendPackage(usersSocket, matchedClientInfo, client.getUserName().getBytes());

                // receive talker's name
                usersSocket.receive(receivePackage);

            } catch (IOException e) {
                System.out.println(e.toString());
            }

            String talkerName = new String(receivePackage.getData(), 0, receivePackage.getLength());
            System.out.println("\nMatched with " + talkerName);

            // Start output stream for receiving Datagram Packages and printing them to Console.
            new Thread("Talker Thread") {
                public void run() {
                    try {
                        while(talking) {
                            DatagramPacket receivePackage = new DatagramPacket(buffer, bufferSize);
                            usersSocket.receive(receivePackage);
                            String receivedString = new String(receivePackage.getData(), 0, receivePackage.getLength());

                            if(receivedString.equals(Properties.endTalkStr)) {
                                talking = false;
                                Properties.printConversationEnd(talkerName);
                                // enter to end input stream which is waiting for line.
                                System.out.println("Press enter to continue.");
                                return;
                            }

                            System.out.print(receivedString);
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

                    sendPackage(usersSocket, matchedClientInfo, String.format("--- %s: %s%n", client.getUserName(), line).getBytes());

                    if(line.equals(Properties.endTalkStr)) {
                        Properties.printConversationEnd(talkerName);
                        talking = false;
                        break;
                    }
                    System.out.printf("--- %s (You): %s%n", client.getUserName(), line);

                } catch(IOException e){
                    System.out.println(e.toString());
                }

            }

        } catch (IOException e) {System.out.println(e.toString()); }
        finally {
            talking = false;
        }

    }

    private static void sendPackage(DatagramSocket usersSocket, ClientInfo clientInfo, byte[] bytes) throws IOException {
        byte[] buffer = new byte[Properties.datagramPacketSize];
        int counter = 0;
        for(int i = 0; i < bytes.length; i++) {
            buffer[counter++] = bytes[i];
            if(counter == Properties.datagramPacketSize - 1){
                usersSocket.send(new DatagramPacket(buffer, counter, clientInfo.ip, clientInfo.port));
                counter = 0;
            }
        }
        usersSocket.send(new DatagramPacket(buffer, counter, clientInfo.ip, clientInfo.port));
    }

    public void dynamicLoaderStream(String str, String loadingSymbols) {

        new Thread("Dynamic Loader") {

            public void run() {
                int lastIndex = loadingSymbols.length() - 1;
                int counter = 0;
                while(searching) {
                    System.out.print("\r" + str + loadingSymbols.substring(0, counter));
                    counter = counter >= lastIndex ? 0 : counter + 1;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) { }
                }
            }
        }.start();

    }
}