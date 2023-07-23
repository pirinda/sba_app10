/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
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
public class DViewTrailer extends DGridPaneView implements ActionListener {
    
    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewTrailer(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.LU_TRAIL, 0, title, null);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

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
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        msSql = "SELECT " +
                "t.id_trail AS " + DDbConsts.FIELD_ID + "1, " +
                "t.code AS " + DDbConsts.FIELD_CODE + ", " +
                "t.name AS " + DDbConsts.FIELD_NAME + ", " +
                "t.plate, " +
                "t.trail_stp_code, " +
                "t.trail_stp_name, " +
                "t.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "t.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "t.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "t.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "t.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRAIL) + " AS t " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "ui.id_usr = t.fk_usr_ins " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "uu.id_usr = t.fk_usr_upd " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY t.name, t.code, t.id_trail;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[10];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.plate", "Placas");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t.trail_stp_code", "Subtipo clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "t.trail_stp_name", "Subtipo descripción");
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
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
