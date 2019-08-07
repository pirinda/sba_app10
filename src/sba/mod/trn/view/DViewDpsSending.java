/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDatePeriod;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridRowView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.lib.gui.DGuiParams;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DTrnEmissionUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDpsSending extends DGridPaneView implements ActionListener {

    private final DGridFilterDatePeriod moFilterDatePeriod;
    private final JButton mjButtonSend;

    /**
     * @param client GUI client.
     * @param subtype
     * @param mode DPS emission type. Constant defined in DUtilConsts (EMT or EMT_PEND).
     * @param title View title.
     */
    public DViewDpsSending(DGuiClient client, int subtype, int mode, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.T_DPS_SND, subtype, title, new DGuiParams(mode));

        int edsEmsTypeId = ((DDbConfigCompany) miClient.getSession().getConfigCompany()).getFkDfrEmsTypeId();

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        mjButtonSend = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SEND), DUtilConsts.TXT_SEND, this);
        mjButtonSend.setEnabled(true);
        mjButtonSend.setEnabled(edsEmsTypeId != DModSysConsts.CS_EMS_TP_NA);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonSend);
    }

    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String num = "";
        String orderBy = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1, 3);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            num = "v.num";
            orderBy = "b.name, bc.code, b.id_bpr, " +
                    "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
        }
        else {
            num = "v.num";
            //num = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.num)), v.num) AS CHAR)";
            orderBy = "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
        }

        sql += (sql.length() == 0 ? "" : "AND ") + "v.id_dps " + (mnGridMode == DUtilConsts.EMT ? "IN " : "NOT IN ") +
                "(SELECT dx.id_dps FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_SND) + " AS dx WHERE dx.b_del = 0 ORDER BY dx.id_dps) ";

        msSql = "SELECT " +
                "v.id_dps AS " + DDbConsts.FIELD_ID + "1, " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), " + num + ") AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), " + num + ") AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), " + num + ") AS f_num, " +
                "v.dt, " +
                "v.tot_cy_r, " +
                "v.exr, " +
                "v.tot_r, " +
                "dt.code, " +
                "dt.name, " +
                "b.id_bpr, " +
                "b.name, " +
                "bc.code, " +
                "bb.name, " +
                "cb.code, " +
                "c.code, " +
                "emit.code, " +
                "emit.name, " +
                "snd.con_1, " +
                "snd.con_2, " +
                "snd.ema_1, " +
                "snd.ema_2, " +
                "IF(v.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
                "(SELECT COUNT(*) FROM " +DModConsts.TablesMap.get(DModConsts.T_DPS_SIG) + " AS dx WHERE dx.id_dps = v.id_dps AND dx.b_del = 0) AS f_evt, " +
                "v.fk_dps_ct AS " + DDbConsts.FIELD_TYPE_ID + "1, " +
                "v.fk_dps_cl AS " + DDbConsts.FIELD_TYPE_ID + "2, " +
                "v.fk_dps_tp AS " + DDbConsts.FIELD_TYPE_ID + "3, " +
                "dt.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dt ON " +
                "v.fk_dps_ct = dt.id_dps_ct AND v.fk_dps_cl = dt.id_dps_cl AND v.fk_dps_tp = dt.id_dps_tp AND " +
                "v.fk_dps_ct = " + mnGridSubtype + " AND v.fk_dps_cl IN " +
                "(" + DModSysConsts.TS_DPS_CL_PUR_DOC[1] + ", " + DModSysConsts.TS_DPS_CL_PUR_ADJ_INC[1] + ", " + DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC[1] + ") " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.fk_bpr_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "v.fk_bpr_bpr = bc.id_bpr AND bc.id_bpr_cl = " + DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype) + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb ON " +
                "v.fk_bpr_bpr = bb.id_bpr AND v.fk_bpr_bra = bb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_own_bpr = cb.id_bpr AND v.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "v.fk_cur = c.id_cur " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_EMI_TP) + " AS emit ON " +
                "v.fk_emi_tp = emit.id_emi_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON " +
                "v.id_dps = dfr.fk_dps_n " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_SND) + " AS snd ON " +
                "v.id_dps = snd.id_dps " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY " + orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[27];

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.tot_cy_r", "Total $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_EXC_RATE, "v.exr", "T. cambio");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.tot_r", "Total $ ML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.con_1", "Contacto 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.ema_1", "Correo-e 1");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.con_2", "Contacto 2");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.ema_2", "Correo-e 2");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.con_3", "Contacto 3");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "snd.ema_3", "Correo-e 3");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "emit.code", "Tipo timbrado");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "f_evt", "Envíos");
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
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.CU_USR);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.T_DPS_SIG);
    }

    @Override
    public void actionMouseClicked() {
        actionSend();
    }

    public void actionSend() {
        if (mjButtonSend.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.sendDps(miClient, (DGridRowView) getSelectedGridRow());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjButtonSend) {
                actionSend();
            }
        }
    }
}
