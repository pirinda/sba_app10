/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterSwitchTax;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDatePeriod;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridRowView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.fin.db.DDbTaxGroupConfigRow;
import sba.mod.trn.db.DTrnUtils;
import sba.mod.trn.form.DDialogCardexStock;
import sba.mod.trn.form.DDialogHistoryPrices;

/**
 *
 * @author Sergio Flores
 */
public class DViewItemPrices extends DGridPaneView implements ActionListener {

    private JButton mjButtonShowCardexStock;
    private JButton mjButtonShowHistoryPrices;
    private JTextField mjTextTaxRate;
    private DDialogCardexStock moDialogCardexStock;
    private DDialogHistoryPrices moDialogHistoryPrices;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private DMyGridFilterSwitchTax moFilterSwitchTax;
    private DDbTaxGroupConfigRow moTaxGroupConfigRow;

    public DViewItemPrices(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.MX_ITM_PRC, DLibConsts.UNDEFINED, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, DLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        mjButtonShowCardexStock = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CARDEX), "Ver tarjeta auxiliar", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowCardexStock);

        mjButtonShowHistoryPrices = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_hst.gif")), "Ver historial precios compra", this);
        mjButtonShowHistoryPrices.setEnabled(miClient.getSession().getUser().hasModuleAccess(DModSysConsts.CS_MOD_PUR));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowHistoryPrices);

        moFilterSwitchTax = new DMyGridFilterSwitchTax(client, this);
        moFilterSwitchTax.initFilter(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isTaxIncluded());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterSwitchTax);

        mjTextTaxRate = new JTextField("");
        mjTextTaxRate.setPreferredSize(new Dimension(60, 23));
        mjTextTaxRate.setEditable(false);
        mjTextTaxRate.setFocusable(false);
        mjTextTaxRate.setToolTipText("Razón impuestos");
        mjTextTaxRate.setHorizontalAlignment(JTextField.TRAILING);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjTextTaxRate);

        moDialogCardexStock = new DDialogCardexStock(client, mnGridSubtype);
        moDialogHistoryPrices = new DDialogHistoryPrices(client, DModSysConsts.TS_DPS_CT_PUR);
        moTaxGroupConfigRow = DTrnUtils.getTaxGroupConfigRowDefault(miClient.getSession());
    }

    private void actionShowCardexStock() {
        if (mjButtonShowCardexStock.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogCardexStock.resetForm();
                    moDialogCardexStock.setValue(DModSysConsts.PARAM_DATE, (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD));
                    moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                    moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM_UNT, gridRow.getRowPrimaryKey()[1]);
                    moDialogCardexStock.populateCardex();
                    moDialogCardexStock.setVisible(true);
                }
            }
        }
    }

    private void actionShowHistoryPrices() {
        if (mjButtonShowHistoryPrices.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogHistoryPrices.resetForm();
                    moDialogHistoryPrices.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                    moDialogHistoryPrices.populateHistory();
                    moDialogHistoryPrices.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        double taxRate = 0;
        String sql = "";
        String sqlDate = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(true);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sqlDate = "AND s.id_yer = " + DLibTimeUtils.digestYear((DGuiDate) filter)[0] + " AND " +
                "s.dt <= '" + DLibUtils.DbmsDateFormatDate.format((DGuiDate) filter) + "' ";

        filter = (Boolean) moFiltersMap.get(DMyGridConsts.FILTER_SWITCH_TAX);
        taxRate = !(Boolean) filter ? 1 : DTrnUtils.computeTaxRate(moTaxGroupConfigRow, DModSysConsts.TS_DPS_CT_SAL);
        mjTextTaxRate.setText(DLibUtils.DecimalFormatValue4D.format(taxRate));

        msSql = "SELECT " +
                "v.id_itm AS " + DDbConsts.FIELD_ID + "1, " +
                "v.fk_unt AS " + DDbConsts.FIELD_ID + "2, " +
                "v.code AS " + DDbConsts.FIELD_CODE + ", " +
                "v.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.prc_srp * " + taxRate + " AS f_prc_srp, " +
                "v.prc_1 * " + taxRate + " AS f_prc_1, " +
                "v.prc_2 * " + taxRate + " AS f_prc_2, " +
                "v.prc_3 * " + taxRate + " AS f_prc_3, " +
                "v.prc_4 * " + taxRate + " AS f_prc_4, " +
                "v.prc_5 * " + taxRate + " AS f_prc_5, " +
                "COALESCE((SELECT dr.prc_unt " +
                "FROM t_dps AS d, t_dps_row AS dr " +
                "WHERE d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND " +
                "d.fk_dps_ct = " + DModSysConsts.TS_DPS_CT_PUR + " AND dr.fk_row_itm = v.id_itm AND dr.fk_adj_ct = " + DModSysConsts.TS_ADJ_CT_NA + " " +
                "ORDER BY d.dt DESC, d.ts_usr_upd DESC LIMIT 1), 0) * " + taxRate + " AS f_lpp, " +
                "u.id_unt, " +
                "u.code, " +
                "v.b_can_upd AS " + DDbConsts.FIELD_CAN_UPD + ", " +
                "v.b_can_dis AS " + DDbConsts.FIELD_CAN_DIS + ", " +
                "v.b_can_del AS " + DDbConsts.FIELD_CAN_DEL + ", " +
                "v.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + ", " +
                "(SELECT SUM(s.mov_in - s.mov_out) " +
                " FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                " WHERE s.id_itm = v.id_itm AND s.id_unt = v.fk_unt AND s.b_del = 0 " + sqlDate + ") AS f_stk " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON " +
                "v.fk_unt = u.id_unt AND v.b_fre_prc = 0 " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY v.name, v.code, v.id_itm, u.code, u.id_unt ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[18];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_srp", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_SRP }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_1", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_RET }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_2", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_HAL }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_3", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_WHO }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_4", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_ESP }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_prc_5", DLibUtils.textProperCase((String) miClient.getSession().readField(DModConsts.MS_ITM_PRC_TP, new int[] { DModSysConsts.MS_ITM_PRC_TP_COS }, DDbRegistry.FIELD_NAME)) + " $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_lpp", "Últ. compra $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_stk", "Existencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_INS_NAME, DGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_INS_TS, DGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_UPD_NAME, DGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_UPD_TS, DGridConsts.COL_TITLE_USER_UPD_TS);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.CU_USR);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_UNT);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.T_IOG);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjButtonShowCardexStock) {
                actionShowCardexStock();
            }
            else if (button == mjButtonShowHistoryPrices) {
                actionShowHistoryPrices();
            }
        }
    }
}
