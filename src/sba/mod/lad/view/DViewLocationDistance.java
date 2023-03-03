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
public class DViewLocationDistance extends DGridPaneView implements ActionListener {
    
    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewLocationDistance(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.LU_LOC_DIST, 0, title, null);
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

        moPaneSettings = new DGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "ld.b_del = 0 ";
        }

        msSql = "SELECT " +
                "ld.id_loc_src AS " + DDbConsts.FIELD_ID + "1, " +
                "ld.id_loc_des AS " + DDbConsts.FIELD_ID + "2, " +
                "CONCAT(lsrc.code, ' - ', ldes.code) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(lsrc.name, ' - ', ldes.name) AS " + DDbConsts.FIELD_NAME + ", " +
                "ld.dist_km, " +
                "lsrc.add_dist_name, " +
                "lsrc.add_zip, " +
                "lsrc.add_loc_name, " +
                "lsrc.add_cou_name, " +
                "lsrc.add_ste_name, " +
                "ldes.add_dist_name, " +
                "ldes.add_zip, " +
                "ldes.add_loc_name, " +
                "ldes.add_cou_name, " +
                "ldes.add_ste_name, " +
                "ld.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "ld.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "ld.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "ld.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "ld.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "acsrc.code, " +
                "acsrc.name, " +
                "acdes.code, " +
                "acdes.name, " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.LU_LOC_DIST) + " AS ld " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS lsrc ON " +
                "lsrc.id_loc = ld.id_loc_src " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS ldes ON " +
                "ldes.id_loc = ld.id_loc_des " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS acsrc ON " +
                "acsrc.id_cty = lsrc.fk_add_cty " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS acdes ON " +
                "acdes.id_cty = ldes.fk_add_cty " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "ui.id_usr = ld.fk_usr_ins " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "uu.id_usr = ld.fk_usr_upd " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY lsrc.name, ldes.name, lsrc.code, ldes.code, ld.id_loc_src, ld.id_loc_des;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[20];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE, 100);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_2D, "ld.dist_km", "Distancia km");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "lsrc.add_dist_name", "Colonia origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "lsrc.add_zip", "CP origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lsrc.add_loc_name", "Localidad origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lsrc.add_cou_name", "Municipio origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lsrc.add_ste_name", "Estado origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "acsrc.name", "País origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "ldes.add_dist_name", "Colonia destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "ldes.add_zip", "CP destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ldes.add_loc_name", "Localidad destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ldes.add_cou_name", "Municipio destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ldes.add_ste_name", "Estado destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "acdes.name", "País destino");
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
        moSuscriptionsSet.add(DModConsts.LU_LOC);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
