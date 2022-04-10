package com.kirillbalabanov.client;

import com.kirillbalabanov.client.exceptions.InvalidNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {

    public static void startMenu() {
        System.out.println("Greetings! Please input your name.");
        String strCase = Properties.nameStartsWithUpperCase ? "Upper Case" : "Lower Case";
        System.out.printf("---Name should start with %s%n" +
                "---Contain %s normal letters%n" +
                "---Be in range smaller than %s symbols%n", strCase, Properties.normalLetterCount, Properties.userNameLen);
    }

    public static void printMainMenu() {
        System.out.println("Print 1 to find talker;");
        System.out.println("Print 2 to leave;");
        System.out.println("Print 3 to change username.");
    }


    public static void main(String[] args) {
        startMenu();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            // getting client instance
            Client client;
            while (true) {
                try {
                    client = new Client(br.readLine());
                    break;
                } catch (InvalidNameException invalidNameException) {
                    System.out.println(invalidNameException.toString());
                }
            }
            System.out.printf("Username %s has been set %n", client.getUserName());


            // main menu loop
            while(true){
                printMainMenu();
                switch (br.readLine()){
                    case "1":
                        DatagramSocket talkerSocket = Control.findUser(client);
                        Control.talk(client, talkerSocket);

                    case "2":
                        return;

                    case "3":
                        // changing name
                        while (true) {
                            try {
                                client.setUserName(br.readLine());
                                break;
                            } catch (InvalidNameException invalidNameException) {
                                System.out.println(invalidNameException.toString());
                            }
                        }
                        System.out.printf("Username %s has been set %n", client.getUserName());
                        break;
                    default:
                        System.out.println("Wrong option");
                }

            }

        } catch (IOException e) { return; }

    }
}
