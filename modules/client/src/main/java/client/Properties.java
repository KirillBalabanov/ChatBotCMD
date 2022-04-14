package client;

/**
 * Class that encapsulates global properties.
 */
public class Properties {
    public static final String endTalkStr = "$stop";
    public static final String defaultPathToSettings = "settings/settings.txt";
    public static final int loadSymbolsPeriodMS = 300;
    public static class Name {
        public static final int userNameLen = 20;
        public static final int normalLetterCount = 3;
        public static final boolean nameStartsWithUpperCase = true;
    }
}
