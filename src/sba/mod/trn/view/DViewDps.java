/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sba.gui.DGuiClientApp;
import sba.gui.prt.DPrtConsts;
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
import sba.lib.grid.DGridRow;
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
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DTrnConsts;
import sba.mod.trn.db.DTrnEmissionUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDps extends DGridPaneView implements ActionListener {

    private static final int ACTION_DISABLE = 1;
    private static final int ACTION_DELETE = 2;
    
    private int[] manFilterDpsTypeKey;
    private int[] manFilterDpsClassKey;
    private boolean mbIsMyDps;
    private boolean mbIsMyDpsDoc;
    private boolean mbCanDisable;
    private boolean mbCanDelete;
    private boolean mbEnableRowNew;
    private boolean mbEnableRowEdit;
    private boolean mbEnableRowCopy;
    private boolean mbEnableRowDisable;
    private boolean mbEnableRowDelete;
    private boolean mbDfrRequired;
    private DGridFilterDate moFilterDate;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private JTextField mjTextUser;
    private JButton mjButtonNewInv;
    private JButton mjButtonNewNot;
    private JButton mjButtonNewTic;
    private JButton mjButtonTypeChange;
    private JButton mjButtonImport;
    private JButton mjButtonPrint;
    private JButton mjButtonPrintAlt;
    private JButton mjButtonDfrSign;            // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrSignVerify;      // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCancel;          // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCancelVerify;    // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCheckStatus;     // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrSend;            // only for Digital Fiscal Receipt (DFR)

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_DPS_... or TX_MY_DPS_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDps(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();
        
        // Set view filters for DPS class or type:

        switch (mnGridType) {
            case DModConsts.TX_MY_DPS_ORD:
            case DModConsts.TX_MY_DPS_DOC:
            case DModConsts.TX_MY_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                // "My DPS" views filter documents by DPS class:
                
                mbIsMyDps = true;
                
                manFilterDpsTypeKey = null;
                manFilterDpsClassKey = DTrnUtils.getDpsClassByMyDpsXType(type, subtype);
                mbDfrRequired = DTrnUtils.isDpsClassForDfr(manFilterDpsClassKey) && configCompany.isEdsApplying();

                mjTextUser = DGridUtils.createTextField("Usuario");
                mjTextUser.setText(miClient.getSession().getUser().getName());
                mjTextUser.setCaretPosition(0);
                getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjTextUser);
                break;

            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
            case DModConsts.TX_DPS_ADJ_INC:
            case DModConsts.TX_DPS_ADJ_DEC:
                // Standard DPS views filter documents by DPS type:
                
                mbIsMyDps = false;
                
                manFilterDpsTypeKey = DTrnUtils.getDpsTypeByDpsXType(type, subtype);
                manFilterDpsClassKey = new int[] { manFilterDpsTypeKey[0], manFilterDpsTypeKey[1] };
                mbDfrRequired = DTrnUtils.isDpsTypeForDfr(manFilterDpsTypeKey) && configCompany.isEdsApplying();

                mjTextUser = null;
                break;
                
            default:
                mbIsMyDps = false;
                
                manFilterDpsTypeKey = null;
                manFilterDpsClassKey = null;
                mbDfrRequired = false;

                mjTextUser = null;
        }
        
        // Set view filters for date or period:

        if (mbIsMyDps) {
            // "My DPS" views filter documents by date:
                
            moFilterDate = new DGridFilterDate(client, this);
            moFilterDate.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, miClient.getSession().getWorkingDate().getTime()));
            moFilterDatePeriod = null;

            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDate);
        }
        else {
            // "My DPS" views filter documents by period:
                
            moFilterDate = null;
            moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        // Initialize special new document buttons:

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
        
        // Assess user permissions:

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
            mbEnableRowDisable = mbCanDisable;
            
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
            mbEnableRowDisable = mbCanDisable;
            
            mbCanDelete = configCompany.isDpsDeletable() && mbEnableRowNew;
            mbEnableRowDelete = mbCanDelete;

            mjButtonTypeChange = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_chg.gif")), "Cambiar tipo " + DUtilConsts.TXT_DOC.toLowerCase(), this);
            mjButtonTypeChange.setEnabled(mbEnableRowNew && DTrnUtils.isDpsClassDocument(manFilterDpsClassKey) && (
                    mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR && miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_ADM) ||
                    mnGridSubtype == DModSysConsts.TS_DPS_CT_SAL && miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_ADM)));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonTypeChange);

            mjButtonImport = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_IMPORT), DUtilConsts.TXT_IMPORT + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
            mjButtonImport.setEnabled(false/*mbEnableRowNew && DTrnUtils.isDpsClassDocument(manFilterDpsClassKey)*/); //2018-01-16 (Sergio Flores): it seems that this button is not already implemented, so it has been disabled!
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonImport);
        }

        mjButtonPrint = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_PRINT), DUtilConsts.TXT_PRINT + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonPrint.setEnabled(true);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonPrint);

        mjButtonPrintAlt = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_print_alt.gif")), DUtilConsts.TXT_PRINT + " " + DUtilConsts.TXT_DOC.toLowerCase() + " (alternativo)", this);
        mjButtonPrintAlt.setEnabled(true);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonPrintAlt);

        mjButtonDfrSign = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN), DUtilConsts.TXT_SIGN + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrSign.setEnabled(mbDfrRequired);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSign);

        mjButtonDfrSignVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN_VER), DUtilConsts.TXT_SIGN_VER + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrSignVerify.setEnabled(mbDfrRequired);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSignVerify);

        mjButtonDfrCancel = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL), DUtilConsts.TXT_CANCEL + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrCancel.setEnabled(mbDfrRequired && mbCanDisable);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCancel);

        mjButtonDfrCancelVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL_VER), DUtilConsts.TXT_CANCEL_VER + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrCancelVerify.setEnabled(mbDfrRequired && mbCanDisable);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCancelVerify);

        mjButtonDfrCheckStatus = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_VIEW), "Consultar estatus " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrCheckStatus.setEnabled(mbDfrRequired);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCheckStatus);

        mjButtonDfrSend = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SEND), DUtilConsts.TXT_SEND + " " + DUtilConsts.TXT_DOC.toLowerCase(), this);
        mjButtonDfrSend.setEnabled(mbDfrRequired && configCompany.getFkDfrEmsTypeId() != DModSysConsts.CS_EMS_TP_NA);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSend);
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

    private boolean proceedDisableDelete(final int[] keyDps, final int action) {
        boolean proceed = true;
        DDbDps dps = (DDbDps) miClient.getSession().readRegistry(DModConsts.T_DPS, keyDps);
        
        try {
            switch (action) {
                case ACTION_DISABLE:
                    if (dps.isDeleted()) {
                        miClient.showMsgBoxWarning("!El documento '" + dps.getDpsNumber() + "' está eliminado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    else if (dps.isDisabled()) {
                        miClient.showMsgBoxWarning("!El documento '" + dps.getDpsNumber() + "' ya está cancelado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    proceed = dps.canDisable(miClient.getSession());
                    break;
                    
                case ACTION_DELETE:
                    if (dps.isDeleted()) {
                        miClient.showMsgBoxWarning("!El documento '" + dps.getDpsNumber() + "' ya está eliminado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    proceed = dps.canDelete(miClient.getSession());
                    break;
                    
                default:
                    proceed = false; // unknown action!
            }
        }
        catch (Exception e) {
            proceed = false;
            DLibUtils.showException(this, e);
        }
        
        return proceed;
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
        boolean isTodayFiltered = true;

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
            isTodayFiltered = ((DGuiDate) filter).getGuiType() == DGuiConsts.GUI_DATE_DATE && ((DGuiDate) filter).equals(miClient.getSession().getWorkingDate());

            jbRowNew.setEnabled(mbEnableRowNew && isTodayFiltered && !mbIsMyDpsDoc);
            jbRowEdit.setEnabled(mbEnableRowEdit && isTodayFiltered);
            jbRowCopy.setEnabled(mbEnableRowCopy && isTodayFiltered);
            jbRowDisable.setEnabled(mbEnableRowDisable && isTodayFiltered);
            jbRowDelete.setEnabled(mbEnableRowDelete && isTodayFiltered);

            if (mbIsMyDpsDoc) {
                mjButtonNewInv.setEnabled(mbEnableRowNew && isTodayFiltered);
                mjButtonNewNot.setEnabled(mbEnableRowNew && isTodayFiltered);
                mjButtonNewTic.setEnabled(mbEnableRowNew && isTodayFiltered);
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
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
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
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON " +
                "v.id_dps = dfr.fk_dps_n " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY " + orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[27];

        String category =  DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype)).toLowerCase();
        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + category);
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
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "b.fis_id", "RFC " + category);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + category);
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
    
    @Override
    public void actionRowDisable() {
        if (jbRowDisable.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(DGridConsts.MSG_CONFIRM_REG_DIS) == JOptionPane.YES_OPTION) {
                boolean updates = false;
                DGridRow[] gridRows = getSelectedGridRows();

                for (DGridRow gridRow : gridRows) {
                    if (((DGridRowView) gridRow).getRowType() != DGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((DGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((DGridRowView) gridRow).isDisableable()) {
                        miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_NON_DISABLEABLE);
                    }
                    else {
                        if (proceedDisableDelete(gridRow.getRowPrimaryKey(), ACTION_DISABLE)) {
                            if (miClient.getSession().getModule(mnModuleType, mnModuleSubtype).disableRegistry(mnGridType, gridRow.getRowPrimaryKey()) == DDbConsts.SAVE_OK) {
                                updates = true;
                            }
                        }
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }

    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(DGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                boolean updates = false;
                DGridRow[] gridRows = getSelectedGridRows();

                for (DGridRow gridRow : gridRows) {
                    if (((DGridRowView) gridRow).getRowType() != DGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((DGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((DGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(DDbConsts.MSG_REG_ + gridRow.getRowName() + DDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        if (proceedDisableDelete(gridRow.getRowPrimaryKey(), ACTION_DELETE)) {
                            if (miClient.getSession().getModule(mnModuleType, mnModuleSubtype).deleteRegistry(mnGridType, gridRow.getRowPrimaryKey()) == DDbConsts.SAVE_OK) {
                                updates = true;
                            }
                        }
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
    
    private void actionTypeChange() {
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

    private void actionPrint(final int printMode) {
        if (printMode == DPrtConsts.PRINT_MODE_STD && mjButtonPrint.isEnabled() || printMode == DPrtConsts.PRINT_MODE_ALT && mjButtonPrintAlt.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.printDps(miClient, (DGridRowView) getSelectedGridRow(), printMode);
            }
        }
    }

    private void actionDfrSign(final int requestType) {
        if (mjButtonDfrSign.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    if (DTrnUtils.isDpsTypeForDfr(DTrnEmissionUtils.getDpsOwnDpsTypeKey(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()))) {
                        DTrnEmissionUtils.signDps(miClient, (DGridRowView) getSelectedGridRow(), requestType);
                    }
                    else {
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionDfrCancel(final int requestType) {
        if (mjButtonDfrCancel.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                boolean enabled = jbRowDisable.isEnabled(); // preserve button enabled status
                
                try {
                    if (DTrnUtils.isDpsTypeForDfr(DTrnEmissionUtils.getDpsOwnDpsTypeKey(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()))) {
                        DTrnEmissionUtils.cancelDps(miClient, (DGridRowView) getSelectedGridRow(), requestType);
                    }
                    else {
                        if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            jbRowDisable.setEnabled(true);
                            actionRowDisable();
                        }
                        else {
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
                finally {
                    jbRowDisable.setEnabled(enabled);   // restore original button enable status
                }
            }
        }
    }

    private void actionDfrCheckStatus() {
        if (mjButtonDfrCheckStatus.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    if (DTrnUtils.isDpsTypeForDfr(DTrnEmissionUtils.getDpsOwnDpsTypeKey(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()))) {
                        DTrnEmissionUtils.checkDps(miClient, (DGridRowView) getSelectedGridRow());
                    }
                    else {
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionDfrSend() {
        if (mjButtonDfrSend.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    if (DTrnUtils.isDpsTypeForDfr(DTrnEmissionUtils.getDpsOwnDpsTypeKey(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()))) {
                        DTrnEmissionUtils.sendDps(miClient, (DGridRowView) getSelectedGridRow());
                    }
                    else {
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
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
                actionPrint(DPrtConsts.PRINT_MODE_STD);
            }
            else if (button == mjButtonPrintAlt) {
                actionPrint(DPrtConsts.PRINT_MODE_ALT);
            }
            else if (button == mjButtonDfrSign) {
                actionDfrSign(DModSysConsts.TX_XMS_REQ_STP_REQ);
            }
            else if (button == mjButtonDfrSignVerify) {
                actionDfrSign(DModSysConsts.TX_XMS_REQ_STP_VER);
            }
            else if (button == mjButtonDfrCancel) {
                actionDfrCancel(DModSysConsts.TX_XMS_REQ_STP_REQ);
            }
            else if (button == mjButtonDfrCancelVerify) {
                actionDfrCancel(DModSysConsts.TX_XMS_REQ_STP_VER);
            }
            else if (button == mjButtonDfrCheckStatus) {
                actionDfrCheckStatus();
            }
            else if (button == mjButtonDfrSend) {
                actionDfrSend();
            }
        }
    }
}
