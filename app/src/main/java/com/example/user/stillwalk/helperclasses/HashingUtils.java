package com.example.user.stillwalk.helperclasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public  class HashingUtils {

    public static String hashPassowrd(String password){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte [] bytes = md.digest();

            StringBuilder builder = new StringBuilder();

            for (byte aByte : bytes) {

                builder.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;

    }
}
