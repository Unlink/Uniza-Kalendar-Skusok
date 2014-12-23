/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vzdelavaniefetcher.tools;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
/**
 *
 * @author Unlink
 */
public class ResourceManager {
    
    
    
    /**
     * Vráti is zdroja z jarka
     * @param fqn fqn zdroja
     * @return 
     */
    public static InputStream getLocalResource(String fqn) {
        StringBuilder sb = new StringBuilder(fqn.length()+1);
        //nahradíme všetky . okrem poslednej za path separator
        for (String s:fqn.split("\\.", fqn.split("\\.").length-1)){
            sb.append('/').append(s);
        }
        
        return ResourceManager.class.getResourceAsStream(sb.toString());
    }
    
    /**
     * Načíta obrázok zo zdrojov
     * @param fqn obrázku
     * @return 
     */
    public static BufferedImage getLocalImage(String fqn) {
        try {
            return ImageIO.read(getLocalResource(fqn));
        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * Načíta zdroj do stringu
     * @param fqn fqn zdroja na načítanie
     * @return String z obsahom alebo null ak sa nepodarilo načítať
     */
    public static String readResourceToString(String fqn) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getLocalResource(fqn), "UTF-8"))){
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append(System.getProperty("line.separator", "\n"));
        } catch (IOException ex) {
            return null;
        }
        return sb.toString();
        
    }
    
    /**
     * Vráti cestu k zložke, z ktorej je spúšťaný program (snáď ;))
     * @return 
     */
    public static File getWorkingDir() {
        return new File(ResourceManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }
}
