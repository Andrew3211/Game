package com.company;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class Main {

    public static String hmac(String data, String key) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String dataWithKey = data + key;
            byte[] dataBytes = dataWithKey.getBytes();
            byte[] dataHmac = sha256.digest(dataBytes);

            return bytesToString(dataHmac);

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

    public static String bytesToString(byte[] bytes){
        StringBuilder sb = new StringBuilder();

        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SecureRandom rand = new SecureRandom();

        if (args.length == 0) {
            System.out.println("No parameters were entered!");
            System.exit(0);
        }
        if (args.length % 2 == 0) {
            System.out.println("An even number of parameters passed!");
            System.exit(0);
        }

        while (true) {
            try {

                byte[] randomBytes = new byte[16];
                rand.nextBytes(randomBytes);

                String randomKeyString = bytesToString(randomBytes);

                int computerAnswer = rand.nextInt(args.length);

                String hmac = hmac(args[computerAnswer], randomKeyString);
                System.out.println("HMAC:" + hmac);

                System.out.println("Available moves: ");
                for (int i = 0; i < args.length; i++) {
                    int outputNumber = i + 1;
                    System.out.println(outputNumber + ": " + args[i]);
                }
                System.out.println("0: Exit");

                System.out.print("Enter your move: ");

                String userAnswerString = scanner.nextLine();
                int userAnswer;
                try {
                    userAnswer = Integer.parseInt(userAnswerString) - 1;
                } catch (NumberFormatException e) {
                    throw new Exception("You didn't enter a number!");
                }

                if (userAnswer == -1) {
                    System.out.println("End of the game!");
                    break;
                }

                if(userAnswer > args.length - 1 || userAnswer < 0){
                    throw new Exception("Wrong number! Please, write the correct number.");
                }

                System.out.println("Your move: " + args[userAnswer]);
                System.out.println("Computer move: " + args[computerAnswer]);

                int part = (args.length - 1) / 2;

                ArrayList<Integer> loseUserNumbers = new ArrayList<>();
                for (int i = 0; i < part; i++) {
                    int res = userAnswer + i + 1;
                    loseUserNumbers.add(res >= args.length ? res - args.length : res);
                }

                if (computerAnswer == userAnswer) {
                    System.out.println("Draw!");
                } else if (loseUserNumbers.contains(computerAnswer)) {
                    System.out.println("You lose!");
                } else {
                    System.out.println("You win!");
                }

                System.out.println("HMAC key:" + randomKeyString);

            } catch (Exception e) {
                System.out.println("An error has occurred: " + e.getLocalizedMessage());
            }
            System.out.println("\n\n\n");
        }
    }
}