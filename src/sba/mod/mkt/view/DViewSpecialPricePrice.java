/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.view;

import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DViewSpecialPricePrice extends DGridPaneView {

    public DViewSpecialPricePrice(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.M_SPE_PRC, DLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new DGridPaneSettings(3);

        msSql = "SELECT " +
                "v.id_spe AS " + DDbConsts.FIELD_ID + "1, " +
                "v.id_pay_tp AS " + DDbConsts.FIELD_ID + "2, " +
                "v.id_itm AS " + DDbConsts.FIELD_ID + "3, " +
                "s.code AS " + DDbConsts.FIELD_CODE + ", " +
                "s.name AS " + DDbConsts.FIELD_NAME + ", " +
                "pt.name, " +
                "i.code, " +
                "i.name, " +
                "v.prc, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.M_SPE_PRC) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_SPE) + " AS s ON " +
                "v.id_spe = s.id_spe " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_PAY_TP) + " AS pt ON " +
                "v.id_pay_tp = pt.id_pay_tp " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i ON " +
                "v.id_itm = i.id_itm " +
                "WHERE s.b_del = 0 " +
                "ORDER BY s.name, s.code, v.id_spe, pt.name, v.id_pay_tp, i.name, i.code, v.id_itm ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[7];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DDbConsts.FIELD_NAME, DGridConsts.COL_TITLE_NAME + " lista precios especiales");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE + " lista precios especiales");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pt.name", DGridConsts.COL_TITLE_TYPE + " pago");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.name", DGridConsts.COL_TITLE_NAME + " ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.code", DGridConsts.COL_TITLE_CODE + " ítem");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "v.prc", "Precio esp. $");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur", "Moneda");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.M_SPE);
        moSuscriptionsSet.add(DModConsts.IU_ITM);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
