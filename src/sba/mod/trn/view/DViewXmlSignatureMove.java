/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDatePeriod;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewXmlSignatureMove extends DGridPaneView {
    
    private DGridFilterDatePeriod moFilterDatePeriod;

    public DViewXmlSignatureMove(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.T_XSM, DLibConsts.UNDEFINED, title);
        
        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        setRowButtonsEnabled(true, true, false, false, true);
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

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);
        
        msSql = "SELECT " +
                "v.id_xsp AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_mov AS " + DDbConsts.FIELD_ID + "2, " +
                "v.dt, " +
                "v.mov_in, " +
                "v.mov_out, " +
                "mc.code, " +
                "mc.name, " +
                "mt.code, " +
                "mt.name, " +
                "COALESCE(cb.code, '" + DUtilConsts.NON_APPLYING + "') AS f_bra, " +
                "dfr.uid, " +
                "p.code AS " + DDbConsts.FIELD_CODE + ", " +
                "p.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSM) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_XSP) + " AS p ON " +
                "v.id_xsp = p.id_xsp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XSM_CL) + " AS mc ON " +
                "v.fk_xsm_cl = mc.id_xsm_cl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XSM_TP) + " AS mt ON " +
                "v.fk_xsm_cl = mt.id_xsm_cl AND v.fk_xsm_tp = mt.id_xsm_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_sig_bpr_n = cb.id_bpr AND v.fk_sig_bra_n = cb.id_bra " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON " +
                "v.fk_dfr_n = dfr.id_dfr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY f_bra, p.code, v.id_xsp, v.id_mov ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[15];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_bra", DUtilConsts.TXT_BRANCH + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, DDbConsts.FIELD_ID + "2", DGridConsts.COL_TITLE_NUM);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", DGridConsts.COL_TITLE_DATE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_4B, "v.mov_in", "Entrada");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_4B, "v.mov_out", "Salida");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mc.name", DGridConsts.COL_TITLE_CLASS + " movimiento");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "mt.name", DGridConsts.COL_TITLE_TYPE + " movimiento");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "dfr.uid", "UUID");
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
        moSuscriptionsSet.add(DModConsts.T_DPS_SIG);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
