/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.srv.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
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
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiSession;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.srv.db.DDbReparation;
import sba.mod.srv.db.DSrvUtils;

/**
 *
 * @author Sergio Flores
 */
public class DViewReparation extends DGridPaneView implements ActionListener {

    private DGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjButtonNewApr;
    private JButton mjButtonNewRej;
    private JButton mjButtonGoPrev;
    private JButton mjButtonGoNext;
    private JButton mjButtonPrint;

    /**
     * @param client GUI Client.
     * @param status Reparation status, constants defined by index 1 of DModSysConsts.SS_SRV_ST_...
     */
    public DViewReparation(DGuiClient client, int status, String title) {
        this(client, status, DLibConsts.UNDEFINED, title);
    }
    /**
     * @param client GUI Client.
     * @param status Reparation status, constants defined by index 1 of DModSysConsts.SS_SRV_ST_...
     * @param mode Delivery date required (by reparation, by delivery), constants defined in DModSysConsts.SX_SRV_DVY_BY_..., otherwise undefined.
     */
    public DViewReparation(DGuiClient client, int status, int mode, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.S_REP, status, title, new DGuiParams(mode));

        if (DLibUtils.belongsTo(mnGridSubtype, new int[] { DLibConsts.UNDEFINED, DModSysConsts.SX_SRV_ST_REP_REJ, DModSysConsts.SX_SRV_ST_REP_DVY })) {
            moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }

        if (mnGridSubtype == DModSysConsts.SX_SRV_ST_REP_NEW) {
            setRowButtonsEnabled(true, true, true, false, true);
        }
        else {
            setRowButtonsEnabled(false, true, false, false, true);
        }

        if (DLibUtils.belongsTo(mnGridSubtype, new int[] { DModSysConsts.SX_SRV_ST_REP_NEW, DModSysConsts.SX_SRV_ST_REP_APR })) {
            mjButtonNewApr = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_tmb_up.gif")), "Aprobar", this);
            mjButtonNewApr.setEnabled(mnGridSubtype == DModSysConsts.SX_SRV_ST_REP_NEW);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonNewApr);

