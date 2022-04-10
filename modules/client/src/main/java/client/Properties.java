package client;

/**
 * Class that encapsulates global properties.
 */
public class Properties {
    public static final int userNameLen = 20;
    public static final int normalLetterCount = 3;
    public static final boolean nameStartsWithUpperCase = true;
    public static final int datagramPacketSize = 256;
    public static final String endTalkStr = "$stop";
    public static void printConversationEnd(String talkerName) {
        System.out.printf("Conversation with %s has ended %n", talkerName);
    }
}
