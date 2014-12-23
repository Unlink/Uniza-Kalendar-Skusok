/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import vzdelavaniefetcher.FetchException;
import vzdelavaniefetcher.Fetcher;
import vzdelavaniefetcher.Termin;
import vzdelavaniefetcher.tools.GUISwingTools;
import vzdelavaniefetcher.tools.ResourceManager;

/**
 *
 * @author Unlink
 */
public class KalendarDay extends javax.swing.JPanel {

    private Callback aCallback;
    
    public KalendarDay(final Termin t) {
        this(t, true);
    }

    public KalendarDay(final Termin t, boolean prihlasovanie) {
        this(t, true, null);
    }

    public KalendarDay(final Termin t, boolean prihlasovanie, Callback paCallback) {
        if (paCallback != null)
            aCallback = paCallback;
        else 
            aCallback = new Callback() {

            @Override
            public void stateChanged(Termin paTermin) {
            }
        };
        initComponents();
        jLabel12.setVisible(t.isNovy());
        jLabel1.setText(new SimpleDateFormat("EEEE", Locale.forLanguageTag("sk-SK")).format(t.getDatum()));
        jLabel2.setText(new SimpleDateFormat("dd", Locale.forLanguageTag("sk-SK")).format(t.getDatum()));
        jLabel3.setText(t.getPredmet().getNazov());
        jLabel4.setText(new SimpleDateFormat("HH:mm", Locale.forLanguageTag("sk-SK")).format(t.getDatum()));
        jLabel7.setText(t.getMiestnost());
        jLabel8.setText(t.getPrihlaseny()+"/"+t.getKapacita());
        if (!prihlasovanie || !t.isSomPrihlaseny()){
            jLabel10.setVisible(false);
            jLabel9.setVisible(false);
        }
        else {
            jLabel10.setText(""+t.getPredmet().getPrihlasenyNa()+".");
        }
        
        if (!prihlasovanie || t.getHrefZapis()==null){
            jLabel11.setVisible(false);
        }
        
        JPopupMenu menu = new JPopupMenu();
        JMenuItem info = new JMenuItem("Informácie", new ImageIcon(ResourceManager.getLocalImage("vzdelavaniefetcher.GUI.images.1370548527_info.png")));
        info.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Info(null, t).setVisible(true);
            }
        });
        menu.add(info);
        if (prihlasovanie && t.getHrefZapis()!=null){
            JMenuItem item;
            if (t.isSomPrihlaseny()) {
                item = new JMenuItem("Odhlás sa", new ImageIcon(ResourceManager.getLocalImage("vzdelavaniefetcher.GUI.images.1370548565_userconfig.png")));
                jLabel11.setToolTipText("Odhlás sa");
            }
            else {
                item = new JMenuItem("Zapíš sa", new ImageIcon(ResourceManager.getLocalImage("vzdelavaniefetcher.GUI.images.1370548565_userconfig.png")));
                jLabel11.setToolTipText("Zapíš sa");
            }
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int res = JOptionPane.showConfirmDialog(KalendarDay.this, "Skutočne chcete vykonať túto akciu?", "Potvrd", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.OK_OPTION) {
                        try {
                            JOptionPane.showMessageDialog(KalendarDay.this, Fetcher.dajInstanciu().terminAkcia(t), "Informácia", JOptionPane.INFORMATION_MESSAGE);
                        } catch (FetchException ex) {
                            GUISwingTools.displayErrorMessage(KalendarDay.this, ex.getMessage());
                        }
                        aCallback.stateChanged(t);
                    }
                }
            });
            menu.add(new JSeparator());
            menu.add(item);
        }
        setComponentPopupMenu(menu);
    }
	
	

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pondelok");
        jLabel1.setAlignmentY(0.0F);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("30");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Nazov Predmetu");

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel4.setText("Čas");

        jLabel5.setText("Miestnosť:");

        jLabel6.setText("Kapacita:");

        jLabel7.setText("XXX");

        jLabel8.setText("xx/xx");

        jLabel9.setText("Pokus:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("xx");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vzdelavaniefetcher/GUI/images/1370548565_userconfig.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("NEW!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)))))
                .addGap(11, 11, 11))
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jLabel9)
                                .addComponent(jLabel10))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(jLabel8)
                                .addComponent(jLabel11)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        setBackground(Color.GRAY.brighter());
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        setBackground(new java.awt.Color(240, 240, 240));
    }//GEN-LAST:event_formMouseExited

	@Override
	protected void paintComponent(Graphics paG) {
		super.paintComponent(paG);
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables


    /**
     * Callback interface
     */
    public interface Callback {
        /**
         * Zmena stavu termínu
         * @param paTermin 
         */
        public void stateChanged(Termin paTermin);
    }

}