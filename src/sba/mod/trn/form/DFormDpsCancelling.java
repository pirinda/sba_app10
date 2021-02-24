/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormDpsCancelling.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.trn.form;

import java.util.Date;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DDbDpsSigning;
import sba.mod.trn.db.DTrnEmissionConsts;

/**
 *
 * @author Sergio Flores
 */
public class DFormDpsCancelling extends DBeanForm {
    
    public static final int RETRY_CANCEL = 1;

    private DDbDps moRegistry;
    private int mnOriginalYear;
    private Date mtOriginalDate;
    private DDbSysXmlSignatureProvider moXmlSignatureProvider;

    /** Creates new form DFormDpsCancelling
     * @param client GUI client.
     */
    public DFormDpsCancelling(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.T_DPS_SIG, DModSysConsts.TX_XMS_REQ_TP_CAN, title);
        initComponents();
        initComponentsCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgAnnulCancel = new javax.swing.ButtonGroup();
        jpContainer = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        moPanelDps = new sba.mod.trn.form.DPanelDps();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        moRadDisable = new sba.lib.gui.bean.DBeanFieldRadio();
        jPanel21 = new javax.swing.JPanel();
        moRadDisableCancel = new sba.lib.gui.bean.DBeanFieldRadio();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        moBoolRetryCancel = new sba.lib.gui.bean.DBeanFieldBoolean();

        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel6.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel6.add(moPanelDps, java.awt.BorderLayout.PAGE_START);

        jpContainer.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la cancelación:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgAnnulCancel.add(moRadDisable);
        moRadDisable.setText("Anular documento: en el sistema solamente");
        moRadDisable.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel8.add(moRadDisable);

        jPanel5.add(jPanel8);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgAnnulCancel.add(moRadDisableCancel);
        moRadDisableCancel.setText("Anular y cancelar documento: en el sistema y ante la autoridad");
        moRadDisableCancel.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel21.add(moRadDisableCancel);

        jPanel5.add(jPanel21);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel5.add(jPanel1);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolRetryCancel.setText("Reintentar la cancelación aún cuando el receptor haya rechazado la solicitud de cancelación anterior");
        moBoolRetryCancel.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel3.add(moBoolRetryCancel);

        jPanel5.add(jPanel3);

        jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jpContainer.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);

        moRadDisable.setBooleanSettings(moRadDisable.getText(), false);
        moRadDisableCancel.setBooleanSettings(moRadDisableCancel.getText(), false);
        moBoolRetryCancel.setBooleanSettings(moBoolRetryCancel.getText(), true);

        moFields.addField(moRadDisable);
        moFields.addField(moRadDisableCancel);
        moFields.addField(moBoolRetryCancel);

        moFields.setFormButton(jbSave);

        moPanelDps.setPanelSettings(miClient);
        moPanelDps.enableShowCardex();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAnnulCancel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jpContainer;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRetryCancel;
    private sba.mod.trn.form.DPanelDps moPanelDps;
    private sba.lib.gui.bean.DBeanFieldRadio moRadDisable;
    private sba.lib.gui.bean.DBeanFieldRadio moRadDisableCancel;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods:
     */
    
    private void displayXmlSignatureProviderSettings() {
        if (moRegistry.getChildDfr() == null || moRegistry.getChildDfr().getFkXmlStatusId() <= DModSysConsts.TS_XML_ST_PEN || !moXmlSignatureProvider.isCancellation()) {
            moRadDisableCancel.setEnabled(false);
            moBoolRetryCancel.setEnabled(false);
            
            moRadDisable.setSelected(true);
        }
        else {
            moRadDisableCancel.setEnabled(true);
            moBoolRetryCancel.setEnabled(true);
            
            moRadDisableCancel.setSelected(true);
        }
        
        moBoolRetryCancel.setSelected(false);
    }
    
    /*
     * Overriden methods:
     */
    
    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {

    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moRegistry = (DDbDps) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        // Set registry:

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            throw new Exception(DGuiConsts.ERR_MSG_FORM_EXIST_REG);
        }
        else {
            jtfRegistryKey.setText("");
        }

        setFormEditable(true);  // enable all controls before setting form values

        mtOriginalDate = moRegistry.getDate();
        mnOriginalYear = DLibTimeUtils.digestYear(mtOriginalDate)[0];

        moPanelDps.setValue(DModSysConsts.PARAM_YEAR, mnOriginalYear);
        moPanelDps.setRegistry(moRegistry);

        addAllListeners();
    }

    @Override
    public DDbDpsSigning getRegistry() throws Exception {
        return new DDbDpsSigning(); //XXX 2018-01-15 (Sergio Flores): Fix this! Improper registry type, even though it is actually obsolete!
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModConsts.CS_XSP:
                moXmlSignatureProvider = (DDbSysXmlSignatureProvider) value;
                displayXmlSignatureProviderSettings();
                break;
            default:
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        switch (type) {
            case DModSysConsts.TX_XMS_REQ_TP_CAN:
                value = moRadDisable.isSelected() ? DTrnEmissionConsts.ANNUL : DTrnEmissionConsts.ANNUL_CANCEL;
                break;
            case RETRY_CANCEL:
                value = moBoolRetryCancel.getValue();
                break;
            default:
        }
        
        return value;
    }
}
