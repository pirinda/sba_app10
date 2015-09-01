/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

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
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DViewTransact extends DGridPaneView {

    private DGridFilterDateRange moFilterDateRange;
    private DMyGridFilterSwitchTax moFilterSwitchTax;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_TRN_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewTransact(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        moFilterDateRange = new DGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(DLibTimeUtils.getWholeMonth(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDateRange);

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
        String innerJoin = "";
        String groupBy = "";
        String orderBy = "";
        Object filter = null;

        filter = (Date[]) moFiltersMap.get(DGridConsts.FILTER_DATE_RANGE);
        where += (where.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDateRange("d.dt", (Date[]) filter);

        filter = (Boolean) moFiltersMap.get(DMyGridConsts.FILTER_SWITCH_TAX);
        field = (Boolean) filter ? "dr.tot_r" : "dr.sbt_r";

        switch (mnGridType) {
            case DModConsts.TX_TRN_CO:
                moPaneSettings = new DGridPaneSettings(1);
                select = "d.fk_own_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "'' AS " + DDbConsts.FIELD_CODE + ", " +
                        "b.name AS " + DDbConsts.FIELD_NAME + ", ";
                innerJoin = "";
                groupBy = "d.fk_own_bpr, " +
                        "b.name ";
                orderBy = "b.name, " +
                        "d.fk_own_bpr ";
                break;

            case DModConsts.TX_TRN_CO_BRA:
                moPaneSettings = new DGridPaneSettings(2);
                select = "d.fk_own_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "d.fk_own_bra AS " + DDbConsts.FIELD_ID + "2, " +
                        "b.name, " +
                        "bb.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "bb.name AS " + DDbConsts.FIELD_NAME + ", ";
                innerJoin = "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb ON " +
                        "d.fk_own_bpr = bb.id_bpr AND d.fk_own_bra = bb.id_bra ";
                groupBy = "d.fk_own_bpr, " +
                        "d.fk_own_bra, " +
                        "b.name, " +
                        "bb.name, " +
                        "bb.code ";
                orderBy = "b.name, " +
                        "bb.name, " +
                        "bb.code, " +
                        "d.fk_own_bpr, " +
                        "d.fk_own_bra ";
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

        msSql = "SELECT " + select +
                "SUM(IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", " + field + ", 0)) AS f_trn, " +
                "SUM(IF(d.fk_dps_cl = " + adjDecClassKey[1] + " AND dr.fk_adj_cl = " + DModSysConsts.TS_ADJ_CL_DEC_DIS[1] + ", " + field + ", 0)) AS f_adj_dec_dis, " +
                "SUM(IF(d.fk_dps_cl = " + adjDecClassKey[1] + " AND dr.fk_adj_cl = " + DModSysConsts.TS_ADJ_CL_DEC_RET[1] + ", " + field + ", 0)) AS f_adj_dec_dev, " +
                "SUM(" + field + " * IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", 1, -1)) AS f_trn_net, " +
                "SUM(IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", dr.qty, 0)) AS f_qty, " +
                "SUM(IF(d.fk_dps_cl = " + adjDecClassKey[1] + ", dr.qty, 0)) AS f_qty_dev, " +
                "SUM(dr.qty * IF(d.fk_dps_cl = " + docClassKey[1] + " OR d.fk_dps_cl = " + adjIncClassKey[1] + ", 1, -1)) AS f_qty_net " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON " +
                "d.id_dps = dr.id_dps " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "d.fk_own_bpr = b.id_bpr " +
                innerJoin +
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

        cols = 7;

        switch (mnGridType) {
            case DModConsts.TX_TRN_CO:
                columns = new DGridColumnView[cols + 1];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " empresa");
                break;

            case DModConsts.TX_TRN_CO_BRA:
                columns = new DGridColumnView[cols + 3];
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " empresa");
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
                columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_trn", "Importe $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_adj_dec_dis", "Descuentos $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_adj_dec_dev", "Devoluciones $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_trn_net", "Importe neto $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_qty", "Cantidad");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_qty_dev", "Cantidad dev.");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_qty_net", "Cantidad neta");
        columns[col++].setSumApplying(true);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.T_DPS);
    }
}
