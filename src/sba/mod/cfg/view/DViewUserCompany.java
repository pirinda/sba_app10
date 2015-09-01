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
public class DViewUserCompany extends DGridPaneView {

    public DViewUserCompany(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.CU_USR_CO, DLibConsts.UNDEFINED, title);

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
        moPaneSettings.setDeletedApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "u.b_del = 0 ";
        }

        msSql = "SELECT " +
                "uc.id_usr AS " + DDbConsts.FIELD_ID + "1, " +
                "uc.id_bpr AS " + DDbConsts.FIELD_ID + "2, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "u.name AS " + DDbConsts.FIELD_NAME + ", " +
                "bp.id_bpr, " +
                "bp.name, " +
                "uc.b_unv, " +
                "u.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "u.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "u.b_sys AS " + DDbConsts.FIELD_IS_SYS + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_CO) + " AS uc ON " +
                "u.id_usr = uc.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS bp ON " +
                "uc.id_bpr = bp.id_bpr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY u.name, uc.id_usr, uc.id_bpr, bp.name ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[6];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bp.name", DGridConsts.COL_TITLE_NAME + " emp");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_L, "uc.b_unv", "Acceso univ.");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
