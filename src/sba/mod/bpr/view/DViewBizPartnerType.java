/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.view;

import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewBizPartnerType extends DGridPaneView {

    public DViewBizPartnerType(DGuiClient client, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.BU_BPR_TP, subtype, title);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
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

        msSql = "SELECT " +
                "v.id_bpr_cl AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_bpr_tp AS " + DDbConsts.FIELD_ID + "2, " +
                "v.code AS " + DDbConsts.FIELD_CODE + ", " +
                "v.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.cdt_lim, " +
                "v.cdt_day, " +
                "v.cdt_day_gra, " +
                "ct.id_cdt_tp, " +
                "ct.name, " +
                "mt.id_mop_tp, " +
                "mt.name, " +
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
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_CDT_TP) + " AS ct ON " +
                "v.fk_cdt_tp = ct.id_cdt_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mt ON " +
                "v.fk_mop_tp = mt.id_mop_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.F_ABP_BPR) + " AS abp ON " +
                "v.fk_abp_bpr = abp.id_abp_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "WHERE v.id_bpr_cl = " + mnGridSubtype + " " +
                (sql.length() == 0 ? "" : "AND " + sql) +
                "ORDER BY v.name, v.id_bpr_cl, v.id_bpr_tp ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[14];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "ct.name", DGridConsts.COL_TITLE_TYPE + " crédito");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.cdt_lim", "Límite crédito $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "v.cdt_day", "Días crédito");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "v.cdt_day_gra", "Días gracia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "mt.name", DGridConsts.COL_TITLE_TYPE + " forma pago");
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
        moSuscriptionsSet.add(DModConsts.F_ABP_BPR);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
