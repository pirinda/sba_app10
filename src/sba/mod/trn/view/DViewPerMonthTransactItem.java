/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.util.Calendar;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterSwitchTax;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterYear;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiParams;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewPerMonthTransactItem extends DGridPaneView {

    private String[] masMonths;
    private DGridFilterYear moFilterYear;
    private DMyGridFilterSwitchTax moFilterSwitchTax;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_PMO_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewPerMonthTransactItem(DGuiClient client, int type, int subtype, String title, DGuiParams params) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title, params);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        masMonths = DLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT);

        moFilterYear = new DGridFilterYear(miClient, this);
        moFilterYear.initFilter(DLibTimeUtils.digestYear(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterYear);

        moFilterSwitchTax = new DMyGridFilterSwitchTax(client, this);
        moFilterSwitchTax.initFilter(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isTaxIncluded());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterSwitchTax);
    }

    @Override
    public void prepareSqlQuery() {
        int[] docClassKey = null;
        int[] adjIncClassKey = null;
        int[] adjDecClassKey = null;
        String field = "";
        String where = "";
        String select = "";
        String groupBy = "";
        String orderBy = "";
        Object filter = null;

        filter = (int[]) moFiltersMap.get(DGridConsts.FILTER_YEAR);
        where += (where.length() == 0 ? "" : "AND ") + "YEAR(d.dt) = " + ((int[]) filter)[0] + " ";

        filter = (Boolean) moFiltersMap.get(DMyGridConsts.FILTER_SWITCH_TAX);
        field = (Boolean) filter ? "dr.tot_r" : "dr.sbt_r";

        switch (mnGridType) {
            case DModConsts.TX_PMO_ITM_GEN:
                moPaneSettings = new DGridPaneSettings(1);
                select = "ig.id_gen AS " + DDbConsts.FIELD_ID + "1, " +
                        "ig.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "ig.name AS " + DDbConsts.FIELD_NAME + ", ";
                groupBy = "ig.id_gen, ig.name, ig.code ";
                orderBy = "ig.name, ig.code, ig.id_gen ";
                break;

            case DModConsts.TX_PMO_ITM_LIN:
                moPaneSettings = new DGridPaneSettings(1);
                select = "ig.id_gen, " +
                        "ig.name, " +
                        "il.id_lin AS " + DDbConsts.FIELD_ID + "1, " +
                        "il.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "COALESCE(il.name, '" + DUtilConsts.NON_APPLYING + "') AS " + DDbConsts.FIELD_NAME + ", ";
                groupBy = "il.id_lin, il.name, il.code, ig.id_gen, ig.name ";
                orderBy = "il.name, il.code, il.id_lin ";
                break;

            case DModConsts.TX_PMO_ITM:
                moPaneSettings = new DGridPaneSettings(1);
                select = "ig.id_gen, " +
                        "ig.name, " +
                        "il.id_lin, " +
                        "COALESCE(il.name, '" + DUtilConsts.NON_APPLYING + "') AS f_lin, " +
                        "i.id_itm AS " + DDbConsts.FIELD_ID + "1, " +
                        "i.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "i.name AS " + DDbConsts.FIELD_NAME + ", ";
                groupBy = "i.id_itm, i.name, i.code, il.id_lin, il.name, ig.id_gen, ig.name ";
                orderBy = "i.name, i.code, i.id_itm ";
                break;

            case DModConsts.TX_PMO_ITM_BPR:
                moPaneSettings = new DGridPaneSettings(1);
                select = "ig.id_gen, " +
                        "ig.name, " +
                        "il.id_lin, " +
                        "COALESCE(il.name, '" + DUtilConsts.NON_APPLYING + "') AS f_lin, " +
                        "i.id_itm AS " + DDbConsts.FIELD_ID + "1, " +
                        "i.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "i.name AS " + DDbConsts.FIELD_NAME + ", " +
                        "b.id_bpr, " +
                        "b.name, " +
                        "bc.code, ";
                groupBy = "i.id_itm, i.name, i.code, il.id_lin, il.name, ig.id_gen, ig.name, b.id_bpr, b.name, bc.code ";
                orderBy = "i.name, i.code, i.id_itm, b.name, b.id_bpr ";
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        switch (mnGridSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                docClassKey = DModSysConsts.TS_DPS_CL_PUR_DOC;
                adjIncClassKey = DModSysConsts.TS_DPS_CL_PUR_ADJ_INC;
                adjDecClassKey = DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC;
                break;

            case DModSysConsts.TS_DPS_CT_SAL:
                docClassKey = DModSysConsts.TS_DPS_CL_SAL_DOC;
                adjIncClassKey = DModSysConsts.TS_DPS_CL_SAL_ADJ_INC;
                adjDecClassKey = DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC;
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql = "SELECT " + select;

        for (int i = 0; i < 12; i++) {
            msSql += "SUM(IF(MONTH(d.dt) <> " + (i + 1) + ", 0, " + field + " * IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", 1, -1))) AS f_trn_net_" + i + ", " +
                    "SUM(IF(MONTH(d.dt) <> " + (i + 1) + ", 0, dr.qty * IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", 1, -1))) AS f_qty_net_" + i;
            msSql += i + 1 < 12 ? ", " : " ";
        }

        msSql += "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON " +
                "d.id_dps = dr.id_dps " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "dr.fk_row_itm = i.id_itm " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS ig ON " +
                "i.fk_gen = ig.id_gen " +
                (mnGridType != DModConsts.TX_PMO_ITM_BPR ? "" :
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON d.fk_bpr_bpr = b.id_bpr " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "b.id_bpr = bc.id_bpr AND bc.id_bpr_cl = " + DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype) + " ") +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_LIN) + " AS il ON " +
                "i.fk_lin_n = il.id_lin " +
                "WHERE d.b_del = 0 AND dr.b_del = 0 AND d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND " +
                "d.fk_dps_ct = " + mnGridSubtype + " AND d.fk_dps_cl IN (" + docClassKey[1] + ", " + adjIncClassKey[1] + ", " + adjDecClassKey[1] + ") " +
                (where.length() == 0 ? "" : "AND " + where) +
                "GROUP BY " + groupBy +
                "ORDER BY " + orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        int cols = 0;
        DGridColumnView[] columns = null;

        cols = (2 * 12);

        switch (mnGridType) {
            case DModConsts.TX_PMO_ITM_GEN:
                columns = new DGridColumnView[cols + 2];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, "Género ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, "Código género ítems");
                break;

            case DModConsts.TX_PMO_ITM_LIN:
                columns = new DGridColumnView[cols + 3];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, "Línea ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, "Código línea ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ig.name", "Género ítems");
                break;

            case DModConsts.TX_PMO_ITM:
                columns = new DGridColumnView[cols + 4];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, DDbConsts.FIELD_NAME, "Ítem");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DDbConsts.FIELD_CODE, "Código ítem");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_lin", "Línea ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ig.name", "Género ítems");
                break;

            case DModConsts.TX_PMO_ITM_BPR:
                columns = new DGridColumnView[cols + 6];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, DDbConsts.FIELD_NAME, "Ítem");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DDbConsts.FIELD_CODE, "Código ítem");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_lin", "Línea ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ig.name", "Género ítems");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        for (int i = 0; i < 12; i++) {
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_trn_net_" + i, DTrnUtils.getDpsCategoryName(mnGridSubtype) + " " + masMonths[i] + " $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_qty_net_" + i, "Cantidad " + masMonths[i]);
            columns[col++].setSumApplying(true);
        }

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.IU_GEN);
        moSuscriptionsSet.add(DModConsts.IU_LIN);
        moSuscriptionsSet.add(DModConsts.IU_ITM);

        if (mnGridType == DModConsts.TX_PMO_ITM_BPR) {
            moSuscriptionsSet.add(DModConsts.BU_BPR);
        }
    }
}
