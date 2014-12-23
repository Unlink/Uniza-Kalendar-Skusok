/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Unlink
 */
public class Predmet implements Serializable, Iterable<Termin> {
    private String aNazov;
    private List<Termin> aTerminy;
    private String aTerminyHref;
    private int aZostavajucichTerminov;
    private int aPrihlasenyNa;

    public Predmet(String aNazov, String aTerminyHref) {
        this.aNazov = aNazov;
        this.aTerminyHref = aTerminyHref;
        aPrihlasenyNa = 0;
        aZostavajucichTerminov = 0;
        aTerminy = new ArrayList<>();
    }

    /**
     * Vráti nazov predmetu
     * @return 
     */
    public String getNazov() {
        return aNazov;
    }

    /**
     * Irerátor na termíny jednotlivého predmetu
     * @return 
     */
    @Override
    public Iterator<Termin> iterator() {
        return aTerminy.iterator();
    }

    /**
     * Vráti počet zostavajúcich termínv
     * @return 
     */
    public int getZostavajucichTerminov() {
        return aZostavajucichTerminov;
    }

    /**
     * Vráti počet prihlásených ludí na termin
     * @return 
     */
    public int getPrihlasenyNa() {
        return aPrihlasenyNa;
    }

    @Override
    public String toString() {
        return aNazov;
    }

    /**
     * Nastaví zoznam termínov
     * @param aTerminy 
     */
    public void setTerminy(List<Termin> aTerminy) {
        this.aTerminy = aTerminy;
    }

    /**
     * Nastaví počet zostavajucich terminov
     * @param aZostavajucichTerminov 
     */
    public void setZostavajucichTerminov(int aZostavajucichTerminov) {
        this.aZostavajucichTerminov = aZostavajucichTerminov;
    }

    /**
     * Nastaví počet prihlásených na terminy
     * @param aPrihlasenyNa 
     */
    public void setPrihlasenyNa(int aPrihlasenyNa) {
        this.aPrihlasenyNa = aPrihlasenyNa;
    }

    /**
     * Vráti url adresu, akcie, na ziskanie info o predmete
     * @return 
     */
    public String getTerminyHref() {
        return aTerminyHref;
    }
    
}
