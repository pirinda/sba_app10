/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.view;

import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewPromoPackagePromo extends DGridPaneView {

    public DViewPromoPackagePromo(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.M_PRM_PRM, DLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new DGridPaneSettings(4);

        msSql = "SELECT " +
                "v.id_prm AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_pay_tp AS " + DDbConsts.FIELD_ID + "2, " +
                "v.id_lnk_itm_tp AS " + DDbConsts.FIELD_ID + "3, " +
                "v.id_ref_itm AS " + DDbConsts.FIELD_ID + "4, " +
                "p.code AS " + DDbConsts.FIELD_CODE + ", " +
                "p.name AS " + DDbConsts.FIELD_NAME + ", " +
                "pt.name, " +
                "CASE " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM + " THEN i.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_MFR + " THEN im.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_BRD + " THEN ib.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_LIN + " THEN il.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_GEN + " THEN ig.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_FAM + " THEN iy.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_TP + " THEN itp.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CL + " THEN icl.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CT + " THEN ict.name " +
                "WHEN v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ALL + " THEN '" + DUtilConsts.ALL + "' ELSE '?' END AS f_ref, " +
                "lnk.name, " +
                "v.dsc_per " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.M_PRM_PRM) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_PRM) + " AS p ON " +
                "v.id_prm = p.id_prm " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_PAY_TP) + " AS pt ON " +
                "v.id_pay_tp = pt.id_pay_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_LNK_ITM_TP) + " AS lnk ON " +
                "v.id_lnk_itm_tp = lnk.id_lnk_tp " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM + " AND v.id_ref_itm = i.id_itm " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_MFR) + " AS im ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_MFR + " AND v.id_ref_itm = im.id_mfr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_BRD) + " AS ib ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_BRD + " AND v.id_ref_itm = ib.id_brd " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_LIN) + " AS il ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_LIN + " AND v.id_ref_itm = il.id_lin " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS ig ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_GEN + " AND v.id_ref_itm = ig.id_gen " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_FAM) + " AS iy ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_FAM + " AND v.id_ref_itm = iy.id_fam " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_TP) + " AS itp ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_TP + " AND v.id_ref_itm = itp.idx " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS icl ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CL + " AND v.id_ref_itm = icl.idx " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CT) + " AS ict ON " +
                "v.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CT + " AND v.id_ref_itm = ict.id_itm_ct " +
                "WHERE p.b_del = 0 " +
                "ORDER BY p.name, p.code, v.id_prm, pt.name, v.id_pay_tp, lnk.name, v.id_lnk_itm_tp, f_ref, v.id_ref_itm ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[6];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " paquete promocional");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " paquete promocional");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pt.name", DGridConsts.COL_TITLE_TYPE + " pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "lnk.name", DGridConsts.COL_TITLE_TYPE + " referencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "f_ref", "Referencia");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_PER_DISC, "v.dsc_per", "Descto. promo. %");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_MFR);
        moSuscriptionsSet.add(DModConsts.IU_BRD);
        moSuscriptionsSet.add(DModConsts.IU_LIN);
        moSuscriptionsSet.add(DModConsts.IU_GEN);
        moSuscriptionsSet.add(DModConsts.IU_FAM);
        moSuscriptionsSet.add(DModConsts.M_PRM);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
