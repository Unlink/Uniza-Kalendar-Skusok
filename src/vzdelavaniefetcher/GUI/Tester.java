/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher.GUI;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vzdelavaniefetcher.FetchException;
import vzdelavaniefetcher.FetcherListner;
import vzdelavaniefetcher.StudijneVysledky;
import vzdelavaniefetcher.StudijnyVysledok;
import vzdelavaniefetcher.Termin;
import vzdelavaniefetcher.tools.SimpleSerializedMappedSet;

/**
 *
 * @author Unlink
 */
public class Tester {

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException, FetchException, ParseException {
        /*File f = new File(Tester.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
         SimpleSerializedEncrypredStringMap ssem = new SimpleSerializedEncrypredStringMap(new File(f,"data.bin"));
        
         System.out.println("Obsah mapy:");
         for(String k:ssem.keySet()) {
         System.out.println(k+" :-> "+ssem.get(k));
         }*/

        /*String bla = "bla.bla.ble";
         String[] split = bla.split("\\.");*/
        /*System.out.println(Tester.class.getCanonicalName());
        
         HashSet<String> h = new HashSet<>();
         h.toArray(args);
        
         SimpleSerializedMappedSet<String, Integer> xx = new SimpleSerializedMappedSet<>(new File("terminy.dat"));
         xx.remove("Unlink", 99105);*/
        //System.out.println(toSHA1("bla"));
        StudijneVysledky sv = new StudijneVysledky();
        //featch(sv);
        System.out.println(sv.toString());
    }

    /*public static String toSHA1(String pass) {
     MessageDigest md = null;
     try {
     md = MessageDigest.getInstance("SHA-1");
     } catch (NoSuchAlgorithmException e) {
     e.printStackTrace();
     }
     return byteArrayToHexString(md.digest(pass.getBytes()));
     }

     public static String byteArrayToHexString(byte[] b) {
     String result = "";
     for (int i = 0; i < b.length; i++) {
     result
     += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
     }
     return result;
     }*/
}
