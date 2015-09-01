/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormCashPayment.java
 *
 * Created on 13/12/2011, 07:25:39 AM
 */

package sba.mod.fin.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.cfg.db.DDbBranchCash;
import sba.mod.fin.db.DDbBookkeepingMoveCustom;
import sba.mod.fin.db.DFinConsts;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DFormCashPayment extends DBeanForm implements ItemListener {

    private int mnYear;
    private int[] manSystemMoveTypeKey;
    private int[] manDpsKey;
    private DDbDps moDps;

    /** Creates new form DFormCashPayment
     * @param type Class of system move. Constants defined in DModSysConts (FS_SYS_MOV_CL_M...).
     * @param subtype Class of business partner. Constants defined in DModSysConts (BS_BPR_CL_...).
     */
    public DFormCashPayment(DGuiClient client, int type, int subtype, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, type, subtype, title);

        switch (type) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                manSystemMoveTypeKey = subtype == DModSysConsts.BS_BPR_CL_VEN ? DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY : DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY;
                break;
            case DModSysConsts.FS_SYS_MOV_CL_MO:
                manSystemMoveTypeKey = subtype == DModSysConsts.BS_BPR_CL_VEN ? DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY : DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY;
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        moKeyBizPartner = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlDps = new javax.swing.JLabel();
        moKeyDps = new sba.lib.gui.bean.DBeanFieldKey();
        moPanelDps = new sba.mod.trn.form.DPanelDps();
        jPanel2 = new javax.swing.JPanel();
        moPanelPayment = new sba.mod.fin.form.DPanelPayment();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel3.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("[Asociado]:*");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jlBizPartner);

        moKeyBizPartner.setPreferredSize(new java.awt.Dimension(310, 23));
        jPanel5.add(moKeyBizPartner);

        jPanel4.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDps.setText("Documento:");
        jlDps.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jlDps);

        moKeyDps.setPreferredSize(new java.awt.Dimension(310, 23));
        jPanel6.add(moKeyDps);

        jPanel4.add(jPanel6);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);
        jPanel3.add(moPanelDps, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del pago:"));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(moPanelPayment, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 800, 500);

        moKeyBizPartner.setKeySettings(miClient, DGuiUtils.getLabelName(jlBizPartner.getText()), true);
        moKeyDps.setKeySettings(miClient, DGuiUtils.getLabelName(jlDps.getText()), false);

        moFields.addField(moKeyBizPartner);
        moFields.addField(moKeyDps);

        moFields.setFormButton(jbSave);

        moPanelDps.setPanelSettings(miClient);
        moPanelDps.enableShowCardex();

        moPanelPayment.setPanelSettings(miClient);
        moPanelPayment.getFields().setFormButton(jbSave);
        moPanelPayment.resetPanel();
        moPanelPayment.setSystemMoveType(manSystemMoveTypeKey, null);

        jlBizPartner.setText(DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype)) + ":*");
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;

            super.windowActivated();

            moPanelPayment.getFieldFirst().getComponent().requestFocus();
        }
    }

    private void renderDps() {
        removeAllListeners();
        reloadCatalogues();

        try {
            if (manDpsKey == null) {
                moKeyBizPartner.setEnabled(true);
                moKeyDps.setEnabled(true);
            }
            else {
                moKeyBizPartner.setEnabled(false);
                moKeyDps.setEnabled(false);

                moDps = (DDbDps) miClient.getSession().readRegistry(DModConsts.T_DPS, manDpsKey);

                moPanelDps.setValue(DModSysConsts.PARAM_YEAR, mnYear);
                moPanelDps.setRegistry(moDps);

                moPanelPayment.setValue(DModSysConsts.PARAM_YEAR, mnYear);
                moPanelPayment.setDps(moDps);
                moPanelPayment.setPayment1Cy((Double) moPanelDps.getValue(DUtilConsts.BAL_CY));
            }

            moKeyBizPartner.resetField();
            moKeyDps.resetField();

            itemStateKeyBizPartner();
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        addAllListeners();
    }

    private void itemStateKeyBizPartner() {
        DGuiParams params = new DGuiParams();

        if (moKeyBizPartner.getSelectedIndex() <= 0) {
            moKeyDps.setEnabled(false);
            moKeyDps.removeAllItems();
        }
        else {
            params.getParamsMap().put(DModSysConsts.PARAM_YEAR, mnYear);
            params.getParamsMap().put(DModSysConsts.PARAM_BPR_CL, mnFormSubtype);
            params.getParamsMap().put(DModSysConsts.PARAM_BPR, moKeyBizPartner.getValue()[0]);
            params.getParamsMap().put(DModSysConsts.PARAM_CUR, DLibConsts.UNDEFINED);   // redundant parameter, when omited is DLibConsts.UNDEFINED
            params.getParamsMap().put(DModSysConsts.PARAM_BAL, DUtilConsts.BAL);        // document's absolute balance

            miClient.getSession().populateCatalogue(moKeyDps, DModConsts.TX_ACC_PAY, DLibConsts.UNDEFINED, params);
            moKeyDps.setEnabled(true);
        }
    }

    private void itemStateKeyDps() {
        try {
            if (moKeyDps.getSelectedIndex() <= 0) {
                moDps = null;
            }
            else {
                moDps = (DDbDps) miClient.getSession().readRegistry(DModConsts.T_DPS, moKeyDps.getValue());
            }

            moPanelDps.setValue(DModSysConsts.PARAM_YEAR, mnYear);
            moPanelDps.setRegistry(moDps);

            if (moDps != null) {
                moPanelPayment.setDps(moDps.getPrimaryKey());
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlDps;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBizPartner;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDps;
    private sba.mod.trn.form.DPanelDps moPanelDps;
    private sba.mod.fin.form.DPanelPayment moPanelPayment;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {
        moKeyBizPartner.addItemListener(this);
        moKeyDps.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyBizPartner.removeItemListener(this);
        moKeyDps.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyBizPartner, DModConsts.BU_BPR, mnFormSubtype, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        DDbBranchCash branchCash = null;
        DGuiClientSessionCustom sessionCustom = (DGuiClientSessionCustom) miClient.getSession().getSessionCustom();

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        // Validate if new registry can be created:

        if (sessionCustom.getBranchKey() == null) {
            mbCanShowForm = false;
            msCanShowFormMessage = DUtilConsts.ERR_MSG_USR_SES_BRA;
            return;
        }
        else if (sessionCustom.getBranchCashKey() == null) {
            mbCanShowForm = false;
            msCanShowFormMessage = DUtilConsts.ERR_MSG_USR_SES_BRA_CSH;
            return;
        }

        // Set registry:

        removeAllListeners();
        reloadCatalogues();

        jtfRegistryKey.setText("");

        mnYear = DLibTimeUtils.digestYear(miClient.getSession().getWorkingDate())[0];
        branchCash = (DDbBranchCash) miClient.getSession().readRegistry(DModConsts.CU_CSH, sessionCustom.getBranchCashKey());

        setFormEditable(true);  // enable all controls before setting form values

        moFields.resetFields();

        moPanelDps.setRegistry(null);

        moPanelPayment.resetPanel();
        moPanelPayment.setSystemMoveType(manSystemMoveTypeKey, branchCash);

        addAllListeners();
    }

    @Override
    public DDbBookkeepingMoveCustom getRegistry() throws Exception {
        return moPanelPayment.getRegistry();
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = moPanelPayment.validatePanel();

            if (validation.isValid()) {
                if (((Double) moPanelPayment.getValue(DUtilConsts.AMT_CY)).compareTo((Double) moPanelDps.getValue(DUtilConsts.BAL_CY)) > 0) {
                    validation.setMessage("El " + DFinConsts.getSysMovePaymentName(manSystemMoveTypeKey[0]).toLowerCase() + " debe ser menor o igual al saldo del documento.");
                }
            }
        }

        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModSysConsts.PARAM_DPS:
                manDpsKey = (int[]) value;
                renderDps();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                DBeanFieldKey field = (DBeanFieldKey) e.getSource();

                if (field == moKeyBizPartner) {
                    itemStateKeyBizPartner();
                }
                else if (field == moKeyDps) {
                    itemStateKeyDps();
                }
            }
        }
    }
}
