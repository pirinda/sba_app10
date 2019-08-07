/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.util.Date;
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
import sba.mod.bpr.db.DBprUtils;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewTransactSerialNumber extends DGridPaneView {

    private DGridFilterDateRange moFilterDateRange;

    /**
     * @param client GUI client.
     * @param dpsCategory DPS category as view subtype.
     * @param title View title.
     */
    public DViewTransactSerialNumber(DGuiClient client, int dpsCategory, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TX_TRC_SNR, dpsCategory, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        moFilterDateRange = new DGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(DLibTimeUtils.getWholeMonth(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        
        if (((DDbConfigCompany) miClient.getSession().getConfigCompany()).getRepSnrGenuses().isEmpty()) {
            miClient.showMsgBoxWarning("¡No se han configurado los ID de géneros de ítems para este reporte!");
        }
    }

    @Override
    public void prepareSqlQuery() {
        int[] docTypeKey = null;
        String where = "";
        Object filter = null;

        filter = (Date[]) moFiltersMap.get(DGridConsts.FILTER_DATE_RANGE);
        where += (where.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDateRange("d.dt", (Date[]) filter);

        moPaneSettings = new DGridPaneSettings(8);

        switch (mnGridSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                docTypeKey = DModSysConsts.TS_DPS_TP_PUR_DOC_INV;
                break;

            case DModSysConsts.TS_DPS_CT_SAL:
                docTypeKey = DModSysConsts.TS_DPS_TP_SAL_DOC_INV;
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql = "SELECT " +
                "s.id_yer AS " + DDbConsts.FIELD_ID + "1, " +
                "s.id_itm AS " + DDbConsts.FIELD_ID + "2, " +
                "s.id_unt AS " + DDbConsts.FIELD_ID + "3, " +
                "s.id_lot AS " + DDbConsts.FIELD_ID + "4, " +
                "s.id_bpr AS " + DDbConsts.FIELD_ID + "5, " +
                "s.id_bra AS " + DDbConsts.FIELD_ID + "6, " +
                "s.id_wah AS " + DDbConsts.FIELD_ID + "7, " +
                "s.id_mov AS " + DDbConsts.FIELD_ID + "8, " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS _num, " +
                "d.dt, " +
                "dt.code, " +
                "dt.name, " +
                "b.id_bpr, " +
                "b.name, " +
                "b.fis_id, " +
                "bc.code, " +
                "bb.name, " +
                "cb.code, " +
                "i.code, " +
                "i.name, " +
                "ib.name, " +
                "ig.name, " +
                "CONCAT('''', s.snr) as _snr, " +
                "IF(d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS _ico " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dt ON " +
                "d.fk_dps_ct = dt.id_dps_ct AND d.fk_dps_cl = dt.id_dps_cl AND d.fk_dps_tp = dt.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "d.fk_bpr_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "d.fk_bpr_bpr = bc.id_bpr AND bc.id_bpr_cl = " + DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype) + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb ON " +
                "d.fk_bpr_bpr = bb.id_bpr AND d.fk_bpr_bra = bb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "d.fk_own_bpr = cb.id_bpr AND d.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON " +
                "d.id_dps = dr.id_dps " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "dr.fk_row_itm = i.id_itm " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS ig ON " +
                "i.fk_gen = ig.id_gen " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_BRD) + " AS ib ON " +
                "i.fk_brd = ib.id_brd " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s ON " +
                "s.fk_dps_inv_dps_n = dr.id_dps AND s.fk_dps_inv_row_n = dr.id_row " +
                "WHERE NOT d.b_del AND NOT dr.b_del AND " + where + " AND " +
                "d.fk_dps_ct = " + docTypeKey[0] + " AND d.fk_dps_cl = " + docTypeKey[1] + " AND d.fk_dps_tp = " + docTypeKey[2] + " AND " +
                "ig.id_gen IN (" + ((DDbConfigCompany) miClient.getSession().getConfigCompany()).getRepSnrGenuses() + ") AND s.snr <> '' " +
                "ORDER BY d.ser, d.num, d.dt, i.code, i.name, ib.name, ig.name, s.snr; ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[14];
        
        String category =  DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase();
        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "d.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "_ico", DGridConsts.COL_TITLE_STAT + " docto");
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "d.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + category);
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", DGridConsts.COL_TITLE_CODE + " ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", DGridConsts.COL_TITLE_NAME + " ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ib.name", "Marca");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ig.name", "Género ítems");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_snr", "Número serie");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_GEN);
        moSuscriptionsSet.add(DModConsts.IU_BRD);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.T_DPS_PRT);
        moSuscriptionsSet.add(DModConsts.T_DPS_SIG);
    }
}
