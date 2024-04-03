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
public class DViewTruck extends DGridPaneView implements ActionListener {
    
    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewTruck(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.LU_TRUCK, 0, title, null);
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
                "t.id_truck AS " + DDbConsts.FIELD_ID + "1, " +
                "t.code AS " + DDbConsts.FIELD_CODE + ", " +
                "t.name AS " + DDbConsts.FIELD_NAME + ", " +
                "t.weight_ton, " +
                "t.plate, " +
                "t.model, " +
                "t.tpt_config_code, " +
                "t.tpt_config_name, " +
                "t.perm_tp_code, " +
                "t.perm_tp_name, " +
                "t.perm_num, " +
                "t.civil_insurance, " +
                "t.civil_policy, " +
                "t.envir_insurance, " +
                "t.envir_policy, " +
                "t.cargo_insurance, " +
                "t.cargo_policy, " +
                "t.prime, " +
                "t.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "t.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "t.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "t.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "t.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK) + " AS t " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "ui.id_usr = t.fk_usr_ins " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "uu.id_usr = t.fk_usr_upd " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY t.name, t.code, t.id_truck;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[22];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "t.weight_ton", "Peso (Tn)");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.plate", "Placas");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_CAL_YEAR, "t.model", "Modelo");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t.tpt_config_code", "Config. vehicular clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "t.tpt_config_name", "Config. vehicular descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "t.perm_tp_code", "Tipo permiso clave");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "t.perm_tp_name", "Tipo permiso descripción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.perm_num", "No. permiso");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "t.civil_insurance", "Aseguradora responsabilidad civil");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.civil_policy", "Póliza civil");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "t.envir_insurance", "Aseguradora daños ambiente");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.envir_policy", "Póliza daños ambiente");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "t.cargo_insurance", "Aseguradora carga");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "t.cargo_policy", "Póliza carga");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "t.prime", "Prima");
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
