/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.view;

import sba.gui.util.DUtilConsts;
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
public class DViewXmlStampsAvailable extends DGridPaneView {
    
    public DViewXmlStampsAvailable(DGuiClient client, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.TX_XSM_AVA, DLibConsts.UNDEFINED, title);
        
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new DGridPaneSettings(3);

        msSql = "SELECT " +
                "v.id_xsp AS " + DDbConsts.FIELD_ID + "1, " +
                "v.fk_sig_bpr_n AS " + DDbConsts.FIELD_ID + "2, " +
                "v.fk_sig_bra_n AS " + DDbConsts.FIELD_ID + "3, " +
                "SUM(v.mov_in) - SUM(v.mov_out) AS f_ava, " +
                "p.code AS " + DDbConsts.FIELD_CODE + ", " +
                "p.name AS " + DDbConsts.FIELD_NAME + ", " +
                "COALESCE(cb.code, '" + DUtilConsts.NON_APPLYING + "') AS f_bra " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSM) + " AS v " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_XSP) + " AS p ON " +
                "v.id_xsp = p.id_xsp " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "v.fk_sig_bpr_n = cb.id_bpr AND v.fk_sig_bra_n = cb.id_bra " +
                "WHERE v.b_del = 0 " +
                "GROUP BY v.id_xsp, v.fk_sig_bpr_n, v.fk_sig_bra_n, p.code, p.name " +
                "ORDER BY f_bra, p.code, p.name, v.id_xsp, v.fk_sig_bpr_n, v.fk_sig_bra_n ";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = new DGridColumnView[3];

        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "f_bra", DUtilConsts.TXT_BRANCH + " empresa");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, DGridConsts.COL_TITLE_CODE);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_4B, "f_ava", "Disponibles");

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.T_DPS_SIG);
        moSuscriptionsSet.add(DModConsts.T_XSM);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }
}
