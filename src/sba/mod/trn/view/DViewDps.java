/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import sba.gui.DGuiClientApp;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDate;
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
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DTrnEmissionUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDps extends DGridPaneView implements ActionListener {

    private int[] manFilterDpsClassKey;
    private int[] manFilterDpsTypeKey;
    private int[] manDpsClassKey;
    private boolean mbIsMyDps;
    private boolean mbIsMyDpsDoc;
    private boolean mbCanDisable;
    private boolean mbCanDelete;
    private boolean mbEnableRowNew;
    private boolean mbEnableRowEdit;
    private boolean mbEnableRowCopy;
    private boolean mbEnableRowDisable;
    private boolean mbEnableRowDelete;
    private boolean mbEnableEds;
    private DGridFilterDate moFilterDate;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private JTextField mjTextUser;
    private JButton mjButtonNewInv;
    private JButton mjButtonNewNot;
    private JButton mjButtonNewTic;
    private JButton mjButtonTypeChange;
    private JButton mjButtonImport;
    private JButton mjButtonPrint;
    private JButton mjButtonSign;
    private JButton mjButtonSignVerify;
    private JButton mjButtonCancel;
    private JButton mjButtonCancelVerify;
    private JButton mjButtonSend;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_DPS_... or TX_MY_DPS_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDps(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

        switch (mnGridType) {
            case DModConsts.TX_MY_DPS_ORD:
            case DModConsts.TX_MY_DPS_DOC:
            case DModConsts.TX_MY_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                manFilterDpsClassKey = DTrnUtils.getDpsClassByMyDpsXType(type, subtype);
                manFilterDpsTypeKey = null;
                manDpsClassKey = manFilterDpsClassKey;
                mbIsMyDps = true;

                mjTextUser = DGridUtils.createTextField("Usuario");
                mjTextUser.setText(miClient.getSession().getUser().getName());
                mjTextUser.setCaretPosition(0);
                getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjTextUser);
                break;

            default:
                manFilterDpsClassKey = null;
                manFilterDpsTypeKey = DTrnUtils.getDpsTypeByDpsXType(type, subtype);
                manDpsClassKey = new int[] { manFilterDpsTypeKey[0], manFilterDpsTypeKey[1] };
                mbIsMyDps = false;

                mjTextUser = null;
        }

        if (mbIsMyDps) {
            moFilterDate = new DGridFilterDate(client, this);
            moFilterDate.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
            moFilterDatePeriod = null;

            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDate);
        }
        else {
            moFilterDate = null;
            moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }

        switch (mnGridType) {
            case DModConsts.TX_MY_DPS_ORD:
                mbIsMyDpsDoc = false;

                mjButtonNewInv = null;
                mjButtonNewNot = null;
                mjButtonNewTic = null;
                break;

            case DModConsts.TX_MY_DPS_DOC:
                mbIsMyDpsDoc = true;

                mjButtonNewInv = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_inv.gif")), DUtilConsts.TXT_CREATE + " " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_DOC_INV).toLowerCase(), this);
                mjButtonNewNot = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_not.gif")), DUtilConsts.TXT_CREATE + " " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_DOC_NOT).toLowerCase(), this);
                mjButtonNewTic = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_tic.gif")), DUtilConsts.TXT_CREATE + " " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_DOC_TIC).toLowerCase(), this);
                getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonNewInv);
                getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonNewNot);
                getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonNewTic);
                break;

            case DModConsts.TX_MY_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                mbIsMyDpsDoc = false;

                mjButtonNewInv = null;
                mjButtonNewNot = null;
                mjButtonNewTic = null;
                break;

            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
            case DModConsts.TX_DPS_ADJ_INC:
            case DModConsts.TX_DPS_ADJ_DEC:
                mbIsMyDpsDoc = false;

                mjButtonNewInv = null;
                mjButtonNewNot = null;
                mjButtonNewTic = null;
                break;

            default:
                mbIsMyDpsDoc = false;

                mjButtonNewInv = null;
                mjButtonNewNot = null;
                mjButtonNewTic = null;
        }

        if (mbIsMyDps) {
            switch (mnGridType) {
                case DModConsts.TX_MY_DPS_ORD:
                    mbEnableRowNew = miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_POS_DPS_ORD) == DModSysConsts.CS_LEV_EDT;
                    break;
                    
                case DModConsts.TX_MY_DPS_DOC:
                    mbEnableRowNew = miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_POS_DPS_INV) == DModSysConsts.CS_LEV_EDT;
                    break;

                case DModConsts.TX_MY_DPS_ADJ_INC:
                case DModConsts.TX_MY_DPS_ADJ_DEC:
                    mbEnableRowNew = miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_POS_DPS_ADJ) == DModSysConsts.CS_LEV_EDT;
                    break;
                default:
            }
            
            mbEnableRowEdit = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_ADM);
            mbEnableRowCopy = false;
            
            mbCanDisable = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_ADM);
            mbEnableRowDisable = mbCanDisable && !(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying());
            
            mbCanDelete = configCompany.isDpsDeletable() && miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_ADM);
            mbEnableRowDelete = mbCanDelete;
        }
        else {
            switch (mnGridType) {
                case DModConsts.TX_DPS_ORD:
                    mbEnableRowNew =
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_PUR_DPS_ORD) == DModSysConsts.CS_LEV_EDT ||
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_SAL && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_SAL_DPS_ORD) == DModSysConsts.CS_LEV_EDT;
                    break;

                case DModConsts.TX_DPS_DOC_INV:
                case DModConsts.TX_DPS_DOC_NOT:
                case DModConsts.TX_DPS_DOC_TIC:
                    mbEnableRowNew =
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_PUR_DPS_INV) == DModSysConsts.CS_LEV_EDT ||
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_SAL && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_SAL_DPS_INV) == DModSysConsts.CS_LEV_EDT;
                    break;

                case DModConsts.TX_DPS_ADJ_INC:
                case DModConsts.TX_DPS_ADJ_DEC:
                    mbEnableRowNew =
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_PUR_DPS_ADJ) == DModSysConsts.CS_LEV_EDT ||
                            mnGridSubtype == DModSysConsts.TS_DPS_CT_SAL && miClient.getSession().getUser().getPrivilegeLevel(DModSysConsts.CS_PRV_SAL_DPS_ADJ) == DModSysConsts.CS_LEV_EDT;
                    break;

                default:
            }

            mbEnableRowEdit = mbEnableRowNew;
            mbEnableRowCopy = mbEnableRowNew;
            
            mbCanDisable = mbEnableRowNew;
            mbEnableRowDisable = mbCanDisable && !(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying());
            
            mbCanDelete = configCompany.isDpsDeletable() && mbEnableRowNew;
            mbEnableRowDelete = mbCanDelete;

            mjButtonTypeChange = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_chg.gif")), "Cambiar tipo " + DUtilConsts.TXT_DOC.toLowerCase(), this);
            mjButtonTypeChange.setEnabled(mbEnableRowNew && DTrnUtils.isDpsClassDocument(manDpsClassKey) && (
                    mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR && miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_ADM) ||
                    mnGridSubtype == DModSysConsts.TS_DPS_CT_SAL && miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_ADM)));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonTypeChange);

            mjButtonImport = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_IMPORT), DUtilConsts.TXT_IMPORT + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
            mjButtonImport.setEnabled(mbEnableRowNew && DTrnUtils.isDpsClassDocument(manDpsClassKey));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonImport);
        }

        mjButtonPrint = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_PRINT), DUtilConsts.TXT_PRINT + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonPrint.setEnabled(true);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonPrint);

        mjButtonSign = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN), DUtilConsts.TXT_SIGN + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonSign.setEnabled(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonSign);

        mjButtonSignVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN_VER), DUtilConsts.TXT_SIGN_VER + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonSignVerify.setEnabled(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonSignVerify);

        mjButtonCancel = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL), DUtilConsts.TXT_CANCEL + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonCancel.setEnabled(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying() && mbCanDisable);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonCancel);

        mjButtonCancelVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL_VER), DUtilConsts.TXT_CANCEL_VER + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonCancelVerify.setEnabled(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying() && mbCanDisable);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonCancelVerify);

        mjButtonSend = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SEND), DUtilConsts.TXT_SEND + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonSend.setEnabled(DTrnUtils.isDpsClassForEds(manDpsClassKey) && configCompany.isEdsApplying() && configCompany.getFkEdsEmsTypeId() != DModSysConsts.CS_EMS_TP_NA);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonSend);
    }

    /*
     * Private methods
     */

    private void evaluatePosCloseSession() {
        DDbRegistry registry = null;

        if (((DDbConfigBranch) miClient.getSession().getConfigBranch()).getFkPosAfterSaleActionTypeId() == DModSysConsts.CS_POS_ASA_TP_CLO) {
            registry = miClient.getSession().getModule(mnModuleType, mnModuleSubtype).getLastRegistry();
            if (registry != null && registry.getQueryResultId() == DDbConsts.SAVE_OK) {
                ((DGuiClientApp) miClient).actionFileCloseSession();
            }
        }
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
        boolean enableButtons = true;

        moPaneSettings = new DGridPaneSettings(1, 3);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(mbIsMyDps ? DGridConsts.FILTER_DATE : DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.dt", (DGuiDate) filter);

        if (mbIsMyDps) {
            enableButtons = ((DGuiDate) filter).getGuiType() == DGuiConsts.GUI_DATE_DATE && ((DGuiDate) filter).equals(miClient.getSession().getWorkingDate());

            jbRowNew.setEnabled(mbEnableRowNew && enableButtons && !mbIsMyDpsDoc);
            jbRowEdit.setEnabled(mbEnableRowEdit && enableButtons);
            jbRowCopy.setEnabled(mbEnableRowCopy && enableButtons);
            jbRowDisable.setEnabled(mbEnableRowDisable && enableButtons);
            jbRowDelete.setEnabled(mbEnableRowDelete && enableButtons);

            if (mbIsMyDpsDoc) {
                mjButtonNewInv.setEnabled(mbEnableRowNew && enableButtons);
                mjButtonNewNot.setEnabled(mbEnableRowNew && enableButtons);
                mjButtonNewTic.setEnabled(mbEnableRowNew && enableButtons);
            }
        }
        else {
            jbRowNew.setEnabled(mbEnableRowNew);
            jbRowEdit.setEnabled(mbEnableRowEdit);
            jbRowCopy.setEnabled(mbEnableRowCopy);
            jbRowDisable.setEnabled(mbEnableRowDisable);
            jbRowDelete.setEnabled(mbEnableRowDelete);
        }

        switch (mnGridSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                num = "v.num";
                orderBy = "b.name, bc.code, b.id_bpr, " +
                        "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
                break;
            case DModSysConsts.TS_DPS_CT_SAL:
                num = "v.num";
                //num = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(v.num)), v.num) AS CHAR)";
                orderBy = "dt.code, v.fk_dps_ct, v.fk_dps_cl, v.fk_dps_tp, v.ser, " + num + ", v.id_dps ";
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
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
                "v.pay_acc, " +
                "dt.code, " +
                "dt.name, " +
                "b.id_bpr, " +
                "b.name, " +
                "b.fis_id, " +
                "bc.code, " +
                "bb.name, " +
                "cb.code, " +
                "c.code, " +
                "payt.code, " +
                "payt.name, " +
                "mopt.code, " +
                "mopt.name, " +
                "emit.code, " +
                "emit.name, " +
                "IF(v.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(eds.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
                "v.fk_dps_ct AS " + DDbConsts.FIELD_TYPE_ID + "1, " +
                "v.fk_dps_cl AS " + DDbConsts.FIELD_TYPE_ID + "2, " +
                "v.fk_dps_tp AS " + DDbConsts.FIELD_TYPE_ID + "3, " +
                "dt.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "v.b_aud AS " + DDbConsts.FIELD_IS_AUD + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_aud AS " + DDbConsts.FIELD_USER_AUD_ID + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_aud AS " + DDbConsts.FIELD_USER_AUD_TS + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ua.name AS " + DDbConsts.FIELD_USER_AUD_NAME + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dt ON " +
                "v.fk_dps_ct = dt.id_dps_ct AND v.fk_dps_cl = dt.id_dps_cl AND v.fk_dps_tp = dt.id_dps_tp " +
                (manFilterDpsClassKey == null ? "" : "AND v.fk_dps_ct = " + manFilterDpsClassKey[0] + " AND v.fk_dps_cl = " + manFilterDpsClassKey[1] + " ") +
                (manFilterDpsTypeKey == null ? "" : "AND v.fk_dps_ct = " + manFilterDpsTypeKey[0] + " AND v.fk_dps_cl = " + manFilterDpsTypeKey[1] + " AND v.fk_dps_tp = " + manFilterDpsTypeKey[2] + " ") +
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
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_PAY_TP) + " AS payt ON " +
                "v.fk_pay_tp = payt.id_pay_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_MOP_TP) + " AS mopt ON " +
                "v.fk_mop_tp = mopt.id_mop_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_EMI_TP) + " AS emit ON " +
                "v.fk_emi_tp = emit.id_emi_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ua ON " +
                "v.fk_usr_aud = ua.id_usr " +
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
        DGridColumnView[] columns = new DGridColumnView[27];

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC");
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
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase());
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.tot_cy_r", "Total $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_EXC_RATE, "v.exr", "T. cambio");
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.tot_r", "Total $ ML");
        columns[col++].setSumApplying(!mbIsMyDps);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "payt.code", "Tipo pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "mopt.code", "Forma pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.pay_acc", "Cta pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "emit.code", "Tipo impresión");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_AUD, DGridConsts.COL_TITLE_IS_AUD);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_AUD_NAME, DGridConsts.COL_TITLE_USER_AUD_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_AUD_TS, DGridConsts.COL_TITLE_USER_AUD_TS);
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
        moSuscriptionsSet.add(DModConsts.T_DPS_PRT);
        moSuscriptionsSet.add(DModConsts.T_DPS_SIG);
        moSuscriptionsSet.add(DModConsts.T_DPS_SND);
    }

    @Override
    public void actionRowEdit() {
        switch (mnGridType) {
            case DModConsts.TX_MY_DPS_DOC:
                if (jbRowEdit.isEnabled()) {
                    if (jtTable.getSelectedRowCount() != 1) {
                        miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
                    }
                    else {
                        DGridRowView gridRow = (DGridRowView) getSelectedGridRow();
                        DGuiParams params = null;
                        int type = DLibConsts.UNDEFINED;

                        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                            miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                        }
                        /* XXX Improve this, documents shoud be rendered in edit mode in form!
                        else if (gridRow.isRowSystem()) {
                            miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                        }
                        */
                        else {
                            params = new DGuiParams();
                            params.setKey(gridRow.getRowPrimaryKey());

                            if (DLibUtils.belongsTo(gridRow.getRowRegistryTypeKey(), new int[][] {
                                DModSysConsts.TS_DPS_TP_PUR_DOC_INV, DModSysConsts.TS_DPS_TP_SAL_DOC_INV })) {
                                type = DModConsts.TX_DPS_DOC_INV;
                            }
                            else if (DLibUtils.belongsTo(gridRow.getRowRegistryTypeKey(), new int[][] {
                                DModSysConsts.TS_DPS_TP_PUR_DOC_NOT, DModSysConsts.TS_DPS_TP_SAL_DOC_NOT })) {
                                type = DModConsts.TX_DPS_DOC_NOT;
                            }
                            else if (DLibUtils.belongsTo(gridRow.getRowRegistryTypeKey(), new int[][] {
                                DModSysConsts.TS_DPS_TP_PUR_DOC_TIC, DModSysConsts.TS_DPS_TP_SAL_DOC_TIC })) {
                                type = DModConsts.TX_DPS_DOC_TIC;
                            }

                            if (type == DLibConsts.UNDEFINED) {
                                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }
                            else {
                                miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(type, mnGridSubtype, params);
                            }
                        }
                    }
                }
                break;

            default:
                super.actionRowEdit(true);  // show also system registries
        }
    }

    public void actionTypeChange() {
        if (mjButtonTypeChange.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isDeleted()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_DELETED);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    DTrnEmissionUtils.changeDpsType(miClient, (DGridRowView) getSelectedGridRow());
                }
            }
        }
    }

    public void actionPrint() {
        if (mjButtonPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.printDps(miClient, (DGridRowView) getSelectedGridRow());
            }
        }
    }

    public void actionSign(final int requestType) {
        if (mjButtonSign.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.signDps(miClient, (DGridRowView) getSelectedGridRow(), requestType);
            }
        }
    }

    public void actionCancel(final int requestType) {
        if (mjButtonCancel.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.cancelDps(miClient, (DGridRowView) getSelectedGridRow(), requestType);
            }
        }
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
            DGuiParams params = null;
            JButton button = (JButton) e.getSource();

            if (button == mjButtonNewInv) {
                params = new DGuiParams();
                params.getParamsMap().put(DModSysConsts.FLAG_IS_POS, true);
                miClient.getSession().showForm(DModConsts.TX_DPS_DOC_INV, mnGridSubtype, params);
                evaluatePosCloseSession();
            }
            else if (button == mjButtonNewNot) {
                params = new DGuiParams();
                params.getParamsMap().put(DModSysConsts.FLAG_IS_POS, true);
                miClient.getSession().showForm(DModConsts.TX_DPS_DOC_NOT, mnGridSubtype, params);
                evaluatePosCloseSession();
            }
            else if (button == mjButtonNewTic) {
                params = new DGuiParams();
                params.getParamsMap().put(DModSysConsts.FLAG_IS_POS, true);
                miClient.getSession().showForm(DModConsts.TX_DPS_DOC_TIC, mnGridSubtype, params);
                evaluatePosCloseSession();
            }
            else if (button == mjButtonTypeChange) {
                actionTypeChange();
            }
            else if (button == mjButtonPrint) {
                actionPrint();
            }
            else if (button == mjButtonSign) {
                actionSign(DModSysConsts.TX_XMS_REQ_STP_REQ);
            }
            else if (button == mjButtonSignVerify) {
                actionSign(DModSysConsts.TX_XMS_REQ_STP_VER);
            }
            else if (button == mjButtonCancel) {
                actionCancel(DModSysConsts.TX_XMS_REQ_STP_REQ);
            }
            else if (button == mjButtonCancelVerify) {
                actionCancel(DModSysConsts.TX_XMS_REQ_STP_VER);
            }
            else if (button == mjButtonSend) {
                actionSend();
            }
        }
    }
}
