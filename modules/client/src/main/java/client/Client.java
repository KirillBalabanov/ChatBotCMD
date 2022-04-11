package client;


import client.exceptions.InvalidNameException;

/**
 * Class is implementing client of {@link server.Server ChatBotServer}
 *
 */
public class Client {
    private String userName;

    public Client(String userName) throws InvalidNameException {
        setUserName(userName);
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Method is repsonsible for setting username.
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

}
