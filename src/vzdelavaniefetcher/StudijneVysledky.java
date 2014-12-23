/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher;

import java.util.HashMap;

/**
 *
 * @author Unlink
 */
public class StudijneVysledky {
    private final HashMap<String, StudijnyVysledok> aVysledky;

    public StudijneVysledky() {
        this.aVysledky = new HashMap<>();
    }
    
    public void vloz(StudijnyVysledok paSv) {
        String key = paSv.getAkademickyRok()+"_"+paSv.getPredmet();
        aVysledky.put(key, paSv);
    }
    
    public boolean isSpraveny(String paPredmet) {
        for (StudijnyVysledok studijnyVysledok : aVysledky.values()) {
            if (studijnyVysledok.getPredmet().equals(paPredmet) && studijnyVysledok.isSpraveny())
                return true;
        }
        return false;
    }
}
