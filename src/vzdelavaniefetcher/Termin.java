/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import vzdelavaniefetcher.tools.Base64;

/**
 *
 * @author Unlink
 */
public class Termin implements Serializable {
    private Predmet aPredmet;
    private Date aDatum;
    private String aMiestnost;
    private String aSkusajuci;
    private int aKapacita;
    private int aPrihlaseny;
    private String aTyp;
    private String aPoznamka;
    
    private boolean aSomPrihlaseny;
    
    private String aHref;
    private String aHrefZapis;
    
    private String aPrihlasenyStudenti;
    
    private boolean aJeNovy;

    public Termin(Predmet aPredmet, Date aDatum, String aMiestnost, String aSkusajuci, int aKapacita, int aPrihlaseny, String aTyp, String aPoznamka, boolean aSomPrihlaseny, String aHref, String aHrefZapis) {
        this.aPredmet = aPredmet;
        this.aDatum = aDatum;
        this.aMiestnost = aMiestnost;
        this.aSkusajuci = aSkusajuci;
        this.aKapacita = aKapacita;
        this.aPrihlaseny = aPrihlaseny;
        this.aTyp = aTyp;
        this.aPoznamka = aPoznamka;
        this.aSomPrihlaseny = aSomPrihlaseny;
        this.aHref = aHref;
        this.aHrefZapis = aHrefZapis;
        this.aJeNovy = false;
    }

    /**
     * Vráti predmet ktorému patrí termín
     * @return 
     */
    public Predmet getPredmet() {
        return aPredmet;
    }
    
    /**
     * Vráti datum konania terminu
     * @return 
     */
    public Date getDatum() {
        return aDatum;
    }

    /**
     * Vráti miestnosť
     * @return 
     */
    public String getMiestnost() {
        return aMiestnost;
    }

    /**
     * Vráti meno skusajuceho
     * @return 
     */
    public String getSkusajuci() {
        return aSkusajuci;
    }

    /**
     * Vráti kapacitu terminu
     * @return 
     */
    public int getKapacita() {
        return aKapacita;
    }

    /**
     * Vráti počet prihlásených na termín
     * @return 
     */
    public int getPrihlaseny() {
        return aPrihlaseny;
    }

    /**
     * Vráti typ terminu
     * @return 
     */
    public String getTyp() {
        return aTyp;
    }

    /**
     * Vráti poznámku k termínu
     * @return 
     */
    public String getPoznamka() {
        return aPoznamka;
    }

    /**
     * Inforumeje o tom, či je uživateľ prihlásený na termín
     * @return 
     */
    public boolean isSomPrihlaseny() {
        return aSomPrihlaseny;
    }

    /**
     * Vráti prihlasených studentov
     * @return 
     */
    public String getPrihlasenyStudenti() {
        return aPrihlasenyStudenti;
    }

    /**
     * Nastaví prihlásených studentov
     * @param aPrihlasenyStudenti 
     */
    void setPrihlasenyStudenti(String aPrihlasenyStudenti) {
        this.aPrihlasenyStudenti = aPrihlasenyStudenti;
    }

    /**
     * Vráti url pre informácie o termíne
     * @return 
     */
    public String getHref() {
        return aHref;
    }

    /**
     * Vráti url na vykonanie akcie / prihlasenie alebo odhlasenie z termínu
     * @return 
     */
    public String getHrefZapis() {
        return aHrefZapis;
    }
    
    
    /** 
     * Vráti id termínu získanú z url adresy predmetu (asi ;))
     * @return id termínu
     */
    public int getTerminId() {
        try {
            String str = new String(Base64.decode(aHref.split("pid=")[1]));
            String terminId = str.split("--")[1];
            return Integer.parseInt(terminId);
        } catch (IOException ex) {
            return -1;
        }
    }

    /**
     * Vráti či je predmet nový alebo nie
     * @return 
     */
    public boolean isNovy() {
        return aJeNovy;
    }

    /**
     * Nastaví status predmetu na nový alebo nie
     * @param aJeNovy 
     */
    public void setNovy(boolean aJeNovy) {
        this.aJeNovy = aJeNovy;
    }
    
    
    
}
