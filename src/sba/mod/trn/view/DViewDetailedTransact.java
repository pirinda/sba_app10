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
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDetailedTransact extends DGridPaneView {

    private DGridFilterDateRange moFilterDateRange;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_DET_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDetailedTransact(DGuiClient client, int type, int subtype, String title) {
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
    }

    @Override
    public void prepareSqlQuery() {
        int[] docClassKey = null;
        int[] adjIncClassKey = null;
        int[] adjDecClassKey = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sqlPos = "";
        String sqlNeg = "";
        String where = "";
        String orderBy = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2, 3);

        filter = (Date[]) moFiltersMap.get(DGridConsts.FILTER_DATE_RANGE);
        where += (where.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDateRange("d.dt", (Date[]) filter);

        switch (mnGridSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                docClassKey = DModSysConsts.TS_DPS_CL_PUR_DOC;
                adjIncClassKey = DModSysConsts.TS_DPS_CL_PUR_ADJ_INC;
                adjDecClassKey = DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC;

                orderBy = "f_b_name, f_bc_code, f_b_id_bpr, " +
                        "f_dt_code, f_d_fk_dps_ct, f_d_fk_dps_cl, f_d_fk_dps_tp, f_d_ser, f_d_num, f_d_id_dps ";
                break;

            case DModSysConsts.TS_DPS_CT_SAL:
                docClassKey = DModSysConsts.TS_DPS_CL_SAL_DOC;
                adjIncClassKey = DModSysConsts.TS_DPS_CL_SAL_ADJ_INC;
                adjDecClassKey = DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC;

                orderBy = "f_dt_code, f_d_fk_dps_ct, f_d_fk_dps_cl, f_d_fk_dps_tp, f_d_ser, f_d_num, f_d_id_dps ";
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        orderBy += ", f_i_name, f_i_code, f_i_id_itm, f_iu_code, f_iu_id_unt ";

        if (mnGridType == DModConsts.TX_DET_ITM_SNR) {
            orderBy += ", f_s_snr ";
        }

        orderBy = "ORDER BY " + orderBy;

        /*
         * NOTE:
         * Query with UNION is used in order to gain performance.
         * Without UNION and the use of IF clauses in SELECT sentence,
         * response time is extremily high.
         */

        sqlPos = "d.tot_cy_r AS f_d_tot_cy_r, " +
                "d.tot_r AS f_d_tot_r, " +
                "dr.qty AS f_dr_qty, " +
                "dr.prc_unt AS f_dr_prc_unt, " +
                "dr.dsc_unt AS f_dr_dsc_unt, " +
                "dr.dsc_row AS f_dr_dsc_row, " +
                "dr.sbt_prv_r AS f_dr_sbt_prv_r, " +
                "dr.dsc_doc AS f_dr_dsc_doc, " +
                "dr.sbt_r AS f_dr_sbt_r, " +
                "dr.tax_cha_r AS f_dr_tax_cha_r, " +
                "dr.tax_ret_r AS f_dr_tax_ret_r, " +
                "dr.tot_r AS f_dr_tot_r, ";

        sqlNeg = "d.tot_cy_r * -1 AS f_d_tot_cy_r, " +
                "d.tot_r * -1 AS f_d_tot_r, " +
                "dr.qty * -1 AS f_dr_qty, " +
                "dr.prc_unt * -1 AS f_dr_prc_unt, " +
                "dr.dsc_unt * -1 AS f_dr_dsc_unt, " +
                "dr.dsc_row * -1 AS f_dr_dsc_row, " +
                "dr.sbt_prv_r * -1 AS f_dr_sbt_prv_r, " +
                "dr.dsc_doc * -1 AS f_dr_dsc_doc, " +
                "dr.sbt_r * -1 AS f_dr_sbt_r, " +
                "dr.tax_cha_r * -1 AS f_dr_tax_cha_r, " +
                "dr.tax_ret_r * -1 AS f_dr_tax_ret_r, " +
                "dr.tot_r * -1 AS f_dr_tot_r, ";

        sql1 = "SELECT " +
                "dr.id_dps AS " + DDbConsts.FIELD_ID + "1, " +
                "dr.id_row AS " + DDbConsts.FIELD_ID + "2, " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS f_doc_num, " +
                "d.id_dps AS f_d_id_dps, " +
                "d.ser AS f_d_ser, " +
                "d.num AS f_d_num, " +
                "d.dt AS f_d_dt, " +
                "d.exr AS f_d_exr, " +
                "d.pay_acc AS f_d_pay_acc, " +
                "d.fk_dps_ct AS f_d_fk_dps_ct, " +
                "d.fk_dps_cl AS f_d_fk_dps_cl, " +
                "d.fk_dps_tp AS f_d_fk_dps_tp, " +
                "dt.code AS f_dt_code, " +
                "dt.name AS f_dt_name, ";

        sql2= "b.id_bpr AS f_b_id_bpr, " +
                "b.name AS f_b_name, " +
                "b.fis_id AS f_b_fis_id, " +
                "bc.code AS f_bc_code, " +
                "bb.name AS f_bb_name, " +
                "cb.code AS f_cb_code, " +
                "c.code AS f_c_code, " +
                "payt.code AS f_payt_code, " +
                "payt.name AS f_payt_name, " +
                "mopt.code AS f_mopt_code, " +
                "mopt.name AS f_mopt_name, " +
                "emit.code AS f_emit_code, " +
                "emit.name AS f_emit_name, " +
                "i.id_itm AS f_i_id_itm, " +
                "i.code AS f_i_code, " +
                "i.name AS f_i_name, " +
                "iu.id_unt AS f_iu_id_unt, " +
                "iu.code AS f_iu_code, " +
                "iu.name AS f_iu_name, " +
                "ib.code AS f_ib_code, " +
                "ib.name AS f_ib_name, " +
                (mnGridType != DModConsts.TX_DET_ITM_SNR ? "" : (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR ? "s.mov_in - s.mov_out" : "s.mov_out - s.mov_in") + " AS f_s_qty, s.snr AS f_s_snr, ") +
                "IF(d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
                "d.fk_dps_ct AS " + DDbConsts.FIELD_TYPE_ID + "1, " +
                "d.fk_dps_cl AS " + DDbConsts.FIELD_TYPE_ID + "2, " +
                "d.fk_dps_tp AS " + DDbConsts.FIELD_TYPE_ID + "3, " +
                "dt.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "d.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "d.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "d.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "d.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "d.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "d.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
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
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "d.fk_cur = c.id_cur " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_PAY_TP) + " AS payt ON " +
                "d.fk_pay_tp = payt.id_pay_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mopt ON " +
                "d.fk_mop_tp = mopt.id_mop_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_EMI_TP) + " AS emit ON " +
                "d.fk_emi_tp = emit.id_emi_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "d.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "d.fk_usr_upd = uu.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON " +
                "d.id_dps = dr.id_dps " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "dr.fk_row_itm = i.id_itm " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS iu ON " +
                "dr.fk_row_unt = iu.id_unt " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_BRD) + " AS ib ON " +
                "i.fk_brd = ib.id_brd " +
                (mnGridType != DModConsts.TX_DET_ITM_SNR ? "" : "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s ON " +
                "s.snr <> '' AND ((s.fk_dps_inv_dps_n = dr.id_dps AND s.fk_dps_inv_row_n = dr.id_row AND s.fk_dps_adj_dps_n IS NULL AND s.fk_dps_adj_row_n IS NULL) OR " +
                "(s.fk_dps_inv_dps_n IS NOT NULL AND s.fk_dps_inv_row_n IS NOT NULL AND s.fk_dps_adj_dps_n = dr.id_dps AND s.fk_dps_adj_row_n = dr.id_row)) ") +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON " +
                "d.id_dps = dfr.fk_dps_n " +
                "WHERE d.b_del = 0 AND dr.b_del = 0 AND d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND ";

        sql3 = (where.length() == 0 ? "" : "AND " + where);

        msSql = sql1 + sqlPos + sql2 +
                "d.fk_dps_ct = " + mnGridSubtype + " AND d.fk_dps_cl IN (" + docClassKey[1] + ", " + adjIncClassKey[1] + ") " +
                sql3 +
                "UNION " +
                sql1 + sqlNeg + sql2 +
                "d.fk_dps_ct = " + mnGridSubtype + " AND d.fk_dps_cl = " + adjDecClassKey[1] + " " +
                sql3 +
                orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        int cols = 0;
        DGridColumnView[] columns = null;

        cols = 36;

        if (mnGridType == DModConsts.TX_DET_ITM_SNR) {
            cols += 2;
        }

        columns = new DGridColumnView[cols];

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "f_b_name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "f_bc_code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "f_b_fis_id", "RFC");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_bb_name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_dt_code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_doc_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "f_d_dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_cb_code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_dt_code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_doc_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "f_d_dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_cb_code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "f_b_name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "f_bc_code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "f_b_fis_id", "RFC");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_bb_name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_d_tot_cy_r", "Total $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_c_code", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_EXC_RATE, "f_d_exr", "T. cambio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_d_tot_r", "Total $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_payt_code", "Tipo pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_mopt_code", "Forma pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_d_pay_acc", "Cta pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_emit_code", "Tipo impresión");

        if (mnGridType == DModConsts.TX_DET_ITM) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "f_i_name", "Ítem");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, "f_i_code", "Código ítem");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_ib_name", "Marca");
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_dr_qty", "Cantidad");
            columns[col++].setSumApplying(true);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "f_iu_code", "Unidad");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "f_dr_prc_unt", "Precio u. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "f_dr_dsc_unt", "Descto. u. $ ML");
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_dsc_row", "Descto. par. $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_sbt_prv_r", "Importe $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_dsc_doc", "Descto. doc. $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_sbt_r", "Subtotal $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tax_cha_r", "Imptos. tras. $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tax_ret_r", "Imptos. rets. $ ML");
            columns[col++].setSumApplying(true);
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tot_r", " $Total par. ML");
            columns[col++].setSumApplying(true);
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "f_i_name", "Ítem");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, "f_i_code", "Código ítem");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_ib_name", "Marca");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_dr_qty", "Cantidad");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "f_iu_code", "Unidad");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "f_dr_prc_unt", "Precio u. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "f_dr_dsc_unt", "Descto. u. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_dsc_row", "Descto. par. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_sbt_prv_r", "Importe $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_dsc_doc", "Descto. doc. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_sbt_r", "Subtotal $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tax_cha_r", "Imptos. tras. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tax_ret_r", "Imptos. rets. $ ML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_dr_tot_r", " $Total par. ML");
            columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_s_qty", "Cantidad NS");
            columns[col++].setSumApplying(true);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "f_s_snr", "Número serie", 150);
        }

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
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_UNT);
        moSuscriptionsSet.add(DModConsts.IU_BRD);
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
