/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormCommissionCalc.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.mkt.form;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.mkt.db.DDbCommissionCalc;
import sba.mod.mkt.db.DRowCommissionCalcDetail;
import sba.mod.mkt.db.DRowCommissionCalc;
import sba.mod.mkt.db.DMktUtils;

/**
 *
 * @author Sergio Flores
 */
public class DFormCommissionCalc extends DBeanForm implements ActionListener {

    private DDbCommissionCalc moRegistry;
    private DGridPaneForm moGridCalc;
    private DGridPaneForm moGridCalcDetail;
    private boolean mbCalculationComputed;

    /** Creates new form DFormCommissionCalc */
    public DFormCommissionCalc(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.M_CMM_CAL, DLibConsts.UNDEFINED, title);
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

        jbgGrid = new javax.swing.ButtonGroup();
        jpSettings = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sba.lib.gui.bean.DBeanFieldDate();
        jlDummy = new javax.swing.JLabel();
        moBoolAudited = new sba.lib.gui.bean.DBeanFieldBoolean();
        jPanel5 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateEnd = new sba.lib.gui.bean.DBeanFieldDate();
        jPanel9 = new javax.swing.JPanel();
        jlCalcType = new javax.swing.JLabel();
        moKeyCalcType = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlValueType = new javax.swing.JLabel();
        moKeyValueType = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jbCalculate = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jtbViewCalc = new javax.swing.JToggleButton();
        jtbViewCalcDetail = new javax.swing.JToggleButton();
        jpCommission = new javax.swing.JPanel();

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de las comisiones:"));
        jpSettings.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDateStart);
        jPanel4.add(moDateStart);

        jlDummy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDummy);

        moBoolAudited.setText("Auditado");
        jPanel4.add(moBoolAudited);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlDateEnd);
        jPanel5.add(moDateEnd);

        jPanel2.add(jPanel5);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCalcType.setText("Tipo cálculo:*");
        jlCalcType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlCalcType);

        moKeyCalcType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel9.add(moKeyCalcType);

        jPanel2.add(jPanel9);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlValueType.setText("Tipo valor base:*");
        jlValueType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlValueType);

        moKeyValueType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(moKeyValueType);

        jPanel2.add(jPanel6);

        jpSettings.add(jPanel2);

        jPanel7.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbCalculate.setText("Calcular");
        jbCalculate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jbCalculate);

        jPanel7.add(jPanel8);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel7.add(jPanel1);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel7.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbgGrid.add(jtbViewCalc);
        jtbViewCalc.setText("Ver resumen");
        jtbViewCalc.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jtbViewCalc);

        jbgGrid.add(jtbViewCalcDetail);
        jtbViewCalcDetail.setText("Ver detalle");
        jtbViewCalcDetail.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jtbViewCalcDetail);

        jPanel7.add(jPanel11);

        jpSettings.add(jPanel7);

        getContentPane().add(jpSettings, java.awt.BorderLayout.NORTH);

        jpCommission.setBorder(javax.swing.BorderFactory.createTitledBorder("Comisiones:"));
        jpCommission.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpCommission, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 960, 600);

        moDateStart.setDateSettings(miClient, DGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateEnd.setDateSettings(miClient, DGuiUtils.getLabelName(jlDateEnd.getText()), true);
        moBoolAudited.setBooleanSettings(moBoolAudited.getText(), false);
        moKeyCalcType.setKeySettings(miClient, DGuiUtils.getLabelName(jlCalcType.getText()), true);
        moKeyValueType.setKeySettings(miClient, DGuiUtils.getLabelName(jlValueType.getText()), true);
        
        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);
        moFields.addField(moBoolAudited);
        moFields.addField(moKeyCalcType);
        moFields.addField(moKeyValueType);
        
        moFields.setFormButton(jbSave);

        moGridCalc = new DGridPaneForm(miClient, DModConsts.M_CMM_CAL_ROW, DModConsts.MX_CMM_CAL_ROW, "Comisiones (resumen)") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[10];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_USR, "Comisionista");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad dec.");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad neta");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto dec. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto neto $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Base c/com. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Base s/com. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Comisión $ M");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        moGridCalcDetail = new DGridPaneForm(miClient, DModConsts.M_CMM_CAL_ROW, DModConsts.MX_CMM_CAL_ROW_DET, "Comisiones (detalle)") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[13];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_USR, "Comisionista");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_ITM_S, DGridConsts.COL_TITLE_NAME + " ítem");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DGridConsts.COL_TITLE_CODE + " ítem");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad dec.");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad neta");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto dec. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Monto neto $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Base c/com. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Base s/com. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Comisión $ M");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        mvFormGrids.add(moGridCalc);
        mvFormGrids.add(moGridCalcDetail);
    }
    
    private DGuiValidation validateCalculationSettings() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = DGuiUtils.validateDateRange(moDateStart, moDateEnd);
        }

        return validation;
    }

    private void actionCalculate() {
        Cursor cursor = null;

        if (DGuiUtils.computeValidation(miClient, validateCalculationSettings())) {
            if (miClient.showMsgBoxConfirm(DGuiConsts.MSG_PROC_WAIT + "\n" + DGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                try {
                    cursor = getCursor();
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    moRegistry = DMktUtils.computeCommissionCalcUser(miClient.getSession(), moDateStart.getValue(), moDateEnd.getValue(), moKeyValueType.getValue()[0]);

                    if (jtbViewCalc.isSelected()) {
                        actionViewCalc();
                    }
                    else {
                        jtbViewCalc.setSelected(true);  // this will trigger method actionViewCalc()
                    }

                    mbCalculationComputed = true;
                    setFormEditable(false);
                }
                catch (SQLException e) {
                    DLibUtils.showException(this, e);
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
                finally {
                    setCursor(cursor);
                }
            }
        }
    }

    private void actionViewCalc() {
        String sql = "";
        ResultSet resultSet = null;
        Vector<DGridRow> rows = new Vector<>();

        try {
            moGridCalc.clearGridRows();

            switch (moRegistry.getFkCommissionCalcTypeId()) {
                case DModSysConsts.MS_CMM_CAL_TP_AGT:
                    sql = "";
                    break;
                case DModSysConsts.MS_CMM_CAL_TP_USR:
                    sql = "SELECT u.id_usr AS f_own_id, u.name AS f_own, " +
                            "SUM(cr.qty) AS f_qty, SUM(cr.qty_inc) AS f_qty_inc, SUM(cr.qty_dec) AS f_qty_dec, " +
                            "SUM(cr.val) AS f_val, SUM(cr.val_inc) AS f_val_inc, SUM(cr.val_dec) AS f_val_dec, " +
                            "SUM(CASE WHEN cr.b_fre_cmm THEN 0 ELSE cr.val_net_r END) AS f_base, " +
                            "SUM(CASE WHEN NOT cr.b_fre_cmm THEN 0 ELSE cr.val_net_r END) AS f_base_free, " +
                            "SUM(cr.cmm_r) AS f_cmm " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_ROW) + " AS cr " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u ON cr.id_ref = u.id_usr " +
                            "WHERE cr.id_cmm = " + moRegistry.getPkCommissionCalcId() + " " +
                            "GROUP BY u.id_usr, u.name " +
                            "ORDER BY u.name, u.id_usr ";
                    break;
                default:
            }

            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                DRowCommissionCalc row = new DRowCommissionCalc();
                row.setOwnerId(resultSet.getInt("f_own_id"));
                row.setOwnerName(resultSet.getString("f_own"));
                row.setQuantity(resultSet.getDouble("f_qty"));
                row.setQuantityInc(resultSet.getDouble("f_qty_inc"));
                row.setQuantityDec(resultSet.getDouble("f_qty_dec"));
                row.setValue(resultSet.getDouble("f_val"));
                row.setValueInc(resultSet.getDouble("f_val_inc"));
                row.setValueDec(resultSet.getDouble("f_val_dec"));
                row.setCommissionBase(resultSet.getDouble("f_base"));
                row.setCommissionBaseFree(resultSet.getDouble("f_base_free"));
                row.setCommission(resultSet.getDouble("f_cmm"));
                rows.add(row);
            }

            moGridCalc.populateGrid(rows);
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        jpCommission.removeAll();
        jpCommission.add(moGridCalc);
        jpCommission.validate();
        jpCommission.repaint();
    }

    private void actionViewCalcDetail() {
        String sql = "";
        ResultSet resultSet = null;
        Vector<DGridRow> rows = new Vector<>();

        try {
            moGridCalcDetail.clearGridRows();

            switch (moRegistry.getFkCommissionCalcTypeId()) {
                case DModSysConsts.MS_CMM_CAL_TP_AGT:
                    sql = "";
                    break;
                case DModSysConsts.MS_CMM_CAL_TP_USR:
                    sql = "SELECT u.id_usr AS f_own_id, u.name AS f_own, " +
                            "i.id_itm, i.code, i.name, iu.code, " +
                            "cr.qty AS f_qty, cr.qty_inc AS f_qty_inc, cr.qty_dec AS f_qty_dec, " +
                            "cr.val AS f_val, cr.val_inc AS f_val_inc, cr.val_dec AS f_val_dec, " +
                            "CASE WHEN cr.b_fre_cmm THEN 0 ELSE cr.val_net_r END AS f_base, " +
                            "CASE WHEN NOT cr.b_fre_cmm THEN 0 ELSE cr.val_net_r END AS f_base_free, " +
                            "cr.cmm_r AS f_cmm " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_ROW) + " AS cr " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u ON cr.id_ref = u.id_usr " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON cr.fk_itm = i.id_itm " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS iu ON i.fk_unt = iu.id_unt " +
                            "WHERE cr.id_cmm = " + moRegistry.getPkCommissionCalcId() + " " +
                            "ORDER BY u.name, u.id_usr, i.name, i.code, i.id_itm ";
                    break;
                default:
            }

            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                DRowCommissionCalcDetail row = new DRowCommissionCalcDetail();
                row.setOwnerId(resultSet.getInt("f_own_id"));
                row.setOwnerName(resultSet.getString("f_own"));
                row.setItemCode(resultSet.getString("i.code"));
                row.setItemName(resultSet.getString("i.name"));
                row.setUnitCode(resultSet.getString("iu.code"));
                row.setQuantity(resultSet.getDouble("f_qty"));
                row.setQuantityInc(resultSet.getDouble("f_qty_inc"));
                row.setQuantityDec(resultSet.getDouble("f_qty_dec"));
                row.setValue(resultSet.getDouble("f_val"));
                row.setValueInc(resultSet.getDouble("f_val_inc"));
                row.setValueDec(resultSet.getDouble("f_val_dec"));
                row.setCommissionBase(resultSet.getDouble("f_base"));
                row.setCommissionBaseFree(resultSet.getDouble("f_base_free"));
                row.setCommission(resultSet.getDouble("f_cmm"));
                rows.add(row);
            }

            moGridCalcDetail.populateGrid(rows);
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        jpCommission.removeAll();
        jpCommission.add(moGridCalcDetail);
        jpCommission.validate();
        jpCommission.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCalculate;
    private javax.swing.ButtonGroup jbgGrid;
    private javax.swing.JLabel jlCalcType;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlValueType;
    private javax.swing.JPanel jpCommission;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JToggleButton jtbViewCalc;
    private javax.swing.JToggleButton jtbViewCalcDetail;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolAudited;
    private sba.lib.gui.bean.DBeanFieldDate moDateEnd;
    private sba.lib.gui.bean.DBeanFieldDate moDateStart;
    private sba.lib.gui.bean.DBeanFieldKey moKeyCalcType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyValueType;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionSave() {
        miClient.showMsgBoxWarning("Para guardar las comisiones es necesario calcularlas.");
        jbCalculate.requestFocus();
    }

    @Override
    public void actionCancel() {
        if (mbCalculationComputed) {
            mnFormResult = DGuiConsts.FORM_RESULT_OK;
            dispose();
        }
        else {
            super.actionCancel();
        }
    }

    @Override
    public void updateFormControlStatus() {
        if (mnFormStatus == DGuiConsts.FORM_STATUS_EDIT) {
            jbCalculate.setEnabled(!moRegistry.isAudited());
        }
        else {
            jbCalculate.setEnabled(false);
        }
    }

    @Override
    public void addAllListeners() {
        jbCalculate.addActionListener(this);
        jtbViewCalc.addActionListener(this);
        jtbViewCalcDetail.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbCalculate.removeActionListener(this);
        jtbViewCalc.removeActionListener(this);
        jtbViewCalcDetail.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyCalcType, DModConsts.MS_CMM_CAL_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyValueType, DModConsts.MS_CMM_VAL_TP, DLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moRegistry = (DDbCommissionCalc) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        mbCalculationComputed = false;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();

            moRegistry.setDateStart(DLibTimeUtils.getBeginOfMonth(miClient.getSession().getWorkingDate()));
            moRegistry.setDateEnd(DLibTimeUtils.getEndOfMonth(miClient.getSession().getWorkingDate()));
            moRegistry.setAudited(false);
            moRegistry.setFkCommissionCalcTypeId(DModSysConsts.MS_CMM_CAL_TP_USR);
            moRegistry.setFkCommissionValueTypeId(DModSysConsts.MS_CMM_VAL_TP_TOT); // XXX Improve this by system configuration!!!

            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moDateStart.setValue(moRegistry.getDateStart());
        moDateEnd.setValue(moRegistry.getDateEnd());
        moBoolAudited.setValue(moRegistry.isAudited());
        moKeyCalcType.setValue(new int[] { moRegistry.getFkCommissionCalcTypeId() });
        moKeyValueType.setValue(new int[] { moRegistry.getFkCommissionValueTypeId() });

        setFormEditable(moRegistry.isRegistryNew());

        moKeyCalcType.setEnabled(false);
        moBoolAudited.setEnabled(false);

        jtbViewCalc.setSelected(true);
        actionViewCalc();

        addAllListeners();
    }

    @Override
    public DDbCommissionCalc getRegistry() throws Exception {
        DDbCommissionCalc registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setDateStart(moDateStart.getValue());
        registry.setDateEnd(moDateEnd.getValue());
        registry.setFkCommissionCalcTypeId(moKeyCalcType.getValue()[0]);
        registry.setFkCommissionValueTypeId(moKeyValueType.getValue()[0]);

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = validateCalculationSettings();

        if (validation.isValid()) {
            if (isFormDataEdited()) {
                validation.setComponent(jbCalculate);
                validation.setMessage("Hay cambios en la sección '" + ((TitledBorder) jpSettings.getBorder()).getTitle() + "'.\n" +
                        "Para guardar las comisiones es necesario calcularlas.");
            }
        }

        return validation;
    }

    @Override
    public boolean isFormDataEdited() {
        return
                !moRegistry.getDateStart().equals(moDateStart.getValue()) ||
                !moRegistry.getDateEnd().equals(moDateEnd.getValue()) ||
                moRegistry.getFkCommissionCalcTypeId() != moKeyCalcType.getValue()[0] ||
                moRegistry.getFkCommissionValueTypeId() != moKeyValueType.getValue()[0];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCalculate) {
                actionCalculate();
            }
        }
        else if (e.getSource() instanceof JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbViewCalc) {
                actionViewCalc();
            }
            else if (toggleButton == jtbViewCalcDetail) {
                actionViewCalcDetail();
            }
        }
    }
}
