/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDate;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridRowView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.trn.db.DTrnUtils;
import sba.mod.trn.form.DDialogCardexDps;

/**
 *
 * @author Sergio Flores
 */
public class DViewDpsDay extends DGridPaneView implements ActionListener {

    private int[] manFilterDpsClassKey;
    private boolean mbIsMyDps;
    private DGridFilterDate moFilterDate;
    private DDialogCardexDps moDialogCardexDps;
    private JButton mjButtonShowCardex;
    private JTextField mjTextUser;

    /**
     * @param client GUI client.
     * @param type Type of view. Constants defined in DModSysConsts (TX_DAY_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDpsDay(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        if (mnGridType == DModConsts.TX_DAY_DPS) {
            mbIsMyDps = false;
        }
        else {
            mbIsMyDps = true;

            mjTextUser = DGridUtils.createTextField("Usuario");
            mjTextUser.setText(miClient.getSession().getUser().getName());
            mjTextUser.setCaretPosition(0);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjTextUser);
        }

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            manFilterDpsClassKey = DModSysConsts.TS_DPS_CL_PUR_DOC;
        }
        else {
            manFilterDpsClassKey = DModSysConsts.TS_DPS_CL_SAL_DOC;
        }

        moFilterDate = new DGridFilterDate(client, this);
        moFilterDate.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDate);

        moDialogCardexDps = new DDialogCardexDps(client);

        mjButtonShowCardex = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CARDEX), "Ver tarjeta auxiliar", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowCardex);
    }

    private void actionShowCardex() {
        if (mjButtonShowCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogCardexDps.resetForm();
                    moDialogCardexDps.setValue(DModSysConsts.PARAM_YEAR, ((int[]) moFiltersMap.get(DGridConsts.FILTER_YEAR))[0]);
                    moDialogCardexDps.setValue(DModSysConsts.PARAM_DPS, gridRow.getRowPrimaryKey());
                    moDialogCardexDps.populateCardex();
                    moDialogCardexDps.setVisible(true);
                }
            }
        }
    }

    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        int sysAccTypeBpr = DLibConsts.UNDEFINED;
        int sysMoveTypeTrn = DLibConsts.UNDEFINED;
        String sql = "";
        String bal = "";
        String balStc = "";
        String num = "";
        String date = "";
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

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);
        date = "AND bkk.id_yer = " + DLibTimeUtils.digestYear((DGuiDate) filter)[0] + " AND " +
                "bkk.dt <= '" + DLibUtils.DbmsDateFormatDate.format((DGuiDate) filter) + "' ";

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            sysAccTypeBpr = DModSysConsts.FS_SYS_ACC_TP_BPR_VEN;
            sysMoveTypeTrn = DModSysConsts.FS_SYS_MOV_CL_PUR;

            bal = "SUM(IF(bkk.b_avl = 0, 0, bkk.cdt - bkk.dbt))";
            balStc = "SUM(bkk.cdt - bkk.dbt)";

            num = "v.num";
            orderBy = "b.name, bc.code, b.id_bpr, " +
                    "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
        }
        else {
            sysAccTypeBpr = DModSysConsts.FS_SYS_ACC_TP_BPR_CUS;
            sysMoveTypeTrn = DModSysConsts.FS_SYS_MOV_CL_SAL;
            bal = "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt - bkk.cdt))";
            balStc = "SUM(bkk.dbt - bkk.cdt)";

            num = "v.num";
            //num = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.num)), v.num) AS CHAR)";
            orderBy = "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
        }

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
                "IF(v.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
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
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + ", " +
                "(SELECT COALESCE(" + balStc + ") FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "WHERE bkk.b_del = 0 AND bkk.fk_dps_inv_n = v.id_dps " + date + " AND bkk.fk_sys_acc_tp = " + sysAccTypeBpr + " AND bkk.fk_sys_mov_cl = " + sysMoveTypeTrn + " AND bkk.fk_sys_mov_tp <> 1) AS f_adj, " +
                "(SELECT COALESCE(" + balStc + ") FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "WHERE bkk.b_del = 0 AND bkk.fk_dps_inv_n = v.id_dps " + date + " AND bkk.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " AND bkk.fk_sys_mov_cl IN (" + DModSysConsts.FS_SYS_MOV_CL_MI + ", " + DModSysConsts.FS_SYS_MOV_CL_MO + ")) AS f_csh, " +
                "(SELECT COALESCE(" + balStc + ") FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "WHERE bkk.b_del = 0 AND bkk.fk_dps_inv_n = v.id_dps " + date + " AND bkk.fk_sys_acc_tp = " + sysAccTypeBpr + ") AS f_bal_stc, " +
                "(SELECT COALESCE(" + bal + ") FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "WHERE bkk.b_del = 0 AND bkk.fk_dps_inv_n = v.id_dps " + date + " AND bkk.fk_sys_acc_tp = " + sysAccTypeBpr + ") AS f_bal " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dt ON " +
                "v.fk_dps_ct = dt.id_dps_ct AND v.fk_dps_cl = dt.id_dps_cl AND v.fk_dps_tp = dt.id_dps_tp " +
                (manFilterDpsClassKey == null ? "" : "AND v.fk_dps_ct = " + manFilterDpsClassKey[0] + " AND v.fk_dps_cl = " + manFilterDpsClassKey[1] + " ") +
                (!mbIsMyDps ? "" : "AND v.fk_usr_ins = " + miClient.getSession().getUser().getPkUserId() + " ") +
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
        DGridColumnView[] columns = new DGridColumnView[23];

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
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.tot_r", "Total $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_adj", "Ajustes $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_csh", "Pagos $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_stc", "Saldo SBC $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal", "Saldo $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
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
        moSuscriptionsSet.add(DModConsts.CU_USR);
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.T_DPS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjButtonShowCardex) {
                actionShowCardex();
            }
        }
    }
}
