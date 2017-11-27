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
public class DViewBizPartnerConfig extends DGridPaneView {

    public DViewBizPartnerConfig(DGuiClient client, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.BU_BPR_CFG, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "b.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_bpr_cl AS " + DDbConsts.FIELD_ID + "2, " +
                "v.code AS " + DDbConsts.FIELD_CODE + ", " +
                "b.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.code_own, " +
                "v.b_cdt_usr, " +
                "v.cdt_lim, " +
                "v.cdt_day, " +
                "v.cdt_day_gra, " +
                "v.cfd_use, " +
                "v.dt_sta, " +
                "v.dt_end_n, " +
                "bt.name, " +
                "c.code, " +
                "ct.name, " +
                "mt.code, " +
                "mt.name, " +
                "abp.name, " +
                "v.fk_abp_bpr_n IS NOT NULL AS f_abp_man, " +
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
                "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.id_bpr = b.id_bpr AND v.id_bpr_cl = " + mnGridSubtype + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP) + " AS bt ON " +
                "v.fk_bpr_cl = bt.id_bpr_cl AND v.fk_bpr_tp = bt.id_bpr_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "v.fk_cur_n = c.id_cur " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_CDT_TP) + " AS ct ON " +
                "v.fk_cdt_tp_n = ct.id_cdt_tp " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mt ON " +
                "v.fk_mop_tp_n = mt.id_mop_tp " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.F_ABP_BPR) + " AS abp ON " +
                "v.fk_abp_bpr_n = abp.id_abp_bpr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY b.name, v.id_bpr ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[24];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bt.name", DGridConsts.COL_TITLE_TYPE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "v.code_own", "Código propio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda default");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_cdt_usr", "Config. manual crédito");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ct.name", DGridConsts.COL_TITLE_TYPE + " crédito");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.cdt_lim", "Lím. crédito $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "v.cdt_day", "Días crédito");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "v.cdt_day_gra", "Días gracia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "mt.code", DGridConsts.COL_TITLE_CODE + " forma pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "mt.name", DGridConsts.COL_TITLE_NAME + " forma pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.cfd_use", "Uso CFDI");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_sta", "Inicio relación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_end_n", "Término relación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "f_abp_man", "Config. manual paquete contable");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "abp.name", "Paquete contable");
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
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BPR_CFG);
        moSuscriptionsSet.add(DModConsts.BU_BPR_TP);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
