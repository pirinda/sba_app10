/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import sba.lib.DLibConsts;
import sba.lib.gui.DGuiModuleUtils;

/**
 *
 * @author Sergio Flores
 */
public class DModUtils implements DGuiModuleUtils {

    public DModUtils() {

    }

    @Override
    public int getModuleTypeByType(final int type) {
        int module = DLibConsts.UNDEFINED;

        if (type >= DModConsts.SU_SYS && type < DModConsts.CS_CSH_TP) {
            module = DModConsts.MOD_SYS;
        }
        else if (type >= DModConsts.CS_CSH_TP && type < DModConsts.BS_BPR_CL) {
            module = DModConsts.MOD_CFG;
        }
        else if (type >= DModConsts.BS_BPR_CL && type < DModConsts.IS_ITM_CT) {
            module = DModConsts.MOD_BPR;
        }
        else if (type >= DModConsts.IS_ITM_CT && type < DModConsts.FS_ACC_CT) {
            module = DModConsts.MOD_ITM;
        }
        else if (type >= DModConsts.FS_ACC_CT && type < DModConsts.TS_FIS_TP) {
            module = DModConsts.MOD_FIN;
        }
        else if (type >= DModConsts.TS_FIS_TP && type < DModConsts.MS_PRC_TP) {
            module = DModConsts.MOD_TRN;
        }
        else if (type >= DModConsts.MS_PRC_TP && type < DModConsts.SS_SRV_TP) {
            module = DModConsts.MOD_MKT;
        }
        else if (type >= DModConsts.SS_SRV_TP) {
            module = DModConsts.MOD_SRV;
        }

        return module;
    }

    @Override
    public int getModuleSubtypeBySubtype(final int type, final int subtype) {
        int submodule = DLibConsts.UNDEFINED;

        if (subtype != DLibConsts.UNDEFINED) {
            if (type >= DModConsts.SU_SYS && type < DModConsts.CS_CSH_TP) {

            }
            else if (type >= DModConsts.CS_CSH_TP && type < DModConsts.BS_BPR_CL) {

            }
            else if (type >= DModConsts.BS_BPR_CL && type < DModConsts.IS_ITM_CT) {

            }
            else if (type >= DModConsts.IS_ITM_CT && type < DModConsts.FS_ACC_CT) {

            }
            else if (type >= DModConsts.FS_ACC_CT && type < DModConsts.TS_FIS_TP) {

            }
            else if (type >= DModConsts.TS_FIS_TP && type < DModConsts.MS_PRC_TP) {
                switch (subtype) {
                    case DModSysConsts.TS_DPS_CT_PUR:
                        submodule = DModSysConsts.CS_MOD_PUR;
                        break;
                    case DModSysConsts.TS_DPS_CT_SAL:
                        submodule = DModSysConsts.CS_MOD_SAL;
                        break;
                    default:
                }
            }
            else if (type >= DModConsts.MS_PRC_TP && type < DModConsts.SS_SRV_TP) {

            }
            else if (type >= DModConsts.SS_SRV_TP) {

            }
        }

        return submodule;
    }
}
