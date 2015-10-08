/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	public Map<String, List<StudijnyVysledok>> getByYear() {
		Map<String, List<StudijnyVysledok>> tmp = new HashMap<>();
		for (StudijnyVysledok value : aVysledky.values()) {
			if (!tmp.containsKey(value.getAkademickyRok())) {
				tmp.put(value.getAkademickyRok(), new LinkedList<StudijnyVysledok>());
			}
			tmp.get(value.getAkademickyRok()).add(value);
		}
		return tmp;
	}
}
