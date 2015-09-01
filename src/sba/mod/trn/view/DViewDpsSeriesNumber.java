/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import sba.lib.DLibConsts;
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
public class DViewDpsSeriesNumber extends DGridPaneView {

    public DViewDpsSeriesNumber(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TU_SER_NUM, DLibConsts.UNDEFINED, title);
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
                "v.id_ser AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_num AS " + DDbConsts.FIELD_ID + "2, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "s.ser AS " + DDbConsts.FIELD_NAME + ", " +
                "v.num_sta, " +
                "v.num_end_n, " +
                "v.apb_yer, " +
                "v.apb_num, " +
                "v.apb_dt_n, " +
                "vtp.id_dps_ct, " +
                "vtp.id_dps_cl, " +
                "vtp.id_dps_tp, " +
                "vct.name, " +
                "vcl.name, " +
                "vtp.name, " +
                "xt.name, " +
                "s.id_ser, " +
                "s.ser, " +
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
                "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_NUM) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON " +
                "v.id_ser = s.id_ser " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CT) + " AS vct ON " +
                "s.fk_dps_ct = vct.id_dps_ct " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CL) + " AS vcl ON " +
                "s.fk_dps_ct = vcl.id_dps_ct AND s.fk_dps_cl = vcl.id_dps_cl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS vtp ON " +
                "s.fk_dps_ct = vtp.id_dps_ct AND s.fk_dps_cl = vtp.id_dps_cl AND s.fk_dps_tp = vtp.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XML_TP) + " AS xt ON " +
                "v.fk_xml_tp = xt.id_xml_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY vtp.id_dps_ct, vtp.id_dps_cl, vtp.id_dps_tp, s.ser, v.id_ser ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[17];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vct.name", "Categoría docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vcl.name", "Clase docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vtp.name", DGridConsts.COL_TITLE_TYPE + " + docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_NAME, "Serie docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, "v.num_sta", "Folio inicial");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, "v.num_end_n", "Folio final");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, "v.apb_num", "Número aprobación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, "v.apb_yer", "Año aprobación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.apb_dt_n", DGridConsts.COL_TITLE_DATE + " aprobación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "xt.name", "Tipo XML");
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
        moSuscriptionsSet.add(DModConsts.TU_SER);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
