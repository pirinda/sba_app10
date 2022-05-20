/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.view;

import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewBizPartner extends DGridPaneView {

    protected int mnBizPartnerClass;

    public DViewBizPartner(DGuiClient client, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.BU_BPR, subtype, title);
        mnBizPartnerClass = mnGridSubtype;
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
                "v.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                "bc.code AS " + DDbConsts.FIELD_CODE + ", " +
                "v.name AS " + DDbConsts.FIELD_NAME + ", " +
                "v.nick, " +
                "v.fis_id, " +
                "v.alt_id, " +
                "v.b_ven, " +
                "v.b_cus, " +
                "v.b_cdr, " +
                "v.b_dbr, " +
                "vt.name, " +
                "et.name, " +
                "CONCAT(tr.code, ' - ', tr.name) AS _tax_reg, " +
                "xat.name, " +
                "bt.name, " +
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
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + ", " +
                "COALESCE(DATEDIFF('" + DLibUtils.DbmsDateFormatDate.format(miClient.getSession().getSystemDate()) + "', MAX(d.dt)), 999999) AS f_days " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BS_IDY_TP) + " AS vt ON " +
                "v.fk_idy_tp = vt.id_idy_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_EMI_TP) + " AS et ON " +
                "v.fk_emi_tp = et.id_emi_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_TAX_REG) + " AS tr ON " +
                "v.fk_tax_reg = tr.id_tax_reg " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XML_ADD_TP) + " AS xat ON " +
                "v.fk_xml_add_tp = xat.id_xml_add_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "v.id_bpr = bc.id_bpr AND bc.id_bpr_cl = " + mnBizPartnerClass + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP) + " AS bt ON " +
                "bc.fk_bpr_cl = bt.id_bpr_cl AND bc.fk_bpr_tp = bt.id_bpr_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN t_dps AS d ON v.id_bpr = d.fk_bpr_bpr AND d.b_del = 0 AND d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND " +
                "d.fk_dps_ct = " + DTrnUtils.getDpsCategoryByBizPartnerClass(mnBizPartnerClass) + " " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "GROUP BY v.id_bpr, bc.code, v.name , v.nick, v.fis_id, v.alt_id, v.b_ven, v.b_cus, vt.name, bt.name, " +
                "v.b_can_upd, v.b_can_dis, v.b_can_del, v.b_dis, v.b_del, v.b_sys, v.fk_usr_ins, v.fk_usr_upd, v.ts_usr_ins, v.ts_usr_upd, ui.name, uu.name " +
                "ORDER BY v.name, v.id_bpr ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[22];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bt.name", DGridConsts.COL_TITLE_TYPE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", DGridConsts.COL_TITLE_TYPE + " persona");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "et.name", DGridConsts.COL_TITLE_TYPE + " emisión");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "v.fis_id", "RFC");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "v.alt_id", "CURP");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "v.b_cus", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_CUS));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "v.b_ven", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_VEN));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "v.b_dbr", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_DBR));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, "v.b_cdr", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_CDR));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "v.nick", "Alias");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "f_days", "Días última transacción");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_tax_reg", "Régimen fiscal");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "xat.name", "Tipo addenda");
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
        moSuscriptionsSet.add(DModConsts.BU_BPR_CFG);
        moSuscriptionsSet.add(DModConsts.BU_BPR_TP);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
