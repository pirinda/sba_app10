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
public class DViewCurrencyDenomination extends DGridPaneView {

    public DViewCurrencyDenomination(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.CS_CUR_DEN, DLibConsts.UNDEFINED, title);
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
        moPaneSettings.setUserApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_cur AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_den AS " + DDbConsts.FIELD_ID + "2, " +
                "c.code AS " + DDbConsts.FIELD_CODE + ", " +
                "c.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.den, " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.fk_usr AS " + DDbConsts.FIELD_USER_USR_ID + ", " +
                "v.ts_usr AS " + DDbConsts.FIELD_USER_USR_TS + ", " +
                "u.name AS " + DDbConsts.FIELD_USER_USR_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR_DEN) + " AS v ON " +
                "c.id_cur = v.id_cur " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS u ON " +
                "v.fk_usr = u.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY c.name, c.id_cur, v.den, v.id_cur, v.id_den ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[6];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.den", "Denominación");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_USR_NAME, DGridConsts.COL_TITLE_USER_USR_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_USR_TS, DGridConsts.COL_TITLE_USER_USR_TS);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.CS_CUR);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
