/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormSerialNumberFix.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.trn.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbItemGenus;
import sba.mod.itm.db.DDbUnit;
import sba.mod.trn.db.DDbSerialNumberFix;

/**
 *
 * @author Sergio Flores
 */
public class DFormSerialNumberFix extends DBeanForm implements ActionListener, ItemListener {

    private DDbSerialNumberFix moRegistry;
    private DDbItem moItem;
    private DDbUnit moUnit;
    private int mnSerialNumberLength;

    /** Creates new form DFormSerialNumberFix */
    public DFormSerialNumberFix(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.T_SNR_FIX, DLibConsts.UNDEFINED, title);
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
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sba.lib.gui.bean.DBeanFieldKey();
        jbItem = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jlUnitCode = new javax.swing.JLabel();
        jtfUnitCode = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlSerialNumber = new javax.swing.JLabel();
        jtfSerialNumberName = new javax.swing.JTextField();
        jtfSerialNumberLength = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlSerialNumberOld = new javax.swing.JLabel();
        moTextSerialNumberOld = new sba.lib.gui.bean.DBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jlSerialNumberNew = new javax.swing.JLabel();
        moTextSerialNumberNew = new sba.lib.gui.bean.DBeanFieldText();

        jpContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlItem);

        moKeyItem.setMaximumRowCount(16);
        moKeyItem.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel3.add(moKeyItem);

        jbItem.setText("...");
        jbItem.setEnabled(false);
        jbItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbItem);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnitCode.setText("Unidad:");
        jlUnitCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlUnitCode);

        jtfUnitCode.setEditable(false);
        jtfUnitCode.setText("NAME");
        jtfUnitCode.setFocusable(false);
        jtfUnitCode.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jtfUnitCode);

        jPanel1.add(jPanel4);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSerialNumber.setText("Número serie:");
        jlSerialNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlSerialNumber);

        jtfSerialNumberName.setEditable(false);
        jtfSerialNumberName.setText("NAME");
        jtfSerialNumberName.setFocusable(false);
        jtfSerialNumberName.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jtfSerialNumberName);

        jtfSerialNumberLength.setEditable(false);
        jtfSerialNumberLength.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSerialNumberLength.setText("0");
        jtfSerialNumberLength.setFocusable(false);
        jtfSerialNumberLength.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel7.add(jtfSerialNumberLength);

        jPanel1.add(jPanel7);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSerialNumberOld.setText("Número serie ant.:");
        jlSerialNumberOld.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlSerialNumberOld);

        moTextSerialNumberOld.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel5.add(moTextSerialNumberOld);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSerialNumberNew.setText("Número serie nvo.:");
        jlSerialNumberNew.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlSerialNumberNew);

        moTextSerialNumberNew.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moTextSerialNumberNew);

        jPanel1.add(jPanel6);

        jpContainer.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);

        moKeyItem.setKeySettings(miClient, DGuiUtils.getLabelName(jlItem.getText()), true);
        moKeyItem.setFieldButton(jbItem);
        moTextSerialNumberOld.setTextSettings(DGuiUtils.getLabelName(jlSerialNumberOld.getText()), DDbItemGenus.LEN_SNR);
        moTextSerialNumberNew.setTextSettings(DGuiUtils.getLabelName(jlSerialNumberNew.getText()), DDbItemGenus.LEN_SNR);
        
        moFields.addField(moKeyItem);
        moFields.addField(moTextSerialNumberOld);
        moFields.addField(moTextSerialNumberNew);
        
        moFields.setFormButton(jbSave);
    }

    private int countSerialNumberMoves(String snr) {
        int count = 0;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT COUNT(*) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                    "WHERE id_itm = " + moItem.getPkItemId() + " AND id_unt = " + moUnit.getPkUnitId() + " AND " +
                    "snr = '" + snr + "' AND b_del = 0 ";
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return count;
    }

    private void actionItem() {

    }

    private void itemStateItem() {
        if (moKeyItem.getSelectedIndex() <= 0) {
            moItem = null;
            moUnit = null;
            mnSerialNumberLength = 0;

            jtfUnitCode.setText("");
            jtfSerialNumberName.setText("");
            jtfSerialNumberLength.setText("");

            moTextSerialNumberOld.setMaxLength(DDbItemGenus.LEN_SNR);
            moTextSerialNumberNew.setMaxLength(DDbItemGenus.LEN_SNR);
        }
        else {
            moItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, moKeyItem.getValue());
            moUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, new int[] { moItem.getFkUnitId() });
            mnSerialNumberLength = moItem.getParentGenus().getSerialNumberLength();

            jtfUnitCode.setText(moUnit.getName());
            jtfSerialNumberName.setText(moItem.getParentGenus().getSerialNumberName());
            jtfSerialNumberLength.setText(mnSerialNumberLength == 0 ? "" : DLibUtils.DecimalFormatInteger.format(mnSerialNumberLength));

            moTextSerialNumberOld.setMaxLength(mnSerialNumberLength == 0 ? DDbItemGenus.LEN_SNR : mnSerialNumberLength);
            moTextSerialNumberNew.setMaxLength(mnSerialNumberLength == 0 ? DDbItemGenus.LEN_SNR : mnSerialNumberLength);
        }

        jtfUnitCode.setCaretPosition(0);
        jtfSerialNumberName.setCaretPosition(0);
        jtfSerialNumberLength.setCaretPosition(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbItem;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlSerialNumber;
    private javax.swing.JLabel jlSerialNumberNew;
    private javax.swing.JLabel jlSerialNumberOld;
    private javax.swing.JLabel jlUnitCode;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JTextField jtfSerialNumberLength;
    private javax.swing.JTextField jtfSerialNumberName;
    private javax.swing.JTextField jtfUnitCode;
    private sba.lib.gui.bean.DBeanFieldKey moKeyItem;
    private sba.lib.gui.bean.DBeanFieldText moTextSerialNumberNew;
    private sba.lib.gui.bean.DBeanFieldText moTextSerialNumberOld;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {
        jbItem.addActionListener(this);
        moKeyItem.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbItem.removeActionListener(this);
        moKeyItem.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyItem, DModConsts.IU_ITM, DUtilConsts.PER_SNR, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moRegistry = (DDbSerialNumberFix) registry;

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

        moKeyItem.setValue(new int[] { moRegistry.getPkItemId() });
        itemStateItem();

        moTextSerialNumberOld.setValue(moRegistry.getSerialNumberOld());
        moTextSerialNumberNew.setValue(moRegistry.getSerialNumberNew());

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public DDbSerialNumberFix getRegistry() throws Exception {
        DDbSerialNumberFix registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkItemId(moItem.getPkItemId());
            registry.setPkUnitId(moUnit.getPkUnitId());
        }

        registry.setSerialNumberOld(moTextSerialNumberOld.getValue());
        registry.setSerialNumberNew(moTextSerialNumberNew.getValue());

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moTextSerialNumberOld.getValue().compareTo(moTextSerialNumberNew.getValue()) == 0) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlSerialNumberNew.getText()) + "'.");
                validation.setComponent(moTextSerialNumberNew);
            }
            else if (mnSerialNumberLength > 0 && mnSerialNumberLength != moTextSerialNumberOld.getValue().length()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlSerialNumberOld.getText()) + "' debe tener una longitud de " + mnSerialNumberLength + " caracteres.");
                validation.setComponent(moTextSerialNumberOld);
            }
            else if (mnSerialNumberLength > 0 && mnSerialNumberLength != moTextSerialNumberNew.getValue().length()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlSerialNumberNew.getText()) + "' debe tener una longitud de " + mnSerialNumberLength + " caracteres.");
                validation.setComponent(moTextSerialNumberNew);
            }
            else if (countSerialNumberMoves(moTextSerialNumberOld.getValue()) == 0) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlSerialNumberOld.getText()) + "' no existe.");
                validation.setComponent(moTextSerialNumberOld);
            }
            else if (countSerialNumberMoves(moTextSerialNumberNew.getValue()) > 0) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlSerialNumberNew.getText()) + "' ya existe.");
                validation.setComponent(moTextSerialNumberNew);
            }
            else if (miClient.showMsgBoxConfirm(DGuiConsts.MSG_CHG_PERMANENT + "\n" + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlItem.getText()) + "' o para otros campos.");
                validation.setComponent(moKeyItem);
            }
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbItem) {
                actionItem();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey) {
            DBeanFieldKey field = (DBeanFieldKey) e.getSource();

            if (field == moKeyItem) {
                itemStateItem();
            }
        }
    }
}
