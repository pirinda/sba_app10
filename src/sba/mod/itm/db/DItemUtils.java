/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.itm.db;

import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DItemUtils {

    /**
     * @param session User GUI session.
     * @param itemLinkType Constants defined in <code>DModSysConsts</code> (IS_LNK_ITM_TP_...).
     * @param itemLinkId Item link ID.
     */
    public static String readItemLinkName(final DGuiSession session, final int itemLinkType, final int itemLinkId) {
        String name = "";

        switch (itemLinkType) {
            case DModSysConsts.IS_LNK_ITM_TP_ITM:
                name = (String) session.readField(DModConsts.IU_ITM, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_MFR:
                name = (String) session.readField(DModConsts.IU_MFR, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_BRD:
                name = (String) session.readField(DModConsts.IU_BRD, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_LIN:
                name = (String) session.readField(DModConsts.IU_LIN, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_GEN:
                name = (String) session.readField(DModConsts.IU_GEN, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_FAM:
                name = (String) session.readField(DModConsts.IU_FAM, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_TP:
                name = (String) session.readField(DModConsts.IX_ITM_TP_BY_IDX, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CL:
                name = (String) session.readField(DModConsts.IX_ITM_CL_BY_IDX, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CT:
                name = (String) session.readField(DModConsts.IS_ITM_CT, new int[] { itemLinkId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ALL:
                name = DUtilConsts.ALL;
                break;
            default:
        }

        return name;
    }
}
