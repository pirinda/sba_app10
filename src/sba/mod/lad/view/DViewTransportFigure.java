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
public class DViewTransportFigure extends DGridPaneView implements ActionListener {
    
    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewTransportFigure(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.LU_TPT_FIGURE, 0, title, null);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "tf.b_del = 0 ";
        }

        msSql = "SELECT " +
                "tf.id_tpt_figure AS " + DDbConsts.FIELD_ID + "1, " +
                "tf.code AS " + DDbConsts.FIELD_CODE + ", " +
                "tf.name AS " + DDbConsts.FIELD_NAME + ", " +
                "tf.fis_id, " +
                "tf.frg_id, " +
                "tf.drv_lic, " +
                "tf.add_str, " +
                "tf.add_num_ext, " +
                "tf.add_num_int, " +
                "tf.add_dist_code, " +
                "tf.add_dist_name, " +
                "tf.add_loc_code, " +
                "tf.add_loc_name, " +
                "tf.add_ref, " +
                "tf.add_cou_code, " +
                "tf.add_cou_name, " +
                "tf.add_ste_code, " +
                "tf.add_ste_name, " +
                "tf.add_zip, " +
                "tf.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "tf.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "tf.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "tf.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "tf.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "tft.code, " +
                "tft.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "fc.code, " +
                "fc.name, " +
                "ac.code, " +
                "ac.name, " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TPT_FIGURE) + " AS tf " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LS_TPT_FIGURE_TP) + " AS tft ON " +
                "tft.id_tpt_figure_tp = tf.fk_tpt_figure_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS fc ON " +
                "fc.id_cty = tf.fk_figure_cty " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS ac ON " +
                "ac.id_cty = tf.fk_add_cty " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "ui.id_usr = tf.fk_usr_ins " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "uu.id_usr = tf.fk_usr_upd " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY tf.name, tf.code, tf.id_tpt_figure;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[27];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_TYPE, DGridConsts.COL_TITLE_TYPE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.fis_id", "RFC");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.drv_lic", "Licencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "fc.code", "País clave figura transporte");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fc.name", "País descripción figura transporte");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.frg_id", "ID fiscal");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "tf.add_zip", "CP");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "tf.add_str", "Calle");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "tf.add_num_ext", "No. exterior");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "tf.add_num_int", "No. interior");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "tf.add_dist_code", "Colonia clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "tf.add_dist_name", "Colonia descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "tf.add_loc_code", "Localidad clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.add_loc_name", "Localidad descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "tf.add_cou_code", "Municipio clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.add_cou_name", "Municipio descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "tf.add_ste_code", "Estado clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tf.add_ste_name", "Estado descripción");
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
