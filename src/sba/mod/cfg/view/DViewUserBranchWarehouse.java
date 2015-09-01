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
public class DViewUserBranchWarehouse extends DGridPaneView {

    public DViewUserBranchWarehouse(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.CU_USR_WAH, DLibConsts.UNDEFINED, title);

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
                "ue.id_bpr AS " + DDbConsts.FIELD_ID + "2, " +
                "ue.id_bra AS " + DDbConsts.FIELD_ID + "3, " +
                "ue.id_wah AS " + DDbConsts.FIELD_ID + "4, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "u.name AS " + DDbConsts.FIELD_NAME + ", " +
                "bpb.id_bpr, " +
                "bpb.code, " +
                "bpb.name, " +
                "e.id_wah, " +
                "e.code, " +
                "e.name, " +
                "et.id_wah_tp, " +
                "et.name, " +
                "u.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "u.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "u.b_sys AS " + DDbConsts.FIELD_IS_SYS + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_WAH) + " AS ue ON " +
                "u.id_usr = ue.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bpb ON " +
                "ue.id_bpr = bpb.id_bpr AND ue.id_bra = bpb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS e ON " +
                "ue.id_bpr = bpb.id_bpr AND ue.id_bra = bpb.id_bra AND ue.id_wah = e.id_wah " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_WAH_TP) + " AS et ON " +
                "e.fk_wah_tp = et.id_wah_tp " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY u.name, ue.id_usr, e.name, ue.id_bpr, ue.id_bra, ue.id_wah ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[9];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "e.name", DGridConsts.COL_TITLE_NAME + " almacén");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "e.code", DGridConsts.COL_TITLE_CODE + " almacén");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "et.name", DGridConsts.COL_TITLE_TYPE + " almacén");
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
        moSuscriptionsSet.add(DModConsts.CU_WAH);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
