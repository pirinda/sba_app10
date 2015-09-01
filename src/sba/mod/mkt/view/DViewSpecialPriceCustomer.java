/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.view;

import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewSpecialPriceCustomer extends DGridPaneView {

    public DViewSpecialPriceCustomer(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.M_SPE_CUS, DLibConsts.UNDEFINED, title);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_lnk_cus_tp AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_ref_cus AS " + DDbConsts.FIELD_ID + "2, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "CASE " +
                "WHEN v.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS + " THEN b.name " +
                "WHEN v.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS_TP + " THEN ct.name ELSE '?' END AS " + DDbConsts.FIELD_NAME + ", " +
                "vt.name, " +
                "s.code, " +
                "s.name, " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.M_SPE_CUS) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.MS_LNK_CUS_TP) + " AS vt ON " +
                "v.id_lnk_cus_tp = vt.id_lnk_cus_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_SPE) + " AS s ON " +
                "v.fk_spe = s.id_spe " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS + " AND v.id_ref_cus = b.id_bpr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.MU_CUS_TP) + " AS ct ON " +
                "v.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS_TP + " AND v.id_ref_cus = ct.id_cus_tp " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY vt.name, vt.id_lnk_cus_tp, " + DDbConsts.FIELD_NAME + ", v.id_ref_cus, s.name, s.code, s.id_spe ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[9];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", DGridConsts.COL_TITLE_TYPE + " referencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, DDbConsts.FIELD_NAME, "Referencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "s.name", DGridConsts.COL_TITLE_NAME + " lista precios especiales");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "s.code", DGridConsts.COL_TITLE_CODE + " lista precios especiales");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
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
        moSuscriptionsSet.add(DModConsts.MU_CUS_TP);
        moSuscriptionsSet.add(DModConsts.M_SPE);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
