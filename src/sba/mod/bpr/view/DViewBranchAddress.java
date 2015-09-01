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
public class DViewBranchAddress extends DGridPaneView {

    public DViewBranchAddress(DGuiClient client, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.BU_ADD, subtype, title);

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
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 AND br.b_del = 0 AND b.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_bra AS " + DDbConsts.FIELD_ID + "2, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "v.name AS " + DDbConsts.FIELD_NAME + ", " +
                "bc.code, " +
                "b.name, " +
                "v.add_1, " +
                "v.add_2, " +
                "v.add_3, " +
                "v.num_ext, " +
                "v.num_int, " +
                "v.loc, " +
                "v.cou, " +
                "v.ste, " +
                "v.zip, " +
                "v.pob, " +
                "v.tcd_1, " +
                "v.tcd_2, " +
                "v.tcd_3, " +
                "v.tce_1, " +
                "v.tce_2, " +
                "v.b_def, " +
                "c.name, " +
                "t1.name, " +
                "t2.name, " +
                "t3.name, " +
                "e1.name, " +
                "e2.name, " +
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
                "FROM " + DModConsts.TablesMap.get(DModConsts.BU_ADD) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS br ON " +
                "v.id_bpr = br.id_bpr AND v.id_bra = br.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.id_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "v.id_bpr = bc.id_bpr AND bc.id_bpr_cl = " + mnGridSubtype + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_TCD_TP) + " AS t1 ON " +
                "v.fk_tcd_1_tp = t1.id_tcd_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_TCD_TP) + " AS t2 ON " +
                "v.fk_tcd_2_tp = t2.id_tcd_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_TCD_TP) + " AS t3 ON " +
                "v.fk_tcd_3_tp = t3.id_tcd_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_TCE_TP) + " AS e1 ON " +
                "v.fk_tce_1_tp = e1.id_tce_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_TCE_TP) + " AS e2 ON " +
                "v.fk_tce_2_tp = e2.id_tce_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS c ON " +
                "v.fk_cty_n = c.id_cty " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY b.name, v.name, v.id_bpr, v.id_bra ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[32];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.name", DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " domicilio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "v.b_def", "Default");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.add_1", "Calle");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.num_ext", "Número exterior", 50);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.num_int", "Número interior", 50);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.add_2", "Colonia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.add_3", "Referencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.loc", "Localidad");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.cou", "Municipio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.ste", "Estado");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.zip", "Código postal");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "v.pob", "Apartado postal");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "c.name", "País");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tcd_1", "Teléfono 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t1.name", "Tipo teléfono 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tcd_2", "Teléfono 2");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t2.name", "Tipo teléfono 2");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tcd_3", "Teléfono 3");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t3.name", "Tipo teléfono 3");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tce_1", "Buzón cuenta 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "e1.name", "Tipo buzón cuenta 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.tce_2", "Buzón cuenta 2");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "e2.name", "Tipo buzón cuenta 2");
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
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
