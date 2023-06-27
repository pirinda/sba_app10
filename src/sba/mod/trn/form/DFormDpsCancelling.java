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

import cfd.ver4.DCfdVer4Consts;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Date;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanFieldRadio;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DDbDpsSigning;
import sba.mod.trn.db.DTrnAnnulParams;
import sba.mod.trn.db.DTrnDoc;
import sba.mod.trn.db.DTrnEmissionConsts;

/**
 *
 * @author Sergio Flores
 */
public class DFormDpsCancelling extends DBeanForm implements ItemListener {
    
    private DTrnDoc moDoc;
    private int mnOriginalYear;
    private Date mtOriginalDate;
    private DDbSysXmlSignatureProvider moXmlSignatureProvider;
    private DecimalFormat moAnnulReasonCodeFormat;

    /**
     * Creates new form DFormDpsCancelling
     * @param client GUI client.
     * @param title
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

        bgAnnulAction = new javax.swing.ButtonGroup();
        jpContainer = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        moPanelDps = new sba.mod.trn.form.DPanelDps();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        moRadAnnulActionDisable = new sba.lib.gui.bean.DBeanFieldRadio();
        jPanel21 = new javax.swing.JPanel();
        moRadAnnulActionDisableCancel = new sba.lib.gui.bean.DBeanFieldRadio();
        jPanel3 = new javax.swing.JPanel();
        moBoolRetryCancel = new sba.lib.gui.bean.DBeanFieldBoolean();
        jPanel1 = new javax.swing.JPanel();
        jlAnnulReasonCode = new javax.swing.JLabel();
        moKeyAnnulReasonCode = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        jlAnnulRelatedUuid = new javax.swing.JLabel();
        moTextAnnulRelatedUuid = new sba.lib.gui.bean.DBeanFieldText();

        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del comprobante:"));
        jPanel6.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel6.add(moPanelDps, java.awt.BorderLayout.LINE_START);

        jpContainer.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la cancelación:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgAnnulAction.add(moRadAnnulActionDisable);
        moRadAnnulActionDisable.setText("ANULAR comprobante: sólo en el sistema");
        moRadAnnulActionDisable.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel8.add(moRadAnnulActionDisable);

        jPanel5.add(jPanel8);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgAnnulAction.add(moRadAnnulActionDisableCancel);
        moRadAnnulActionDisableCancel.setText("ANULAR y CANCELAR comprobante: tanto en el sistema como ante la autoridad");
        moRadAnnulActionDisableCancel.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel21.add(moRadAnnulActionDisableCancel);

        jPanel5.add(jPanel21);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolRetryCancel.setText("Reintentar cancelar nuevamente aún si el receptor rechazó la solicitud de cancelación anterior");
        moBoolRetryCancel.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel3.add(moBoolRetryCancel);

        jPanel5.add(jPanel3);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnulReasonCode.setText("Motivo:*");
        jlAnnulReasonCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlAnnulReasonCode);

        moKeyAnnulReasonCode.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel1.add(moKeyAnnulReasonCode);

        jPanel5.add(jPanel1);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnnulRelatedUuid.setText("UUID relacionado:*");
        jlAnnulRelatedUuid.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlAnnulRelatedUuid);

        moTextAnnulRelatedUuid.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel4.add(moTextAnnulRelatedUuid);

        jPanel5.add(jPanel4);

        jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jpContainer.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);

        moRadAnnulActionDisable.setBooleanSettings(moRadAnnulActionDisable.getText(), false);
        moRadAnnulActionDisableCancel.setBooleanSettings(moRadAnnulActionDisableCancel.getText(), false);
        moBoolRetryCancel.setBooleanSettings(moBoolRetryCancel.getText(), false);
        moKeyAnnulReasonCode.setKeySettings(miClient, DGuiUtils.getLabelName(jlAnnulReasonCode), true);
        moTextAnnulRelatedUuid.setTextSettings(DGuiUtils.getLabelName(jlAnnulRelatedUuid), DCfdVer4Consts.LEN_UUID, DCfdVer4Consts.LEN_UUID);

        moFields.addField(moRadAnnulActionDisable);
        moFields.addField(moRadAnnulActionDisableCancel);
        moFields.addField(moBoolRetryCancel);
        moFields.addField(moKeyAnnulReasonCode);
        moFields.addField(moTextAnnulRelatedUuid);

        moFields.setFormButton(jbSave);

        moPanelDps.setPanelSettings(miClient);
        moPanelDps.enableShowCardex();
        
        moAnnulReasonCodeFormat = new DecimalFormat("00");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAnnulAction;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlAnnulReasonCode;
    private javax.swing.JLabel jlAnnulRelatedUuid;
    private javax.swing.JPanel jpContainer;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRetryCancel;
    private sba.lib.gui.bean.DBeanFieldKey moKeyAnnulReasonCode;
    private sba.mod.trn.form.DPanelDps moPanelDps;
    private sba.lib.gui.bean.DBeanFieldRadio moRadAnnulActionDisable;
    private sba.lib.gui.bean.DBeanFieldRadio moRadAnnulActionDisableCancel;
    private sba.lib.gui.bean.DBeanFieldText moTextAnnulRelatedUuid;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods:
     */
    
