/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package vzdelavaniefetcher.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Unlink
 */
public class SimpleSerializedEncrypredStringMap implements Map<String, String> {

    private final File aFile;
    private HashMap<String, byte[]> aMap;

    /**
     * Konštruktor, ktorý načíta mapu asociovanú zo súborom, pokiaľ súbor neexsistuje, tak ho vytvorí a vytvorí prázdnu mapu
     * @param aFile 
     */
    public SimpleSerializedEncrypredStringMap(File aFile) {
        this.aFile = aFile;
        this.aMap = new HashMap<>();
        if (aFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(aFile))){
                aMap = (HashMap<String, byte[]>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SimpleSerializedEncrypredStringMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Uloží aktuálny obsah mapy, do súboru
     */
    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(aFile))){
            oos.writeObject(aMap);
        } catch (IOException ex) {
            Logger.getLogger(SimpleSerializedEncrypredStringMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int size() {
        return aMap.size();
    }

    @Override
    public boolean isEmpty() {
        return aMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return aMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String get(Object key) {
        return new String(Encryper.decrypt(aMap.get(key), createKeyForKey(key.toString())));
    }

    @Override
    public String put(String key, String value) {
        String ret = null;
        if (aMap.containsKey(key)) {
            ret = get(key);
        }
        
        aMap.put(key, Encryper.encrypt(value, createKeyForKey(key)));
        save();
        return ret;
    }

    /**
     * Vytvorí klúč pre hodnotu, na základe kluca mapy
     * TODO - vymyslieť lepší algoritmus generovania kľúča
     * @param key
     * @return 
     */
    private String createKeyForKey(String key) {
        return key+this.getClass().getName();
    }

    @Override
    public String remove(Object key) {
        String ret = null;
        if (aMap.containsKey(key)) {
            ret = get(key);
            aMap.remove(key);
            save();
        }
        return ret;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        aMap.clear();
        save();
    }

    @Override
    public Set<String> keySet() {
        return aMap.keySet();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
