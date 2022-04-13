package client;

import server.ServerSettings;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class is responsible for controlling program execution.
 */
public class Control {
    private final Client client;

    Control(Client client) {
        this.client = client;
    }

    /**
     * Method is responsible for connecting to server, sending {@link client.Client},
     * and starting a talk with matched user.
     */
    public void findAndTalk(BufferedReader br) {
        try{
            // creating dynamicLoader Thread and running it.
            ScheduledExecutorService scheduledExecutorService = Control.dynamicLoaderScheduledExecutor("Finding user ", ". . .");

            Client matchedClient = find();
            // stop executor.
            scheduledExecutorService.shutdownNow();
            System.out.println("\nMatched with " + matchedClient.getUserName());

            // if client is host then create ServerSocket, otherwise connect to host.
            scheduledExecutorService = Control.dynamicLoaderScheduledExecutor("Establishing a connection", ". . .");

            Socket workingSocket;
            ServerSocket serverSocket = null;

            boolean isHost = client.isHost(matchedClient);

            if(isHost) {
                serverSocket = new ServerSocket(client.getCurrentPort());
                workingSocket = serverSocket.accept();
            }
            else{
                workingSocket = new Socket(matchedClient.getIp(), matchedClient.getCurrentPort());
            }

            scheduledExecutorService.shutdownNow();
            System.out.println("\nSuccessfully connected!");
            System.out.println("Print " + Properties.endTalkStr + " to stop the conversation.");

            talk(matchedClient, workingSocket, br);

            System.out.println("Conversation with " + matchedClient.getUserName() + " ended");

            // close server socket if host
            if(isHost) serverSocket.close();

        } catch (Exception e) {e.printStackTrace(); }
    }

    private Client find() throws IOException, ClassNotFoundException {

        // get server settings from settings/settings.txt.
        ServerSettings serverSettings = ServerSettings.importSettings(Properties.defaultPathToSettings);

        // connect to server
        Socket serverSocket = new Socket(serverSettings.getIp(), serverSettings.getPort());

        // setting port that would be freed to client
        client.setCurrentPort(serverSocket.getLocalPort());

        Client matchedClientInfo = null;

        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream = null;
        try{
            objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            objectOutputStream.writeObject(this.client);

            objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
            matchedClientInfo = (Client) objectInputStream.readObject();
        }
        finally {
            if(objectOutputStream != null) objectOutputStream.close();
        }
        return matchedClientInfo;
    }

    private void talk(Client matchedClientInfo, Socket workingSocket, BufferedReader br) {

        try(BufferedWriter writeToTalker = new BufferedWriter(new OutputStreamWriter(workingSocket.getOutputStream()))) {

            // Creat Thread for matched user output.
            new Thread(() -> {
                try (BufferedReader readFromTalker = new BufferedReader(new InputStreamReader(workingSocket.getInputStream()))){
                    while(true) {
                        String str = readFromTalker.readLine();
                        if (str == null || str.equals(Properties.endTalkStr)) {
                            System.out.println("User " + matchedClientInfo.getUserName() + " has left.");
                            System.out.println("Press enter to continue.");
                            break;
                        }
                        System.out.printf("--- %s: %s %n", matchedClientInfo.getUserName(), str);
                    }
                } catch (IOException ignore) { }
            }).start();

            // Input from console and write to user.
            while(true) {
                String str = br.readLine();

                writeToTalker.write(str);
                writeToTalker.newLine();
                writeToTalker.flush();

                if(str.equals(Properties.endTalkStr)) {
                    break;
                }
                System.out.printf("--- %s (You): %s %n", client.getUserName(), str);
            }
        } catch (IOException ignore) { }
    }

    /**
     * Method is creating Thread that would be printing loadingSymbols after String each 0.5 sec in cycle.
     * To stop the thread call stopThread.
     * @param str - string that would not be erased
     * @param loadingSymbols - symbols that would be appearing every 0.5 seconds in cycle.
     * @return Thread anonymous class.
     */
    public static ScheduledExecutorService dynamicLoaderScheduledExecutor(String str, String loadingSymbols) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            private int counter = 0;

            public void run() {
                int lastIndex = loadingSymbols.length() - 1;
                System.out.print("\r" + str + loadingSymbols.substring(0, counter));
                counter = counter >= lastIndex ? 0 : counter + 1;
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 500, TimeUnit.MILLISECONDS);
        return scheduledExecutorService;
    }
}