            mjButtonNewRej = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_tmb_dow.gif")), "Rechazar", this);
            mjButtonNewRej.setEnabled(true);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonNewRej);
        }

        if (DLibUtils.belongsTo(mnGridSubtype, new int[] { DModSysConsts.SX_SRV_ST_REP_APR, DModSysConsts.SX_SRV_ST_REP_REJ, DModSysConsts.SX_SRV_ST_REP_FIN, DModSysConsts.SX_SRV_ST_REP_DVY })) {
            mjButtonGoPrev = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif")), "Retroceder", this);
            mjButtonGoPrev.setEnabled(true);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonGoPrev);

            mjButtonGoNext = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_rgt.gif")), "Avanzar", this);
            mjButtonGoNext.setEnabled(DLibUtils.belongsTo(mnGridSubtype, new int[] { DModSysConsts.SX_SRV_ST_REP_APR, DModSysConsts.SX_SRV_ST_REP_FIN }));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonGoNext);
        }

        mjButtonPrint = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_PRINT), DUtilConsts.TXT_PRINT, this);
        mjButtonPrint.setEnabled(true);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonPrint);
    }

    /*
     * Private methods
     */

    private void actionChangeStatus(final int newStatus) {
        DDbReparation reparation = null;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else if (miClient.showMsgBoxConfirm("¿Está seguro que desea " + (newStatus == DModSysConsts.SX_SRV_ST_REP_APR ? "autorizar" : "rechazar") + " el comprobante?") == JOptionPane.YES_OPTION) {
            try {
                reparation = (DDbReparation) miClient.getSession().readRegistry(DModConsts.S_REP, getSelectedGridRow().getRowPrimaryKey());
                reparation.setFkServiceStatusId(newStatus);
                reparation.save(miClient.getSession());
                miClient.getSession().notifySuscriptors(mnGridType);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    private void actionGoNext() {
        int newStatus = DLibConsts.UNDEFINED;
        DDbReparation reparation = null;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else if (miClient.showMsgBoxConfirm("¿Está seguro que desea avanzar el comprobante con fecha " + DLibUtils.DateFormatDate.format(miClient.getSession().getWorkingDate()) + "?") == JOptionPane.YES_OPTION) {
            try {
                reparation = (DDbReparation) miClient.getSession().readRegistry(DModConsts.S_REP, getSelectedGridRow().getRowPrimaryKey());
                if (reparation.getNoteReparation().isEmpty()) {
                    miClient.showMsgBoxWarning("El comprobante carece de la descripción del servicio realizado.");
                }
                else {
                    switch (reparation.getFkServiceStatusId()) {
                        case DModSysConsts.SX_SRV_ST_REP_APR:
                            newStatus = DModSysConsts.SX_SRV_ST_REP_FIN;
                            reparation.setDateFinished_n(miClient.getSession().getWorkingDate());
                            break;
                        case DModSysConsts.SX_SRV_ST_REP_FIN:
                            newStatus = DModSysConsts.SX_SRV_ST_REP_DVY;
                            reparation.setDateDelivery_n(miClient.getSession().getWorkingDate());
                            if (miClient.showMsgBoxConfirm("¿Desea imprimir el comprobante?") == JOptionPane.YES_OPTION) {
                                reparation.setPostSaveTarget(reparation);
                                reparation.setPostSaveMethod(DDbReparation.class.getMethod("print", DGuiSession.class));
                                reparation.setPostSaveMethodArgs(new Object[] { miClient.getSession() });
                            }
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }

                    reparation.setFkServiceStatusId(newStatus);
                    miClient.getSession().saveRegistry(reparation);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    private void actionGoPrev() {
        int newStatus = DLibConsts.UNDEFINED;
        DDbReparation reparation = null;

        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else if (miClient.showMsgBoxConfirm("¿Está seguro que desea retroceder el comprobante?") == JOptionPane.YES_OPTION) {
            try {
                reparation = (DDbReparation) miClient.getSession().readRegistry(DModConsts.S_REP, getSelectedGridRow().getRowPrimaryKey());

                switch (reparation.getFkServiceStatusId()) {
                    case DModSysConsts.SX_SRV_ST_REP_DVY:
                        newStatus = DModSysConsts.SX_SRV_ST_REP_FIN;
                        reparation.setDateDelivery_n(null);
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_FIN:
                        newStatus = DModSysConsts.SX_SRV_ST_REP_APR;
                        reparation.setDateFinished_n(null);
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_APR:
                        newStatus = DModSysConsts.SX_SRV_ST_REP_NEW;
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_REJ:
                        newStatus = DModSysConsts.SX_SRV_ST_REP_NEW;
                        break;
                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                reparation.setFkServiceStatusId(newStatus);
                reparation.save(miClient.getSession());
                miClient.getSession().notifySuscriptors(mnGridType);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    private void actionPrint() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            try {
                DSrvUtils.printReparation(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey()[0]);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String field = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        if (filter != null) {
            if (mnGridSubtype == DModSysConsts.SX_SRV_ST_REP_DVY && mnGridMode == DModSysConsts.SX_SRV_DVY_BY_DVY) {
                field = "v.dt_dvy_n";
            }
            else {
                field = "v.dt";
            }
            sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterDate(field, (DGuiDate) filter);
        }

        if (mnGridSubtype != DLibConsts.UNDEFINED) {
            sql += (sql.length() == 0 ? "" : "AND ") + "v.fk_srv_tp = " + DModSysConsts.SS_SRV_TP_REP + " AND v.fk_srv_st = " + mnGridSubtype + " ";
        }

        msSql = "SELECT " +
                "v.id_rep AS " + DDbConsts.FIELD_ID + "1, " +
                "v.num AS " + DDbConsts.FIELD_CODE + ", " +
                "v.num AS " + DDbConsts.FIELD_NAME + ", " +
                "v.num, " +
                "v.dt, " +
                "v.dt_est, " +
                "v.dt_cmt, " +
                "v.dt_fin_n, " +
                "v.dt_dvy_n, " +
                "v.mdl, " +
                "v.snr, " +
                "v.fail, " +
                "v.note_equ, " +
                "v.note_rep, " +
                "v.est, " +
                "v.war, " +
                "v.war, " +
                "b.name, " +
                "equ.code, " +
                "equ.name, " +
                "brd.name, " +
                "st.code, " +
                "st.name, " +
                "DATEDIFF(v.dt_est, '" + DLibUtils.DbmsDateFormatDate.format(miClient.getSession().getSystemDate()) + "') AS f_est, " +
                "DATEDIFF(v.dt_cmt, '" + DLibUtils.DbmsDateFormatDate.format(miClient.getSession().getSystemDate()) + "') AS f_cmt, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur, " +
                "v.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "v.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "v.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "v.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "v.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.S_REP) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "v.fk_bpr_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.SU_EQU) + " AS equ ON " +
                "v.fk_equ = equ.id_equ " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_BRD) + " AS brd ON " +
                "v.fk_brd = brd.id_brd " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.SS_SRV_ST) + " AS st ON " +
                "v.fk_srv_tp = st.id_srv_tp AND v.fk_srv_st = st.id_srv_st " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "v.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "v.fk_usr_upd = uu.id_usr " +
                (sql.length() == 0 ? "" : "WHERE " + sql) +
                "ORDER BY v.num, v.id_rep ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[22];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_RAW, "v.num", "Folio comp.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt", "Fecha comp.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DBprUtils.getBizPartnerClassNameSng(DModSysConsts.BS_BPR_CL_CUS));
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "equ.code", "Equipo");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "brd.name", "Marca");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.mdl", "Modelo");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.snr", "Núm. serie");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "v.est", "Presupuesto $");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur", "Moneda");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "st.code",  DGridConsts.COL_TITLE_STAT + " comp.");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_est", "Presupuesto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "f_est", "Días presupuesto");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_cmt", "Compromiso");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "f_cmt", "Días compromiso");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_fin_n", "Terminado");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "v.dt_dvy_n", "Entregado");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "v.war", "Días garantía");
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
        moSuscriptionsSet.add(DModConsts.IU_BRD);
        moSuscriptionsSet.add(DModConsts.SU_EQU);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjButtonNewApr) {
                actionChangeStatus(DModSysConsts.SX_SRV_ST_REP_APR);
            }
            else if (button == mjButtonNewRej) {
                actionChangeStatus(DModSysConsts.SX_SRV_ST_REP_REJ);
            }
            else if (button == mjButtonGoNext) {
                actionGoNext();
            }
            else if (button == mjButtonGoPrev) {
                actionGoPrev();
            }
            else if (button == mjButtonPrint) {
                actionPrint();
            }
        }
    }
}
