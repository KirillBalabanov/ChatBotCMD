package client;

import client.exceptions.InvalidNameException;
import server.ClientInfo;
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

    /**
     * Creates a client instance with given username.
     * @throws InvalidNameException if name doesn't match {@link Properties}
     */
    public Client(String userName) throws InvalidNameException, UnknownHostException {
        setUserName(userName);
        ip = InetAddress.getLocalHost();
    }

    /**
     * Creates client with userName = null. Username must be set later!
     */
    public Client() throws UnknownHostException {
        this.userName = null;
        this.ip = InetAddress.getLocalHost();
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
        Properties.Name.NameValidator nameValidator = new Properties.Name.NameValidator(userName);

        if(!nameValidator.notNull()) throw new InvalidNameException("Null name.");
        if(!nameValidator.inLen()) {
            throw new InvalidNameException("Name out of length.");
        }
        if(nameValidator.startsWithUpperCase() != Properties.Name.nameStartsWithUpperCase) {
            throw new InvalidNameException("Name starts with wrong case.");
        }
        if(!nameValidator.containsNormalLettersCount()) {
            throw new InvalidNameException("Name should contain " + Properties.Name.normalLetterCount + " normal letters");
        }
        if(!nameValidator.isValid()) throw new InvalidNameException("""
                Name could contain:
                Letters from A to Z non sensitive case, digits 0-9
                Symbols: '_', '.', '-'""", nameValidator.getInvalidSymbols());
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

    /**
     * Method is returning {@link server.ClientInfo} based on {@link server.ClientInfo} ip, port and username.
     */
    public ClientInfo createClientInfo() {
        return new ClientInfo(this.ip, this.currentPort, this.userName);
    }

}
