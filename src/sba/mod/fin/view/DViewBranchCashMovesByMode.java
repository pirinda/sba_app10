/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.view;

import sba.gui.DGuiClientSessionCustom;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterBranchEntity;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDate;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewBranchCashMovesByMode extends DGridPaneView {

    private DGridFilterDate moFilterDate;
    private DMyGridFilterBranchEntity moFilterBranchEntity;

    /**
     * @param client GUI client.
     * @param type Constants defined in DModConsts (FX_BKK_TP...).
     * @param title View title.
     */
    public DViewBranchCashMovesByMode(DGuiClient client, int type, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, DLibConsts.UNDEFINED, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        moFilterDate = new DGridFilterDate(miClient, this);
        moFilterDate.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDate);

        moFilterBranchEntity = new DMyGridFilterBranchEntity(this, DModConsts.CU_CSH);
        moFilterBranchEntity.initFilter(((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).createFilterBranchCashKey());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterBranchEntity);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String groupBy = "";
        String orderBy = "";
        Object filter = null;

        filter = (int[]) moFiltersMap.get(DMyGridConsts.FILTER_BRANCH_CSH);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterKey(new String[] { "bkk.fk_csh_bpr_n", "bkk.fk_csh_bra_n", "bkk.fk_csh_csh_n", }, (int[]) filter);
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("bkk.dt", (DGuiDate) filter);

        switch (mnGridType) {
            case DModConsts.FX_BKK_MOP:
                moPaneSettings = new DGridPaneSettings(2);
                msSql = "SELECT " +
                        "bkk.fk_sys_mov_cl AS " + DDbConsts.FIELD_ID + "1, " +
                        "bkk.fk_mop_tp AS " + DDbConsts.FIELD_ID + "2, ";
                groupBy = "bkk.fk_sys_mov_cl, bkk.fk_mop_tp, " +
                        "smcl.id_sys_mov_cl, smcl.code, smcl.name, ";
                orderBy = "smcl.name, smcl.code, smcl.id_sys_mov_cl, ";
                break;
            case DModConsts.FX_BKK_MOP_SMT:
                moPaneSettings = new DGridPaneSettings(3);
                msSql = "SELECT " +
                        "bkk.fk_sys_mov_cl AS " + DDbConsts.FIELD_ID + "1, " +
                        "bkk.fk_sys_mov_tp AS " + DDbConsts.FIELD_ID + "2, " +
                        "bkk.fk_mop_tp AS " + DDbConsts.FIELD_ID + "3, ";
                groupBy = "bkk.fk_sys_mov_cl, bkk.fk_sys_mov_tp, bkk.fk_mop_tp, " +
                        "smcl.id_sys_mov_cl, smcl.code, smcl.name, " +
                        "smtp.id_sys_mov_cl, smtp.id_sys_mov_tp, smtp.code, smtp.name, ";
                orderBy = "smcl.name, smcl.code, smcl.id_sys_mov_cl, " +
                        "smtp.name, smtp.code, smtp.id_sys_mov_cl, smtp.id_sys_mov_tp, ";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += "mopt.code AS " + DDbConsts.FIELD_CODE + ", " +
                "mopt.name AS " + DDbConsts.FIELD_NAME + ", " +
                "smcl.id_sys_mov_cl, " +
                "smcl.code, " +
                "smcl.name, " +
                "smtp.id_sys_mov_cl, " +
                "smtp.id_sys_mov_tp, " +
                "smtp.code, " +
                "smtp.name, " +
                "mopt.id_mop_tp, " +
                "mopt.code, " +
                "mopt.name, " +
                "COALESCE(SUM(bkk.dbt), 0) AS f_dbt, " +
                "COALESCE(SUM(bkk.cdt), 0) AS f_cdt, " +
                "COALESCE(SUM(bkk.dbt - bkk.cdt), 0) AS f_bal " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mopt ON " +
                "bkk.fk_mop_tp = mopt.id_mop_tp AND bkk.b_del = 0 AND " +
                "bkk.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_SYS_MOV_CL) + " AS smcl ON " +
                "bkk.fk_sys_mov_cl = smcl.id_sys_mov_cl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_SYS_MOV_TP) + " AS smtp ON " +
                "bkk.fk_sys_mov_cl = smtp.id_sys_mov_cl AND bkk.fk_sys_mov_tp = smtp.id_sys_mov_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "bkk.fk_cur = c.id_cur " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "GROUP BY " + groupBy +
                "mopt.id_mop_tp, mopt.code, mopt.name " +
                "ORDER BY " + orderBy +
                "mopt.name, mopt.code, mopt.id_mop_tp ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = null;
        switch (mnGridType) {
            case DModConsts.FX_BKK_MOP:
                columns = new DGridColumnView[5];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "smcl.name", "Clase movimiento");
                break;
            case DModConsts.FX_BKK_MOP_SMT:
                columns = new DGridColumnView[6];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "smcl.name", "Clase movimiento");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "smtp.name", "Tipo movimiento");
                break;
            default:
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "mopt.name", "Forma pago");
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dbt", "Ingreso $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_cdt", "Egreso $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal", "Saldo $ ML");
        columns[col++].setSumApplying(true);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.F_BKK);
        moSuscriptionsSet.add(DModConsts.FX_BKK_CTM);
        moSuscriptionsSet.add(DModConsts.T_DFR);
        moSuscriptionsSet.add(DModConsts.TX_DFR_PAY);
    }
}
