/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterBranchEntity;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
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
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.fin.db.DDbBookkeepingMoveCustom;
import sba.mod.fin.db.DFinUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewBranchCashMoves extends DGridPaneView implements ActionListener {

    private DGridFilterDatePeriod moFilterDatePeriod;
    private DMyGridFilterBranchEntity moFilterBranchEntity;

    private JButton mjCreateCusPayment;
    private JButton mjCreateCusPaymentAdvance;
    private JButton mjCreateVenPayment;
    private JButton mjCreateVenPaymentAdvance;
    private JButton mjCreateDbrPayment;
    private JButton mjCreateCdrPayment;
    private JButton mjCreateOwnersEquity;
    private JButton mjCreateExchangeRate;
    private JButton mjCreateAdjustment;
    private JButton mjCreateTransfer;
    private JButton mjComputeStc;

    /**
     * @param client GUI client.
     * @param type View type. Constants defined in DModConsts (FX_BKK_CTM...).
     * @param subtype View subtype. Constants defined in DModSysConsts (FS_SYS_MOV_CL_MI or FS_SYS_MOV_CL_MO).
     * @param title View title.
     */
    public DViewBranchCashMoves(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(mnGridType == DModConsts.FX_BKK_CTM);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(mnGridType == DModConsts.FX_BKK_CTM);
        jtbFilterDeleted.setEnabled(mnGridType == DModConsts.FX_BKK_CTM);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moFilterBranchEntity = new DMyGridFilterBranchEntity(this, DModConsts.CU_CSH);
        moFilterBranchEntity.initFilter(((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).createFilterBranchCashKey());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterBranchEntity);

        switch (mnGridSubtype) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                if (mnGridType == DModConsts.FX_BKK_CTM) {
                    mjCreateCusPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_cus.gif")), "Ingreso pago cliente", this);
                    mjCreateCusPaymentAdvance = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_cus_adv.gif")), "Ingreso anticipo cliente", this);
                    mjCreateVenPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_ven.gif")), "Ingreso reembolso proveedor", this);
                    mjCreateVenPaymentAdvance = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_ven_adv.gif")), "Ingreso reembolso anticipo proveedor", this);
                    mjCreateDbrPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_dbr.gif")), "Ingreso pago deudor diverso", this);
                    mjCreateCdrPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_cdr.gif")), "Ingreso pago acreedor diverso", this);
                    mjCreateOwnersEquity = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_eqy.gif")), "Ingreso capital contable", this);
                    mjCreateExchangeRate = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_exr.gif")), "Ingreso por diferencia cambiaria", this);
                    mjCreateAdjustment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_adj.gif")), "Ingreso por ajuste", this);
                    mjCreateTransfer = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_in_tra.gif")), "Ingreso por traspaso", this);
                }
                else {
                    mjComputeStc = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif")), "Ingreso disponible", this);
                }
                break;

            case DModSysConsts.FS_SYS_MOV_CL_MO:
                if (mnGridType == DModConsts.FX_BKK_CTM) {
                    mjCreateCusPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_cus.gif")), "Egreso reembolso cliente", this);
                    mjCreateCusPaymentAdvance = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_cus_adv.gif")), "Egreso reembolso anticipo cliente", this);
                    mjCreateVenPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_ven.gif")), "Egreso pago proveedor", this);
                    mjCreateVenPaymentAdvance = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_ven_adv.gif")), "Egreso anticipo proveedor", this);
                    mjCreateDbrPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_dbr.gif")), "Egreso pago deudor diverso", this);
                    mjCreateCdrPayment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_cdr.gif")), "Egreso pago acreedor diverso", this);
                    mjCreateOwnersEquity = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_eqy.gif")), "Egreso capital contable", this);
                    mjCreateExchangeRate = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_exr.gif")), "Egreso por diferencia cambiaria", this);
                    mjCreateAdjustment = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_adj.gif")), "Egreso por ajuste", this);
                    mjCreateTransfer = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/mon_out_tra.gif")), "Egreso por traspaso", this);
                }
                else {
                    mjComputeStc = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif")), "Egreso disponible", this);
                }
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (mnGridType == DModConsts.FX_BKK_CTM) {
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateCusPayment);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateCusPaymentAdvance);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateVenPayment);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateVenPaymentAdvance);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateDbrPayment);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateCdrPayment);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateOwnersEquity);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateExchangeRate);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateAdjustment);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjCreateTransfer);

            mjCreateCusPaymentAdvance.setEnabled(false);
            mjCreateVenPaymentAdvance.setEnabled(false);
            mjCreateExchangeRate.setEnabled(false);
            mjCreateTransfer.setEnabled(false);
        }
        else {
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjComputeStc);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (int[]) moFiltersMap.get(DMyGridConsts.FILTER_BRANCH_CSH);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterKey(new String[] { "v.fk_csh_bpr_n", "v.fk_csh_bra_n", "v.fk_csh_csh_n", }, (int[]) filter);
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);

        if (mnGridType == DModConsts.FX_BKK_CTM_STC) {
            sql += (sql.length() == 0 ? "" : "AND v.b_avl = 0 ");
        }

        msSql = "SELECT " +
                "v.fk_bkk_yer_n AS " + DDbConsts.FIELD_ID + "1, " +
                "v.fk_bkk_num_n AS " + DDbConsts.FIELD_ID + "2, " +
                "vt.code AS " + DDbConsts.FIELD_CODE + ", " +
                "vt.name AS " + DDbConsts.FIELD_NAME + ", " +
                "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.fk_bkk_num_n)), v.fk_bkk_num_n) AS CHAR) AS f_num, " +
                "v.dt, " +
                "v.sup, " +
                "v.txt, " +
                "v.dbt + v.cdt AS f_amt, " +
                "v.dbt_cy + v.cdt_cy AS f_amt_cy, " +
                "v.exr, " +
                "vt.name, " +
                "c.code, " +
                "c.name, " +
                "cb.code, " +
                "cb.name, " +
                "ec.code, " +
                "ec.name, " +
                "mopt.name, " +
                "valt.name, " +
                "b.name, " +
                "v.b_exr, " +
                "v.b_avl AS " + DDbConsts.FIELD_IS_AVL + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_avl AS " + DDbConsts.FIELD_USER_AUD_ID + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_avl AS " + DDbConsts.FIELD_USER_AUD_TS + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ua.name AS " + DDbConsts.FIELD_USER_AUD_NAME + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_SYS_MOV_TP) + " AS vt ON " +
                "v.fk_sys_mov_cl = vt.id_sys_mov_cl AND v.fk_sys_mov_tp = vt.id_sys_mov_tp AND " +
                "v.fk_sys_mov_cl = " + mnGridSubtype + " AND v.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "v.fk_cur = c.id_cur " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mopt ON " +
                "v.fk_mop_tp = mopt.id_mop_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_VAL_TP) + " AS valt ON " +
                "v.fk_val_tp = valt.id_val_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_own_bpr = cb.id_bpr AND v.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS ec ON " +
                "v.fk_csh_bpr_n = ec.id_bpr AND v.fk_csh_bra_n = ec.id_bra AND v.fk_csh_csh_n = ec.id_csh " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ua ON " +
                "v.fk_usr_avl = ua.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.fk_bpr_bpr_n = b.id_bpr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY v.dt, cb.code, ec.code, v.fk_csh_bpr_n, v.fk_csh_bra_n, v.fk_csh_csh_n, " +
                "vt.name, v.fk_sys_mov_cl, v.fk_sys_mov_tp, f_num, v.txt, v.sup ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[23];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", "Fecha mov.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DGridConsts.COL_TITLE_CODE + " " + DUtilConsts.TXT_BRANCH.toLowerCase() + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "ec.code", DGridConsts.COL_TITLE_CODE + " cuenta dinero");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "vt.name", "Tipo mov.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", "Núm. mov.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_amt_cy", DFinUtils.getCashMoveNameSng(mnGridSubtype) + " $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_EXC_RATE, "v.exr", "T. cambio");
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_amt", DFinUtils.getCashMoveNameSng(mnGridSubtype) + " $ ML");
        columns[col++].setSumApplying(true);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_AVL, DGridConsts.COL_TITLE_IS_AVL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "v.txt", "Concepto", 150);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mopt.name", "Forma pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "valt.name", "Respaldo");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "v.sup", "Soporte", 50);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", "Asociado");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_AUD_NAME, DGridConsts.COL_TITLE_USER_AVL_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_AUD_TS, DGridConsts.COL_TITLE_USER_AVL_TS);
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
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.F_BKK);
        moSuscriptionsSet.add(DModConsts.T_DFR);
        moSuscriptionsSet.add(DModConsts.TX_DFR_PAY);

        if (mnGridType == DModConsts.FX_BKK_CTM_STC) {
            moSuscriptionsSet.add(DModConsts.FX_BKK_CTM);
        }
    }

    @Override
    public void actionRowEdit() {
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();
                DDbBookkeepingMoveCustom moveCustom = null;
                DGuiParams params = null;

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    moveCustom = (DDbBookkeepingMoveCustom) miClient.getSession().readRegistry(DModConsts.FX_BKK_CTM, gridRow.getRowPrimaryKey());
                    params = new DGuiParams(gridRow.getRowPrimaryKey());
                    params.setTypeKey(moveCustom.getSystemMoveKey());

                    miClient.getSession().getModule(DModConsts.MOD_FIN).showForm(mnGridType, DLibConsts.UNDEFINED, params);
                    moFormParams = null;
                }
            }
        }
    }

    public void actionComputeStc() {
        if (mjComputeStc.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    if (miClient.showMsgBoxConfirm(DGuiConsts.MSG_CNF_PROC) == JOptionPane.OK_OPTION) {
                        DFinUtils.computeStc(miClient.getSession(), gridRow.getRowPrimaryKey());
                        miClient.getSession().notifySuscriptors(DModConsts.F_BKK);
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

            if (button == mjCreateCusPayment) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY : DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateCusPaymentAdvance) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_CUS_ADV : DModSysConsts.FS_SYS_MOV_TP_MO_CUS_ADV);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateVenPayment) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY : DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateVenPaymentAdvance) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_VEN_ADV : DModSysConsts.FS_SYS_MOV_TP_MO_VEN_ADV);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateDbrPayment) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY : DModSysConsts.FS_SYS_MOV_TP_MO_DBR_PAY);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateCdrPayment) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY : DModSysConsts.FS_SYS_MOV_TP_MO_CDR_PAY);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateOwnersEquity) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_EQY : DModSysConsts.FS_SYS_MOV_TP_MO_EQY);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateExchangeRate) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_EXR : DModSysConsts.FS_SYS_MOV_TP_MO_EXR);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateAdjustment) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_ADJ : DModSysConsts.FS_SYS_MOV_TP_MO_ADJ);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjCreateTransfer) {
                params.setTypeKey(mnGridSubtype == DModSysConsts.FS_SYS_MOV_CL_MI ? DModSysConsts.FS_SYS_MOV_TP_MI_TRA : DModSysConsts.FS_SYS_MOV_TP_MO_TRA);
                miClient.getSession().showForm(DModConsts.FX_BKK_CTM, DLibConsts.UNDEFINED, params);
            }
            else if (button == mjComputeStc) {
                actionComputeStc();
            }
        }
    }
}
