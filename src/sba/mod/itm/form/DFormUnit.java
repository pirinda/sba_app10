/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormUnit.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.itm.form;

import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.itm.db.DDbUnit;

/**
 *
 * @author Sergio Flores
 */
public class DFormUnit extends DBeanForm {

    private DDbUnit moRegistry;

    /** Creates new form DFormUnit */
    public DFormUnit(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.IU_UNT, DLibConsts.UNDEFINED, title);
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

        jpContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sba.lib.gui.bean.DBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sba.lib.gui.bean.DBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlCfdUnitKey = new javax.swing.JLabel();
        moTextCfdUnitKey = new sba.lib.gui.bean.DBeanFieldText();
        jlCfdUnitKeyHint = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jlRatioKg = new javax.swing.JLabel();
        moDecRatioKg = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlRatioKgHing = new javax.swing.JLabel();

        jpContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setForeground(new java.awt.Color(0, 102, 102));
        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlCode);

        moTextCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(moTextCode);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel4.add(moTextName);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdUnitKey.setText("Clave Unidad:*");
        jlCfdUnitKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlCfdUnitKey);

        moTextCfdUnitKey.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel5.add(moTextCfdUnitKey);

        jlCfdUnitKeyHint.setForeground(java.awt.Color.gray);
        jlCfdUnitKeyHint.setText("(SAT)");
        jlCfdUnitKeyHint.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel5.add(jlCfdUnitKeyHint);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRatioKg.setText("Equivalencia kg:");
        jlRatioKg.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlRatioKg);
        jPanel6.add(moDecRatioKg);

        jlRatioKgHing.setForeground(java.awt.Color.gray);
        jlRatioKgHing.setText("(Razón de conversión a kg, si aplica)");
        jlRatioKgHing.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel6.add(jlRatioKgHing);

        jPanel1.add(jPanel6);

        jpContainer.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 480, 300);
        
        moTextCode.setTextSettings(DGuiUtils.getLabelName(jlCode), 10);
        moTextCode.setTextCaseType(DLibConsts.UNDEFINED);
        moTextName.setTextSettings(DGuiUtils.getLabelName(jlName), 50);
        moTextCfdUnitKey.setTextSettings(DGuiUtils.getLabelName(jlCfdUnitKey), 3);
        moDecRatioKg.setDecimalSettings(DGuiUtils.getLabelName(jlRatioKg), DGuiConsts.GUI_TYPE_DEC, false);
        
        moFields.addField(moTextCode);
        moFields.addField(moTextName);
        moFields.addField(moTextCfdUnitKey);
        moFields.addField(moDecRatioKg);
        
        moFields.setFormButton(jbSave);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlCfdUnitKey;
    private javax.swing.JLabel jlCfdUnitKeyHint;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlRatioKg;
    private javax.swing.JLabel jlRatioKgHing;
    private javax.swing.JPanel jpContainer;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRatioKg;
    private sba.lib.gui.bean.DBeanFieldText moTextCfdUnitKey;
    private sba.lib.gui.bean.DBeanFieldText moTextCode;
    private sba.lib.gui.bean.DBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

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
        moRegistry = (DDbUnit) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextCode.setValue(moRegistry.getCode());
        moTextName.setValue(moRegistry.getName());
        moTextCfdUnitKey.setValue(moRegistry.getCfdUnitKey());
        moDecRatioKg.setValue(moDecRatioKg.getValue());

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public DDbUnit getRegistry() throws Exception {
        DDbUnit registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());
        registry.setRatioKg(moDecRatioKg.getValue());
        registry.setCfdUnitKey(moTextCfdUnitKey.getValue());

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
