/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.util.Calendar;
import java.util.Date;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterSwitchTax;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDateRange;
import sba.lib.grid.DGridFilterYear;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewPerMonthTransactAgent extends DGridPaneView {

    private String[] masMonths;
    private DGridFilterYear moFilterYear;
    private DMyGridFilterSwitchTax moFilterSwitchTax;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_PMO_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewPerMonthTransactAgent(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

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
            case DModConsts.TX_PMO_AGT_TP:
                moPaneSettings = new DGridPaneSettings(1);
                select = "at.id_agt_tp AS " + DDbConsts.FIELD_ID + "1, " +
                        "'' AS " + DDbConsts.FIELD_CODE + ", " +
                        "COALESCE(at.name, '" + DUtilConsts.NON_APPLYING + "') AS " + DDbConsts.FIELD_NAME + ", ";
                groupBy = "at.id_agt_tp, at.name ";
                orderBy = "at.name, at.id_agt_tp ";
                break;

            case DModConsts.TX_PMO_AGT:
                moPaneSettings = new DGridPaneSettings(1);
                select = "a.id_bpr " + DDbConsts.FIELD_ID + "1, " +
                        "'' " + DDbConsts.FIELD_CODE + ", " +
                        "COALESCE(a.name, '" + DUtilConsts.NON_APPLYING + "') AS " + DDbConsts.FIELD_NAME + ", " +
                        "at.id_agt_tp, " +
                        "at.name, ";
                groupBy = "a.id_bpr, a.name, at.id_agt_tp, at.name ";
                orderBy = "a.name, a.id_bpr ";
                break;

            case DModConsts.TX_PMO_AGT_ITM:
                moPaneSettings = new DGridPaneSettings(1);
                select = "a.id_bpr " + DDbConsts.FIELD_ID + "1, " +
                        "'' " + DDbConsts.FIELD_CODE + ", " +
                        "COALESCE(a.name, '" + DUtilConsts.NON_APPLYING + "') AS " + DDbConsts.FIELD_NAME + ", " +
                        "at.id_agt_tp, " +
                        "at.name, " +
                        "i.id_itm, " +
                        "i.name, " +
                        "i.code, ";
                groupBy = "a.id_bpr, a.name, at.id_agt_tp, at.name, i.id_itm, i.name, i.code ";
                orderBy = "a.name, a.id_bpr, i.name, i.code, i.id_itm ";
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
                (mnGridType != DModConsts.TX_PMO_AGT_ITM ? "" :
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                    "dr.fk_row_itm = i.id_itm ") +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS a ON " +
                "d.fk_agt_n = a.id_bpr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.M_AGT_CFG) + " AS ac ON " +
                "a.id_bpr = ac.id_agt " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.MU_AGT_TP) + " AS at ON " +
                "ac.fk_agt_tp = at.id_agt_tp " +
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
            case DModConsts.TX_PMO_AGT_TP:
                columns = new DGridColumnView[cols + 1];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_TYPE + " agente");
                break;

            case DModConsts.TX_PMO_AGT:
                columns = new DGridColumnView[cols + 2];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " agente");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "at.name", DGridConsts.COL_TITLE_TYPE + " agente");
                break;

            case DModConsts.TX_PMO_AGT_ITM:
                columns = new DGridColumnView[cols + 4];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " agente");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "at.name", DGridConsts.COL_TITLE_TYPE + " agente");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "i.name", DGridConsts.COL_TITLE_NAME + " ítem");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", DGridConsts.COL_TITLE_CODE + " ítem");
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
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.MU_AGT_TP);

        if (mnGridType == DModConsts.TX_PMO_AGT_ITM) {
            moSuscriptionsSet.add(DModConsts.IU_ITM);
        }
    }
}
