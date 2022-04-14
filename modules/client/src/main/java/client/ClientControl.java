package client;

import server.ServerSettings;

import java.io.*;
import java.net.*;

/**
 * Class is responsible for controlling program execution.
 */
public class ClientControl {
    private final Client client;
    private int currentPort;

    ClientControl(Client client) {
        this.client = client;
        currentPort = -1;
    }

    /**
     * Method is responsible for connecting to server, sending client info and starting a talk with matched user.
     *
     *
     */
    public void findAndTalk(BufferedReader br) {
        try{
            // creating dynamicLoader Thread and running it.
            StoppedThread dynamicLoader = ClientControl.dynamicLoaderStream("Finding user ", ". . .");
            new Thread(dynamicLoader).start();

            ClientInfo matchedClient = find();
            // stop dynamicLoader Thread.
            dynamicLoader.stopThread();
            System.out.println("\nMatched with " + matchedClient.name);

            // if client is host then create ServerSocket, otherwise connect to host.
            dynamicLoader = ClientControl.dynamicLoaderStream("Establishing a connection", ". . .");
            new Thread(dynamicLoader).start();
            Socket workingSocket;
            ServerSocket serverSocket = null;
            if(matchedClient.isHost) {
                workingSocket = new Socket(matchedClient.ip, matchedClient.port);
            }
            else{
                serverSocket = new ServerSocket(currentPort);
                workingSocket = serverSocket.accept();
            }
            dynamicLoader.stopThread();
            System.out.println("\nSuccessfully connected!");

            talk(matchedClient, workingSocket, br);

            System.out.println("Conversation with " + matchedClient.name + " ended");

            // close server socket if host
            if(!matchedClient.isHost && serverSocket != null) serverSocket.close();

        } catch (Exception ignore) { }
    }

    private ClientInfo find() throws IOException, ClassNotFoundException {

        // get server settings from settings/settings.txt.
        ServerSettings serverSettings = ServerSettings.importSettings("settings/settings.txt");

        // connect to server
        Socket serverSocket = new Socket(serverSettings.getIp(), serverSettings.getPort());

        // setting port that would be freed to client
        currentPort = serverSocket.getLocalPort();

        ClientInfo matchedClientInfo = null;

        try (ObjectInputStream objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream())) {
            // send ClientInfo to server
            objectOutputStream.writeObject(new ClientInfo(InetAddress.getLocalHost(), currentPort, client.getUserName()));

            // get ClientInfo of matched user
            matchedClientInfo = (ClientInfo) objectInputStream.readObject();

        }
        return matchedClientInfo;
    }

    private void talk(ClientInfo matchedClientInfo, Socket workingSocket, BufferedReader br) {

        try(BufferedWriter writeToTalker = new BufferedWriter(new OutputStreamWriter(workingSocket.getOutputStream()));
            BufferedReader readFromTalker = new BufferedReader(new InputStreamReader(workingSocket.getInputStream()))) {

            // Creat Thread for matched user output
            new Thread() {
                public void run() {
                    try {
                        while(true) {
                            String str = readFromTalker.readLine();
                            if (str == null || str.equals(Properties.endTalkStr)) {
                                System.out.println("User " + matchedClientInfo.name + " has left.");
                                System.out.println("Press enter to continue.");
                                // close socket so that try to read/write to socket would throw IOException
                                workingSocket.close();
                                break;
                            }
                            System.out.printf("--- %s: %s %n", matchedClientInfo.name, str);
                        }
                    } catch (IOException ignore) { }
                }
            }.start();

            // Input from console and write to user.
            while(true) {
                String str = br.readLine();

                writeToTalker.write(str);
                writeToTalker.newLine();
                writeToTalker.flush();

                if(str.equals(Properties.endTalkStr)) {
                    // close socket so that try to read/write to socket would throw IOException
                    workingSocket.close();
                    break;
                }
                System.out.printf("--- %s (You): %s %n", client.getUserName(), str);
            }
        } catch (IOException e) {System.out.println(e.toString());}
    }

    /**
     * Method is creating Thread that would be printing loadingSymbols after String each 0.5 sec in cycle.
     * To stop the thread call stopThread.
     * @param str - string that would not be erased
     * @param loadingSymbols - symbols that would be appearing every 0.5 seconds in cycle.
     * @return Thread anonymous class.
     */
    public static StoppedThread dynamicLoaderStream(String str, String loadingSymbols) {
        return new StoppedThread() {
            private boolean stopped = false;

            public void stopThread() {stopped = true;}

            public boolean isAlive() {
                return !stopped;
            }

            public void run() {
                int lastIndex = loadingSymbols.length() - 1;
                int counter = 0;
                while(!stopped) {
                    System.out.print("\r" + str + loadingSymbols.substring(0, counter));
                    counter = counter >= lastIndex ? 0 : counter + 1;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) { }
                }
            }
        };

    }
}