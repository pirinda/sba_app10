/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DFinUtils {

    /**
     * @param sysMoveClass Constants defined in DModSysConsts (FS_SYS_MOV_CL_M...).
     * @return System move class acronym.
     */
    public static String getCashMoveNameAcronym(final int sysMoveClass) {
        String name = "";

        switch (sysMoveClass) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                name = "ING";
                break;
            case DModSysConsts.FS_SYS_MOV_CL_MO:
                name = "EGR";
                break;
            default:
        }

        return name;
    }

    /**
     * @param sysMoveClass Constants defined in DModSysConsts (FS_SYS_MOV_CL_M...).
     * @return System move class singular name.
     */
    public static String getCashMoveNameSng(final int sysMoveClass) {
        String name = "";

        switch (sysMoveClass) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                name = "Ingreso";
                break;
            case DModSysConsts.FS_SYS_MOV_CL_MO:
                name = "Egreso";
                break;
            default:
        }

        return name;
    }

    /**
     * @param sysMoveClass Constants defined in DModSysConsts (FS_SYS_MOV_CL_M...).
     * @return System move class plural name.
     */
    public static String getCashMoveNamePlr(final int sysMoveClass) {
        String name = "";

        switch (sysMoveClass) {
            case DModSysConsts.FS_SYS_MOV_CL_MI:
                name = "Ingresos";
                break;
            case DModSysConsts.FS_SYS_MOV_CL_MO:
                name = "Egresos";
                break;
            default:
        }

        return name;
    }

    public static DDbAbpBranchCash readAbpBranchCash(final DGuiSession session, final int[] entityKey) {
        String sql = "";
        ResultSet resultSet = null;
        DDbAbpBranchCash abp = null;

        try {
            sql = "SELECT fk_abp_csh " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CSH) +  " " +
                    "WHERE id_bpr = " + entityKey[0] + " AND id_bra = " + entityKey[1] + " AND id_csh = " + entityKey[2] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                abp = new DDbAbpBranchCash();
                abp.read(session, new int[] { resultSet.getInt(1) });
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }

        return abp;
    }

    public static DDbAbpBranchWarehouse readAbpBranchWarehouse(final DGuiSession session, final int[] entityKey) {
        String sql = "";
        ResultSet resultSet = null;
        DDbAbpBranchWarehouse abp = null;

        try {
            sql = "SELECT fk_abp_wah " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_WAH) +  " " +
                    "WHERE id_bpr = " + entityKey[0] + " AND id_bra = " + entityKey[1] + " AND id_wah = " + entityKey[2] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                abp = new DDbAbpBranchWarehouse();
                abp.read(session, new int[] { resultSet.getInt(1) });
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }

        return abp;
    }

    public static DDbAbpBizPartner readAbpBizPartner(final DGuiSession session, final int[] bizPartnerKey, final int bizPartnerClass) {
        String sql = "";
        ResultSet resultSet = null;
        DDbAbpBizPartner abp = null;

        try {
            sql = "SELECT bc.fk_abp_bpr_n " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) +  " AS bc " +
                    "WHERE bc.id_bpr = " + bizPartnerKey[0] + " AND bc.id_bpr_cl = " + bizPartnerClass + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                if (resultSet.getInt(1) != DLibConsts.UNDEFINED) {
                    abp = new DDbAbpBizPartner();
                    abp.read(session, new int[] { resultSet.getInt(1) });
                }
                else {
                    sql = "SELECT bt.fk_abp_bpr " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) +  " AS bc " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP) +  " AS bt ON " +
                            "bc.fk_bpr_cl = bt.id_bpr_cl AND bc.fk_bpr_tp = bt.id_bpr_tp AND " +
                            "bc.id_bpr = " + bizPartnerKey[0] + " AND bc.id_bpr_cl = " + bizPartnerClass + " ";
                    resultSet = session.getStatement().executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                    }
                    else {
                        abp = new DDbAbpBizPartner();
                        abp.read(session, new int[] { resultSet.getInt(1) });
                    }
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }

        return abp;
    }

    public static DDbAbpItem readAbpItem(final DGuiSession session, final int[] itemKey) {
        int lineId = DLibConsts.UNDEFINED;
        int genusId = DLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;
        DDbAbpItem abp = null;

        try {
            sql = "SELECT fk_abp_itm_n, fk_lin_n, fk_gen " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) +  " " +
                    "WHERE id_itm = " + itemKey[0] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                if (resultSet.getInt(1) != DLibConsts.UNDEFINED) {
                    abp = new DDbAbpItem();
                    abp.read(session, new int[] { resultSet.getInt(1) });
                }
                else {
                    lineId = resultSet.getInt(2);
                    genusId = resultSet.getInt(3);

                    if (lineId != DLibConsts.UNDEFINED) {
                        sql = "SELECT fk_abp_itm_n " +
                                "FROM " + DModConsts.TablesMap.get(DModConsts.IU_LIN) +  " " +
                                "WHERE id_lin = " + lineId + " ";
                        resultSet = session.getStatement().executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                        }
                        else {
                            if (resultSet.getInt(1) != DLibConsts.UNDEFINED) {
                                abp = new DDbAbpItem();
                                abp.read(session, new int[] { resultSet.getInt(1) });
                            }
                        }
                    }

                    if (abp == null) {
                        sql = "SELECT fk_abp_itm " +
                                "FROM " + DModConsts.TablesMap.get(DModConsts.IU_GEN) +  " " +
                                "WHERE id_gen = " + genusId + " ";
                        resultSet = session.getStatement().executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                        }
                        else {
                            if (resultSet.getInt(1) != DLibConsts.UNDEFINED) {
                                abp = new DDbAbpItem();
                                abp.read(session, new int[] { resultSet.getInt(1) });
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }

        return abp;
    }

    /**
     * Compute subject to collect (STC).
     */
    public static void computeStc(final DGuiSession session, final int[] bkkNumberKey) {
        String sql = "";

        try {
            sql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " SET b_avl = 1 " +
                    "WHERE fk_bkk_yer_n = " + bkkNumberKey[0] + " AND fk_bkk_num_n = " + bkkNumberKey[1] + " ";
            session.getStatement().execute(sql);
        }
        catch (SQLException e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DFinUtils.class.getName(), e);
        }
    }
    
    public static void checkNewYear(final DGuiSession session, final int newYear) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        DDbYear year = null;
        
        sql = "SELECT count(*) FROM " + DModConsts.TablesMap.get(DModConsts.F_YER) + " " +
                "WHERE id_yer = " + newYear + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) == 0) {
                year = new DDbYear();
                year.setPkYearId(newYear);
                year.createPeriods();
                year.save(session);
            }
        }
    }

    /**
     * @param typeKey System move type. Constants defined in DModSysConsts (FS_SYS_MOV_CL_...).
     */
    public static boolean isSysMoveTypeForDebit(final int[] typeKey) {
        boolean is = false;

        if (typeKey[0] == DModSysConsts.FS_SYS_MOV_CL_MI) {
            is = true;
        }
        else if (DLibUtils.belongsTo(typeKey, new int[][] {
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ADV,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ADV,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_ACC })) {
            is = true;
        }
        else if (DLibUtils.belongsTo(typeKey, new int[][] {
                DModSysConsts.FS_SYS_MOV_TP_YC_DBT,
                DModSysConsts.FS_SYS_MOV_TP_YO_DBT,
                DModSysConsts.FS_SYS_MOV_TP_JOU_DBT })) {
            is = true;
        }

        return is;
    }

    /**
     * @param typeKey System move type. Constants defined in DModSysConsts (FS_SYS_MOV_CL_...).
     */
    public static boolean isSysMoveTypeForCredit(final int[] typeKey) {
        boolean is = false;

        if (typeKey[0] == DModSysConsts.FS_SYS_MOV_CL_MO) {
            is = true;
        }
        else if (DLibUtils.belongsTo(typeKey, new int[][] {
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ADV,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ADV,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ACC,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_EXR,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_ADJ,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_TRA,
                DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_ACC })) {
            is = true;
        }
        else if (DLibUtils.belongsTo(typeKey, new int[][] {
                DModSysConsts.FS_SYS_MOV_TP_YC_CDT,
                DModSysConsts.FS_SYS_MOV_TP_YO_CDT,
                DModSysConsts.FS_SYS_MOV_TP_JOU_CDT })) {
            is = true;
        }

        return is;
    }

    /**
     * @param bizPartnerClass Constants defined in DModSysConsts (BS_BPR_CL_...).
     * @return Constant defined in DModSysConsts (FS_SYS_ACC_TP_BPR_...).
     */
    public static int getSysAccountTypeForBizPartnerClass(final int bizPartnerClass) {
        int type = DLibConsts.UNDEFINED;

        switch (bizPartnerClass) {
            case DModSysConsts.BS_BPR_CL_VEN:
                type = DModSysConsts.FS_SYS_ACC_TP_BPR_VEN;
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
                type = DModSysConsts.FS_SYS_ACC_TP_BPR_CUS;
                break;
            case DModSysConsts.BS_BPR_CL_CDR:
                type = DModSysConsts.FS_SYS_ACC_TP_BPR_CDR;
                break;
            case DModSysConsts.BS_BPR_CL_DBR:
                type = DModSysConsts.FS_SYS_ACC_TP_BPR_DBR;
                break;
            default:
        }

        return type;
    }

    /**
     * @param bizPartnerClass Constants defined in DModSysConsts (BS_BPR_CL_...).
     * @return Constant defined in DModSysConsts (FS_SYS_MOV_TP_MI_...).
     */
    public static int[] getSysMoveTypeForCashPaymentInForBizPartnerClass(final int bizPartnerClass) {
        int[] type = null;

        switch (bizPartnerClass) {
            case DModSysConsts.BS_BPR_CL_VEN:
                type = DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY;
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
                type = DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY;
                break;
            case DModSysConsts.BS_BPR_CL_CDR:
                type = DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY;
                break;
            case DModSysConsts.BS_BPR_CL_DBR:
                type = DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY;
                break;
            default:
        }

        return type;
    }

    public static int getBizPartnerClassForSysMoveType(final int[] typeKey) {
        int bizPartnerClass = DLibConsts.UNDEFINED;

        if (isSysMoveTypeForBranchCashBizPartnerClassVen(typeKey) || isSysMoveTypeForBizPartnerClassVen(typeKey)) {
            bizPartnerClass = DModSysConsts.BS_BPR_CL_VEN;
        }
        else if (isSysMoveTypeForBranchCashBizPartnerClassCus(typeKey) || isSysMoveTypeForBizPartnerClassCus(typeKey)) {
            bizPartnerClass = DModSysConsts.BS_BPR_CL_CUS;
        }
        else if (isSysMoveTypeForBranchCashBizPartnerClassCdr(typeKey)) {
            bizPartnerClass = DModSysConsts.BS_BPR_CL_CDR;
        }
        else if (isSysMoveTypeForBranchCashBizPartnerClassDbr(typeKey)) {
            bizPartnerClass = DModSysConsts.BS_BPR_CL_DBR;
        }

        return bizPartnerClass;
    }

    public static boolean isSysMoveTypeForBranchCashMove(final int[] typeKey) {
        return isSysMoveTypeForBranchCashMoveIn(typeKey) || isSysMoveTypeForBranchCashMoveOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashMoveIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_CUS_ADV, DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_VEN_ADV, DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MI_EQY, DModSysConsts.FS_SYS_MOV_TP_MI_EXR, DModSysConsts.FS_SYS_MOV_TP_MI_ADJ, DModSysConsts.FS_SYS_MOV_TP_MI_TRA
        });
    }

    public static boolean isSysMoveTypeForBranchCashMoveOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_CUS_ADV, DModSysConsts.FS_SYS_MOV_TP_MO_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_VEN_ADV, DModSysConsts.FS_SYS_MOV_TP_MO_CDR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MO_EQY, DModSysConsts.FS_SYS_MOV_TP_MO_EXR, DModSysConsts.FS_SYS_MOV_TP_MO_ADJ, DModSysConsts.FS_SYS_MOV_TP_MO_TRA
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerMove(final int[] typeKey) {
        return isSysMoveTypeForBranchCashBizPartnerMoveIn(typeKey) || isSysMoveTypeForBranchCashBizPartnerMoveOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerMoveIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_CUS_ADV, DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_VEN_ADV, DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerMoveOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_CUS_ADV, DModSysConsts.FS_SYS_MOV_TP_MO_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_VEN_ADV, DModSysConsts.FS_SYS_MOV_TP_MO_CDR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerClassVen(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_VEN_ADV,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_VEN_ADV
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerClassCus(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_CUS_ADV,
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_CUS_ADV
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerClassCdr(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_CDR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerClassDbr(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_DBR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashBizPartnerDiverse(final int[] typeKey) {
        return isSysMoveTypeForBranchCashBizPartnerClassCdr(typeKey) || isSysMoveTypeForBranchCashBizPartnerClassDbr(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashPayment(final int[] typeKey) {
        return isSysMoveTypeForBranchCashPaymentIn(typeKey) || isSysMoveTypeForBranchCashPaymentOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashPaymentIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MI_CDR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashPaymentOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_DBR_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY, DModSysConsts.FS_SYS_MOV_TP_MO_CDR_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashPaymentAdvance(final int[] typeKey) {
        return isSysMoveTypeForBranchCashPaymentAdvanceIn(typeKey) || isSysMoveTypeForBranchCashPaymentAdvanceOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashPaymentAdvanceIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_ADV,
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_ADV
        });
    }

    public static boolean isSysMoveTypeForBranchCashPaymentAdvanceOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_ADV,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_ADV
        });
    }

    public static boolean isSysMoveTypeForBranchCashPaymentDps(final int[] typeKey) {
        return isSysMoveTypeForBranchCashPaymentDpsIn(typeKey) || isSysMoveTypeForBranchCashPaymentDpsOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashPaymentDpsIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MI_VEN_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashPaymentDpsOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_CUS_PAY,
            DModSysConsts.FS_SYS_MOV_TP_MO_VEN_PAY
        });
    }

    public static boolean isSysMoveTypeForBranchCashOnlyCash(final int[] typeKey) {
        return isSysMoveTypeForBranchCashOnlyCashIn(typeKey) || isSysMoveTypeForBranchCashOnlyCashOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashOnlyCashIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_EXR, DModSysConsts.FS_SYS_MOV_TP_MI_ADJ, DModSysConsts.FS_SYS_MOV_TP_MI_TRA
        });
    }

    public static boolean isSysMoveTypeForBranchCashOnlyCashOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MO_EXR, DModSysConsts.FS_SYS_MOV_TP_MO_ADJ, DModSysConsts.FS_SYS_MOV_TP_MO_TRA
        });
    }

    public static boolean isSysMoveTypeForBranchCashTransfer(final int[] typeKey) {
        return isSysMoveTypeForBranchCashTransferIn(typeKey) || isSysMoveTypeForBranchCashTransferOut(typeKey);
    }

    public static boolean isSysMoveTypeForBranchCashTransferIn(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] { DModSysConsts.FS_SYS_MOV_TP_MI_TRA });
    }

    public static boolean isSysMoveTypeForBranchCashTransferOut(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] { DModSysConsts.FS_SYS_MOV_TP_MO_TRA });
    }

    public static boolean isSysMoveTypeForExchangeRate(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_MI_EXR, DModSysConsts.FS_SYS_MOV_TP_MO_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_EXR, DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_EXR, DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_EXR, DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_EXR, DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_EXR
        });
    }

    public static boolean isSysMoveTypeForBizPartnerDoc(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerDocVen(typeKey) || isSysMoveTypeForBizPartnerDocCus(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerDocVen(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerDocVenDec(typeKey) || isSysMoveTypeForBizPartnerDocVenInc(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerDocCus(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerDocCusDec(typeKey) || isSysMoveTypeForBizPartnerDocCusInc(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerDocVenDec(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ADV,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerDocVenInc(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ADV,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerDocCusDec(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ADV,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerDocCusInc(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ADV,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerAdv(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerAdvVen(typeKey) || isSysMoveTypeForBizPartnerAdvCus(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerAdvVen(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerAdvVenDec(typeKey) || isSysMoveTypeForBizPartnerAdvVenInc(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerAdvCus(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerAdvCusDec(typeKey) || isSysMoveTypeForBizPartnerAdvCusInc(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerAdvVenDec(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_DEC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerAdvVenInc(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_VEN_ADV_INC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerAdvCusDec(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_DEC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerAdvCusInc(final int[] typeKey) {
        return DLibUtils.belongsTo(typeKey, new int[][] {
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_EXR,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_ADJ,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_TRA,
            DModSysConsts.FS_SYS_MOV_TP_CUS_ADV_INC_ACC
        });
    }

    public static boolean isSysMoveTypeForBizPartnerClassVen(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerDocVen(typeKey) || isSysMoveTypeForBizPartnerAdvVen(typeKey);
    }

    public static boolean isSysMoveTypeForBizPartnerClassCus(final int[] typeKey) {
        return isSysMoveTypeForBizPartnerDocCus(typeKey) || isSysMoveTypeForBizPartnerAdvCus(typeKey);
    }
}
