/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.gui.prt;

import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sba.gui.DGuiClientApp;
import sba.lib.DLibUtils;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DPrtUtils {

    private static String getReportFileName(final int type) {
        String fileName = "";

        switch (type) {
            case DModConsts.TR_DPS:
                fileName = "reps/dps.jasper";
                break;
            case DModConsts.TR_DPS_CFD:
                fileName = "reps/cfd.jasper";
                break;
            case DModConsts.TR_DPS_CFDI_32:
                fileName = "reps/cfdi.jasper";
                break;
            case DModConsts.TR_DPS_CFDI_33:
                fileName = "reps/cfdi33.jasper";
                break;
            case DModConsts.TR_DPS_CFDI_33_CRP_10:
                fileName = "reps/trn_cfdi_33_crp_10.jasper";
                break;
            case DModConsts.SR_REP_NEW:
                fileName = "reps/rep_new.jasper";
                break;
            case DModConsts.SR_REP_DVY:
                fileName = "reps/rep_dvy.jasper";
                break;
            default:
        }

        return fileName;
    }

    public static HashMap<String, Object> createReportHashMap(final DGuiSession session) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        //map.put("sCompanyName", ((DGuiClientSessionCustom) session.getSessionCustom()).getCompany().getName());
        map.put("sUserName", session.getUser().getName());
        map.put("sAppName", DGuiClientApp.APP_NAME);
        map.put("sAppCopyright", DGuiClientApp.APP_COPYRIGHT);
        map.put("sAppProvider", DGuiClientApp.APP_PROVIDER);
        map.put("oFormatDate", DLibUtils.DateFormatDate);
        map.put("oFormatDateShort", DLibUtils.DateFormatDateShort);
        map.put("oFormatDatetime", DLibUtils.DateFormatDatetime);
        map.put("oFormatTime", DLibUtils.DateFormatTime);

        return map;
    }

    public static void printReport(final DGuiSession session, final int type, final HashMap<String, Object> hashMap) throws Exception, JRException {
        File file = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        try {
            file = new File(getReportFileName(type));

            jasperReport = (JasperReport) JRLoader.loadObject(file);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, session.getStatement().getConnection());

            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(DGuiClientApp.APP_NAME);
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            DLibUtils.showException(DPrtUtils.class.getName(), e);
        }
    }

    public static void exportReportToPdfFile(final DGuiSession session, final int type, final HashMap<String, Object> hashMap, String destFileName) throws Exception, JRException {
        File file = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;

        try {
            file = new File(getReportFileName(type));

            jasperReport = (JasperReport) JRLoader.loadObject(file);
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, session.getStatement().getConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
        }
        catch (Exception e) {
            DLibUtils.showException(DPrtUtils.class.getName(), e);
        }
    }
}