    private void displayXmlSignatureProviderSettings() {
        if (moDoc.getDfr() == null || moDoc.getDfr().getFkXmlStatusId() <= DModSysConsts.TS_XML_ST_PEN || !moXmlSignatureProvider.isCancellation()) {
            moRadAnnulActionDisableCancel.setEnabled(false);
        }
        else {
            moRadAnnulActionDisableCancel.setEnabled(true);
        }
        
        moRadAnnulActionDisable.setSelected(true);
        itemStateChangedDisableCancel();
    }
    
    private void itemStateChangedDisableCancel() {
        boolean isCancel = moRadAnnulActionDisableCancel.isSelected();
        
        moBoolRetryCancel.resetField();
        moBoolRetryCancel.setEnabled(isCancel);
        
        moKeyAnnulReasonCode.resetField();
        moKeyAnnulReasonCode.setEnabled(isCancel);
        
        itemStateChangedAnnulReasonCode();
    }
    
    private void itemStateChangedAnnulReasonCode() {
        moTextAnnulRelatedUuid.resetField();
        moTextAnnulRelatedUuid.setEnabled(DLibUtils.compareKeys(moKeyAnnulReasonCode.getValue(), new int[] { DLibUtils.parseInt(DCfdVer4Consts.CAN_MOTIVO_ERROR_CON_REL) }));
    }
    
    /*
     * Overriden methods:
     */
    
    @Override
    public void addAllListeners() {
        moRadAnnulActionDisable.addItemListener(this);
        moRadAnnulActionDisableCancel.addItemListener(this);
        moKeyAnnulReasonCode.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moRadAnnulActionDisable.removeItemListener(this);
        moRadAnnulActionDisableCancel.removeItemListener(this);
        moKeyAnnulReasonCode.removeItemListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadCatalogues() {
        moKeyAnnulReasonCode.removeAllItems();
        moKeyAnnulReasonCode.addItem(new DGuiItem("- " + DGuiUtils.getLabelName(jlAnnulReasonCode) + " -"));
        for (String code : DCfdVer4Consts.CancelaciónMotivos.keySet()) {
            moKeyAnnulReasonCode.addItem(new DGuiItem(new int[] { DLibUtils.parseInt(code) }, code + " - " + DCfdVer4Consts.CancelaciónMotivos.get(code)));
        }
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moDoc = (DTrnDoc) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        // Set registry:

        removeAllListeners();
        reloadCatalogues();

        if (moDoc != null && ((DDbRegistry) moDoc).isRegistryNew()) {
            throw new Exception(DGuiConsts.ERR_MSG_FORM_EXIST_REG);
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(((DDbRegistry) moDoc).getPrimaryKey()));
        }

        setFormEditable(true);  // enable all controls before setting form values

        mtOriginalDate = moDoc.getDocDate();
        mnOriginalYear = DLibTimeUtils.digestYear(mtOriginalDate)[0];

        moPanelDps.setValue(DModSysConsts.PARAM_YEAR, mnOriginalYear);
        moPanelDps.setRegistry(moDoc instanceof DDbDps ? (DDbDps) moDoc : null);

        addAllListeners();
    }

    @Override
    public DDbDpsSigning getRegistry() throws Exception {
        return new DDbDpsSigning(); // a dummy registry!
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }

    /**
     * Must be invoked afger method setRegistry().
     * @param type
     * @param value 
     */
    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModConsts.CS_XSP:
                moXmlSignatureProvider = (DDbSysXmlSignatureProvider) value;
                displayXmlSignatureProviderSettings();
                break;
            default:
                // do nothing
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        switch (type) {
            case DModConsts.TX_DFR_ANNUL_PARAMS:
                value = new DTrnAnnulParams(
                        moRadAnnulActionDisable.isSelected() ? DTrnEmissionConsts.ACTION_ANNUL : DTrnEmissionConsts.ACTION_ANNUL_CANCEL,
                        moBoolRetryCancel.getValue(),
                        moKeyAnnulReasonCode.getSelectedIndex() <= 0 ? "" : moAnnulReasonCodeFormat.format(moKeyAnnulReasonCode.getValue()[0]),
                        moTextAnnulRelatedUuid.getValue(),
                        false);
                break;
            default:
                // do nothing
        }
        
        return value;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldRadio && e.getStateChange() == ItemEvent.SELECTED) {
            DBeanFieldRadio field = (DBeanFieldRadio) e.getSource();
            
            if (field == moRadAnnulActionDisable || field == moRadAnnulActionDisableCancel) {
                itemStateChangedDisableCancel();
            }
        }
        else if (e.getSource() instanceof DBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
            DBeanFieldKey field = (DBeanFieldKey) e.getSource();
            
            if (field == moKeyAnnulReasonCode) {
                itemStateChangedAnnulReasonCode();
            }
        }
    }
}
