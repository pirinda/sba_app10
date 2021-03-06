/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogUserBranch.java
 *
 * Created on 25/09/2011, 05:20:14 PM
 */

package sba.mod.cfg.form;

import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.cfg.db.DDbUserBranch;

/**
 *
 * @author Sergio Flores
 */
public class DDialogUserBranch extends DBeanFormDialog {

    private DDbUserBranch moRegistry;

    /** Creates new form DDialogUserBranch */
    public DDialogUserBranch(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.CU_USR_BRA, DLibConsts.UNDEFINED, title);
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlBranch = new javax.swing.JLabel();
        moKeyBranch = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel4 = new javax.swing.JPanel();
        moBoolUniversalCash = new sba.lib.gui.bean.DBeanFieldBoolean();
        jPanel5 = new javax.swing.JPanel();
        moBoolUniversalWarehouse = new sba.lib.gui.bean.DBeanFieldBoolean();
        jPanel6 = new javax.swing.JPanel();
        moBoolUniversalDpsSeries = new sba.lib.gui.bean.DBeanFieldBoolean();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBranch.setForeground(java.awt.Color.blue);
        jlBranch.setText("Sucursal empresa:*");
        jlBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlBranch);

        moKeyBranch.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(moKeyBranch);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolUniversalCash.setText("Acceso universal a cuentas de dinero");
        moBoolUniversalCash.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(moBoolUniversalCash);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolUniversalWarehouse.setText("Acceso universal a almacenes de bienes");
        moBoolUniversalWarehouse.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel5.add(moBoolUniversalWarehouse);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolUniversalDpsSeries.setText("Acceso universal a series de documentos");
        moBoolUniversalDpsSeries.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moBoolUniversalDpsSeries);

        jPanel2.add(jPanel6);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        moKeyBranch.setKeySettings(miClient, DGuiUtils.getLabelName(jlBranch.getText()), true);
        moBoolUniversalCash.setBooleanSettings(moBoolUniversalCash.getText(), false);
        moBoolUniversalWarehouse.setBooleanSettings(moBoolUniversalWarehouse.getText(), false);
        moBoolUniversalDpsSeries.setBooleanSettings(moBoolUniversalDpsSeries.getText(), false);

        moFields.addField(moKeyBranch);
        moFields.addField(moBoolUniversalCash);
        moFields.addField(moBoolUniversalWarehouse);
        moFields.addField(moBoolUniversalDpsSeries);

        moFields.setFormButton(jbSave);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlBranch;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolUniversalCash;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolUniversalDpsSeries;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolUniversalWarehouse;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBranch;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyBranch, DModConsts.BU_BRA, DLibConsts.UNDEFINED, new DGuiParams(new int[] { DUtilConsts.BPR_CO_ID }));
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        int bizPartnerId = DLibConsts.UNDEFINED;
        int branchId = DLibConsts.UNDEFINED;

        moRegistry = (DDbUserBranch) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        bizPartnerId = moRegistry.getPkBizPartnerId();  // preserver branch key on copy
        branchId = moRegistry.getPkBranchId();          // preserver branch key on copy

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
        }
        else {

        }

        moKeyBranch.setValue(new int[] { bizPartnerId, branchId });
        moBoolUniversalCash.setValue(moRegistry.isUniversalCash());
        moBoolUniversalWarehouse.setValue(moRegistry.isUniversalWarehouse());
        moBoolUniversalDpsSeries.setValue(moRegistry.isUniversalDpsSeries());

        if (moRegistry.isRegistryNew()) {
            moKeyBranch.setEditable(true);
        }
        else {
            moKeyBranch.setEditable(false);
        }

        addAllListeners();
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        DDbUserBranch registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkBizPartnerId(moKeyBranch.getValue()[0]);
            registry.setPkBranchId(moKeyBranch.getValue()[1]);
        }

        registry.setUniversalCash(moBoolUniversalCash.getValue());
        registry.setUniversalWarehouse(moBoolUniversalWarehouse.getValue());
        registry.setUniversalDpsSeries(moBoolUniversalDpsSeries.getValue());

        registry.setXtaBranchName(((DGuiItem) moKeyBranch.getComponent().getSelectedItem()).getItem());

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
