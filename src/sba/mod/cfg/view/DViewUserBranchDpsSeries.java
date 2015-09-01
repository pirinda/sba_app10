/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.view;

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
public class DViewUserBranchDpsSeries extends DGridPaneView {

    public DViewUserBranchDpsSeries(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.CU_USR_SER_BRA, DLibConsts.UNDEFINED, title);

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

        moPaneSettings = new DGridPaneSettings(4);
        moPaneSettings.setDeletedApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "u.b_del = 0 ";
        }

        msSql = "SELECT " +
                "ue.id_usr AS " + DDbConsts.FIELD_ID + "1, " +
                "ue.id_ser AS " + DDbConsts.FIELD_ID + "2, " +
                "ue.id_bpr AS " + DDbConsts.FIELD_ID + "3, " +
                "ue.id_bra AS " + DDbConsts.FIELD_ID + "4, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "u.name AS " + DDbConsts.FIELD_NAME + ", " +
                "bpb.id_bpr, " +
                "bpb.code, " +
                "bpb.name, " +
                "e.id_ser, " +
                "e.ser, " +
                "etp.id_dps_ct, " +
                "etp.id_dps_cl, " +
                "etp.id_dps_tp, " +
                "ect.name, " +
                "ecl.name, " +
                "etp.name, " +
                "u.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "u.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "u.b_sys AS " + DDbConsts.FIELD_IS_SYS + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " AS ue ON " +
                "u.id_usr = ue.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS e ON " +
                "ue.id_ser = e.id_ser " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CT) + " AS ect ON " +
                "e.fk_dps_ct = ect.id_dps_ct " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_CL) + " AS ecl ON " +
                "e.fk_dps_ct = ecl.id_dps_ct AND e.fk_dps_cl = ecl.id_dps_cl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS etp ON " +
                "e.fk_dps_ct = etp.id_dps_ct AND e.fk_dps_cl = etp.id_dps_cl AND e.fk_dps_tp = etp.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bpb ON " +
                "ue.id_bpr = bpb.id_bpr AND ue.id_bra = bpb.id_bra " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY u.name, ue.id_usr, etp.id_dps_ct, etp.id_dps_cl, etp.id_dps_tp, e.ser, ue.id_ser, bpb.name, ue.id_bpr, ue.id_bra ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[10];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ect.name", "Categoría docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ecl.name", "Clase docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "etp.name", "Tipo docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "e.ser", "Serie docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bpb.name", DGridConsts.COL_TITLE_NAME + " suc");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "bpb.code", DGridConsts.COL_TITLE_CODE + " suc");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.TU_SER);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
