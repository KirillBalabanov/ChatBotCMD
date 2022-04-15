package client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that encapsulates global properties.
 */
public class Properties {
    public static final String endTalkStr = "$stop";
    public static final String defaultPathToSettings = "settings/settings.txt";
    public static final int loadSymbolsPeriodMS = 300;

    public static class UserName {
        public static final int userNameLen = 20;
        public static final int normalLetterCount = 3;
        public static final boolean nameStartsWithUpperCase = true;
        public static final Pattern regex = Pattern.compile("[^A-Z a-z._\\-0-9]");

        public static class NameValidator {
            private final String str;

            public NameValidator(String str) {
                this.str = str;
            }

            public boolean containsNormalLettersCount() {
                Pattern pattern = Pattern.compile(String.format("[a-zA-Z]{%d,}", normalLetterCount));
                return pattern.matcher(this.str).find();
            }

            public boolean inLen() {
                return this.str.length() > 0 && this.str.length() < userNameLen;
            }

            public boolean startsWithUpperCase() {
                return Character.isUpperCase(this.str.charAt(0));
            }

            public boolean isValid() {
                return !regex.matcher(this.str).find();
            }

            public String getInvalidSymbols() {
                StringBuilder stringBuilder = new StringBuilder();
                Matcher matcher = regex.matcher(this.str);
                while(matcher.find()) stringBuilder.append(String.format("'%s'", matcher.group())).append(", ");
                stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ".");
                return stringBuilder.toString();
            }
            public boolean notNull() { return this.str != null; }
        }
    }

}
