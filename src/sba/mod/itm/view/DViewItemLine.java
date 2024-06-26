/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.itm.view;

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
public class DViewItemLine extends DGridPaneView {

    public DViewItemLine(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.IU_LIN, DLibConsts.UNDEFINED, title);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1);
        moPaneSettings.setUpdatableApplying(true);
        moPaneSettings.setDisableableApplying(true);
        moPaneSettings.setDeletableApplying(true);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " +
                "v.id_lin AS " + DDbConsts.FIELD_ID + "1, " +
                "v.code AS " + DDbConsts.FIELD_CODE + ", " +
                "v.name AS " + DDbConsts.FIELD_NAME + ", " +
                "gen.name, " +
                "brd.name, " +
                "mfr.name, " +
                "cmp.name, " +
                "dep.name, " +
                "unt.code, " +
                "v.cfd_itm_key, " +
                "v.b_buk, " +
                "v.b_inv, " +
                "v.b_lot, " +
                "v.b_snr, " +
                "v.b_fre_prc, " +
                "v.b_fre_dsc, " +
                "v.b_fre_cmm, " +
                "CONCAT(xcs.code, ' - ', xcs.name) AS _xcs_name, " +
                "CONCAT(xpf.code, ' - ', xpf.name) AS _xpf_name, " +
                "CONCAT(xsc.code, ' - ', xsc.name) AS _xsc_name, " +
                "v.b_can_upd AS " + DDbConsts.FIELD_CAN_UPD + ", " +
                "v.b_can_dis AS " + DDbConsts.FIELD_CAN_DIS + ", " +
                "v.b_can_del AS " + DDbConsts.FIELD_CAN_DEL + ", " +
                "v.b_dis AS " + DDbConsts.FIELD_IS_DIS + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.IU_LIN) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS gen ON " +
                "v.fk_gen = gen.id_gen " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_BRD) + " AS brd ON " +
                "v.fk_brd = brd.id_brd " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_MFR) + " AS mfr ON " +
                "v.fk_mfr = mfr.id_mfr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_CMP) + " AS cmp ON " +
                "v.fk_cmp = cmp.id_cmp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_DEP) + " AS dep ON " +
                "v.fk_dep = dep.id_dep " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS unt ON " +
                "v.fk_unt = unt.id_unt " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LS_XCC_COFEPRIS_SECT) + " AS xcs ON " +
                "v.fk_xcc_cofepris_sect = xcs.id_xcc_cofepris_sect " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LS_XCC_PHARM_FORM) + " AS xpf ON " +
                "v.fk_xcc_pharm_form = xpf.id_xcc_pharm_form " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LS_XCC_SPEC_COND) + " AS xsc ON " +
                "v.fk_xcc_spec_cond = xsc.id_xcc_spec_cond " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY v.name, v.code, v.id_lin ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[26];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "gen.name", "Género ítems");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "brd.name", "Marca");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mfr.name", "Fabricante");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cmp.name", "Componente");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dep.name", "Departamento");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "unt.code", "Unidad");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.cfd_itm_key", "Clave ProdServ (SAT)");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_xcs_name", "Sector COFEPRIS");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_xpf_name", "Forma farmacéutica");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_xsc_name", "Condiciones especiales");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_buk", "A granel");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_inv", "Inventariable");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_lot", "Lotes");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_snr", "Números serie");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_fre_prc", "Sin precios");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_fre_dsc", "Sin descuentos");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_fre_cmm", "Sin comisiones");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DIS, DGridConsts.COL_TITLE_IS_DIS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_SYS, DGridConsts.COL_TITLE_IS_SYS);
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
        moSuscriptionsSet.add(DModConsts.IU_GEN);
        moSuscriptionsSet.add(DModConsts.IU_BRD);
        moSuscriptionsSet.add(DModConsts.IU_MFR);
        moSuscriptionsSet.add(DModConsts.IU_DEP);
        moSuscriptionsSet.add(DModConsts.IU_CMP);
        moSuscriptionsSet.add(DModConsts.IU_UNT);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
