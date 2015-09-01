/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DBprUtils {

    /**
     * @param bizPartnerClass Constants defined in DModSysConsts (BS_BPR_CL_...).
     * @return Business partner class singular name.
     */
    public static String getBizPartnerClassNameSng(final int bizPartnerClass) {
        String name = "";

        switch (bizPartnerClass) {
            case DModSysConsts.BS_BPR_CL_VEN:
                name = "Proveedor";
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
                name = "Cliente";
                break;
            case DModSysConsts.BS_BPR_CL_CDR:
                name = "Acreedor";
                break;
            case DModSysConsts.BS_BPR_CL_DBR:
                name = "Deudor";
                break;
            default:
        }

        return name;
    }

    /**
     * @param bizPartnerClass Constants defined in DModSysConsts (BS_BPR_CL_...).
     * @return Business partner class plural name.
     */
    public static String getBizPartnerClassNamePlr(final int bizPartnerClass) {
        String name = "";

        switch (bizPartnerClass) {
            case DModSysConsts.BS_BPR_CL_VEN:
                name = "Proveedores";
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
                name = "Clientes";
                break;
            case DModSysConsts.BS_BPR_CL_CDR:
                name = "Acreedores";
                break;
            case DModSysConsts.BS_BPR_CL_DBR:
                name = "Deudores";
                break;
            default:
        }

        return name;
    }
}
