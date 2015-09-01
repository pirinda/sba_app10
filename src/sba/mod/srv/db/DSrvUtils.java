/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.srv.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import sba.gui.prt.DPrtUtils;
import sba.lib.DLibUtils;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbSysCurrency;

/**
 *
 * @author Sergio Flores
 */
public abstract class DSrvUtils {

    public static int getNextReparationNumber(final DGuiSession session) throws SQLException, Exception {
        int nextNumber = 1;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(num), 0) + 1 FROM " + DModConsts.TablesMap.get(DModConsts.S_REP) + " WHERE b_del = 0; ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            nextNumber = resultSet.getInt(1);
        }

        return nextNumber;
    }

    public static void printReparation(final DGuiSession session, final int idReparation) throws Exception {
        HashMap<String, Object> map = null;
        DDbSysCurrency currency = (DDbSysCurrency) session.readRegistry(DModConsts.CS_CUR, session.getSessionCustom().getLocalCurrencyKey());
        DDbReparation reparation = (DDbReparation) session.readRegistry(DModConsts.S_REP, new int[] { idReparation });

        map = DPrtUtils.createReportHashMap(session);
        map.put("nRepID", idReparation);
        map.put("sTotalText", DLibUtils.translateValueToText(reparation.getEstimate(), DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits(),
                session.getSessionCustom().getLocalLanguage(),
                currency.getCurrencySingular(), currency.getCurrencyPlural(), currency.getCurrencyPrefix(), currency.getCurrencySuffix()));

        if (reparation.getFkServiceStatusId() == DModSysConsts.SX_SRV_ST_REP_NEW) {
            DPrtUtils.printReport(session, DModConsts.SR_REP_NEW, map);
        }
        else {
            DPrtUtils.printReport(session, DModConsts.SR_REP_DVY, map);
        }
    }
}
