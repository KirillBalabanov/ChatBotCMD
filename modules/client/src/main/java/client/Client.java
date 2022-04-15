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
     * Creates client with userName = null. Username must be set later.
     */
    public Client() throws UnknownHostException {
        this.userName = null;
        this.ip = InetAddress.getLocalHost();
    }

    /**
     * Method is responsible for setting username.
     * If username is not valid to {@link Properties.UserName.NameValidator} throws
     * {@link InvalidNameException}
     */
    public void setUserName(String userName) throws InvalidNameException {
        Properties.UserName.NameValidator nameValidator = new Properties.UserName.NameValidator(userName);

        if(!nameValidator.notNull()) throw new InvalidNameException("Null name.");
        if(!nameValidator.inLen()) {
            throw new InvalidNameException("Name out of length.");
        }
        if(nameValidator.startsWithUpperCase() != Properties.UserName.nameStartsWithUpperCase) {
            throw new InvalidNameException("Name starts with wrong case.");
        }
        if(!nameValidator.containsNormalLettersCount()) {
            throw new InvalidNameException("Name should contain " + Properties.UserName.normalLetterCount + " normal letters");
        }
        if(!nameValidator.isValid()) throw new InvalidNameException("" +
                "Name could contain\n" +
                "Letters from A to Z non sensitive case, digits 0-9\n" +
                "Symbols: '_', '.', '-'\n" +
                "Invalid symbols in name: \n" +
                nameValidator.getInvalidSymbols());
        this.userName = userName;
    }

    /**
     * Method is returning {@link server.ClientInfo} based on {@link server.ClientInfo} ip, port and username.
     */
    public ClientInfo createClientInfo() {
        return new ClientInfo(this.ip, this.currentPort, this.userName);
    }

    public String getUserName() {
        return userName;
    }

    public int getCurrentPort() { return currentPort; }

    public void setCurrentPort(int port) { this.currentPort = port; }
}
