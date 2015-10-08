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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Unlink
 * @param <K>
 * @param <V>
 */
public class SimpleSerializedMappedSet<K, V> {

    private Map<K, Set<V>> aData;
    private final File aFile;

    /**
     * Vytvorí novú mapu
     *
     * @param paFile
     */
    public SimpleSerializedMappedSet(File paFile) {
        aFile = paFile;

        if (aFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(aFile))) {
                aData = (Map<K, Set<V>>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                aData = new HashMap<>();
            }
        }
        else {
            aData = new HashMap<>();
            try {
                aFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Uloží aktuálny obsah mapy, do súboru
     */
    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(aFile))) {
            oos.writeObject(aData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Vloží prvok, do daného klúca
     *
     * @param key
     * @param value
     */
    public void add(K key, V value) {
        if (!aData.containsKey(key)) {
            aData.put(key, new HashSet<V>());
        }
        aData.get(key).add(value);
        save();
    }

    /**
     * Overí, či mapa obsahuje kľúč
     *
     * @param key
     * @return
     */
    public boolean containsKey(K key) {
        return aData.containsKey(key);
    }

    /**
     * Overí či exsistuje v danom kľúči zadaná hodnota
     *
     * @param key
     * @param value
     * @return
     */
    public boolean contains(K key, V value) {
        return aData.containsKey(key) && aData.get(key).contains(value);
    }

    /**
     * Odstráni kľúč
     *
     * @param key
     * @return
     */
    public boolean removeKey(K key) {
        boolean r = aData.remove(key) != null;
        if (r) {
            save();
        }
        return r;
    }

    /**
     * Odstráni hodnotu z kľúča
     *
     * @param key
     * @param value
     * @return
     */
    public boolean remove(K key, V value) {
        if (!aData.containsKey(key)) {
            return false;
        }
        boolean r = aData.get(key).remove(value);
        if (r) {
            save();
        }
        return r;
    }
}
