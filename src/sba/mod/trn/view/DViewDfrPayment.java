/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
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
import sba.mod.trn.db.DDbDfr;
import sba.mod.trn.db.DTrnConsts;
import sba.mod.trn.db.DTrnEmissionUtils;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewDfrPayment extends DGridPaneView implements ActionListener {

    private static final int ACTION_DISABLE = 1;
    private static final int ACTION_DELETE = 2;
    
    private DGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjButtonPrint;
    private JButton mjButtonDfrSign;            // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrSignVerify;      // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCancel;          // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCancelVerify;    // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrCheckStatus;     // only for Digital Fiscal Receipt (DFR)
    private JButton mjButtonDfrSend;            // only for Digital Fiscal Receipt (DFR)

    /**
     * @param client GUI client.
     * @param title View title.
     */
    public DViewDfrPayment(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TX_DFR_PAY, 0, title);
        
        setRowButtonsEnabled(true, true, false, true, false);
        jtbFilterDeleted.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));

        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        mjButtonPrint = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_PRINT), DUtilConsts.TXT_PRINT + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonPrint);

        mjButtonDfrSign = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN), DUtilConsts.TXT_SIGN + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSign);

        mjButtonDfrSignVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SIGN_VER), DUtilConsts.TXT_SIGN_VER + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSignVerify);

        mjButtonDfrCancel = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL), DUtilConsts.TXT_CANCEL + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCancel);

        mjButtonDfrCancelVerify = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CANCEL_VER), DUtilConsts.TXT_CANCEL_VER + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCancelVerify);

        mjButtonDfrCheckStatus = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_VIEW), "Consultar estatus comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrCheckStatus);

        mjButtonDfrSend = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SEND), DUtilConsts.TXT_SEND + " comprobante", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonDfrSend);
    }

    /*
     * Private methods
     */
    
    private boolean proceedDisableDelete(final int[] keyDfr, final int action) {
        boolean proceed = true;
        DDbDfr dfr = (DDbDfr) miClient.getSession().readRegistry(DModConsts.T_DFR, keyDfr);
        
        try {
            switch (action) {
                case ACTION_DISABLE:
                    if (dfr.isDeleted()) {
                        miClient.showMsgBoxWarning("!El comprobante '" + dfr.getDfrNumber() + "' está eliminado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    else if (dfr.isDisabled()) {
                        miClient.showMsgBoxWarning("!El comprobante '" + dfr.getDfrNumber() + "' ya está cancelado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    proceed = dfr.canDisable(miClient.getSession());
                    break;

                case ACTION_DELETE:
                    if (dfr.isDeleted()) {
                        miClient.showMsgBoxWarning("!El comprobante '" + dfr.getDfrNumber() + "' ya está eliminado!\n" + DTrnConsts.ERR_MSG_NOT_PROCEED);
                        proceed = false;
                    }
                    proceed = dfr.canDelete(miClient.getSession());
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
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1, 1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.isEmpty() ? "" : "AND ") + DGridUtils.getSqlFilterDate("v.doc_ts", (DGuiDate) filter);

        msSql = "SELECT " +
                "v.id_dfr AS " + DDbConsts.FIELD_ID + "1, " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), v.num) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), v.num) AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(v.ser, IF(LENGTH(v.ser) = 0, '', '-'), v.num) AS f_num, " +
                "v.doc_ts, " +
                "v.uid, " +
                "v.b_bkk, " +
                "xtp.code, " +
                "xtp.name, " +
                "xstp.code, " +
                "xstp.name, " +
                "xst.code, " +
                "xst.name, " +
                "b.id_bpr, " +
                "b.name, " +
                "b.fis_id, " +
                "bc.code, " +
                "cb.code, " +
                "csh.code, " +
                "IF(v.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS f_ico, " +
                "IF(v.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(v.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(v.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS f_xml, " +
                "v.fk_xml_tp AS " + DDbConsts.FIELD_TYPE_ID + "1, " +
                "xtp.name AS " + DDbConsts.FIELD_TYPE + ", " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.b_sys AS " + DDbConsts.FIELD_IS_SYS + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XML_TP) + " AS xtp ON v.fk_xml_tp = xtp.id_xml_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XML_STP) + " AS xstp ON v.fk_xml_stp = xstp.id_xml_stp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_XML_ST) + " AS xst ON v.fk_xml_st = xst.id_xml_st " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.fk_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "v.fk_bpr = bc.id_bpr AND bc.id_bpr_cl = " + DModSysConsts.BS_BPR_CL_CUS + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_own_bpr = cb.id_bpr AND v.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS csh ON " +
                "v.fk_csh_bpr_n = csh.id_bpr AND v.fk_csh_bra_n = csh.id_bra AND v.fk_csh_csh_n = csh.id_csh " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                "WHERE v.fk_xml_stp = " + DModSysConsts.TS_XML_STP_CFDI_CRP + " " + (sql.isEmpty() ? "" : "AND " + sql) +
                "ORDER BY v.ser, v.num, v.id_dfr;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[20];

        String catetory = DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(DModSysConsts.BS_BPR_CL_CUS)).toLowerCase();
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, "v.doc_ts", DGridConsts.COL_TITLE_DATE + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "csh.code", DUtilConsts.TXT_BRANCH_CSH + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_ico", DGridConsts.COL_TITLE_STAT + " docto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "f_xml", "XML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + catetory);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + catetory);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "b.fis_id", "RFC " + catetory);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_M, "v.b_bkk", "Aplicación pagos");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.uid", "UUID");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "xtp.name", "Tipo XML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "xstp.name", "Subtipo XML");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "xst.name", "Estatus XML");
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
        moSuscriptionsSet.add(DModConsts.T_DFR);
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

    /**
     * By now, DFR deletions is not implemented. This method is preserved just for consistence.
     */
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
    
    private void actionPrint() {
        if (mjButtonPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DTrnEmissionUtils.printDfr(miClient, (DGridRowView) getSelectedGridRow());
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
                    DTrnEmissionUtils.signDfr(miClient, (DGridRowView) getSelectedGridRow(), requestType);
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
                try {
                    DTrnEmissionUtils.cancelDfr(miClient, (DGridRowView) getSelectedGridRow(), requestType);
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
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
                    DTrnEmissionUtils.checkDfr(miClient, (DGridRowView) getSelectedGridRow());
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
                    DTrnEmissionUtils.sendDfr(miClient, (DGridRowView) getSelectedGridRow());
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
            JButton button = (JButton) e.getSource();

            if (button == mjButtonPrint) {
                actionPrint();
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
