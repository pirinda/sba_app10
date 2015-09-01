/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import sba.gui.util.DUtilConsts;
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
import sba.mod.bpr.db.DBprUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDpsTypeChange extends DGridPaneView {

    private DGridFilterDatePeriod moFilterDatePeriod;

    /**
     * @param client GUI client.
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDpsTypeChange(DGuiClient client, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.T_DPS_CHG, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String numNew = "";
        String numOld = "";
        String orderBy = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(2, 3);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.new_dt", (DGuiDate) filter);

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            numNew = "v.new_num";
            numOld = "v.old_num";
            orderBy = "b.name, bc.code, b.id_bpr, " +
                    "dnt.code, v.fk_new_dps_ct, v.fk_new_dps_cl, v.fk_new_dps_tp, v.new_ser, " + numNew + ", " +
                    "dot.code, v.fk_old_dps_ct, v.fk_old_dps_cl, v.fk_old_dps_tp, v.old_ser, " + numOld + ", v.id_dps ";
        }
        else {
            numNew = "v.new_num";
            numOld = "v.old_num";
            //numNew = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.new_num)), v.new_num) AS CHAR)";
            //numOld = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.old_num)), v.old_num) AS CHAR)";
            orderBy = "dnt.code, v.fk_new_dps_ct, v.fk_new_dps_cl, v.fk_new_dps_tp, v.new_ser, " + numNew + ", " +
                    "dot.code, v.fk_old_dps_ct, v.fk_old_dps_cl, v.fk_old_dps_tp, v.old_ser, " + numOld + ", v.id_dps ";
        }

        msSql = "SELECT " +
                "v.id_dps AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_chg AS " + DDbConsts.FIELD_ID + "2, " +
                "CONCAT(v.new_ser, IF(LENGTH(v.new_ser) = 0, '', '-'), " + numNew + ") AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(v.new_ser, IF(LENGTH(v.new_ser) = 0, '', '-'), " + numNew + ") AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(v.new_ser, IF(LENGTH(v.new_ser) = 0, '', '-'), " + numNew + ") AS f_new_num, " +
                "CONCAT(v.old_ser, IF(LENGTH(v.old_ser) = 0, '', '-'), " + numOld + ") AS f_old_num, " +
                "v.new_dt, " +
                "v.old_dt, " +
                "d.tot_cy_r, " +
                "d.exr, " +
                "d.tot_r, " +
                "dnt.code, " +
                "dnt.name, " +
                "dot.code, " +
                "dot.name, " +
                "b.id_bpr, " +
                "b.name, " +
                "bc.code, " +
                "bb.name, " +
                "cb.code, " +
                "c.code, " +
                "IF(d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
                "v.fk_new_dps_ct AS " + DDbConsts.FIELD_TYPE_ID + "1, " +
                "v.fk_new_dps_cl AS " + DDbConsts.FIELD_TYPE_ID + "2, " +
                "v.fk_new_dps_tp AS " + DDbConsts.FIELD_TYPE_ID + "3, " +
                "dnt.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_CHG) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d ON v.id_dps = d.id_dps AND " +
                "d.fk_dps_ct = " + mnGridSubtype + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dnt ON " +
                "v.fk_new_dps_ct = dnt.id_dps_ct AND v.fk_new_dps_cl = dnt.id_dps_cl AND v.fk_new_dps_tp = dnt.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dot ON " +
                "v.fk_old_dps_ct = dot.id_dps_ct AND v.fk_old_dps_cl = dot.id_dps_cl AND v.fk_old_dps_tp = dot.id_dps_tp " +
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
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_EDS) + " AS eds ON " +
                "v.id_dps = eds.id_dps " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY " + orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[21];

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dnt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_new_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.new_dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dot.code", DGridConsts.COL_TITLE_TYPE + " docto. anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_old_num", DGridConsts.COL_TITLE_NUM + " docto anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.old_dt", DGridConsts.COL_TITLE_DATE + " docto anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dnt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_new_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.new_dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dot.code", DGridConsts.COL_TITLE_TYPE + " docto anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_old_num", DGridConsts.COL_TITLE_NUM + " docto anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.old_dt", DGridConsts.COL_TITLE_DATE + " docto anterior");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "d.tot_cy_r", "Total $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_EXC_RATE, "d.exr", "T. cambio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "d.tot_r", "Total $ ML");
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
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
        moSuscriptionsSet.add(DModConsts.T_DPS);
    }
}
