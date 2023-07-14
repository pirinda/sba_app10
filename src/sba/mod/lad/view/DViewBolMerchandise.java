/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.view;

import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDatePeriod;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewBolMerchandise extends DGridPaneView {

    private DGridFilterDatePeriod moFilterDatePeriod;

    /**
     * @param client GUI Client.
     * @param title Title.
     */
    public DViewBolMerchandise(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.L_BOL_MERCH, 0, title, null);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlx = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(3);

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            sql += (sql.isEmpty() ? "" : "AND ") + DGridUtils.getSqlFilterDate("b.dt", (DGuiDate) filter);
            sqlx += (sqlx.isEmpty() ? "" : "AND ") + DGridUtils.getSqlFilterDate("bx.dt", (DGuiDate) filter);
        }
        
        msSql = "SELECT "
                + "i.name AS " + DDbConsts.FIELD_NAME + ", "
                + "i.code AS " + DDbConsts.FIELD_CODE + ", "
                + "i.id_itm AS " + DDbConsts.FIELD_ID + "1, "
                + "u.name, "
                + "u.code, "
                + "u.id_unt AS " + DDbConsts.FIELD_ID + "2, "
                + "bld.name, "
                + "bld.code, "
                + "bld.code_des, "
                + "bld.id_loc AS " + DDbConsts.FIELD_ID + "3, "
                + "SUM(bm.qty) AS _qty, "
                + "SUM(bm.weight_kg) AS _weight_kg, "
                + "COUNT(*) AS _count "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL) + " AS b "
                + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH) + " AS bm ON bm.id_bol = b.id_bol "
                + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON i.id_itm = bm.fk_itm "
                + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON u.id_unt = bm.fk_unt "
                + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON dfr.fk_bol_n = b.id_bol "
                + "LEFT OUTER JOIN ("
                + " SELECT bx.id_bol, l.id_loc, l.name, l.code, l.code_des "
                + " FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL) + " AS bx "
                + " INNER JOIN " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " AS bl ON bl.id_bol = bx.id_bol "
                + " INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS l ON l.id_loc = bl.fk_loc "
                + " INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfrx ON dfrx.fk_bol_n = bx.id_bol "
                + " WHERE NOT bx.b_del AND NOT bx.b_temp "
                + " AND " + sqlx
                + " AND dfrx.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + " "
                + " AND bl.fk_loc_tp = " + DModSysConsts.LS_LOC_TP_DES + " "
                + " ORDER BY bl.id_bol, bl.id_loc DESC) AS bld ON bld.id_bol = b.id_bol "
                + "WHERE NOT b.b_del AND NOT b.b_temp "
                + "AND " + sql
                + "AND dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + " "
                + "GROUP BY i.name, i.code, i.id_itm, bld.name, bld.code, bld.code_des, bld.id_loc, u.name, u.code, u.id_unt "
                + "ORDER BY i.name, i.code, i.id_itm, bld.name, bld.code, bld.code_des, bld.id_loc, u.name, u.code, u.id_unt;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[8];
        
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, DDbConsts.FIELD_NAME, "Nombre ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DDbConsts.FIELD_CODE, "Código ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "bld.name", "Nombre destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "bld.code_des", "Código destino", 65);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "_qty", "Cantidad");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "_weight_kg", "Peso (kg)");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_4B, "_count", "Nodos mercancías");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.L_BOL);
        moSuscriptionsSet.add(DModConsts.T_DFR);
        moSuscriptionsSet.add(DModConsts.LU_LOC);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_UNT);
    }
}
