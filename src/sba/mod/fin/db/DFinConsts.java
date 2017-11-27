/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DFinConsts {

    public static final String TXT_MOP_NON_DEF = "NO IDENTIFICADO";
    public static final String TXT_PAY_TP_CSH = "CONTADO";
    public static final String TXT_PAY_TP_CDT = "CRÉDITO";

    /**
     * @param sysMoveClass System move class. Constants defined in DModSysConsts (FS_SYS_MOV_CL_M...).
     */
    public static String getSysMovePaymentName(final int sysMoveClass) {
        String name = "";

        switch (sysMoveClass) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                name = "Cobro";
                break;
            case DModSysConsts.FS_SYS_MOV_CL_MO:
                name = "Pago";
                break;
            default:
        }

        return name;
    }
}
