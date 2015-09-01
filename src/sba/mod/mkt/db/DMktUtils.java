/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sba.gui.util.DUtilConsts;
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
public abstract class DMktUtils {

    /**
     * @param valueTypeId Constants defined in DModSysConsts.MS_CMM_VAL_TP...
     */
    public static DDbCommissionCalc computeCommissionCalcAgent(final DGuiSession session, final Date dateStart, final Date dateEnd, final int valueTypeId) throws SQLException, Exception {
        return null;
    }

    /**
     * @param valueTypeId Constants defined in DModSysConsts.MS_CMM_VAL_TP...
     */
    public static DDbCommissionCalc computeCommissionCalcUser(final DGuiSession session, final Date dateStart, final Date dateEnd, final int valueTypeId) throws SQLException, Exception {
        int id = DLibConsts.UNDEFINED;
        double cmm = 0;
        String sql = "";
        String value = "";
        Statement statement = null;
        Statement statementAux = null;
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        DDbCommissionCalc calc = null;
        
        switch (valueTypeId) {
            case DModSysConsts.MS_CMM_VAL_TP_SBT:
                value = "sbt_r";
                break;
            case DModSysConsts.MS_CMM_VAL_TP_TOT:
                value = "tot_r";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        statement = session.getStatement().getConnection().createStatement();
        statementAux = session.getStatement().getConnection().createStatement();

        // Read former commissions calculation or create a new one:

        sql = "SELECT id_cmm FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL) + " " +
                "WHERE " +
                "dt_sta = '" + DLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                "dt_end = '" + DLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                "fk_cmm_cal_tp = " + DModSysConsts.MS_CMM_CAL_TP_USR + " AND fk_cmm_val_tp = " + valueTypeId + " ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        if (id != DLibConsts.UNDEFINED) {
            calc = (DDbCommissionCalc) session.readRegistry(DModConsts.M_CMM_CAL, new int[] { id });

            if (calc.isDeleted()) {
                throw new Exception(DDbConsts.MSG_REG_ +
                        DLibUtils.DateFormatDate.format(dateStart) + " - " + DLibUtils.DateFormatDate.format(dateEnd) +
                        DDbConsts.MSG_REG_IS_DELETED);
            }
            else if (calc.isAudited()) {
                throw new Exception(DDbConsts.MSG_REG_ +
                        DLibUtils.DateFormatDate.format(dateStart) + " - " + DLibUtils.DateFormatDate.format(dateEnd) +
                        DDbConsts.MSG_REG_IS_AUDITED);
            }

            calc.getChildRows().clear();
        }
        else {
            calc = new DDbCommissionCalc();

            calc.setDateStart(dateStart);
            calc.setDateEnd(dateEnd);
            calc.setFkCommissionCalcTypeId(DModSysConsts.MS_CMM_CAL_TP_USR);
            calc.setFkCommissionValueTypeId(valueTypeId);
            calc.setAudited(false);
            calc.setFkUserAuditedId(DUtilConsts.USR_NA_ID);
        }

        // Compute commissions calculation:

        sql = "SELECT t.fk_usr_ins AS f_usr, u.fk_usr_ctm_tp AS f_usr_tp, t.fk_row_itm AS f_itm, t.fk_row_unt AS f_unt, " +
                "u.name, i.b_fre_cmm, i.name, i.code, iu.code, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_DOC[1] + " THEN t.f_qty ELSE 0 END) AS f_qty, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_INC[1] + " THEN t.f_qty ELSE 0 END) AS f_qty_inc, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC[1] + " THEN t.f_qty ELSE 0 END) AS f_qty_dec, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_DOC[1] + " THEN t.f_trn ELSE 0 END) AS f_trn, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_INC[1] + " THEN t.f_trn ELSE 0 END) AS f_trn_inc, " +
                "SUM(CASE WHEN t.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC[1] + " THEN t.f_trn ELSE 0 END) AS f_trn_dec " +
                " " +
                "FROM ( " +
                " " +
                "SELECT d.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt, " +
                "SUM(dr.qty) AS f_qty, " +
                "SUM(dr." + value + ") AS f_trn " +
                "FROM t_dps AS d " +
                "INNER JOIN t_dps_row AS dr ON d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND " +
                "d.dt BETWEEN '" + DLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + DLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                "d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND " +
                "d.fk_dps_ct = " + DModSysConsts.TS_DPS_CL_SAL_DOC[0] + " AND d.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_DOC[1] + " " +
                "GROUP BY d.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt " +
                " " +
                "UNION " +
                " " +
                "SELECT x.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt, " +
                "SUM(dr.qty) AS f_qty, " +
                "SUM(dr." + value + ") AS f_trn " +
                "FROM t_dps AS d " +
                "INNER JOIN t_dps_row AS dr ON d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND " +
                "d.dt BETWEEN '" + DLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + DLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                "d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND " +
                "d.fk_dps_ct = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_INC[0] + " AND d.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_INC[1] + " " +
                "INNER JOIN t_dps AS x ON dr.fk_src_dps_n = x.id_dps " +
                "GROUP BY x.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt " +
                " " +
                "UNION " +
                " " +
                "SELECT x.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt, " +
                "SUM(dr.qty) AS f_qty, " +
                "SUM(dr." + value + ") AS f_trn " +
                "FROM t_dps AS d " +
                "INNER JOIN t_dps_row AS dr ON d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND " +
                "d.dt BETWEEN '" + DLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + DLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND " +
                "d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " AND " +
                "d.fk_dps_ct = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC[0] + " AND d.fk_dps_cl = " + DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC[1] + " " +
                "INNER JOIN t_dps AS x ON dr.fk_src_dps_n = x.id_dps " +
                "GROUP BY x.fk_usr_ins, d.fk_dps_cl, dr.fk_row_itm, dr.fk_row_unt " +
                " " +
                "ORDER BY fk_usr_ins, fk_dps_cl, fk_row_itm, fk_row_unt " +
                " " +
                ") AS t " +
                " " +
                "INNER JOIN cu_usr AS u ON t.fk_usr_ins = u.id_usr " +
                "INNER JOIN iu_itm AS i ON t.fk_row_itm = i.id_itm " +
                "INNER JOIN iu_unt AS iu ON t.fk_row_unt = iu.id_unt " +
                " " +
                "GROUP BY t.fk_usr_ins, u.fk_usr_ctm_tp, t.fk_row_itm, t.fk_row_unt, " +
                "u.name, i.b_fre_cmm, i.name, i.code, iu.code " +
                " " +
                "ORDER BY u.name, t.fk_usr_ins, i.b_fre_cmm, i.name, i.code, t.fk_row_itm ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            sql = "SELECT i.id_itm, i.b_fre_cmm, " +
                    "c.id_lnk_usr_tp, c.id_ref_usr, c.id_lnk_itm_tp, c.id_ref_itm, " +
                    "c.cmm_per, c.cmm_unt, c.fk_cmm_tp " +
                    "FROM iu_itm AS i " +
                    "INNER JOIN iu_gen AS ig ON i.fk_gen = ig.id_gen AND i.id_itm = " + resultSet.getInt("f_itm") + " " +
                    "INNER JOIN is_itm_tp AS it ON ig.fk_itm_ct = it.id_itm_ct AND ig.fk_itm_cl = it.id_itm_cl AND ig.fk_itm_tp = it.id_itm_tp " +
                    "INNER JOIN is_itm_cl AS ic ON ig.fk_itm_ct = ic.id_itm_ct AND ig.fk_itm_cl = ic.id_itm_cl " +
                    "LEFT OUTER JOIN m_cmm_cfg_usr AS c ON c.b_del = 0 AND ( " +
                    "(c.id_lnk_usr_tp = " + DModSysConsts.MS_LNK_USR_TP_USR + " AND c.id_ref_usr = " + resultSet.getInt("f_usr") + ") OR " +
                    "(c.id_lnk_usr_tp = " + DModSysConsts.MS_LNK_USR_TP_USR_TP + " AND c.id_ref_usr = " + resultSet.getInt("f_usr_tp") + ")) AND ( " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM + " AND c.id_ref_itm = i.id_itm) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_MFR + " AND c.id_ref_itm = i.fk_mfr) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_BRD + " AND c.id_ref_itm = i.fk_brd) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_LIN + " AND c.id_ref_itm = i.fk_lin_n) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_GEN + " AND c.id_ref_itm = ig.id_gen) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_FAM + " AND c.id_ref_itm = ig.fk_fam) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_TP + " AND c.id_ref_itm = it.idx) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CL + " AND c.id_ref_itm = ic.idx) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ITM_CT + " AND c.id_ref_itm = ig.fk_itm_ct) OR " +
                    "(c.id_lnk_itm_tp = " + DModSysConsts.IS_LNK_ITM_TP_ALL + " AND c.id_ref_itm = 0)) " +
                    "ORDER BY c.id_lnk_usr_tp, c.id_lnk_itm_tp " +
                    "LIMIT 1 ";
            resultSetAux = statementAux.executeQuery(sql);
            if (!resultSetAux.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                DDbCommissionCalcRow calcRow = new DDbCommissionCalcRow();

                calcRow.setPkReferenceId(resultSet.getInt("f_usr"));
                calcRow.setQuantity(resultSet.getDouble("f_qty"));
                calcRow.setQuantityInc(resultSet.getDouble("f_qty_inc"));
                calcRow.setQuantityDec(resultSet.getDouble("f_qty_dec"));
                calcRow.setQuantityNet_r(DLibUtils.round(calcRow.getQuantity() + calcRow.getQuantityInc() - calcRow.getQuantityDec(), DLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits()));
                calcRow.setValue(resultSet.getDouble("f_trn"));
                calcRow.setValueInc(resultSet.getDouble("f_trn_inc"));
                calcRow.setValueDec(resultSet.getDouble("f_trn_dec"));
                calcRow.setValueNet_r(DLibUtils.round(calcRow.getValue() + calcRow.getValueInc() - calcRow.getValueDec(), DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
                calcRow.setCommissionPercentage(resultSetAux.getDouble("c.cmm_per"));
                calcRow.setCommissionUnitary(resultSetAux.getDouble("c.cmm_unt"));

                calcRow.setFreeOfCommission(resultSetAux.getBoolean("i.b_fre_cmm"));
                calcRow.setFkItemId(resultSetAux.getInt("i.id_itm"));

                id = resultSetAux.getInt("c.id_lnk_usr_tp");
                calcRow.setFkLinkOwnerTypeId(id != DLibConsts.UNDEFINED ? id : DModSysConsts.MS_LNK_USR_TP_USR);
                calcRow.setFkReferenceOwnerId(resultSetAux.getInt("c.id_ref_usr"));

                id = resultSetAux.getInt("c.id_lnk_itm_tp");
                calcRow.setFkLinkItemTypeId(id != DLibConsts.UNDEFINED ? id : DModSysConsts.IS_LNK_ITM_TP_ITM);
                calcRow.setFkReferenceItemId(resultSetAux.getInt("c.id_ref_itm"));
                
                if (resultSetAux.getInt("c.fk_cmm_tp") == DModSysConsts.MS_CMM_TP_UNT) {
                    calcRow.setFkCommissionTypeId(DModSysConsts.MS_CMM_TP_UNT);
                    cmm = calcRow.isFreeOfCommission() ? 0 : (calcRow.getCommissionUnitary() * calcRow.getQuantityNet_r());
                    calcRow.setCommission_r(DLibUtils.round(cmm, DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
                }
                else {
                    calcRow.setFkCommissionTypeId(DModSysConsts.MS_CMM_TP_PER);
                    cmm = calcRow.isFreeOfCommission() ? 0 : (calcRow.getCommissionPercentage() * calcRow.getValueNet_r());
                    calcRow.setCommission_r(DLibUtils.round(cmm, DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
                }
                
                calc.getChildRows().add(calcRow);
            }
        }

        calc.save(session);

        return calc;
    }
}
