package com.kirillbalabanov.client;

/**
 * Class that encapsulates global properties.
 */
public class Properties {
    static final int userNameLen = 20;
    static final int normalLetterCount = 3;
    static final boolean nameStartsWithUpperCase = true;
    static final int datagramPacketSize = 256;
    static final String endTalkStr = "$stop";
    static void printConversationEnd(String talkerName) {
        System.out.printf("Conversation with %s has ended %n", talkerName);
    }
}
