/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher;

import java.util.Date;

/**
 *
 * @author Unlink
 */
public class StudijnyVysledok {
    private static final String cFx = "Fx";
    
    private String aAkademickyRok;
    private String aSemester;
    
    private String aKodPredmetu;
    private String aPredmet;
    private String aPov;
    
    private String aBodyZaSemester;
    private Date aZapocet;
    
    private String aZapocetZn;
    
    private Date aSkuska;
    private String aSkuskaZn;
    
    private int aKreditov;

    public StudijnyVysledok(String aAkademickyRok, String aSemester, String aKodPredmetu, String aPredmet, String aPov, String aBodyZaSemester, Date aZapocet, String aZapocetZn, Date aSkuska, String aSkuskaZn, int aKreditov) {
        this.aAkademickyRok = aAkademickyRok;
        this.aSemester = aSemester;
        this.aKodPredmetu = aKodPredmetu;
        this.aPredmet = aPredmet;
        this.aPov = aPov;
        this.aBodyZaSemester = aBodyZaSemester;
        this.aZapocet = aZapocet;
        this.aZapocetZn = aZapocetZn;
        this.aSkuska = aSkuska;
        this.aSkuskaZn = aSkuskaZn;
        this.aKreditov = aKreditov;
    }

    public String getAkademickyRok() {
        return aAkademickyRok;
    }

    public String getSemester() {
        return aSemester;
    }

    public String getPredmet() {
        return aPredmet;
    }

    public String getPov() {
        return aPov;
    }

    public String getBodyZaSemester() {
        return aBodyZaSemester;
    }

    public Date getZapocet() {
        return aZapocet;
    }

    public String getZapocetZn() {
        return aZapocetZn;
    }

    public Date getSkuska() {
        return aSkuska;
    }

    public String getSkuskaZn() {
        return aSkuskaZn;
    }

    public int getKreditov() {
        return aKreditov;
    }
    
    public boolean isSpraveny() {
        return (!aSkuskaZn.isEmpty() && !aSkuskaZn.equals(cFx)) || (!aZapocetZn.isEmpty() && !aZapocetZn.equals(cFx));
    }
    
}
