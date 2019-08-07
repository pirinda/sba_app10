/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterBranchEntity;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
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
import sba.lib.gui.DGuiParams;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.trn.db.DDbIog;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewStockMoves extends DGridPaneView implements ActionListener {

    private JButton mjCreateInExternalAdj;
    private JButton mjCreateInExternalInv;
    private JButton mjCreateInInternalTra;
    private JButton mjCreateInInternalCnv;
    private JButton mjCreateOutExternalAdj;
    private JButton mjCreateOutExternalInv;
    private JButton mjCreateOutInternalTra;
    private JButton mjCreateOutInternalCnv;
    private JButton mjPrint;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private DMyGridFilterBranchEntity moFilterBranchEntity;

    /**
     * @param client GUI client.
     * @param title View title.
     */
    public DViewStockMoves(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TX_STK_MOV, DLibConsts.UNDEFINED, title);

        jbRowNew.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moFilterBranchEntity = new DMyGridFilterBranchEntity(this, DModConsts.CU_WAH);
        moFilterBranchEntity.initFilter(((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).createFilterBranchWarehouseKey());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterBranchEntity);

        mjCreateInExternalAdj = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_in_ext_adj.gif")), DUtilConsts.TXT_CREATE + " entrada ajuste", this);
        mjCreateInExternalInv = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_in_ext_inv.gif")), DUtilConsts.TXT_CREATE + " entrada inventario", this);
        mjCreateInInternalTra = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_in_int_tra.gif")), DUtilConsts.TXT_CREATE + " entrada traspaso", this);
        mjCreateInInternalCnv = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_in_int_cnv.gif")), DUtilConsts.TXT_CREATE + " entrada conversión", this);
        mjCreateOutExternalAdj = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_out_ext_adj.gif")), DUtilConsts.TXT_CREATE + " salida ajuste", this);
        mjCreateOutExternalInv = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_out_ext_inv.gif")), DUtilConsts.TXT_CREATE + " salida inventario", this);
        mjCreateOutInternalTra = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_out_int_tra.gif")), DUtilConsts.TXT_CREATE + " salida traspaso", this);
        mjCreateOutInternalCnv = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/stk_out_int_cnv.gif")), DUtilConsts.TXT_CREATE + " salida conversión", this);
        mjPrint = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_PRINT), DUtilConsts.TXT_PRINT + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);

        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateInExternalAdj);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateInExternalInv);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateInInternalTra);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateInInternalCnv);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateOutExternalAdj);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateOutExternalInv);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateOutInternalTra);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateOutInternalCnv);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjPrint);

        mjCreateInInternalTra.setEnabled(false);
        mjCreateInInternalCnv.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String aux = "";
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);

        filter = (int[]) moFiltersMap.get(DMyGridConsts.FILTER_BRANCH_WAH);
        if (filter != null) {
            aux = DGridUtils.getSqlFilterKey(new String[] { "v.fk_wah_bpr", "v.fk_wah_bra", "v.fk_wah_wah" }, (int[]) filter);
            if (!aux.isEmpty()) {
                sql += (sql.length() == 0 ? "" : "AND ") + aux;
            }
        }

        msSql = "SELECT " +
                "v.id_iog AS " + DDbConsts.FIELD_ID + "1, " +
                "CAST(v.num AS char) AS " + DDbConsts.FIELD_CODE + ", " +
                "CAST(v.num AS char) AS " + DDbConsts.FIELD_NAME + ", " +
                "CAST(v.num AS char) AS f_num, " +
                "v.dt, " +
                "v.val_r, " +
                "dt.name, " +
                "cb.code, " +
                "cb.name, " +
                "ew.code, " +
                "ew.name, " +
                "v.b_aud AS " + DDbConsts.FIELD_IS_AUD + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_aud AS " + DDbConsts.FIELD_USER_AUD_ID + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_aud AS " + DDbConsts.FIELD_USER_AUD_TS + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ua.name AS " + DDbConsts.FIELD_USER_AUD_NAME + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_IOG_TP) + " AS dt ON " +
                "v.fk_iog_ct = dt.id_iog_ct AND v.fk_iog_cl = dt.id_iog_cl AND v.fk_iog_tp = dt.id_iog_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_wah_bpr = cb.id_bpr AND v.fk_wah_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS ew ON " +
                "v.fk_wah_bpr = ew.id_bpr AND v.fk_wah_bra = ew.id_bra AND v.fk_wah_wah = ew.id_wah " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ua ON " +
                "v.fk_usr_aud = ua.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY v.num, v.id_iog ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[17];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", DGridConsts.COL_TITLE_DATE + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cb.name", DGridConsts.COL_TITLE_NAME + " " + DUtilConsts.TXT_BRANCH.toLowerCase() + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DGridConsts.COL_TITLE_CODE + " " + DUtilConsts.TXT_BRANCH.toLowerCase() + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ew.name", "Almacén");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "ew.code", DGridConsts.COL_TITLE_CODE + " almacén");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dt.name", DGridConsts.COL_TITLE_TYPE + " docto");
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.val_r", "Total $ M");
        columns[col++].setSumApplying(true);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_AUD, DGridConsts.COL_TITLE_IS_AUD);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_AUD_NAME, DGridConsts.COL_TITLE_USER_AUD_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_AUD_TS, DGridConsts.COL_TITLE_USER_AUD_TS);
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
        moSuscriptionsSet.add(DModConsts.T_IOG);
        moSuscriptionsSet.add(DModConsts.T_DPS);
    }

    @Override
    public void actionRowEdit() {
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                int type = DLibConsts.UNDEFINED;
                int subtype = DLibConsts.UNDEFINED;
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();
                DGuiParams params = null;
                DDbIog iog = null;

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    params = new DGuiParams(gridRow.getRowPrimaryKey());
                    iog = (DDbIog) miClient.getSession().readRegistry(DModConsts.T_IOG, gridRow.getRowPrimaryKey());

                    subtype = DModSysConsts.TS_IOG_CT_IN;

                    if (DLibUtils.belongsTo(iog.getIogTypeKey(), new int[][] { DModSysConsts.TS_IOG_TP_IN_PUR_PUR })) {
                        type = DModConsts.TX_IOG_PUR;
                    }
                    else if (DLibUtils.belongsTo(iog.getIogTypeKey(), new int[][] { DModSysConsts.TS_IOG_TP_IN_SAL_SAL })) {
                        type = DModConsts.TX_IOG_SAL;
                    }
                    else if (DLibUtils.belongsTo(iog.getIogTypeKey(), new int[][] { DModSysConsts.TS_IOG_TP_IN_EXT_ADJ, DModSysConsts.TS_IOG_TP_IN_EXT_INV, DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ, DModSysConsts.TS_IOG_TP_OUT_EXT_INV })) {
                        type = DModConsts.TX_IOG_EXT;
                    }
                    else if (DLibUtils.belongsTo(iog.getIogTypeKey(), new int[][] { DModSysConsts.TS_IOG_TP_IN_INT_TRA, DModSysConsts.TS_IOG_TP_IN_INT_CNV, DModSysConsts.TS_IOG_TP_OUT_INT_TRA, DModSysConsts.TS_IOG_TP_OUT_INT_CNV })) {
                        type = DModConsts.TX_IOG_INT;
                    }
                    else {
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }

                    if (type != DLibConsts.UNDEFINED) {
                        miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(type, subtype, params);
                        moFormParams = null;
                    }
                }
            }
        }
    }

    private void actionPrint() {
        if (mjPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isDeleted()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_DELETED);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    try {
                        HashMap<String, Object> paramsMap = miClient.createReportParams();

                        int iogId = getSelectedGridRow().getRowPrimaryKey()[0];
                        paramsMap.put("nParentIogId", (long) iogId);
                        paramsMap.put("nChildIogId", (long) DTrnUtils.getIogCounterpartId(miClient.getSession(), iogId));

                        miClient.getSession().printReport(DModConsts.T_IOG, 0, null, paramsMap);
                    }
                    catch (Exception e) {
                        DLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            DGuiParams params = new DGuiParams();

            if (button == mjCreateInExternalAdj) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_IN_EXT_ADJ);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_IN, params);
            }
            else if (button == mjCreateInExternalInv) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_IN_EXT_INV);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_IN, params);
            }
            else if (button == mjCreateInInternalTra) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_IN_INT_TRA);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_IN, params);
            }
            else if (button == mjCreateInInternalCnv) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_IN_INT_CNV);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_IN, params);
            }
            else if (button == mjCreateOutExternalAdj) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_OUT, params);
            }
            else if (button == mjCreateOutExternalInv) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_OUT_EXT_INV);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_OUT, params);
            }
            else if (button == mjCreateOutInternalTra) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_OUT_INT_TRA);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_OUT, params);
            }
            else if (button == mjCreateOutInternalCnv) {
                params.setTypeKey(DModSysConsts.TS_IOG_TP_OUT_INT_CNV);
                miClient.getSession().showForm(DModConsts.TX_IOG_EXT, DModSysConsts.TS_IOG_CT_OUT, params);
            }
            else if (button == mjPrint) {
                actionPrint();
            }
        }
    }
}
