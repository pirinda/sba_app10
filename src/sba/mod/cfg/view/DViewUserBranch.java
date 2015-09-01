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
public class DViewUserBranch extends DGridPaneView {

    public DViewUserBranch(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.CU_USR_BRA, DLibConsts.UNDEFINED, title);

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

        moPaneSettings = new DGridPaneSettings(3);
        moPaneSettings.setDeletedApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "u.b_del = 0 ";
        }

        msSql = "SELECT " +
                "ub.id_usr AS " + DDbConsts.FIELD_ID + "1, " +
                "ub.id_bpr AS " + DDbConsts.FIELD_ID + "2, " +
                "ub.id_bra AS " + DDbConsts.FIELD_ID + "3, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "u.name AS " + DDbConsts.FIELD_NAME + ", " +
                "bpb.id_bpr, " +
                "bpb.code, " +
                "bpb.name, " +
                "ub.b_unv_csh, " +
                "ub.b_unv_wah, " +
                "ub.b_unv_ser_bra, " +
                "u.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "u.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "u.b_sys AS " + DDbConsts.FIELD_IS_SYS + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_BRA) + " AS ub ON " +
                "u.id_usr = ub.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bpb ON " +
                "ub.id_bpr = bpb.id_bpr AND ub.id_bra = bpb.id_bra " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY u.name, ub.id_usr, ub.id_bpr, ub.id_bra, bpb.name ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[9];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bpb.name", DGridConsts.COL_TITLE_NAME + " suc");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "bpb.code", DGridConsts.COL_TITLE_CODE + " suc");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_L, "ub.b_unv_csh", "Acceso univ. cuentas dinero");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_L, "ub.b_unv_wah", "Acceso univ. almacenes");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_L, "ub.b_unv_ser_bra", "Acceso univ. series doctos");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
