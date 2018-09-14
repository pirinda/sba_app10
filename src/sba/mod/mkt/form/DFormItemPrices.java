/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormItemPrices.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.mkt.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.JToggleButton;
import sba.gui.mygrid.DMyGridFilterSwitchTax;
import sba.gui.mygrid.cell.DMyGridCellRendererItemPrices;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.grid.DGridUtils;
import sba.lib.grid.cell.DGridCellRendererNumber;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.fin.db.DDbTaxGroupConfigRow;
import sba.mod.mkt.db.DDbAgentType;
import sba.mod.mkt.db.DRowItemPrices;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DFormItemPrices extends DBeanFormDialog implements ActionListener {

    private static final int COL_LPP = 4;

    private DDbTaxGroupConfigRow moTaxGroupConfigRow;
    private DMyGridCellRendererItemPrices moCellRendererItemPrices;
    private DGridPaneForm moGridItemPrices;
    private String[] masPriceLists;
    private String[] masPriceColumns;

    /** Creates new form DFormItemPrices */
    public DFormItemPrices(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.MX_ITM_PRC, DLibConsts.UNDEFINED, title);
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

        jpSettings = new javax.swing.JPanel();
        jtbTaxIncluded = new javax.swing.JToggleButton();
        jtfTaxRate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jpContainer = new javax.swing.JPanel();

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de precios:"));
        jpSettings.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtbTaxIncluded.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/swi_tax_off.gif"))); // NOI18N
        jtbTaxIncluded.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbTaxIncluded.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/swi_tax_on.gif"))); // NOI18N
        jpSettings.add(jtbTaxIncluded);

        jtfTaxRate.setEditable(false);
        jtfTaxRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRate.setText("0.0000");
        jtfTaxRate.setToolTipText("Razón impuestos");
        jtfTaxRate.setFocusable(false);
        jtfTaxRate.setPreferredSize(new java.awt.Dimension(60, 23));
        jpSettings.add(jtfTaxRate);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Precio = cero:");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 23));
        jpSettings.add(jLabel1);

        jTextField1.setBackground(new java.awt.Color(255, 255, 0));
        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField1.setText("0.0000");
        jTextField1.setFocusable(false);
        jTextField1.setPreferredSize(new java.awt.Dimension(50, 23));
        jpSettings.add(jTextField1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Precio <= últ. compra:");
        jLabel2.setPreferredSize(new java.awt.Dimension(115, 23));
        jpSettings.add(jLabel2);

        jTextField2.setBackground(new java.awt.Color(255, 153, 0));
        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField2.setText("0.0000");
        jTextField2.setFocusable(false);
        jTextField2.setPreferredSize(new java.awt.Dimension(50, 23));
        jpSettings.add(jTextField2);

        getContentPane().add(jpSettings, java.awt.BorderLayout.PAGE_START);

        jpContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Precios de ítems:"));
        jpContainer.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        int prices = 0;
        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

        DGuiUtils.setWindowBounds(this, 1024, 640);

        moFields.setFormButton(jbSave);

        jtbTaxIncluded.setSelected(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isTaxIncluded());
        jtbTaxIncluded.setToolTipText(jtbTaxIncluded.isSelected() ? DMyGridFilterSwitchTax.TAX_Y : DMyGridFilterSwitchTax.TAX_N);

        moTaxGroupConfigRow = DTrnUtils.getTaxGroupConfigRowDefault(miClient.getSession());
        moCellRendererItemPrices = new DMyGridCellRendererItemPrices(getDecimalFormat());

        prices = 0;
        if (configCompany.isPriceSrpApplying()) {
            prices++;
        }
        if (configCompany.isPrice1Applying()) {
            prices++;
        }
        if (configCompany.isPrice2Applying()) {
            prices++;
        }
        if (configCompany.isPrice3Applying()) {
            prices++;
        }
        if (configCompany.isPrice4Applying()) {
            prices++;
        }
        if (configCompany.isPrice5Applying()) {
            prices++;
        }

        masPriceLists = new String[prices];
        masPriceColumns = new String[prices];

        prices = 0;
        if (configCompany.isPriceSrpApplying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_SRP }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_srp";
        }
        if (configCompany.isPrice1Applying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_RET }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_1";
        }
        if (configCompany.isPrice2Applying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_HAL }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_2";
        }
        if (configCompany.isPrice3Applying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_WHO }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_3";
        }
        if (configCompany.isPrice4Applying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_ESP }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_4";
        }
        if (configCompany.isPrice5Applying()) {
            masPriceLists[prices] = DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_COS }, DDbRegistry.FIELD_NAME)) + " $ ML";
            masPriceColumns[prices++] = "prc_5";
        }

        moGridItemPrices = new DGridPaneForm(miClient, DModConsts.MX_ITM_PRC, DModConsts.IU_ITM, "Precios ítems") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm columns[] = new DGridColumnForm[5 + masPriceLists.length];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_ITM_S, DGridConsts.COL_TITLE_NAME + " ítem");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DGridConsts.COL_TITLE_CODE + " ítem");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Existencia");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Últ. compra $ ML", getGridCellRendererNumber());

                for (String priceList : masPriceLists) {
                    columns[col] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT_UNIT, priceList + " $ ML", moCellRendererItemPrices);
                    columns[col++].setEditable(true);
                }

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        jpContainer.add(moGridItemPrices, BorderLayout.CENTER);

        /* XXX Cell renderers not implemented yet in XML
        mvFormGrids.add(moGridItemPrices);
        */
    }

    private DecimalFormat getDecimalFormat() {
        return jtbTaxIncluded.isSelected() ? DLibUtils.getDecimalFormatAmount() : DLibUtils.getDecimalFormatAmountUnitary();
    }

    private DGridCellRendererNumber getGridCellRendererNumber() {
        return jtbTaxIncluded.isSelected() ? DGridUtils.getCellRendererNumberAmount() : DGridUtils.getCellRendererNumberAmountUnitary();
    }

    private void saveItemPrices() {
        int i = 0;
        int decimals = DLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        boolean saved = false;
        double[] prices = null;
        String sql = "";
        Statement statement = null;
        DRowItemPrices rowItemPrices = null;

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            statement = miClient.getSession().getStatement();

            for (DGridRow row : moGridItemPrices.getModel().getGridRows()) {
                rowItemPrices = (DRowItemPrices) row;

                if (rowItemPrices.isRowEdited()) {
                    prices = rowItemPrices.getPrices();

                    sql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " SET ";
                    for (i = 0; i < masPriceColumns.length; i++) {
                        sql += (i == 0 ? "" : ", ") + masPriceColumns[i] + " = " + DLibUtils.round(prices[i], decimals);
                    }
                    sql += " WHERE id_itm = " + rowItemPrices.getPkItemId() + " ";

                    statement.execute(sql);
                    saved = true;
                }
            }

            if (!saved) {
                miClient.showMsgBoxInformation("No se modificó ningún precio.");
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void actionTaxIncluded() {
        int index = moGridItemPrices.getTable().getSelectedRow();
        boolean taxIncluded = jtbTaxIncluded.isSelected();

        jtbTaxIncluded.setToolTipText(taxIncluded ? DMyGridFilterSwitchTax.TAX_Y : DMyGridFilterSwitchTax.TAX_N);
        moGridItemPrices.getGridColumn(COL_LPP).setCellRenderer(getGridCellRendererNumber());
        moGridItemPrices.getTable().getColumnModel().getColumn(COL_LPP).setCellRenderer(getGridCellRendererNumber());
        moCellRendererItemPrices.setNumberFormat(getDecimalFormat());

        for (DGridRow row : moGridItemPrices.getModel().getGridRows()) {
            ((DRowItemPrices) row).setTaxIncluded(taxIncluded);
        }

        moGridItemPrices.renderGridRows();
        moGridItemPrices.setSelectedGridRow(index);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JToggleButton jtbTaxIncluded;
    private javax.swing.JTextField jtfTaxRate;
    // End of variables declaration//GEN-END:variables

    public void populateItemPrices() {
        int price = 0;
        double taxRate = DTrnUtils.computeTaxRate(moTaxGroupConfigRow, DModSysConsts.TS_DPS_CT_SAL);
        double[] prices = null;
        String sql = "";
        ResultSet resultSet = null;
        DRowItemPrices row = null;
        Vector<DGridRow> gridRows = new Vector<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

        removeAllListeners();

        jtfTaxRate.setText(DLibUtils.DecimalFormatValue4D.format(taxRate));

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            sql = "SELECT i.id_itm, i.fk_unt, i.code, i.name, " +
                    "i.prc_srp, i.prc_1, i.prc_2, i.prc_3, i.prc_4, i.prc_5, " +
                    "COALESCE((SELECT dr.prc_unt " +
                    "FROM t_dps AS d, t_dps_row AS dr " +
                    "WHERE d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND " +
                    "d.fk_dps_ct = " + DModSysConsts.TS_DPS_CT_PUR + " AND dr.fk_row_itm = i.id_itm AND dr.fk_adj_ct = " + DModSysConsts.TS_ADJ_CT_NA + " " +
                    "ORDER BY d.dt DESC, d.ts_usr_upd DESC LIMIT 1), 0) AS f_lpp, " +
                    "u.id_unt, " +
                    "u.code, " +
                    "(SELECT SUM(s.mov_in - s.mov_out) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                    "WHERE s.id_itm = i.id_itm AND s.id_unt = i.fk_unt AND s.b_del = 0 AND " +
                    "s.id_yer = " + miClient.getSession().getWorkingYear() + ") AS f_stk " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON " +
                    "i.fk_unt = u.id_unt " +
                    "WHERE i.b_del = 0 AND i.b_fre_prc = 0 " +
                    "ORDER BY i.name, i.code, i.id_itm, u.code, u.id_unt ";

            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                row = new DRowItemPrices(resultSet.getInt("i.id_itm"), resultSet.getString("i.code"), resultSet.getString("i.name"));

                row.setUnitCode(resultSet.getString("u.code"));
                row.setStock(resultSet.getDouble("f_stk"));
                row.setPriceLpp(resultSet.getDouble("f_lpp"));
                row.setTaxRate(taxRate);
                row.setTaxIncluded(jtbTaxIncluded.isSelected());

                price = 0;
                prices = new double[masPriceLists.length];

                if (configCompany.isPriceSrpApplying()) {
                    prices[price++] = resultSet.getDouble("i.prc_srp");
                }
                if (configCompany.isPrice1Applying()) {
                    prices[price++] = resultSet.getDouble("i.prc_1");
                }
                if (configCompany.isPrice2Applying()) {
                    prices[price++] = resultSet.getDouble("i.prc_2");
                }
                if (configCompany.isPrice3Applying()) {
                    prices[price++] = resultSet.getDouble("i.prc_3");
                }
                if (configCompany.isPrice4Applying()) {
                    prices[price++] = resultSet.getDouble("i.prc_4");
                }
                if (configCompany.isPrice5Applying()) {
                    prices[price++] = resultSet.getDouble("i.prc_5");
                }

                row.setPrices(prices);

                gridRows.add(row);
            }

            moGridItemPrices.populateGrid(gridRows);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jtbTaxIncluded.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jtbTaxIncluded.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {

    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbAgentType getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void actionSave() {
        if (jbSave.isEnabled()) {
            if (DGuiUtils.computeValidation(miClient, validateForm())) {
                if (moGridItemPrices.getTable().isEditing()) {
                    moGridItemPrices.getTable().getCellEditor().stopCellEditing();
                }

                saveItemPrices();

                mnFormResult = DGuiConsts.FORM_RESULT_OK;
                dispose();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbTaxIncluded) {
                actionTaxIncluded();
            }
        }
    }
}
