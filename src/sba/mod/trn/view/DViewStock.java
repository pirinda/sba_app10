/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.ImageIcon;
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
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.trn.db.DTrnImportDeclaration;
import sba.mod.trn.form.DDialogCardexStock;
import sba.mod.trn.form.DDialogHistoryPrices;

/**
 *
 * @author Sergio Flores
 */
public class DViewStock extends DGridPaneView implements ActionListener {

    private final JButton mjButtonShowCardexStock;
    private final JButton mjButtonShowHistoryPrices;
    private final DDialogCardexStock moDialogCardexStock;
    private final DDialogHistoryPrices moDialogHistoryPrices;
    private final DGridFilterDatePeriod moFilterDatePeriod;
    private final DMyGridFilterBranchEntity moFilterBranchEntity;

    /**
     * @param client GUI client.
     * @param type Type of stock. Constant defined in DModConsts (TX_STK or TX_STK_WAH).
     * @param subtype Subtype of stock. Constants defined in DUtilConsts (PER_...).
     * @param title View title.
     */
    public DViewStock(DGuiClient client, int type, int subtype, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, type, subtype, title);

        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE);
        moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_DATE, DLibTimeUtils.getEndOfYear(miClient.getSession().getWorkingDate()).getTime()));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        moFilterBranchEntity = new DMyGridFilterBranchEntity(this, DModConsts.CU_WAH);
        moFilterBranchEntity.initFilter(((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).createFilterBranchWarehouseKey());
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterBranchEntity);

        mjButtonShowCardexStock = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_CARDEX), "Ver tarjeta auxiliar", this);
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowCardexStock);

        mjButtonShowHistoryPrices = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_hst.gif")), "Ver historial precios compra", this);
        mjButtonShowHistoryPrices.setEnabled(miClient.getSession().getUser().hasModuleAccess(DModSysConsts.CS_MOD_PUR));
        getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowHistoryPrices);

        moDialogCardexStock = new DDialogCardexStock(client, mnGridSubtype);
        moDialogHistoryPrices = new DDialogHistoryPrices(client, DModSysConsts.TS_DPS_CT_PUR);
    }

    private void actionShowCardexStock() {
        if (mjButtonShowCardexStock.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogCardexStock.resetForm();
                    moDialogCardexStock.setValue(DModSysConsts.PARAM_DATE, (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD));

                    /* XXX To do:
                     * Add branch and warehouse filter.
                     */

                    switch (mnGridSubtype) {
                        case DUtilConsts.PER_ITM:
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM_UNT, gridRow.getRowPrimaryKey()[1]);
                            break;
                        case DUtilConsts.PER_LOT:
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_LOT_KEY, gridRow.getRowPrimaryKey());
                            break;
                        case DUtilConsts.PER_SNR:
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM_UNT, gridRow.getRowPrimaryKey()[1]);
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_SNR, gridRow.getComplementsMap().get(DModSysConsts.PARAM_SNR));
                            break;
                        case DUtilConsts.PER_IMP_DEC:
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_ITM_UNT, gridRow.getRowPrimaryKey()[1]);
                            moDialogCardexStock.setValue(DModSysConsts.PARAM_IMP_DEC, new DTrnImportDeclaration((String) gridRow.getComplementsMap().get(DModSysConsts.PARAM_IMP_DEC), (Date) gridRow.getComplementsMap().get(DModSysConsts.PARAM_IMP_DEC_DT)));
                            break;
                        default:
                    }

                    moDialogCardexStock.populateCardex();
                    moDialogCardexStock.setVisible(true);
                }
            }
        }
    }

    private void actionShowHistoryPrices() {
        if (mjButtonShowHistoryPrices.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                DGridRowView gridRow = (DGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogHistoryPrices.resetForm();
                    moDialogHistoryPrices.setValue(DModSysConsts.PARAM_ITM, gridRow.getRowPrimaryKey()[0]);
                    moDialogHistoryPrices.populateHistory();
                    moDialogHistoryPrices.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlSelect = "";
        String sqlHaving = "";
        String sqlGroupBy = "";
        String sqlOrderBy = "";
        String sqlInnerJoin = "";
        Object filter = null;
        
        String selectIds = 
                "s.id_itm AS " + DDbConsts.FIELD_ID + "1, " +
                "s.id_unt AS " + DDbConsts.FIELD_ID + "2, ";
        
        String selectNames =
                "i.name AS " + DDbConsts.FIELD_NAME + ", " +
                "i.code AS " + DDbConsts.FIELD_CODE + ", " +
                "u.code";
        
        String groupBy =
                "s.id_itm, " +
                "s.id_unt, " +
                "i.name, " +
                "i.code, " +
                "u.code";
        
        String orderBy =
                "i.name, " +
                "i.code, " +
                "u.code, " +
                "s.id_itm, " +
                "s.id_unt";
        
        int idsWah = 0;
        
        if (mnGridType == DModConsts.TX_STK_WAH) {
            idsWah = 3;
            
            selectIds +=
                    "s.id_bpr AS " + DDbConsts.FIELD_ID + "3, " +
                    "s.id_bra AS " + DDbConsts.FIELD_ID + "4, " +
                    "s.id_wah AS " + DDbConsts.FIELD_ID + "5, ";
            
            selectNames += ", " +
                    "w.code";
            
            groupBy += ", " +
                    "s.id_bpr, " +
                    "s.id_bra, " +
                    "s.id_wah, " +
                    "w.code";
            
            orderBy += ", " +
                    "w.code, " +
                    "s.id_bpr, " +
                    "s.id_bra, " +
                    "s.id_wah";
            
            sqlInnerJoin += "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS w ON " +
                    "s.id_bpr = w.id_bpr AND s.id_bra = w.id_bra AND s.id_wah = w.id_wah ";
        }
        
        switch (mnGridSubtype) {
            case DUtilConsts.PER_ITM:
                moPaneSettings = new DGridPaneSettings(2 + idsWah);
                sqlSelect += selectIds + selectNames + ", ";
                sqlGroupBy += groupBy + " ";
                sqlOrderBy += orderBy + " ";
                break;

            case DUtilConsts.PER_LOT:
                moPaneSettings = new DGridPaneSettings(2 + idsWah + 1);
                sqlSelect += selectIds + "s.id_lot AS " + DDbConsts.FIELD_ID + (2 + idsWah + 1) + ", " + selectNames + ", " +
                        "l.lot, " +
                        "l.dt_exp_n, ";
                sqlGroupBy += groupBy + ", " +
                        "s.id_lot, " +
                        "l.lot, " +
                        "l.dt_exp_n ";
                sqlOrderBy += orderBy + ", " +
                        "l.lot, " +
                        "l.dt_exp_n, " +
                        "s.id_lot ";

                sqlInnerJoin += "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " AS l ON " +
                        "s.id_itm = l.id_itm AND s.id_unt = l.id_unt AND s.id_lot = l.id_lot ";
                break;

            case DUtilConsts.PER_SNR:
                moPaneSettings = new DGridPaneSettings(2 + idsWah);
                sqlSelect += selectIds + selectNames + ", " +
                        "s.snr, " +
                        "s.snr AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_SNR + ", ";
                sqlGroupBy += groupBy + ", " +
                        "s.snr ";
                sqlOrderBy += orderBy + ", " +
                        "s.snr ";
                break;

            case DUtilConsts.PER_IMP_DEC:
                moPaneSettings = new DGridPaneSettings(2 + idsWah);
                sqlSelect += selectIds + selectNames + ", " +
                        "s.imp_dec, " +
                        "s.imp_dec_dt_n, " +
                        "s.imp_dec AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_IMP_DEC + ", " +
                        "s.imp_dec_dt_n AS " + DDbConsts.FIELD_COMP + DModSysConsts.PARAM_IMP_DEC_DT + ", ";
                sqlGroupBy += groupBy + ", " +
                        "s.imp_dec, " +
                        "s.imp_dec_dt_n ";
                sqlOrderBy += orderBy + ", " +
                        "s.imp_dec, " +
                        "s.imp_dec_dt_n ";
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sqlHaving = "HAVING f_stk <> 0 ";
        }

        filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
        sql += (sql.length() == 0 ? "" : "AND ") + "s.id_yer = " + DLibTimeUtils.digestYear((DGuiDate) filter)[0] + " AND " +
                "s.dt <= '" + DLibUtils.DbmsDateFormatDate.format((DGuiDate) filter) + "' ";

        filter = (int[]) moFiltersMap.get(DMyGridConsts.FILTER_BRANCH_WAH);
        if (filter != null) {
            String aux = DGridUtils.getSqlFilterKey(new String[] { "s.id_bpr", "s.id_bra", "s.id_wah" }, (int[]) filter);
            if (!aux.isEmpty()) {
                sql += (sql.length() == 0 ? "" : "AND ") + aux;
            }
        }

        msSql = "SELECT " + sqlSelect + "SUM(s.mov_in - s.mov_out) AS f_stk " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "s.id_itm = i.id_itm " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON " +
                "s.id_unt = u.id_unt " +
                sqlInnerJoin +
                "WHERE NOT s.b_del " + (sql.isEmpty() ? "" : "AND " + sql) +
                (mnGridSubtype == DUtilConsts.PER_SNR ? "AND s.snr <> '' " : "") +
                (mnGridSubtype == DUtilConsts.PER_IMP_DEC ? "AND s.imp_dec <> '' " : "") +
                "GROUP BY " + sqlGroupBy + sqlHaving +
                "ORDER BY " + sqlOrderBy + ";";

        switch (mnGridSubtype) {
            case DUtilConsts.PER_SNR:
                moColumnComplementsMap.put(DModSysConsts.PARAM_SNR, DGridConsts.COL_TYPE_TEXT);
                break;
            case DUtilConsts.PER_IMP_DEC:
                moColumnComplementsMap.put(DModSysConsts.PARAM_IMP_DEC, DGridConsts.COL_TYPE_TEXT);
                moColumnComplementsMap.put(DModSysConsts.PARAM_IMP_DEC_DT, DGridConsts.COL_TYPE_DATE);
                break;
            default:
        }
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        int cols = 0;
        DGridColumnView[] columns = null;

        cols = 4;

        if (mnGridType == DModConsts.TX_STK_WAH) {
            cols += 1; // for code of warehouse
        }
        
        switch (mnGridSubtype) {
            case DUtilConsts.PER_ITM:
                break;
            case DUtilConsts.PER_LOT:
                cols += 2;
                break;
            case DUtilConsts.PER_SNR:
                cols += 1;
                break;
            case DUtilConsts.PER_IMP_DEC:
                cols += 2;
                break;
            default:
        }

        columns = new DGridColumnView[cols];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " ítem");

        if (mnGridType == DModConsts.TX_STK_WAH) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "w.code", DGridConsts.COL_TITLE_CODE + " almacén");
        }
        
        if (mnGridSubtype == DUtilConsts.PER_LOT) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "l.lot", "Lote");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "l.dt_exp_n", "Caducidad");
        }

        if (mnGridSubtype == DUtilConsts.PER_SNR) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "s.snr", "Número serie", 150);
        }

        if (mnGridSubtype == DUtilConsts.PER_IMP_DEC) {
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT, "s.imp_dec", "Pedimento", 150);
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, "s.imp_dec_dt_n", "Importación");
        }

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_QTY, "f_stk", "Existencias");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.code", "Unidad");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.IU_UNT);
        moSuscriptionsSet.add(DModConsts.T_DPS);
        moSuscriptionsSet.add(DModConsts.T_IOG);
        moSuscriptionsSet.add(DModConsts.T_SNR_FIX);
    }

    @Override
    public void actionMouseClicked() {
        actionShowCardexStock();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjButtonShowCardexStock) {
                actionShowCardexStock();
            }
            else if (button == mjButtonShowHistoryPrices) {
                actionShowHistoryPrices();
            }
        }
    }
}
