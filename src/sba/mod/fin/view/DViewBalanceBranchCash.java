/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.mygrid.DMyGridConsts;
import sba.gui.mygrid.DMyGridFilterBranchEntity;
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
import sba.mod.fin.form.DDialogCardexBranchCash;

/**
 *
 * @author Sergio Flores
 */
public class DViewBalanceBranchCash extends DGridPaneView implements ActionListener {

    private JButton mjButtonShowCardex;
    private DDialogCardexBranchCash moDialogCardexBranchCash;
    private DGridFilterDatePeriod moFilterDatePeriod;
    private DMyGridFilterBranchEntity moFilterBranchEntity;

    /**
     * @param client GUI client.
     * @param type Type of business partner balance. Constants defined in DModConsts (FX_BAL_BPR).
     * @param title View title.
     * @param params GUI parameters (view mode and submode). Constants defined in DUtilConsts (PER_...).
     */
    public DViewBalanceBranchCash(DGuiClient client, int type, String title, DGuiParams params) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, DLibConsts.UNDEFINED, title, params);
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, DLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moFilterBranchEntity = new DMyGridFilterBranchEntity(this, DModConsts.CU_CSH);
        moFilterBranchEntity.initFilter(((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).createFilterBranchCashKey());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterBranchEntity);

        mjButtonShowCardex = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CARDEX), "Ver tarjeta auxiliar", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowCardex);

        moDialogCardexBranchCash = new DDialogCardexBranchCash(miClient, mnGridMode);
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
                    moDialogCardexBranchCash.resetForm();
                    moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_DATE, (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD));

                    switch (mnGridMode) {
                        case DUtilConsts.PER_BPR_BRA:
                            moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_BPR_BRA, gridRow.getRowPrimaryKey());
                            break;
                        case DUtilConsts.PER_ENT_CSH:
                            moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_BRA_CSH, gridRow.getRowPrimaryKey());
                            break;
                        default:
                    }

                    switch (mnGridSubmode) {
                        case DUtilConsts.PER_BPR_BRA:
                        case DUtilConsts.PER_ENT_CSH:
                            break;
                        case DUtilConsts.PER_CUR:
                            moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_CUR, gridRow.getComplementsMap().get(DModSysConsts.PARAM_CUR));
                            break;
                        case DUtilConsts.PER_REF:
                            moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_CUR, gridRow.getComplementsMap().get(DModSysConsts.PARAM_CUR));
                            moDialogCardexBranchCash.setValue(DModSysConsts.PARAM_REF, gridRow.getComplementsMap().get(DModSysConsts.PARAM_REF));
                            break;
                        default:
                    }

                    moDialogCardexBranchCash.populateCardex();
                    moDialogCardexBranchCash.setVisible(true);
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
        String select = "";
        String having = "";
        String groupBy = "";
        String orderBy = "";
        Object filter = null;

        switch (mnGridMode) {
            case DUtilConsts.PER_BPR_BRA:
                moPaneSettings = new DGridPaneSettings(2);
                select = "cb.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "cb.id_bra AS " + DDbConsts.FIELD_ID + "2, " +
                        "cb.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "cb.name AS " + DDbConsts.FIELD_NAME + ", ";
                groupBy = "cb.id_bpr, " +
                        "cb.id_bra, " +
                        "cb.code, " +
                        "cb.name";
                orderBy = "cb.name, " +
                        "cb.code, " +
                        "cb.id_bpr, " +
                        "cb.id_bra";
                break;
            case DUtilConsts.PER_ENT_CSH:
                moPaneSettings = new DGridPaneSettings(3);
                select = "be.id_bpr AS " + DDbConsts.FIELD_ID + "1, " +
                        "be.id_bra AS " + DDbConsts.FIELD_ID + "2, " +
                        "be.id_csh AS " + DDbConsts.FIELD_ID + "3, " +
                        "be.code AS " + DDbConsts.FIELD_CODE + ", " +
                        "be.name AS " + DDbConsts.FIELD_NAME + ", " +
                        "cb.code, " +
                        "cb.name, ";
                groupBy = "be.id_bpr, " +
                        "be.id_bra, " +
                        "be.id_csh, " +
                        "be.code, " +
                        "be.name, " +
                        "cb.code, " +
                        "cb.name";
                orderBy = "be.name, " +
                        "be.code, " +
                        "be.id_bpr, " +
                        "be.id_bra, " +
                        "be.id_csh";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        switch (mnGridSubmode) {
            case DUtilConsts.PER_BPR_BRA:
            case DUtilConsts.PER_ENT_CSH:
                select += "";
                groupBy += "";
                orderBy += "";
                break;
            case DUtilConsts.PER_CUR:
                select += "c.id_cur , c.id_cur AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_CUR + ", c.code, ";
                groupBy += ", c.id_cur, c.code";
                orderBy += ", c.code, c.id_cur";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            switch (mnGridSubmode) {
                case DUtilConsts.PER_BPR_BRA:
                case DUtilConsts.PER_ENT_CSH:
                    having = "HAVING NOT (f_bal = 0 AND f_bal_stc = 0) ";
                    break;
                case DUtilConsts.PER_CUR:
                    having = "HAVING NOT (f_bal = 0 AND f_bal_stc = 0 AND f_bal_cy = 0 AND f_bal_cy_stc = 0) ";
                    break;
                default:
            }
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + "bkk.id_yer = " + DLibTimeUtils.digestYear((DGuiDate) filter)[0] + " AND " +
                "bkk.dt <= '" + DLibUtils.DbmsDateFormatDate.format((DGuiDate) filter) + "' ";

        filter = (int[]) moFiltersMap.get(DMyGridConsts.FILTER_BRANCH_CSH);
        if (filter != null) {
            sql += (sql.length() == 0 ? "" : "AND ") + DGridUtils.getSqlFilterKey(new String[] { "bkk.fk_csh_bpr_n", "bkk.fk_csh_bra_n", "bkk.fk_csh_csh_n", }, (int[]) filter);
        }

        msSql = "SELECT " + select +
                "SUM(bkk.dbt - bkk.cdt) AS f_bal_stc, " +
                "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt - bkk.cdt)) AS f_bal, " +
                "SUM(bkk.dbt_cy - bkk.cdt_cy) AS f_bal_cy_stc, " +
                "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt_cy - bkk.cdt_cy)) AS f_bal_cy " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "bkk.fk_own_bpr = cb.id_bpr AND bkk.fk_own_bra = cb.id_bra AND " +
                "bkk.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS be ON " +
                "bkk.fk_csh_bpr_n = be.id_bpr AND bkk.fk_csh_bra_n = be.id_bra AND bkk.fk_csh_csh_n = be.id_csh " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "bkk.fk_cur = c.id_cur " +
                "WHERE bkk.b_del = 0 " + (sql.length() == 0 ? "" : "AND " + sql) +
                "GROUP BY " + groupBy + " " + having +
                "ORDER BY " + orderBy;

        switch (mnGridSubmode) {
            case DUtilConsts.PER_CUR:
                moColumnComplementsMap.put(DModSysConsts.PARAM_CUR, DGridConsts.COL_TYPE_INT_2B);
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
            case DUtilConsts.PER_BPR_BRA:
                cols += 2;
                break;
            case DUtilConsts.PER_ENT_CSH:
                cols += 4;
                break;
            default:
        }

        switch (mnGridSubmode) {
            case DUtilConsts.PER_BPR_BRA:
            case DUtilConsts.PER_ENT_CSH:
                break;
            case DUtilConsts.PER_CUR:
                cols += 3;
                break;
            default:
        }

        cols += 2;

        columns = new DGridColumnView[cols];

        if (mnGridMode == DUtilConsts.PER_BPR_BRA) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
        }
        else if (mnGridMode == DUtilConsts.PER_ENT_CSH) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " cuenta dinero");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " cuenta dinero");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cb.name", DGridConsts.COL_TITLE_NAME + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "cb.code", DGridConsts.COL_TITLE_CODE + " " + DUtilConsts.TXT_BRANCH.toLowerCase());
        }

        if (mnGridSubmode == DUtilConsts.PER_CUR) {
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
        moSuscriptionsSet.add(DModConsts.T_IOM);
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
