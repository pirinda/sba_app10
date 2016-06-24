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
public class DViewDpsSeriesBranch extends DGridPaneView {

    public DViewDpsSeriesBranch(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TU_SER_BRA, DLibConsts.UNDEFINED, title);
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

        moPaneSettings = new DGridPaneSettings(1);
        moPaneSettings.setDisabledApplying(true);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_ser AS " + DDbConsts.FIELD_ID + "1, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "v.ser AS " + DDbConsts.FIELD_NAME + ", " +
                "vtp.id_dps_ct, " +
                "vtp.id_dps_cl, " +
                "vtp.id_dps_tp, " +
                "vct.name, " +
                "vcl.name, " +
                "vtp.name, " +
                "bpb.id_bpr, " +
                "bpb.id_bra, " +
                "bpb.code, " +
                "bpb.name, " +
                "v.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS vb ON " +
                "v.id_ser = vb.id_ser " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CT) + " AS vct ON " +
                "v.fk_dps_ct = vct.id_dps_ct " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CL) + " AS vcl ON " +
                "v.fk_dps_ct = vcl.id_dps_ct AND v.fk_dps_cl = vcl.id_dps_cl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS vtp ON " +
                "v.fk_dps_ct = vtp.id_dps_ct AND v.fk_dps_cl = vtp.id_dps_cl AND v.fk_dps_tp = vtp.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bpb ON " +
                "vb.id_bpr = bpb.id_bpr AND vb.id_bra = bpb.id_bra " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY vtp.id_dps_ct, vtp.id_dps_cl, vtp.id_dps_tp, v.ser, v.id_ser, bpb.name, vb.id_bpr, vb.id_bra ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[9];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vct.name", "Categoría docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vcl.name", "Clase docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vtp.name", DGridConsts.COL_TITLE_TYPE + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_NAME, "Serie docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bpb.name", DGridConsts.COL_TITLE_NAME + " suc");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "bpb.code", DGridConsts.COL_TITLE_CODE + " suc");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.TU_SER);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
