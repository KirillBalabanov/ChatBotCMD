package client;


import client.exceptions.InvalidNameException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class is implementing client of {@link server.Server ChatBotServer}
 *
 */
public class Client {
    private String userName;
    private int currentPort;
    private final InetAddress ip;

    public Client(String userName) throws InvalidNameException, UnknownHostException {
        setUserName(userName);
        ip = InetAddress.getLocalHost();
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Method is responsible for setting username.
     * If username is not valid to {@link Properties} throws
     * {@link InvalidNameException}
     */
    public void setUserName(String userName) throws InvalidNameException {
        if(userName == null) throw new InvalidNameException("Null name.");
        if(userName.length() == 0 || userName.length() > Properties.userNameLen) {
            throw new InvalidNameException("Name out of length.");
        }
        if(Character.isUpperCase(userName.charAt(0)) != Properties.nameStartsWithUpperCase) {
            throw new InvalidNameException("Name starts with wrong case.");
        }
        int normalLettersCounter = 0;
        for(int i = 0; i < userName.length(); i++) {
            if(Character.isLetter(userName.charAt(i))) normalLettersCounter++;
        }
        if(normalLettersCounter < Properties.normalLetterCount) {
            throw new InvalidNameException("Name should contain " + Properties.normalLetterCount + " normal letters");
        }
        this.userName = userName;
    }

    public int getCurrentPort() { return currentPort; }

    public void setCurrentPort(int port) { this.currentPort = port; }

    public InetAddress getIp() { return this.ip; }

    /**
     * Method return true if current user should open a {@link java.net.ServerSocket} so that given user could connect
     * to him. Otherwise given client would open a server.
     */
    public boolean isHostToUser(Client client) {
        if(this.currentPort == client.getCurrentPort()) {
            return this.hashCode() - client.hashCode() < 0;
        }
        return this.getCurrentPort() - client.getCurrentPort() < 0;
    }

}
