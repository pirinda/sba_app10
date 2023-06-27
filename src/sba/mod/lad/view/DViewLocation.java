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
public class DViewLocation extends DGridPaneView implements ActionListener {
    
    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewLocation(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.LU_LOC, 0, title, null);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "l.b_del = 0 ";
        }

        msSql = "SELECT " +
                "l.id_loc AS " + DDbConsts.FIELD_ID + "1, " +
                "l.code AS " + DDbConsts.FIELD_CODE + ", " +
                "l.code_src, " +
                "l.code_des, " +
                "l.name AS " + DDbConsts.FIELD_NAME + ", " +
                "l.add_str, " +
                "l.add_num_ext, " +
                "l.add_num_int, " +
                "l.add_dist_code, " +
                "l.add_dist_name, " +
                "l.add_loc_code, " +
                "l.add_loc_name, " +
                "l.add_ref, " +
                "l.add_cou_code, " +
                "l.add_cou_name, " +
                "l.add_ste_code, " +
                "l.add_ste_name, " +
                "l.add_zip, " +
                "l.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "l.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "l.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "l.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "l.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "lt.code, " +
                "lt.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "ac.code, " +
                "ac.name, " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS l " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LS_LOC_TP) + " AS lt ON " +
                "lt.id_loc_tp = l.fk_loc_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS ac ON " +
                "ac.id_cty = l.fk_add_cty " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "ui.id_usr = l.fk_usr_ins " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "uu.id_usr = l.fk_usr_upd " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY l.name, l.code, l.id_loc;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[22];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_TYPE, DGridConsts.COL_TITLE_TYPE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "l.add_zip", "CP");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "l.add_str", "Calle");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "l.add_num_ext", "No. exterior");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "l.add_num_int", "No. interior");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "l.add_dist_code", "Colonia clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "l.add_dist_name", "Colonia descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "l.add_loc_code", "Localidad clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "l.add_loc_name", "Localidad descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "l.add_cou_code", "Municipio clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "l.add_cou_name", "Municipio descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "l.add_ste_code", "Estado clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "l.add_ste_name", "Estado descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "ac.code", "País clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ac.name", "País descripción");
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
