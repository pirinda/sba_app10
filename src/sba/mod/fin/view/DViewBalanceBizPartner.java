/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
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
import sba.mod.fin.db.DFinUtils;
import sba.mod.fin.form.DDialogCardexBizPartner;

/**
 *
 * @author Sergio Flores
 */
public class DViewBalanceBizPartner extends DGridPaneView implements ActionListener {

    int mnBizPartnerClass;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjButtonShowCardex;
    private DDialogCardexBizPartner moDialogCardexBizPartner;

    /**
     * @param client GUI client.
     * @param type Type of business partner balance. Constants defined in DModConsts (FX_BAL_BPR).
     * @param subtype Class of business partner. Constants defined in DModSysConsts (BS_BPR_CL_...).
     * @param title View title.
     * @param params GUI parameters (view mode and submode). Constants defined in DUtilConsts (PER_...).
     */
    public DViewBalanceBizPartner(DGuiClient client, int type, int subtype, String title, DGuiParams params) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title, params);
        mnBizPartnerClass = mnGridSubtype;
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, DLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        mjButtonShowCardex = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CARDEX), "Ver tarjeta auxiliar", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowCardex);

        moDialogCardexBizPartner = new DDialogCardexBizPartner(miClient, mnBizPartnerClass, mnGridMode);
    }

    /*
     * Private methods
     */

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
                    moDialogCardexBizPartner.resetForm();
                    moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_DATE, (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD));

                    switch (mnGridMode) {
                        case DUtilConsts.PER_BPR:
                            moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_BPR, gridRow.getRowPrimaryKey());
                            break;
                        case DUtilConsts.PER_BPR_BRA:
                            moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_BPR_BRA, gridRow.getRowPrimaryKey());
                            break;
                        default:
                    }

                    switch (mnGridSubmode) {
                        case DUtilConsts.PER_BPR:
                        case DUtilConsts.PER_BPR_BRA:
                            break;
                        case DUtilConsts.PER_CUR:
                            moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_CUR, gridRow.getComplementsMap().get(DModSysConsts.PARAM_CUR));
                            break;
                        case DUtilConsts.PER_REF:
                            moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_CUR, gridRow.getComplementsMap().get(DModSysConsts.PARAM_CUR));
                            moDialogCardexBizPartner.setValue(DModSysConsts.PARAM_REF, gridRow.getComplementsMap().get(DModSysConsts.PARAM_REF));
                            break;
                        default:
                    }

                    moDialogCardexBizPartner.populateCardex();
                    moDialogCardexBizPartner.setVisible(true);
                }
            }
        }
    }

    /*
     * Overrided methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sum = "";
        String select = "";
        String having = "";
        String groupBy = "";
        String orderBy = "";
        Object filter = null;

        switch (mnGridMode) {
            case DUtilConsts.PER_BPR:
                moPaneSettings = new DGridPaneSettings(1);
                select = "b.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "b.name AS " + DDbConsts.FIELD_NAME + ", " +
                        "bc.code AS " + DDbConsts.FIELD_CODE + "";
                groupBy = "b.id_bpr, " +
                        "b.name, " +
                        "bc.code";
                orderBy = "b.name, " +
                        "bc.code, " +
                        "b.id_bpr";
                break;
            case DUtilConsts.PER_BPR_BRA:
                moPaneSettings = new DGridPaneSettings(2);
                select = "b.id_bpr, " +
                        "b.name AS " + DDbConsts.FIELD_NAME + ", " +
                        "bc.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "bb.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "bb.id_bra AS " + DDbConsts.FIELD_ID + "2, " +
                        "bb.name";
                groupBy = "b.id_bpr, " +
                        "b.name, " +
                        "bc.code, " +
                        "bb.id_bpr, " +
                        "bb.id_bra, " +
                        "bb.name";
                orderBy = "b.name, " +
                        "bc.code, " +
                        "b.id_bpr, " +
                        "bb.name, " +
                        "bb.id_bpr, " +
                        "bb.id_bra";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        switch (mnGridSubmode) {
            case DUtilConsts.PER_BPR:
            case DUtilConsts.PER_BPR_BRA:
                select += "";
                groupBy += "";
                orderBy += "";
                break;
            case DUtilConsts.PER_CUR:
                select += ", c.id_cur, c.id_cur AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_CUR + ", c.code";
                groupBy += ", c.id_cur, c.code";
                orderBy += ", c.code, c.id_cur";
                break;
            case DUtilConsts.PER_REF:
                select += ", bkk.ref, bkk.ref AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_REF + ", " +
                        "c.id_cur, c.id_cur AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_CUR + ", c.code";
                groupBy += ", bkk.ref, c.id_cur, c.code";
                orderBy += ", bkk.ref, c.code, c.id_cur";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        switch (mnGridSubtype) {
            case DModSysConsts.BS_BPR_CL_VEN:
            case DModSysConsts.BS_BPR_CL_CDR:
                sum = ", SUM(bkk.cdt - bkk.dbt) AS f_bal_stc, " +
                        "SUM(IF(bkk.b_avl = 0, 0, bkk.cdt - bkk.dbt)) AS f_bal, " +
                        "SUM(bkk.cdt_cy - bkk.dbt_cy) AS f_bal_cy_stc, " +
                        "SUM(IF(bkk.b_avl = 0, 0, bkk.cdt_cy - bkk.dbt_cy)) AS f_bal_cy ";
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
            case DModSysConsts.BS_BPR_CL_DBR:
                sum = ", SUM(bkk.dbt - bkk.cdt) AS f_bal_stc, " +
                        "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt - bkk.cdt)) AS f_bal, " +
                        "SUM(bkk.dbt_cy - bkk.cdt_cy) AS f_bal_cy_stc, " +
                        "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt_cy - bkk.cdt_cy)) AS f_bal_cy ";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            switch (mnGridSubmode) {
                case DUtilConsts.PER_BPR:
                case DUtilConsts.PER_BPR_BRA:
                    having = "HAVING NOT (f_bal = 0 AND f_bal_stc = 0) ";
                    break;
                case DUtilConsts.PER_CUR:
                case DUtilConsts.PER_REF:
                    having += "HAVING NOT (f_bal = 0 AND f_bal_stc = 0 AND f_bal_cy = 0 AND f_bal_cy_stc = 0) ";
                    break;
                default:
                    miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + "bkk.id_yer = " + DLibTimeUtils.digestYear((DGuiDate) filter)[0] + " AND " +
                "bkk.dt <= '" + DLibUtils.DbmsDateFormatDate.format((DGuiDate) filter) + "' ";

        msSql = "SELECT " + select + sum +
                "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "bkk.fk_bpr_bpr_n = b.id_bpr AND bkk.fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(mnBizPartnerClass) + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "b.id_bpr = bc.id_bpr AND bc.id_bpr_cl = " + mnBizPartnerClass + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb ON " +
                "bkk.fk_bpr_bpr_n = bb.id_bpr AND bkk.fk_bpr_bra_n = bb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "bkk.fk_cur = c.id_cur " +
                "WHERE bkk.b_del = 0 " + (sql.length() == 0 ? "" : "AND " + sql) +
                "GROUP BY " + groupBy + " " + having +
                "ORDER BY " + orderBy;

        switch (mnGridSubmode) {
            case DUtilConsts.PER_CUR:
                moColumnComplementsMap.put(DModSysConsts.PARAM_CUR, DGridConsts.COL_TYPE_INT_2B);
                break;
            case DUtilConsts.PER_REF:
                moColumnComplementsMap.put(DModSysConsts.PARAM_CUR, DGridConsts.COL_TYPE_INT_2B);
                moColumnComplementsMap.put(DModSysConsts.PARAM_REF, DGridConsts.COL_TYPE_TEXT);
                break;
            default:
        }
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        int cols = 0;
        DGridColumnView[] columns = null;

        switch (mnGridMode) {
            case DUtilConsts.PER_BPR:
                cols += 2;
                break;
            case DUtilConsts.PER_BPR_BRA:
                cols += 3;
                break;
            default:
        }

        switch (mnGridSubmode) {
            case DUtilConsts.PER_BPR:
            case DUtilConsts.PER_BPR_BRA:
                break;
            case DUtilConsts.PER_CUR:
                cols += 3;
                break;
            case DUtilConsts.PER_REF:
                cols += 4;
                break;
            default:
        }

        cols += 2;

        columns = new DGridColumnView[cols];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());

        if (mnGridMode == DUtilConsts.PER_BPR_BRA) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
        }

        if (mnGridSubmode == DUtilConsts.PER_REF) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "bkk.ref", "Referencia");
        }

        if (DLibUtils.belongsTo(mnGridSubmode, new int[] { DUtilConsts.PER_CUR, DUtilConsts.PER_REF })) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_cy_stc", "Saldo SBC $ M");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_cy", "Saldo $ M");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        }

        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_stc", "Saldo SBC $ ML");
        columns[col++].setSumApplying(true);
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal", "Saldo $ ML");
        columns[col++].setSumApplying(true);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.BU_BPR);
        moSuscriptionsSet.add(DModConsts.BU_BRA);
        moSuscriptionsSet.add(DModConsts.F_BKK);
        moSuscriptionsSet.add(DModConsts.F_BKK_REC);
        moSuscriptionsSet.add(DModConsts.FX_BKK_CTM);
        moSuscriptionsSet.add(DModConsts.T_DFR);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.TX_DFR_PAY);
    }

    @Override
    public void actionMouseClicked() {
        actionShowCardex();
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
