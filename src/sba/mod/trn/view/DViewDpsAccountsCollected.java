/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterYear;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridRowView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.fin.db.DFinUtils;
import sba.mod.trn.db.DTrnUtils;
import sba.mod.trn.form.DDialogCardexDps;

/**
 *
 * @author Sergio Flores
 */
public class DViewDpsAccountsCollected extends DGridPaneView implements ActionListener {

    private int mnBizPartnerClass;
    private DGridFilterYear moFilterYear;
    private DDialogCardexDps moDialogCardexDps;
    private JButton mjButtonShowCardex;

    /**
     * @param client GUI client.
     * @param type Type of DPS. Constants defined in DModConsts (TX_ACC_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title View title.
     */
    public DViewDpsAccountsCollected(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);

        mnBizPartnerClass = DTrnUtils.getBizPartnerClassByDpsCategory(mnGridSubtype);
        moFilterYear = new DGridFilterYear(miClient, this);
        moFilterYear.initFilter(DLibTimeUtils.digestYear(miClient.getSession().getWorkingDate()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterYear);

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

    @Override
    public void prepareSqlQuery() {
        int year = 0;
        String num = "";
        String sum = "";
        String orderBy = "";

        moPaneSettings = new DGridPaneSettings(1);

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            num = "d.num";
            sum = "SUM(bkk.cdt - bkk.dbt) AS f_bal_stc, " +
                    "SUM(IF(bkk.b_avl = 0, 0, bkk.cdt - bkk.dbt)) AS f_bal, " +
                    "SUM(bkk.cdt_cy - bkk.dbt_cy) AS f_bal_cy_stc, " +
                    "SUM(IF(bkk.b_avl = 0, 0, bkk.cdt_cy - bkk.dbt_cy)) AS f_bal_cy ";
            orderBy = "b.name, bc.code, b.id_bpr, " +
                    "dt.code, d.fk_dps_ct, d.fk_dps_cl, d.fk_dps_tp, d.ser, " + num + ", d.id_dps ";
        }
        else {
            num = "d.num";
            //num = "CAST(CONCAT(REPEAT('0', " + DLibConsts.LEN_REF_NUM + " - LENGTH(d.num)), d.num) AS CHAR)";
            sum = "SUM(bkk.dbt - bkk.cdt) AS f_bal_stc, " +
                    "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt - bkk.cdt)) AS f_bal, " +
                    "SUM(bkk.dbt_cy - bkk.cdt_cy) AS f_bal_cy_stc, " +
                    "SUM(IF(bkk.b_avl = 0, 0, bkk.dbt_cy - bkk.cdt_cy)) AS f_bal_cy ";
            orderBy = "dt.code, d.fk_dps_ct, d.fk_dps_cl, d.fk_dps_tp, d.ser, " + num + ", d.id_dps ";
        }

        year = ((int[]) moFiltersMap.get(DGridConsts.FILTER_YEAR))[0];

        msSql = "SELECT " +
                "d.id_dps AS " + DDbConsts.FIELD_ID + "1, " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS " + DDbConsts.FIELD_NAME + ", " +
                "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num) AS f_num, " +
                "d.dt, d.cdt_day, ADDDATE(d.dt_cdt, d.cdt_day) AS f_dt_mat, " +
                "d.tot_r, d.tot_cy_r, dt.code, b.name, bc.code, bb.name, cb.code, c.code, " + sum +
                "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                "INNER JOIN  " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d ON " +
                "bkk.fk_dps_inv_n = d.id_dps AND bkk.id_yer = " + year + " AND bkk.b_del = 0 AND " +
                "bkk.fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(mnBizPartnerClass) + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TS_DPS_TP) + " AS dt ON " +
                "d.fk_dps_ct = dt.id_dps_ct AND d.fk_dps_cl = dt.id_dps_cl AND d.fk_dps_tp = dt.id_dps_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b ON " +
                "d.fk_bpr_bpr = b.id_bpr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                "d.fk_bpr_bpr = bc.id_bpr AND bc.id_bpr_cl = " + mnBizPartnerClass + " " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb ON " +
                "d.fk_bpr_bpr = bb.id_bpr AND d.fk_bpr_bra = bb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "d.fk_own_bpr = cb.id_bpr AND d.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                "d.fk_cur = c.id_cur " +
                "GROUP BY d.id_dps, d.ser, d.num, d.dt, d.tot_r, d.tot_cy_r, dt.code, b.name, bc.code, bb.name, cb.code, c.code " +
                "HAVING f_bal = 0 AND f_bal_cy = 0 " +
                "ORDER BY " + orderBy;
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[16];

        if (mnGridSubtype == DModSysConsts.TS_DPS_CT_PUR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "d.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "d.cdt_day", "Días crédito");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "f_dt_mat", "Fecha venc.");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
        }
        else {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "dt.code", DGridConsts.COL_TITLE_TYPE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, "f_num", DGridConsts.COL_TITLE_NUM + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "d.dt", DGridConsts.COL_TITLE_DATE + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_2B, "d.cdt_day", "Días crédito");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "f_dt_mat", "Fecha venc.");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "b.name", DGridConsts.COL_TITLE_NAME + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_BPR, "bc.code", DGridConsts.COL_TITLE_CODE + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bb.name", DUtilConsts.TXT_BRANCH + " " + DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass).toLowerCase());
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "d.tot_cy_r", "Total $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_cy_stc", "Saldo SBC $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "f_bal_cy", "Saldo $ M");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.code", "Moneda");
        columns[col] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT, "d.tot_r", "Total $ ML");
        columns[col++].setSumApplying(true);
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
