package com.pmapp.password_manager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class secret {
    public static String sha512Hasher(String password, String uname)throws NoSuchAlgorithmException{

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        byte[] salt = gensalt(uname);
        System.out.println("In hasher salt: " + salt);
        md.update(salt);
        byte[] hashpass = md.digest(password.getBytes());
        //String hashpassS = new String(hashpass);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashpass.length; i++) {
            sb.append(Integer.toString((hashpass[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashpassS = sb.toString();

        return hashpassS;
    }

    public static byte[] gensalt(String uname) throws NoSuchAlgorithmException {

        /*SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);*/
        byte[] unameBytes = uname.getBytes();
        System.out.println("straight string hash: " + unameBytes);
        byte[] revStr = new byte[unameBytes.length];
        for (int i = 0; i<unameBytes.length ; i++){
            revStr[i] = unameBytes[unameBytes.length - i - 1];
            //System.out.println("sssalt: " + revStr);
        }
        System.out.println("name rev: " + new String(revStr) + ", revUnameHash: " + revStr);
        byte[] salt = new String(revStr).getBytes();
        System.out.println("salt: " + new String(revStr).getBytes() + ", "+salt);
        return salt;
    }

    public static String genpass(int length){

        String charec = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
        char temp;
        String tempe, genp = "";
        Random random = new Random();
        for(int i = 0; i < length; i++){
            temp = charec.charAt(random.nextInt(charec.length()));
            tempe = Character.toString(temp);
            genp = genp + tempe;
            //System.out.println(tempe + "+++++++" + genp);
        }
        return genp;
    }
}
