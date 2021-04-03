package com.pmapp.password_manager;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class secret {
    public static String reducedPass;

    public static String getReducedPass() {
        return reducedPass;
    }

    public static void setReducedPass(String reducedPass) {
        secret.reducedPass = reducedPass;
    }

    public static String sha512Hasher(String password, String uname) throws NoSuchAlgorithmException {

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

        byte[] unameBytes = uname.getBytes();
        System.out.println("straight string hash: " + unameBytes);
        byte[] revStr = new byte[unameBytes.length];
        for (int i = 0; i < unameBytes.length; i++) {
            revStr[i] = unameBytes[unameBytes.length - i - 1];
            //System.out.println("sssalt: " + revStr);
        }
        System.out.println("name rev: " + new String(revStr) + ", revUnameHash: " + revStr);
        byte[] salt = new String(revStr).getBytes();
        System.out.println("salt: " + new String(revStr).getBytes() + ", " + salt);
        return salt;
    }

    public static String genKey(String uname) {
        FirebaseDatabase fDatabase;
        DatabaseReference dbRef;
        reducedPass = "";
        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference("users");

        Query checkUser = dbRef.orderByChild("username").equalTo(uname);
        Log.i("testpass", "(Main)onClick: before event listener");
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passFromDb = snapshot.child(uname).child("password").getValue(String.class);
                    Log.i("testpass", "(Main)onClick:-------- pass= "+passFromDb);
                    for (int i = 0; i < 8; i++ ){
                        reducedPass += passFromDb.charAt(i);
                        setReducedPass(reducedPass);
                        //return reducedPass;
                    }
                    Log.i("testpass", "(Main)onClick:-------- pass= "+reducedPass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Log.i("testpass", "(secret)decrypt: reducedPass= "+ getReducedPass());
        return getReducedPass();
    }

    public static String genpass(int length) {

        String charec = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
        char temp;
        String tempe, genp = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            temp = charec.charAt(random.nextInt(charec.length()));
            tempe = Character.toString(temp);
            genp = genp + tempe;
            //System.out.println(tempe + "+++++++" + genp);
        }
        return genp;
    }

    private static final String ALGO = "AES";

    private byte[] keyValue;

    public secret(String key) {
        keyValue = key.getBytes();
    }

    public Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
    /*
    public byte[] encrypt(String messg) throws Exception {
        Log.i("testpass", "(secret)encrypt: in encryt func");
        Key key = generateKey();
        Log.i("testpass", "(secret)encrypt: key= "+key);
        Cipher cipher = Cipher.getInstance(ALGO);
        Log.i("testpass", "(secret)encrypt: Key length= "+ key.getEncoded().length);
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), ALGO);
        //IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(cipher.ENCRYPT_MODE, keySpec);
        Log.i("testpass", "(secret)encrypt: cipher= "+cipher);
        byte[] raw = cipher.doFinal(messg.getBytes());
        //Base64 encode=new Base64();
        Log.i("testpass", "(secret)encrypt: raw= "+raw);
        //String encText = Base64.encodeToString(raw, Base64.NO_WRAP));
        //String encText = new String(raw, "ISO-8859-1");
        Log.i("testpass", "(secret)encrypt: encText= "+raw);
        return raw;
        // 55c75be8e1ea4186bf296c180f22d17cc52f8ff6fad75a16131af2b6cca7ce6c7a8f5025f3af3baa979bae1106cf62b0ca4769dbb8b52ad1df533c2fcb5580ad
    }*/

    public static String encrypt(String msg, String key){
        Log.i("testpass", "(secret)encrypt: in encryt func");
        Log.i("testpass", "(secret)encrypt: key= "+key);
        try {
            Log.i("testpass", "(secret)encrypt: in Try");
            String encText = AESCrypt.encrypt(key, msg);
            Log.i("testpass", "(secret)encrypt: encText= "+encText);
            return encText;
        } catch (GeneralSecurityException e) {
            Log.i("testpass", "(secret)encrypt: Exception: "+e.toString());
        }
        return null;
    }

    public static String decrypt(String msg, String key){
        Log.i("testpass", "(secret)decrypt: in decrypt func");
        Log.i("testpass", "(secret)decrypt: key= "+key);
        try {
            Log.i("testpass", "(secret)decrypt: in Try");
            String decText = AESCrypt.decrypt(key, msg);
            Log.i("testpass", "(secret)decrypt: decText= "+decText);
            return decText;
        } catch (GeneralSecurityException e) {
            Log.i("testpass", "(secret)decrypt: Exception: "+e.toString());
        }
        return null;
    }

    /*
    public String decrypt(byte[] encrypted) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), ALGO);
        cipher.init(cipher.DECRYPT_MODE, keySpec);
        //BASE64Decoder decode=new BASE64Decoder();
        //byte[] raw = Base64.decode(encrypted, Base64.NO_WRAP);
        byte[] stringBytes = cipher.doFinal(encrypted);
        String decText = new String(stringBytes, "UTF8");
        return decText;
    }*/
}
