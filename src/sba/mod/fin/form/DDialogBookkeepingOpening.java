/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.fin.form;

import javax.swing.JOptionPane;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.fin.db.DDbProcessBookkeepingOpening;

/**
 *
 * @author Sergio Flores
 */
public class DDialogBookkeepingOpening extends DBeanFormDialog {

    /**
     * Creates new form DDialogBookkeepingOpening
     */
    public DDialogBookkeepingOpening(DGuiClient client) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.FX_PRC_BKK_OPE, DLibConsts.UNDEFINED, "Apertura de ejercicio contable");
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moIntYear = new sba.lib.gui.bean.DBeanFieldInteger();
        jPanel6 = new javax.swing.JPanel();
        jlIcon = new javax.swing.JLabel();

        setTitle("Saldos contables iniciales");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Generación de saldos contables iniciales:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(1, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Ejercicio por abrir:*");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlYear);

        moIntYear.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(moIntYear);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jlIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_fin_bw.png"))); // NOI18N
        jPanel6.add(jlIcon);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-336)/2, (screenSize.height-188)/2, 336, 188);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlIcon;
    private javax.swing.JLabel jlYear;
    private sba.lib.gui.bean.DBeanFieldInteger moIntYear;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 320, 200);

        moIntYear.setIntegerSettings(DGuiUtils.getLabelName(jlYear.getText()), DGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moIntYear.setMinInteger(2000);
        moIntYear.setMaxInteger(2100);

        moFields.addField(moIntYear);
        moFields.setFormButton(jbSave);

        moIntYear.setValue(miClient.getSession().getWorkingYear());
    }

    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (DGuiUtils.computeValidation(miClient, validateForm())) {
                if (miClient.showMsgBoxConfirm("¿Está seguro que desea generar los saldos contables iniciales del ejercicio " + moIntYear.getValue() + "?") == JOptionPane.YES_OPTION) {
                    boolean saved = false;
                    DDbProcessBookkeepingOpening pbo = null;

                    try {
                        pbo = new DDbProcessBookkeepingOpening();
                        pbo.setPkOpeningYearId(moIntYear.getValue());
                        pbo.save(miClient.getSession());

                        if (pbo.getQueryResultId() == DDbConsts.SAVE_OK) {
                            saved = true;
                            miClient.getSession().notifySuscriptors(DModConsts.F_BKK);
                            miClient.showMsgBoxInformation(DLibConsts.MSG_PROCESS_FINISHED);
                        }
                    }
                    catch (Exception e) {
                        DLibUtils.showException(this, e);
                    }

                    if (saved) {
                        mnFormResult = DGuiConsts.FORM_RESULT_OK;
                        dispose();
                    }
                }
            }
        }
    }

    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
