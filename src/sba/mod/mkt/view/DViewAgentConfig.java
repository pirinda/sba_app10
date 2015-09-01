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
import sba.mod.bpr.db.DBprUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewAgentConfig extends DGridPaneView {

    public DViewAgentConfig(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.M_AGT_CFG, DLibConsts.UNDEFINED, title);
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
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_agt AS " + DDbConsts.FIELD_ID + "1, " +
                "'' AS " + DDbConsts.FIELD_CODE + ", " +
                "b.name AS " + DDbConsts.FIELD_NAME + ", " +
                "b.nick, " +
                "b.fis_id, " +
                "b.alt_id, " +
                "b.b_ven, " +
                "b.b_cus, " +
                "b.b_cdr, " +
                "b.b_dbr, " +
                "vt.name, " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.M_AGT_CFG) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.MU_AGT_TP) + " AS vt ON " +
                "v.fk_agt_tp = vt.id_agt_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.id_agt = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY b.name, v.id_agt ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[14];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " agente");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "vt.name", DGridConsts.COL_TITLE_TYPE + " agente");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.alt_id", "CURP");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "b.b_cus", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_CUS));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "b.b_ven", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_VEN));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "b.b_dbr", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_VEN));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "b.b_cdr", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_CUS));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.nick", "Alias");
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
        moSuscriptionsSet.add(DModConsts.MU_AGT_TP);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
