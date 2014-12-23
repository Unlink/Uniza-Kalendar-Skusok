/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher.tools;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Unlink
 */
public class Encryper {

    /**
     * Zašifruje dáta, podla kľúča
     * @param value Hodnota na zašifrovanie
     * @param pass Kluc na sifrovanie
     * @return Zasifrovane data
     */
    public static byte[] encrypt(String value, String pass) {
        byte[] encrypted = null;
        try {

            byte[] raw = pass.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            raw = sha.digest(raw);
            raw = Arrays.copyOf(raw, 16);
            Key skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];

            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivParams);
            encrypted  = cipher.doFinal(value.getBytes());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encrypted;
    }

    /**
     * Metóda dešifruje dáta
     * @param encrypted zašifrované dáta
     * @param pass heslo
     * @return dešifrované dáta, pokiaľ sú správne dáta aj kľúč, inak null
     */
    public static byte[] decrypt(byte[] encrypted, String pass) {
        byte[] original = null;
        Cipher cipher = null;
        try {
            byte[] raw = pass.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            raw = sha.digest(raw);
            raw = Arrays.copyOf(raw, 16);
            Key key = new SecretKeySpec(raw, "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivByte = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParamsSpec);
            original= cipher.doFinal(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return original;
    }  
}
