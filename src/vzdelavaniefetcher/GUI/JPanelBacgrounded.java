/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import vzdelavaniefetcher.tools.ResourceManager;

/**
 * Panel, ktorý na pozadí vykreslí obrázok
 * @author Unlink
 */
public class JPanelBacgrounded extends JPanel {

    /**
     * Chache pozadia, aby sa nemusel obrázok vždy scalovať 
     */
    private BufferedImage aBackground;
    private final String aImg;
    private final String aCVersion;
    
    /**
     * Konštruktor panelu
     * @param paImg Obrázok pozadia, musí byť zo zdrojov, zadáva sa ako fqn
     */
    public JPanelBacgrounded(String paImg) {
        aImg = paImg;
        aCVersion = ResourceManager.readResourceToString("version.txt");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (aBackground == null || (aBackground.getWidth() != getWidth() || aBackground.getHeight() != getHeight())) {
            try {  
                aBackground = ImageIO.read(ResourceManager.getLocalResource(aImg));

                int width = getWidth();
                int height = getHeight();

                if (aBackground.getWidth()*height > aBackground.getHeight()*getWidth()) {
                    width = aBackground.getWidth()*height/aBackground.getHeight();
                } else {
                    height = aBackground.getHeight()*width/aBackground.getWidth();
                }
                BufferedImage newImage = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = newImage.createGraphics();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setBackground(getBackground());
                    g2.clearRect(0, 0, width, height);
                    g2.drawImage(aBackground, 0, 0, width, height, null);
                } finally {
                    g2.dispose();
                }
                aBackground = newImage;
            }
            catch (IOException ex) {}
        }
            
        if (aBackground!=null)
            g.drawImage(aBackground, (getWidth()-aBackground.getWidth())/2, ((getHeight()-aBackground.getHeight())/2), null);

        g.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 10));
        
        String copyrights = "© Michal Ďuračík Verzia: "+aCVersion;
        
        int w = g.getFontMetrics().stringWidth(copyrights);
        
        g.drawString(copyrights, getWidth()-w-5, getHeight()-5);
    }

}
