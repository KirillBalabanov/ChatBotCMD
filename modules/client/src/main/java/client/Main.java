package client;

import client.exceptions.InvalidNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void startMenu() {
        System.out.println("Greetings! Please input your name.");
        String strCase = Properties.Name.nameStartsWithUpperCase ? "Upper Case" : "Lower Case";
        System.out.printf("---Name should start with %s%n" +
                "---Contain %s normal letters%n" +
                "---Be in range smaller than %s symbols%n", strCase, Properties.Name.normalLetterCount, Properties.Name.userNameLen);
    }

    public static void printMainMenu() {
        System.out.println("Print 1 to find talker;");
        System.out.println("Print 2 to leave;");
        System.out.println("Print 3 to change username.");
    }

    public static void setUserName(Client client, BufferedReader br) {
        while (true) {
            try {
                client.setUserName(br.readLine());
                break;
            } catch (InvalidNameException | IOException invalidNameException) {
                invalidNameException.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            // getting client instance
            startMenu();
            Client client = new Client();
            Control control;
            setUserName(client, br);

            System.out.printf("Username %s has been set %n", client.getUserName());
            control = new Control(client);

            // main menu loop
            while(true){
                printMainMenu();
                switch (br.readLine()){
                    case "1":
                        control.findAndTalk(br);
                        break;

                    case "2":
                        return;

                    case "3":
                        // changing name
                        System.out.println("Please input your name.");
                        setUserName(client, br);
                        System.out.printf("Username %s has been set %n", client.getUserName());
                        break;
                    default:
                        System.out.println("Wrong option");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
