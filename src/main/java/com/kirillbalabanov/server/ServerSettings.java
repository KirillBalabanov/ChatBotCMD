package com.kirillbalabanov.server;

import java.io.*;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.ServerSocket;

/**
 * Class is encapsulating server settings such as ip and port.
 * <table>
 *     <caption>Static Methods</caption>
 *     <thead>
 *         <tr>
 *             <th>Method</th>
 *             <th>Description</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <th>importSettings</th>
 *             <td>creates {@link com.kirillbalabanov.server.ServerSettings} from existing file data.</td>
 *         </tr>
 *         <tr>
 *             <th>exportSettings</th>
 *             <td>export current {@link com.kirillbalabanov.server.ServerSettings} class data to file</td>
 *         </tr>
 *         <tr>
 *             <th>localSettings</th>
 *             <td>get {@link com.kirillbalabanov.server.ServerSettings} encapsulating local settings.</td>
 *         </tr>
 *     </tbody>
 * </table>
 */
public class ServerSettings {
    private final InetAddress ip;
    private final int port;

    /**
     * Construct class encapsulating free port and ip of current machine.
     * Basically uses for <b>server</b> establish.
     * <p>
     * Creates from <b>Local Settings.</b>
     * </p>
     * @throws IOException
     */
    private ServerSettings() throws IOException{
        this.ip = InetAddress.getLocalHost();
        this.port = getFreePort();
    }

    /**
     * Constructor uses for creating an object of existing params.
     * Basically uses by <b>clients</b> to get server settings.
     * @param ip server ip.
     * @param port server port.
     */
    private ServerSettings(InetAddress ip, int port) throws PortUnreachableException {
        if(ip == null) throw new NullPointerException("IP cannot be null");
        if(port == -1) throw new PortUnreachableException("Port cannot be -1");
        this.ip = ip;
        this.port = port;
    }

    /**
     * Method is exporting settings of {@link com.kirillbalabanov.server.ServerSettings} to file.
     * Every setting is represented in new line as String.
     * @param serverSettings {@link com.kirillbalabanov.server.ServerSettings} class, encapsulating ip and port.
     * @param path path to file.
     * @param fileName file name.
     */
    public static void exportSettings(ServerSettings serverSettings, String path, String fileName) throws IOException{
        try(FileWriter fileWriter = new FileWriter(path + "\\" + fileName)) {
            fileWriter.write(serverSettings.ip.toString() + "\n");
            fileWriter.write(serverSettings.port);
        }
    }

    /**
     * Method is exporting settings of {@link com.kirillbalabanov.server.ServerSettings} to file <b>settings.txt</b>
     * with given path.
     * Every setting is represented in new line as String.
     * @param serverSettings {@link com.kirillbalabanov.server.ServerSettings} class, encapsulating ip and port.
     * @param path path to file.
     * @throws IOException
     */
    public static void exportSettings(ServerSettings serverSettings, String path) throws IOException {
        exportSettings(serverSettings, path, "settings.txt");
    }


    /**
     * Method is used for importing data from file and creating {@link com.kirillbalabanov.server.ServerSettings}
     * encapsulating these settings.
     * @param path path to file
     */
    public static ServerSettings importSettings(String path) throws IOException {
        int port = -1;
        InetAddress ip = null;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path ))) {
            String ipStr = bufferedReader.readLine();
            port = Integer.parseInt(bufferedReader.readLine());
            ip = InetAddress.getByAddress(ipStr.getBytes());
        }
        return new ServerSettings(ip, port);
    }

    /**
     * Static method used to get {@link com.kirillbalabanov.server.ServerSettings} encapsulating local settings.
     */
    public static ServerSettings localSettings() throws IOException {
        return new ServerSettings();
    }

    /**
     * Method defines free port and returns it.
     * @return free port
     */
    private int getFreePort() throws IOException {
        try(ServerSocket ss = new ServerSocket(0)) {
            return ss.getLocalPort();
        }
    }

    public int getPort() {return this.port;}
    public InetAddress getIp() {return this.ip;}
}
