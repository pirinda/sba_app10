/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprEmail;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbUser;
import sba.mod.fin.db.DDbTaxGroupConfigRow;
import sba.mod.fin.db.DFinUtils;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbItemGenus;
import sba.mod.itm.db.DDbItemLine;
import sba.mod.mkt.db.DDbCustomerItemPriceType;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnUtils {

    /**
     * @param subtype Constants defined in DModSysConts (CS_MOD_...).
     */
    public static String getModuleAcronym(int module) {
        String name = "";

        switch (module) {
            case DModSysConsts.CS_MOD_PUR:
                name = "CPA";
                break;
            case DModSysConsts.CS_MOD_SAL:
                name = "VTA";
                break;
            default:
        }

        return name;
    }

    /**
     * @param dpsCategory Constants defined in DModSysConts (TS_DPS_CT_...).
     */
    public static String getDpsCategoryName(int dpsCategory) {
        String name = "";

        switch (dpsCategory) {
            case DModSysConsts.TS_DPS_CT_PUR:
                name = "Compras";
                break;
            case DModSysConsts.TS_DPS_CT_SAL:
                name = "Ventas";
                break;
            default:
        }

        return name;
    }

    /**
     * @param xType Constants defined in DModConts (TX_DPS_...).
     */
    public static String getDpsXTypeNameSng(int xType) {
        String name = "";

        switch (xType) {
            case DModConsts.TX_DPS_ORD:
                name += "Pedido";
                break;
            case DModConsts.TX_DPS_DOC_INV:
                name += "Factura";
                break;
            case DModConsts.TX_DPS_DOC_NOT:
                name += "Nota venta";
                break;
            case DModConsts.TX_DPS_DOC_TIC:
                name += "Ticket";
                break;
            case DModConsts.TX_DPS_ADJ_INC:
                name += "Nota débito";
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
                name += "Nota crédito";
                break;
            default:
        }

        return name;
    }

    /**
     * @param xType Constants defined in DModConts (TX_DPS_...).
     */
    public static String getDpsXTypeNamePlr(int xType) {
        String name = "";

        switch (xType) {
            case DModConsts.TX_DPS_ORD:
                name += "Pedidos";
                break;
            case DModConsts.TX_DPS_DOC_INV:
                name += "Facturas";
                break;
            case DModConsts.TX_DPS_DOC_NOT:
                name += "Notas venta";
                break;
            case DModConsts.TX_DPS_DOC_TIC:
                name += "Tickets";
                break;
            case DModConsts.TX_DPS_ADJ_INC:
                name += "Notas débito";
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
                name += "Notas crédito";
                break;
            default:
        }

        return name;
    }

    /**
     * @param myXType Constants defined in DModConts (TX_MY_DPS_...).
     */
    public static String getMyDpsXTypeNameSng(int myXType) {
        String name = "";

        switch (myXType) {
            case DModConsts.TX_MY_DPS_ORD:
                name += "Mi pedido";
                break;
            case DModConsts.TX_MY_DPS_DOC:
                name += "Mi documento";
                break;
            case DModConsts.TX_MY_DPS_ADJ_INC:
                name += "Mi nota débito";
                break;
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                name += "Mi nota crédito";
                break;
            default:
        }

        return name;
    }

    /**
     * @param myXType Constants defined in DModConts (TX_MY_DPS_...).
     */
    public static String getMyDpsXTypeNamePlr(int myXType) {
        String name = "";

        switch (myXType) {
            case DModConsts.TX_MY_DPS_ORD:
                name += "Mis pedidos";
                break;
            case DModConsts.TX_MY_DPS_DOC:
                name += "Mis documentos";
                break;
            case DModConsts.TX_MY_DPS_ADJ_INC:
                name += "Mis notas débito";
                break;
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                name += "Mis notas crédito";
                break;
            default:
        }

        return name;
    }

    /**
     * @param adjClassKey Constants defined in DModSysConsts (TS_ADJ_CL_...).
     */
    public static String getAdjustmentClassName(int[] adjClassKey) {
        String name = "";

        if (DLibUtils.compareKeys(adjClassKey, DModSysConsts.TS_ADJ_CL_INC_INC)) {
            name = "Aumento";
        }
        else if (DLibUtils.compareKeys(adjClassKey, DModSysConsts.TS_ADJ_CL_INC_ADD)) {
            name = "Adición";
        }
        else if (DLibUtils.compareKeys(adjClassKey, DModSysConsts.TS_ADJ_CL_DEC_DIS)) {
            name = "Descuento";
        }
        else if (DLibUtils.compareKeys(adjClassKey, DModSysConsts.TS_ADJ_CL_DEC_RET)) {
            name = "Devolución";
        }

        return name;
    }

    /**
     * @param iogCategory Constants defined in DModSysConts (TS_IOG_CT_...).
     */
    public static String getIogCategoryName(int iogCategory) {
        String name = "";

        switch (iogCategory) {
            case DModSysConsts.TS_IOG_CT_IN:
                name = "Entrada de bienes";
                break;
            case DModSysConsts.TS_IOG_CT_OUT:
                name = "Salida de bienes";
                break;
            default:
        }

        return name;
    }

    /**
     * @param foundBy Constants defined in DTrnConsts (FOUND_BY_...).
     */
    public static String getFoundByName(int foundBy) {
        String name = "";

        switch (foundBy) {
            case DTrnConsts.FOUND_BY_CODE:
                name = "Código";
                break;
            case DTrnConsts.FOUND_BY_BARCODE:
                name = "Código de barras";
                break;
            case DTrnConsts.FOUND_BY_SNR:
                name = "Número de serie";
                break;
            default:
        }

        return name;
    }

    /**
     * @param session Current GUI session.
     * @param bizPartnerKey Business partner key.
     */
    public static DDbCustomerItemPriceType getItemPriceType(final DGuiSession session, final int[] bizPartnerKey) {
        int[] key = null;
        String sql = "";
        ResultSet resultSet = null;
        DDbCustomerItemPriceType itemPriceType = null;

        try {
            // Check if item price type is defined on customer (by customer):

            sql = "SELECT ipt.id_lnk_cus_tp, ipt.id_ref_cus, ipt.fk_itm_prc_tp " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.M_CUS_ITM_PRC_TP) + " AS ipt " +
                    "WHERE ipt.b_del = 0 AND ipt.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS + " AND " +
                    "ipt.id_ref_cus = " + bizPartnerKey[0] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                key = new int[] { resultSet.getInt(1), resultSet.getInt(2) } ;
            }
            else {
                // Check if item price type is defined on customer configuration (by customer type):

                sql = "SELECT ipt.id_lnk_cus_tp, ipt.id_ref_cus, ipt.fk_itm_prc_tp " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.M_CUS_ITM_PRC_TP) + " AS ipt " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_CUS_CFG) + " AS cc ON " +
                        "ipt.b_del = 0 AND ipt.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS_TP + " AND " +
                        "ipt.id_ref_cus = cc.fk_cus_tp AND cc.b_del = 0 AND cc.id_cus = " + bizPartnerKey[0] + " ";
                resultSet = session.getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    key = new int[] { resultSet.getInt(1), resultSet.getInt(2) } ;
                }
                else {
                    // Check if item price type is defined on business partner type (by customer type):

                    sql = "SELECT ipt.id_lnk_cus_tp, ipt.id_ref_cus, ipt.fk_itm_prc_tp " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP) + " AS bt ON " +
                            "bc.b_del = 0 AND bc.id_bpr = " + bizPartnerKey[0] + " AND bc.id_bpr_cl = " + DModSysConsts.BS_BPR_CL_CUS + " AND " +
                            "bc.fk_bpr_cl = bt.id_bpr_cl AND bc.fk_bpr_tp = bt.id_bpr_tp " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_CUS_ITM_PRC_TP) + " AS ipt ON " +
                            "ipt.b_del = 0 AND ipt.id_lnk_cus_tp = " + DModSysConsts.MS_LNK_CUS_TP_CUS_TP + " AND " +
                            "ipt.id_ref_cus = bt.fk_cus_tp_n ";
                    resultSet = session.getStatement().executeQuery(sql);
                    if (resultSet.next()) {
                        key = new int[] { resultSet.getInt(1), resultSet.getInt(2) } ;
                    }
                }
            }

            if (key == null) {
                itemPriceType = new DDbCustomerItemPriceType();
            }
            else {
                itemPriceType = (DDbCustomerItemPriceType) session.readRegistry(DModConsts.M_CUS_ITM_PRC_TP, key);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return itemPriceType;
    }

    public static double getItemPrice(final DDbItem item, final int itemPriceType, final double discount) {
        double price = 0;

        switch (itemPriceType) {
            case DModSysConsts.MS_ITM_PRC_TP_SRP:
                price = item.getPriceSrp();
                break;
            case DModSysConsts.MS_ITM_PRC_TP_RET:
                price = item.getPrice1();
                break;
            case DModSysConsts.MS_ITM_PRC_TP_HAL:
                price = item.getPrice2();
                break;
            case DModSysConsts.MS_ITM_PRC_TP_WHO:
                price = item.getPrice3();
                break;
            case DModSysConsts.MS_ITM_PRC_TP_ESP:
                price = item.getPrice4();
                break;
            case DModSysConsts.MS_ITM_PRC_TP_COS:
                price = item.getPrice5();
                break;
            default:
        }

        if (!item.isFreeOfDiscount() && discount > 0d) {
            price *= (1d - discount);
        }

        return price;
    }

    /**
     * @param itemPriceType Constants defined in DModSysConsts.MS_ITM_PRC_TP_...
     * @param item Item registry;
     */
    public static double getItemSalesPrice(final DGuiSession session, final DDbItem item, final int[] bizPartnerKey, final int paymentType, final int itemPriceType, final double discount) {
        int id = DLibConsts.UNDEFINED;
        int linkItem = DLibConsts.UNDEFINED;
        int linkCustomer = DLibConsts.UNDEFINED;
        int customerType = DLibConsts.UNDEFINED;
        int promotionalItemPriceType = DLibConsts.UNDEFINED;
        int attempt = 0;
        int[] customerLinks = new int[2];
        double price = 0;
        double promotionalDiscount = 0;
        boolean found = false;
        String sql = "";
        ResultSet resultSet = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();

        if (!(configCompany.isCustomerFixedPrices() || configCompany.isCustomerSpecialPrices() || configCompany.isCustomerPromotionalPackages())) {
            price = getItemPrice(item, itemPriceType, discount);
        }
        else {
            try {
                // Get marketing customer type:

                sql = "SELECT bt.fk_cus_tp_n "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc "
                        + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_TP ) + " AS bt ON "
                        + "bc.fk_bpr_cl = bt.id_bpr_cl AND bc.fk_bpr_tp = bt.id_bpr_tp AND "
                        + "bc.id_bpr = " + bizPartnerKey[0] + " AND bc.id_bpr_cl = " + DModSysConsts.BS_BPR_CL_CUS + " ";
                resultSet = session.getStatement().executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    customerType = resultSet.getInt(1);

                    customerLinks[0] = bizPartnerKey[0];
                    customerLinks[1] = customerType;

                    if (configCompany.isCustomerFixedPrices()) {
                        // 1. Check if there is a fixed price:

                        SEARCH1:
                        for (linkCustomer = DModSysConsts.MS_LNK_CUS_TP_CUS; linkCustomer <= DModSysConsts.MS_LNK_CUS_TP_CUS_TP; linkCustomer++) {
                            sql = "SELECT prc "
                                    + "FROM " + DModConsts.TablesMap.get(DModConsts.M_CUS_FIX) + " "
                                    + "WHERE id_lnk_cus_tp = " + linkCustomer + " AND id_ref_cus = " + customerLinks[linkCustomer - 1] + " AND "
                                    + "id_itm = " + item.getPkItemId() + " ";
                            resultSet = session.getStatement().executeQuery(sql);
                            if (resultSet.next()) {
                                price = resultSet.getDouble(1);
                                found = true;
                                break SEARCH1;
                            }
                        }
                    }

                    if (!found && configCompany.isCustomerSpecialPrices()) {
                        // 2. Check if there is a special price:

                        SEARCH2:
                        for (linkCustomer = DModSysConsts.MS_LNK_CUS_TP_CUS; linkCustomer <= DModSysConsts.MS_LNK_CUS_TP_CUS_TP; linkCustomer++) {
                            sql = "SELECT sspe.id_spe "
                                    + "FROM " + DModConsts.TablesMap.get(DModConsts.M_SPE_CUS) + " AS scus "
                                    + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_SPE) + " AS sspe ON "
                                    + "scus.fk_spe = sspe.id_spe AND sspe.b_del = 0 AND "
                                    + "scus.id_lnk_cus_tp = " + linkCustomer + " AND scus.id_ref_cus = " + customerLinks[linkCustomer - 1] + " ";
                            resultSet = session.getStatement().executeQuery(sql);
                            if (resultSet.next()) {
                                id = resultSet.getInt(1);

                                for (attempt = 1; attempt <= 2; attempt++) {
                                    /*
                                     * Attempt 1: check if there is a matching special price according to provided payment type.
                                     * Attempt 2: check if there is a matching all-purpouse special price.
                                     */

                                    sql = "SELECT prc "
                                            + "FROM " + DModConsts.TablesMap.get(DModConsts.M_SPE_PRC) + " "
                                            + "WHERE id_spe = " + id + " AND id_pay_tp = " + (attempt == 1 ? paymentType : DModSysConsts.FS_PAY_TP_NA) + " AND "
                                            + "id_itm = " + item.getPkItemId() + " ";
                                    resultSet = session.getStatement().executeQuery(sql);
                                    if (resultSet.next()) {
                                        price = resultSet.getDouble(1);
                                        found = true;
                                        break SEARCH2;
                                    }
                                }
                            }
                        }
                    }

                    if (!found && configCompany.isCustomerPromotionalPackages()) {
                        // 3. Check if there is a promotional package:

                        SEARCH3:
                        for (linkCustomer = DModSysConsts.MS_LNK_CUS_TP_CUS; linkCustomer <= DModSysConsts.MS_LNK_CUS_TP_CUS_TP; linkCustomer++) {
                            sql = "SELECT pprm.id_prm, pprm.fk_itm_prc_tp "
                                    + "FROM " + DModConsts.TablesMap.get(DModConsts.M_PRM_CUS) + " AS pcus "
                                    + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_PRM) + " AS pprm ON "
                                    + "pcus.fk_prm = pprm.id_prm AND pprm.b_del = 0 AND "
                                    + "pcus.id_lnk_cus_tp = " + linkCustomer + " AND pcus.id_ref_cus = " + customerLinks[linkCustomer - 1] + " ";
                            resultSet = session.getStatement().executeQuery(sql);
                            if (resultSet.next()) {
                                id = resultSet.getInt(1);
                                promotionalItemPriceType = resultSet.getInt(2);

                                for (linkItem = DModSysConsts.IS_LNK_ITM_TP_ITM; linkItem <= DModSysConsts.IS_LNK_ITM_TP_ALL; linkItem++) {
                                    for (attempt = 1; attempt <= 2; attempt++) {
                                        /*
                                         * Attempt 1: check if there is a matching promotional package according to provided payment type.
                                         * Attempt 2: check if there is a matching all-purpouse promotional package.
                                         */

                                        sql = "SELECT dsc_per "
                                                + "FROM " + DModConsts.TablesMap.get(DModConsts.M_PRM_PRM) + " "
                                                + "WHERE id_prm = " + id + " AND id_pay_tp = " + (attempt == 1 ? paymentType : DModSysConsts.FS_PAY_TP_NA) + " AND "
                                                + "id_lnk_itm_tp = " + linkItem + " AND id_ref_itm = " + item.getItemLinkId(linkItem) + " ";
                                        resultSet = session.getStatement().executeQuery(sql);
                                        if (resultSet.next()) {
                                            promotionalDiscount = resultSet.getDouble(1);
                                            price = getItemPrice(item, promotionalItemPriceType, promotionalDiscount);
                                            found = true;
                                            break SEARCH3;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!found) {
                        // 4. If no price was found, get item price:

                        price = getItemPrice(item, itemPriceType, discount);
                    }
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnUtils.class.getName(), e);
            }
        }

        return price;
    }

    /**
     * @param session Current GUI session.
     * @param dpsCategory Constants defined in DModSysConsts.TS_DPS_CT_...
     * @param currencyKey Currency key.
     * @param itemKey Item key.
     * @return Double array containing 0: unitary price, 1: unitary discount and 2: row discount.
     */
    public static double[] getItemLastPrices(final DGuiSession session, final int dpsCategory, final int[] currencyKey, final int[] itemKey) {
        return getItemLastPrices(session, dpsCategory, currencyKey, itemKey, null);
    }

    /**
     * @param session Current GUI session.
     * @param dpsCategory Constants defined in DModSysConsts.TS_DPS_CT_...
     * @param bizPartnerKey_n Business partner key.
     * @param currencyKey Currency key.
     * @param itemKey Item key.
     * @return Double array containing 0: unitary price, 1: unitary discount and 2: row discount.
     */
    public static double[] getItemLastPrices(final DGuiSession session, final int dpsCategory, final int[] currencyKey, final int[] itemKey, final int[] bizPartnerKey_n) {
        double[] prices = new double[3];
        String sql = "";
        ResultSet resultSet = null;

        try {
            // Retreive last item price by business partner key and item key:

            if (bizPartnerKey_n != null) {
                sql = "SELECT dr.prc_unt_cy, dr.dsc_unt_cy, dr.dsc_row_cy, d.dt, d.ts_usr_upd " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 " +
                        "WHERE d.fk_dps_ct = " + dpsCategory + " AND d.fk_bpr_bpr = " + bizPartnerKey_n[0] + " AND " +
                        "d.fk_cur = " + currencyKey[0] + " AND dr.fk_row_itm = " + itemKey[0] + " AND dr.fk_adj_ct = " + DModSysConsts.TS_ADJ_CT_NA + " " +
                        "ORDER BY d.dt DESC, d.ts_usr_upd DESC " +
                        "LIMIT 1 ";
                resultSet = session.getStatement().executeQuery(sql);
            }

            if (bizPartnerKey_n != null && resultSet.next()) {
                prices[0] = resultSet.getDouble(1);
                prices[1] = resultSet.getDouble(2);
                prices[2] = resultSet.getDouble(3);
            }
            else {
                // Retreive last item price by item key:

                sql = "SELECT dr.prc_unt_cy, dr.dsc_unt_cy, dr.dsc_row_cy, d.dt, d.ts_usr_upd " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 " +
                        "WHERE d.fk_dps_ct = " + dpsCategory + " AND " +
                        "d.fk_cur = " + currencyKey[0] + " AND dr.fk_row_itm = " + itemKey[0] + " AND dr.fk_adj_ct = " + DModSysConsts.TS_ADJ_CT_NA + " " +
                        "ORDER BY d.dt DESC, d.ts_usr_upd DESC " +
                        "LIMIT 1 ";
                resultSet = session.getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    prices[0] = resultSet.getDouble(1);
                    prices[1] = resultSet.getDouble(2);
                    prices[2] = resultSet.getDouble(3);
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return prices;
    }

    /**
     * @param session Current GUI session.
     * @param key Item genus primary key.
     */
    public static int[] getTaxGroupForItemGenus(final DGuiSession session, final int[] key) {
        int[] taxGroupkey = null;
        DDbItemGenus itemGenus = (DDbItemGenus) session.readRegistry(DModConsts.IU_GEN, key);

        if (itemGenus != null) {
            taxGroupkey = new int[] { itemGenus.getFkTaxGroupId() };
        }

        return taxGroupkey ;
    }

    /**
     * @param session Current GUI session.
     * @param key Item line primary key.
     */
    public static int[] getTaxGroupForItemLine(final DGuiSession session, final int[] key) {
        int[] taxGroupKey = null;
        DDbItemLine itemLine = (DDbItemLine) session.readRegistry(DModConsts.IU_LIN, key);

        if (itemLine != null) {
            taxGroupKey = itemLine.getFkTaxGroupId_n() == DLibConsts.UNDEFINED ? null : new int[] { itemLine.getFkTaxGroupId_n() };
            if (taxGroupKey == null) {
                taxGroupKey = getTaxGroupForItemGenus(session, new int[] { itemLine.getFkItemGenusId() });
            }
        }

        return taxGroupKey;
    }

    /**
     * @param session Current GUI session.
     * @param key Item primary key.
     */
    public static int[] getTaxGroupForItem(final DGuiSession session, final int[] key) {
        int[] taxGroupKey = null;
        DDbItem item = (DDbItem) session.readRegistry(DModConsts.IU_ITM, key);

        if (item != null) {
            taxGroupKey = item.getFkTaxGroupId_n() == DLibConsts.UNDEFINED ? null : new int[] { item.getFkTaxGroupId_n() };
            if (taxGroupKey == null) {
                if (item.getFkItemLineId_n() != DLibConsts.UNDEFINED) {
                    taxGroupKey = getTaxGroupForItemLine(session, new int[] { item.getFkItemLineId_n() });
                }
                else {
                    taxGroupKey = getTaxGroupForItemGenus(session, new int[] { item.getFkItemGenusId() });
                }
            }
        }

        return taxGroupKey;
    }

    /**
     * @param session Current GUI session.
     */
    public static DDbTaxGroupConfigRow getTaxGroupConfigRowDefault(final DGuiSession session) {
        return getTaxGroupConfigRow(session,
                new int[] { ((DDbConfigCompany) session.getConfigCompany()).getFkTaxGroupId_n() },
                new int[] { ((DDbConfigBranch) session.getConfigBranch()).getFkTaxRegionId() },
                ((DGuiClientSessionCustom) session.getSessionCustom()).getIdentityTypeDefault(),
                session.getWorkingDate());
    }

    /**
     * @param session Current GUI session.
     * @param taxGroupKey Tax group primary key.
     * @param taxRegionKey Tax region primary key.
     * @param identityType Constants defined in DModSysConsts.BS_IDY_TP...
     * @param date Transaction date.
     */
    public static DDbTaxGroupConfigRow getTaxGroupConfigRow(final DGuiSession session, final int[] taxGroupKey, final int[] taxRegionKey, final int identityType, final Date date) {
        String sql = "";
        int[] key = new int[] { taxGroupKey[0], DLibConsts.UNDEFINED, DLibConsts.UNDEFINED };
        ResultSet resultSet = null;
        DDbTaxGroupConfigRow taxGroupconfigRow = null;

        try {
            // Check if item price type is defined on customer (by customer):

            sql = "SELECT MAX(id_cfg) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.FU_TAX_GRP_CFG) + " " +
                    "WHERE id_tax_grp = " + taxGroupKey[0] + " AND fk_tax_reg = " + taxRegionKey[0] + " AND " +
                    "dt_sta = (SELECT MAX(dt_sta) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.FU_TAX_GRP_CFG) + " " +
                    "WHERE id_tax_grp = " + taxGroupKey[0] + " AND fk_tax_reg = " + taxRegionKey[0] + " AND " +
                    "dt_sta <= '" + DLibUtils.DbmsDateFormatDate.format(date) + "') ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                key[1] = resultSet.getInt(1);

                sql = "SELECT id_row " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.FU_TAX_GRP_CFG_ROW) + " " +
                        "WHERE id_tax_grp = " + key[0] + " AND id_cfg = " + key[1] + " AND " +
                        "fk_idy_tp = " + identityType + " ";
                resultSet = session.getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    key[2] = resultSet.getInt(1);

                    taxGroupconfigRow = new DDbTaxGroupConfigRow();
                    taxGroupconfigRow.read(session, key);
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return taxGroupconfigRow;
    }

    public static double computeTaxRate(final DDbTaxGroupConfigRow row, final int dpsCategory) {
        double rate = 1;

        switch (dpsCategory) {
            case DModSysConsts.TS_DPS_CT_PUR:
                rate += row.getPurchaseTaxChargedRate1();
                rate += row.getPurchaseTaxChargedRate2();
                rate += row.getPurchaseTaxChargedRate3();
                rate -= row.getPurchaseTaxRetainedRate1();
                rate -= row.getPurchaseTaxRetainedRate2();
                rate -= row.getPurchaseTaxRetainedRate3();
                break;
            case DModSysConsts.TS_DPS_CT_SAL:
                rate += row.getSaleTaxChargedRate1();
                rate += row.getSaleTaxChargedRate2();
                rate += row.getSaleTaxChargedRate3();
                rate -= row.getSaleTaxRetainedRate1();
                rate -= row.getSaleTaxRetainedRate2();
                rate -= row.getSaleTaxRetainedRate1();
                break;
            default:
        }

        return rate;
    }

    public static double computePrice(final double priceNet, final double taxRate) {
        return computePrice(priceNet, 0, taxRate);
    }

    public static double computePrice(final double priceNet, final double price, final double taxRate) {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        double computedPrice = taxRate == 0 ? 0 : priceNet * (1 / taxRate);

        if (DLibUtils.round(price, decs) == DLibUtils.round(computedPrice, decs)) {
            computedPrice = price;
        }

        return computedPrice;
    }

    /**
     * @param session Current GUI session.
     * @param itemFilter Constants defined in DTrnConsts.ITEM_FILTER_...
     * @param findMode Constants defined in DTrnConsts.FIND_MODE_BY_...
     * @param year Fiscal year.
     * @param branchWarehouseKey Warehouse key.
     */
    public static Vector<DRowFindItem> readFindItems(final DGuiSession session, final int itemFilter, final int findMode, final int year, final int[] branchWarehouseKey) {
        return readFindItems(session, itemFilter, findMode, year, branchWarehouseKey, DLibConsts.UNDEFINED, null);
    }

    /**
     * @param session Current GUI session.
     * @param itemFilter Constants defined in DTrnConsts.ITEM_FILTER_...
     * @param findMode Constants defined in DTrnConsts.FIND_MODE_BY_...
     * @param year Fiscal year.
     * @param branchWarehouseKey Warehouse key.
     * @param itemPriceType
     * @param taxGroupConfigRow
     */
    public static Vector<DRowFindItem> readFindItems(final DGuiSession session, final int itemFilter, final int findMode, final int year, final int[] branchWarehouseKey, final int itemPriceType, final DDbTaxGroupConfigRow taxGroupConfigRow) {
        int table = DLibConsts.UNDEFINED;
        double taxRate = taxGroupConfigRow == null ? 1 : computeTaxRate(taxGroupConfigRow, DModSysConsts.TS_DPS_CT_SAL);
        boolean showPriceNet = false;
        String sql = "";
        String fieldExtra = "";
        String fieldPrice = "";
        String fieldFilter = "";
        Vector<DRowFindItem> rows = new Vector<DRowFindItem>();
        ResultSet resultSet = null;

        try {
            switch (findMode) {
                case DTrnConsts.FIND_MODE_BY_CODE:
                    table = DModConsts.IU_BRD;
                    fieldExtra = "brd";
                    sql = "i.code, i.name, i.id_itm ";
                    break;
                case DTrnConsts.FIND_MODE_BY_NAME:
                    table = DModConsts.IU_BRD;
                    fieldExtra = "brd";
                    sql = "i.name, i.code, i.id_itm ";
                    break;
                case DTrnConsts.FIND_MODE_BY_BRD:
                    table = DModConsts.IU_BRD;
                    fieldExtra = "brd";
                    sql = "c.name, c.id_" + fieldExtra + ", i.name, i.code, i.id_itm ";
                    break;
                case DTrnConsts.FIND_MODE_BY_MFR:
                    table = DModConsts.IU_MFR;
                    fieldExtra = "mfr";
                    sql = "c.name, c.id_" + fieldExtra + ", i.name, i.code, i.id_itm ";
                    break;
                case DTrnConsts.FIND_MODE_BY_CMP:
                    table = DModConsts.IU_CMP;
                    fieldExtra = "cmp";
                    sql = "c.name, c.id_" + fieldExtra + ", i.name, i.code, i.id_itm ";
                    break;
                case DTrnConsts.FIND_MODE_BY_DEP:
                    table = DModConsts.IU_DEP;
                    fieldExtra = "dep";
                    sql = "c.name, c.id_" + fieldExtra + ", i.name, i.code, i.id_itm ";
                    break;
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            switch (itemPriceType) {
                case DModSysConsts.MS_ITM_PRC_TP_SRP:
                    fieldPrice = "i.prc_srp";
                    break;
                case DModSysConsts.MS_ITM_PRC_TP_RET:
                    fieldPrice = "i.prc_1";
                    break;
                case DModSysConsts.MS_ITM_PRC_TP_HAL:
                    fieldPrice = "i.prc_2";
                    break;
                case DModSysConsts.MS_ITM_PRC_TP_WHO:
                    fieldPrice = "i.prc_3";
                    break;
                case DModSysConsts.MS_ITM_PRC_TP_ESP:
                    fieldPrice = "i.prc_4";
                    break;
                case DModSysConsts.MS_ITM_PRC_TP_COS:
                    fieldPrice = "i.prc_5";
                    break;
                default:
                    fieldPrice = "0";
            }

            switch (itemFilter) {
                case DTrnConsts.ITEM_FILTER_ALL:
                    fieldFilter = "";
                    break;
                case DTrnConsts.ITEM_FILTER_INV:
                    fieldFilter = "AND i.b_inv = 1 ";
                    break;
                case DTrnConsts.ITEM_FILTER_INV_PAC:
                    fieldFilter = "AND i.fk_itm_pac_n <> " + DLibConsts.UNDEFINED + " ";
                    break;
            }

            sql = "SELECT i.id_itm, u.id_unt, i.code, i.name, i.b_inv, i.fk_itm_pac_n, c.id_" + fieldExtra + ", c.name, " +
                    "u.code, ig.fk_itm_ct, ig.fk_itm_cl, ig.fk_itm_tp, ict.code, icl.code, itp.code, " +
                    "i.fk_tax_grp_n, il.fk_tax_grp_n, ig.fk_tax_grp, " + fieldPrice + " AS f_prc, " +
                    "SUM(s.mov_in - s.mov_out) AS f_stk " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS ig ON i.fk_gen = ig.id_gen AND i.b_del = 0 AND i.b_dis = 0 " + fieldFilter +
                    "INNER JOIN " + DModConsts.TablesMap.get(table) + " AS c ON i.fk_" + fieldExtra + " = c.id_" + fieldExtra + " " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CT) + " AS ict ON ig.fk_itm_ct = ict.id_itm_ct " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS icl ON ig.fk_itm_ct = icl.id_itm_ct AND ig.fk_itm_cl = icl.id_itm_cl " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_TP) + " AS itp ON ig.fk_itm_ct = itp.id_itm_ct AND ig.fk_itm_cl = itp.id_itm_cl AND ig.fk_itm_tp = itp.id_itm_tp " +
                    "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s ON i.id_itm = s.id_itm AND s.id_yer = " + year + " AND " +
                    "s.id_bpr = " + branchWarehouseKey[0] + " AND s.id_bra = " + branchWarehouseKey[1] + " AND s.id_wah = " + branchWarehouseKey[2] + " AND s.b_del = 0 " +
                    "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON i.fk_unt = u.id_unt " +
                    "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_LIN) + " AS il ON i.fk_lin_n = il.id_lin " +
                    "GROUP BY i.id_itm, u.id_unt, i.code, i.name, i.b_inv, i.fk_itm_pac_n, c.id_" + fieldExtra + ", c.name, " +
                    "u.code, ig.fk_itm_ct, ig.fk_itm_cl, ig.fk_itm_tp, ict.code, icl.code, itp.code, " +
                    "i.fk_tax_grp_n, il.fk_tax_grp_n, ig.fk_tax_grp" + (!fieldPrice.contains("i.") ? "" : ", " + fieldPrice) + " " +
                    "ORDER BY " + sql;
            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                DRowFindItem row = new DRowFindItem(findMode, resultSet.getInt("i.id_itm"), resultSet.getInt("u.id_unt"), resultSet.getString("i.code"), resultSet.getString("i.name"), resultSet.getString("c.name"));

                row.setFkItemCategoryId(resultSet.getInt("ig.fk_itm_ct"));
                row.setFkItemClassId(resultSet.getInt("ig.fk_itm_cl"));
                row.setFkItemTypeId(resultSet.getInt("ig.fk_itm_tp"));
                row.setItemCategoryCode(resultSet.getString("ict.code"));
                row.setItemClassCode(resultSet.getString("icl.code"));
                row.setItemTypeCode(resultSet.getString("itp.code"));
                row.setInventoriable(resultSet.getBoolean("i.b_inv"));
                row.setConvertible(resultSet.getInt("i.fk_itm_pac_n") != DLibConsts.UNDEFINED);
                row.setStock(resultSet.getDouble("f_stk"));
                row.setUnitCode(resultSet.getString("u.code"));

                if (taxGroupConfigRow != null) {
                    if (taxGroupConfigRow.getPkTaxGroupId() == resultSet.getInt("i.fk_tax_grp_n")) {
                        showPriceNet = true;
                    }
                    else if (taxGroupConfigRow.getPkTaxGroupId() == resultSet.getInt("il.fk_tax_grp_n")) {
                        showPriceNet = true;
                    }
                    else if (taxGroupConfigRow.getPkTaxGroupId() == resultSet.getInt("ig.fk_tax_grp")) {
                        showPriceNet = true;
                    }
                    else {
                        showPriceNet = false;
                    }
                }

                row.setPrice(resultSet.getDouble("f_prc"));
                row.setPriceNet(!showPriceNet ? row.getPrice() : row.getPrice() * taxRate);

                rows.add(row);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return rows;
    }

    /**
     * @param session Current GUI session.
     * @param findMode Constants defined in DTrnConsts.FIND_MODE_BY_...
     * @param bizPartnerClass Constants defined in DModSysConsts.BS_BPR_CL_...
     */
    public static Vector<DRowFindBizPartner> readFindBizPartners(final DGuiSession session, final int findMode, final int bizPartnerClass) {
        String sql = "";
        Vector<DRowFindBizPartner> rows = new Vector<DRowFindBizPartner>();
        ResultSet resultSet = null;

        try {
            switch (findMode) {
                case DTrnConsts.FIND_MODE_BY_CODE:
                    sql = "bc.code, b.name, b.id_bpr ";
                    break;
                case DTrnConsts.FIND_MODE_BY_NAME:
                    sql = "b.name, bc.code, b.id_bpr ";
                    break;
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            sql = "SELECT b.id_bpr, bc.code, b.name, b.fis_id, bc.fk_bpr_cl, bc.fk_bpr_tp " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " AS bc ON " +
                    "b.id_bpr = bc.id_bpr AND bc.id_bpr_cl = " + bizPartnerClass + " AND b.b_del = 0 AND b.b_dis = 0 " +
                    "ORDER BY " + sql;
            resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                DRowFindBizPartner row = new DRowFindBizPartner(findMode, resultSet.getInt("b.id_bpr"), resultSet.getString("bc.code"), resultSet.getString("b.name"), resultSet.getString("b.fis_id"));
                row.setFkBizPartnerClassId(resultSet.getInt("bc.fk_bpr_cl"));
                row.setFkBizPartnerTypeId(resultSet.getInt("bc.fk_bpr_tp"));
                rows.add(row);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return rows;
    }

    public static DTrnItemsFound digestItemsFound(final DGuiSession session, final String textToFind) {
        int index = textToFind.indexOf("*");
        double quantity = 1;
        String sql = "";
        String text = "";
        ResultSet resultSet = null;
        DTrnItemsFound itemsFound = new DTrnItemsFound();

        try {
            if (index == -1) {
                text = DLibUtils.textTrim(textToFind);
            }
            else {
                quantity = DLibUtils.parseDouble(textToFind.substring(0, index));

                if (index + 1 < textToFind.length()) {
                    text = textToFind.substring(index + 1);
                }

                text = DLibUtils.textTrim(text);
            }

            itemsFound.setTextToFind(text);

            if (text.length() > 0) {
                // Try to find item by code:

                sql = "SELECT id_itm " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " " +
                        "WHERE b_dis = 0 AND b_del = 0 AND code = '" + text + "' ";
                resultSet = session.getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    itemsFound.setFoundBy(DTrnConsts.FOUND_BY_CODE);
                    itemsFound.setCode(text);
                    itemsFound.getItemsFoundKeys().add(new int[] { resultSet.getInt(1) });
                }

                if (itemsFound.getItemsFoundKeys().isEmpty()) {
                    // Try to find item by barcode:

                    sql = "SELECT ib.id_itm " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_ITM_BAR) + " AS ib ON i.id_itm = ib.id_itm AND " +
                            "i.b_dis = 0 AND i.b_del = 0 AND ib.bar = '" + text + "' ";
                    resultSet = session.getStatement().executeQuery(sql);
                    while (resultSet.next()) {
                        itemsFound.setFoundBy(DTrnConsts.FOUND_BY_BARCODE);
                        itemsFound.setBarcode(text);
                        itemsFound.getItemsFoundKeys().add(new int[] { resultSet.getInt(1) });
                    }

                    if (itemsFound.getItemsFoundKeys().isEmpty()) {
                        // Try to find item by serial number:

                        sql = "SELECT DISTINCT id_itm FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                                "WHERE b_del = 0 AND snr = '" + text + "' ";
                        resultSet = session.getStatement().executeQuery(sql);
                        while (resultSet.next()) {
                            itemsFound.setFoundBy(DTrnConsts.FOUND_BY_SNR);
                            itemsFound.setSerialNumber(text);
                            itemsFound.getItemsFoundKeys().add(new int[] { resultSet.getInt(1) });
                        }

                        if (itemsFound.getItemsFoundKeys().isEmpty() && text.length() > 1) {
                            // Last chance to find item by serial number removing posible verification digit:

                            sql = "SELECT DISTINCT id_itm FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                                    "WHERE b_del = 0 AND snr = '" + text.substring(0, text.length() - 1) + "' ";
                            resultSet = session.getStatement().executeQuery(sql);
                            while (resultSet.next()) {
                                itemsFound.setFoundBy(DTrnConsts.FOUND_BY_SNR);
                                itemsFound.setSerialNumber(text.substring(0, text.length() - 1));
                                itemsFound.getItemsFoundKeys().add(new int[] { resultSet.getInt(1) });
                            }

                            if (itemsFound.getItemsFoundKeys().size() > 0) {
                                itemsFound.setTextToFind(text.substring(0, text.length() - 1));
                            }
                        }

                        if (itemsFound.getItemsFoundKeys().isEmpty()) {
                            if (index == -1) {
                                quantity = DLibUtils.parseDouble(text);
                            }
                        }
                    }
                }
            }

            itemsFound.setQuantity(quantity);
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return itemsFound;
    }

    public static String composeDpsNumber(final String series, final int number) {
        return (series.length() == 0 ? "" : series + "-") + number;
    }

    public static String composeDpsReference(final String series, final int number) {
        return series + DLibUtils.DecimalReferenceFormat.format(number);
    }

    public static DDbDpsSeries getDpsSeries(final DGuiSession session, final int[] dpsTypeKey, final String series) {
        String sql = "";
        ResultSet resultSet = null;
        DDbDpsSeries dpsSeries = null;

        try {
            sql = "SELECT id_ser " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " " +
                    "WHERE b_del = 0 AND ser = '" + series + "' AND " +
                    "fk_dps_ct = " + dpsTypeKey[0] + " AND fk_dps_cl = " + dpsTypeKey[1] + " AND fk_dps_tp = " + dpsTypeKey[2] + " " +
                    "LIMIT 1 ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                dpsSeries = (DDbDpsSeries) session.readRegistry(DModConsts.TU_SER, new int[] { resultSet.getInt(1) });
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return dpsSeries;
    }

    public static DDbDpsSeriesNumber getDpsSeriesNumber(final DGuiSession session, final int[] dpsTypeKey, final String series, final int number) {
        String sql = "";
        ResultSet resultSet = null;
        DDbDpsSeriesNumber dpsSeriesNumber = null;

        try {
            sql = "SELECT sn.id_ser, sn.id_num " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER_NUM) + " AS sn ON " +
                    "s.b_del = 0 AND s.id_ser = sn.id_ser AND s.ser = '" + series + "' AND " +
                    "s.fk_dps_ct = " + dpsTypeKey[0] + " AND s.fk_dps_cl = " + dpsTypeKey[1] + " AND s.fk_dps_tp = " + dpsTypeKey[2] + " AND " +
                    "sn.b_del = 0 AND sn.num_sta <= " + number + " AND (sn.num_end_n IS NULL OR sn.num_end_n >= " + number + ") " +
                    "ORDER BY sn.ts_usr_ins DESC " +
                    "LIMIT 1 ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                dpsSeriesNumber = (DDbDpsSeriesNumber) session.readRegistry(DModConsts.TU_SER_NUM, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return dpsSeriesNumber;
    }

    public static int getNextNumberForDps(final DGuiSession session, final int[] dpsTypeKey, final String series) {
        int number = 0;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT COALESCE(MAX(num), 0) + 1 " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " " +
                    "WHERE b_del = 0 AND fk_dps_ct = " + dpsTypeKey[0] + " AND fk_dps_cl = " + dpsTypeKey[1] + " AND fk_dps_tp = " + dpsTypeKey[2] + " AND " +
                    "ser = '" + series + "' ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return number;
    }

    public static int getNextNumberForIog(final DGuiSession session, final int iogCategory, final int[] warehouseKey) {
        int number = 0;
        int[] entityKey = null;
        String sql = "";
        ResultSet resultSet = null;

        if (((DDbConfigCompany) session.getConfigCompany()).getFkDnpTypeIogId() == DModSysConsts.TS_DNP_TP_ENT) {
            entityKey = new int[] { warehouseKey[0], warehouseKey[1] };
        }
        else {
            entityKey = warehouseKey;
        }

        try {
            sql = "SELECT COALESCE(MAX(num), 0) + 1 " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " " +
                    "WHERE b_del = 0 AND fk_iog_ct = " + iogCategory + " AND " +
                    "fk_wah_bpr = " + entityKey[0] + " AND fk_wah_bra = " + entityKey[1] + " " +
                    (entityKey.length == 2 ? "" : "AND fk_wah_wah = " + entityKey[2] + " ");
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return number;
    }

    public static int getNextNumberForIom(final DGuiSession session, final int iomCategory, final int[] cashKey) {
        int number = 0;
        int[] entityKey = null;
        String sql = "";
        ResultSet resultSet = null;

        if (((DDbConfigCompany) session.getConfigCompany()).getFkDnpTypeIomId() == DModSysConsts.TS_DNP_TP_ENT) {
            entityKey = new int[] { cashKey[0], cashKey[1] };
        }
        else {
            entityKey = cashKey;
        }

        try {
            sql = "SELECT COALESCE(MAX(num), 0) + 1 " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_IOM) + " " +
                    "WHERE b_del = 0 AND fk_iog_ct = " + iomCategory + " AND " +
                    "fk_csh_bpr = " + entityKey[0] + " AND fk_csh_bra = " + entityKey[1] + " " +
                    (entityKey.length == 2 ? "" : "AND fk_csh_csh = " + entityKey[2] + " ");
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return number;
    }
    
    public static int getIogCounterpartId(final DGuiSession session, final int iogId) throws Exception {
        int iogCounterpartId = 0;
        
        String sql = "SELECT id_iog "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " "
                + "WHERE fk_src_iog_n = " + iogId + " AND NOT b_del;";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            iogCounterpartId = resultSet.getInt(1);
        }
        
        return iogCounterpartId;
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     */
    public static boolean isDpsNumberAutomatic(final int[] dpsClassKey) {
        return DLibUtils.compareKeys(dpsClassKey, DModSysConsts.TS_DPS_CL_PUR_ORD) || dpsClassKey[0] == DModSysConsts.TS_DPS_CT_SAL;
    }

    /**
     * Validates document number.
     * If document is new, and its numbers are automatically defined by system, and number is already in use,
     * then three attempts are done to set a valid number.
     */
    public static String validateNumberForDps(final DGuiSession session, final DDbDps dps) {
        int attempts = 0;
        int number = 0;
        int count = 0;
        String sql = "";
        String msg = "";
        ResultSet resultSet = null;
        boolean isDpsNumberAutomatic = DTrnUtils.isDpsNumberAutomatic(dps.getDpsClassKey());
        Vector<DDbDpsSeriesNumber> dpsSeriesNumbers = null;

        try {
            // Validate that document number is not already in use:

            attempts = dps.isRegistryNew() && isDpsNumberAutomatic ? 3 : 1;
            number = dps.getNumber();

            for (int i = 0; i < attempts; i++) {
                sql = "SELECT COUNT(*) FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " " +
                        "WHERE b_del = 0 AND ser = '" + dps.getSeries() + "' AND num = " + number + " AND " +
                        "fk_dps_ct = " + dps.getFkDpsCategoryId() + " AND fk_dps_cl = " + dps.getFkDpsClassId() + " AND fk_dps_tp = " + dps.getFkDpsTypeId() + " AND " +
                        "id_dps <> " + dps.getPkDpsId() + " " + (isDpsNumberAutomatic ? "" : "AND fk_bpr_bpr = " + dps.getFkBizPartnerBizPartnerId() + " ");
                resultSet = session.getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }

                if (count == 0) {
                    break;
                }
                else {
                    if (dps.isRegistryNew() && isDpsNumberAutomatic) {
                        number = getNextNumberForDps(session, dps.getDpsTypeKey(), dps.getSeries());
                        Thread.sleep(500);
                    }
                }
            }

            if (count > 0) {
                msg = (count == 1 ? "Existe 1 documento " : "Existen " + count + " documentos ") + "con el folio: '" + dps.getDpsNumber() + "'.";
            }
            else {
                if (isDpsNumberAutomatic) {
                    // Validate document series:

                    sql = "SELECT COUNT(*) FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " " +
                            "WHERE b_del = 0 AND ser = '" + dps.getSeries() + "' AND " +
                            "fk_dps_ct = " + dps.getFkDpsCategoryId() + " AND fk_dps_cl = " + dps.getFkDpsClassId() + " AND fk_dps_tp = " + dps.getFkDpsTypeId() + " ";
                    resultSet = session.getStatement().executeQuery(sql);
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }

                    if (count == 0) {
                        msg = DUtilConsts.ERR_MSG_DPS_SER_NON_AVA;
                    }
                    else if (count > 1) {
                        msg = DUtilConsts.ERR_MSG_DPS_SER_MUL_AVA;
                    }
                    else {
                        sql = "SELECT id_ser FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " " +
                                "WHERE b_del = 0 AND ser = '" + dps.getSeries() + "' AND " +
                                "fk_dps_ct = " + dps.getFkDpsCategoryId() + " AND fk_dps_cl = " + dps.getFkDpsClassId() + " AND fk_dps_tp = " + dps.getFkDpsTypeId() + " ";
                        resultSet = session.getStatement().executeQuery(sql);
                        if (!resultSet.next()) {
                            msg = DDbConsts.ERR_MSG_REG_NOT_FOUND;
                        }
                        else {
                            dpsSeriesNumbers = ((DDbUser) session.getUser()).getAuxBranchDpsSeriesNumbers(resultSet.getInt(1));

                            if (dpsSeriesNumbers.isEmpty()) {
                                msg = DUtilConsts.ERR_MSG_DPS_SER_NUM_NON_AVA;
                            }
                            else if (dpsSeriesNumbers.size() > 1) {
                                msg = DUtilConsts.ERR_MSG_DPS_SER_NUM_MUL_AVA;
                            }
                            else {
                                if (number < dpsSeriesNumbers.get(0).getNumberStart()) {
                                    msg = DUtilConsts.ERR_MSG_DPS_SER_NUM_MIN + " (" + DUtilConsts.TXT_MIN + " " + dpsSeriesNumbers.get(0).getNumberStart() + ").";
                                }
                                else if (dpsSeriesNumbers.get(0).getNumberEnd_n() != 0 && number > dpsSeriesNumbers.get(0).getNumberEnd_n()) {
                                    msg = DUtilConsts.ERR_MSG_DPS_SER_NUM_MAX + " (" + DUtilConsts.TXT_MAX + " " + dpsSeriesNumbers.get(0).getNumberStart() + ").";
                                }
                                else {
                                    if (dps.isRegistryNew() && number != dps.getNumber()) {
                                        dps.setNumber(number);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (InterruptedException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return msg;
    }

    public static String getLastPaymentAccount(final DGuiSession session, final int[] dpsTypeKey, final int[] bizPartnerKey) {
        String sql = "";
        String paymentAccount = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT dt, pay_acc " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " " +
                    "WHERE b_del = 0 AND fk_bpr_bpr = " + bizPartnerKey[0] + " AND " +
                    "fk_dps_ct = " + dpsTypeKey[0] + " AND fk_dps_cl = " + dpsTypeKey[1] + " AND fk_dps_tp = " + dpsTypeKey[2] + " " +
                    "ORDER BY dt DESC, ts_usr_ins DESC " +
                    "LIMIT 1 ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                paymentAccount = resultSet.getString(2);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return paymentAccount;
    }

    public static int getLotId(final DGuiSession session, final int itemId, final int unitId, final String lot, final Date expiration) {
        int idLot = DLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_lot " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " " +
                    "WHERE id_itm = " + itemId + " AND id_unt = " + unitId + " AND " +
                    "lot = '" + lot + "' AND dt_exp_n = '" + DLibUtils.DbmsDateFormatDate.format(expiration) + "' ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                idLot = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return idLot;
    }

    private static double getStock(final DGuiSession session, final int year, final int itemId, final int unitId, final int lotId, final int[] warehouseKey_n,
            final boolean appliesSerialNumber, final String serialNumber,
            final boolean appliesImportDeclaration, final String importDeclaration, final Date importDeclarationDate) {
        double stock = 0;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT SUM(mov_in - mov_out) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                    "WHERE b_del = 0 AND id_yer = " + year + " AND " +
                    "id_itm = " + itemId + " AND id_unt = " + unitId + " " + (lotId == DLibConsts.UNDEFINED ? "" : "AND id_lot = " + lotId + " ") +
                    (warehouseKey_n == null ? "" : "AND id_bpr = " + warehouseKey_n[0] + " AND id_bra = " + warehouseKey_n[1] + " AND id_wah = " + warehouseKey_n[2] + " ") +
                    (!appliesSerialNumber ? "" : "AND snr = '" + serialNumber + "' ") +
                    (!appliesImportDeclaration ? "" : "AND imp_dec = '" + importDeclaration + "' AND imp_dec_dt_n = '" + DLibUtils.DbmsDateFormatDate.format(importDeclarationDate) + "' ");
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                stock = resultSet.getDouble(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return stock;
    }

    public static double getStockForItemUnit(final DGuiSession session, final int year, final int itemId, final int unitId, final int[] warehouseKey_n) {
        return getStock(session, year, itemId, unitId, DLibConsts.UNDEFINED, warehouseKey_n, false, "", false, "", null);
    }

    public static double getStockForLot(final DGuiSession session, final int year, final int[] lotKey, final int[] warehouseKey_n) {
        return getStock(session, year, lotKey[0], lotKey[1], lotKey[2], warehouseKey_n, false, "", false, "", null);
    }

    public static double getStockForLotSerialNumber(final DGuiSession session, final int year, final int[] lotKey, final int[] warehouseKey, final String serialNumber) {
        return getStock(session, year, lotKey[0], lotKey[1], lotKey[2], warehouseKey, true, serialNumber, false, "", null);
    }

    public static double getStockForLotSerialNumberImportDeclaration(final DGuiSession session, final int year, final int[] lotKey, final int[] warehouseKey, final String serialNumber, final String importDeclaration, final Date importDeclarationDate) {
        return getStock(session, year, lotKey[0], lotKey[1], lotKey[2], warehouseKey, true, serialNumber, true, importDeclaration, importDeclarationDate);
    }

    public static double getStockForLotImportDeclaration(final DGuiSession session, final int year, final int[] lotKey, final int[] warehouseKey_n, final String importDeclaration, final Date importDeclarationDate) {
        return getStock(session, year, lotKey[0], lotKey[1], lotKey[2], warehouseKey_n, false, "", true, importDeclaration, importDeclarationDate);
    }

    /**
     * Obtain available lots, from desired item and unit, from desired warehouse, in order to propouse a set of stock moves, acording to required quantity.
     *
     * @param session User session.
     * @param year Year of date of pretended stock move.
     * @param itemId ID of desired item.
     * @param unitId ID of desired unit.
     * @param warehouseKey Primary key of selected warehose for pretended stock move.
     * @param currentIog Current IOG for descarting its stock moves.
     * @param currentStockMoves Stock moves in current document for subtracting then from available stock lots.
     * @param quantityRequired Total quantity required to limit stock lots available.
     */
    public static ArrayList<DTrnStockMove> getAvailableLots(final DGuiSession session, final int year, final int itemId, final int unitId, final int[] warehouseKey, final int currentIog, final ArrayList<DTrnStockMove> currentStockMoves, final double quantityRequired) throws SQLException, Exception {
        double quantity = 0;
        String sql = "";
        ResultSet resultSet = null;
        ArrayList<DTrnStockMove> availableStockMoves = new ArrayList<DTrnStockMove>();
        ArrayList<DTrnStockMove> availableLots = new ArrayList<DTrnStockMove>();

        // 1. Obtain available stock lots, discarting stock moves of current IOG if any:
        sql = "SELECT s.id_itm, s.id_unt, s.id_lot, l.dt_exp_n, l.lot, SUM(s.mov_in - s.mov_out) AS f_stk " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " AS l ON " +
                "s.id_itm = l.id_itm AND s.id_unt = l.id_unt AND s.id_lot = l.id_lot " +
                "WHERE s.id_yer = " + year + " AND s.id_itm = " + itemId + " AND s.id_unt = " + unitId + " AND " +
                "s.id_bpr = " + warehouseKey[0] + " AND s.id_bra = " + warehouseKey[1] + " AND s.id_wah = " + warehouseKey[2] + " AND s.b_del = 0 " +
                (currentIog == DLibConsts.UNDEFINED? "" : "AND s.fk_iog_iog <> " + currentIog + " ") +
                "GROUP BY s.id_itm, s.id_unt, s.id_lot, l.dt_exp_n, l.lot " +
                "HAVING f_stk <> 0 " +
                "ORDER BY l.dt_exp_n, l.lot, s.id_itm, s.id_unt, s.id_lot ";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            availableStockMoves.add(new DTrnStockMove(new int[] { itemId, unitId, resultSet.getInt("s.id_lot"), warehouseKey[0], warehouseKey[1], warehouseKey[2] }, resultSet.getDouble("f_stk"), "", "", null, resultSet.getString("l.lot"), resultSet.getDate("l.dt_exp_n")));
        }

        // 2. Subtract current stock moves from available stock lots:

        for (DTrnStockMove currentStockMove : currentStockMoves) {
            for (DTrnStockMove availableStockMove : availableStockMoves) {
                if (currentStockMove.getLot().compareTo(availableStockMove.getLot()) == 0 && DLibTimeUtils.isSameDate(currentStockMove.getDateExpiration(), availableStockMove.getDateExpiration())) {
                    availableStockMove.setQuantity(availableStockMove.getQuantity() - currentStockMove.getQuantity());
                    break;
                }
            }
        }

        // 3. Process really available stock lots, acording to required quantity:

        for (DTrnStockMove availableStockMove : availableStockMoves) {
            if (availableStockMove.getQuantity() > 0) {
                if (quantity + availableStockMove.getQuantity() > quantityRequired) {
                    availableStockMove.setQuantity(quantityRequired - quantity);
                }

                availableLots.add(availableStockMove);

                quantity += availableStockMove.getQuantity();
                if (quantity >= quantityRequired) {
                    break;
                }
            }
        }

        return availableLots;
    }

    public static double getBalanceForBizPartner(final DGuiSession session, final int year, final int[] bizPartnerKey, final int bizPartnerClass) {
        double balance = 0;
        String sql = "";
        ResultSet resultSet = null;

        try {
            switch (bizPartnerClass) {
                case DModSysConsts.BS_BPR_CL_VEN:
                    sql = "SUM(cdt - dbt) AS f_fal ";
                    break;
                case DModSysConsts.BS_BPR_CL_CUS:
                    sql = "SUM(dbt - cdt) AS f_fal ";
                    break;
            }

            sql = "SELECT " + sql +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " " +
                    "WHERE b_del = 0 AND id_yer = " + year + " AND " +
                    "fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass) + " AND " +
                    "fk_bpr_bpr_n = " + bizPartnerKey[0] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                balance = resultSet.getDouble(1);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return balance;
    }

    /**
     * Gets document balance.
     * @param session
     * @param year
     * @param dpsKey
     * @return 
     */
    public static DTrnAmount getBalanceForDps(final DGuiSession session, final int year, final int[] dpsKey) {
        return getBalanceForDps(session, year, dpsKey, null);
    }

    /**
     * Gets document balance excluding provided bookkeeping number.
     * @param session
     * @param year
     * @param dpsKey
     * @return 
     */
    public static DTrnAmount getBalanceForDps(final DGuiSession session, final int year, final int[] dpsKey, final int[] bkkNumberKey_n) {
        int cur = 0;
        int bizPartner = 0;
        int bizPartnerClass = 0;
        String sql = "";
        ResultSet resultSet = null;
        DTrnAmount amount = null;

        try {
            sql = "SELECT fk_dps_ct, fk_bpr_bpr, fk_cur FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " WHERE id_dps = " + dpsKey[0] + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                cur = resultSet.getInt("fk_cur");
                bizPartner = resultSet.getInt("fk_bpr_bpr");
                bizPartnerClass = DTrnUtils.getBizPartnerClassByDpsCategory(resultSet.getInt("fk_dps_ct"));

                switch (bizPartnerClass) {
                    case DModSysConsts.BS_BPR_CL_VEN:
                        sql = "SUM(cdt - dbt) AS f_fal, SUM(cdt_cy - dbt_cy) AS f_fal_cy ";
                        break;
                    case DModSysConsts.BS_BPR_CL_CUS:
                        sql = "SUM(dbt - cdt) AS f_fal, SUM(dbt_cy - cdt_cy) AS f_fal_cy ";
                        break;
                }

                sql = "SELECT " + sql +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " " +
                        "WHERE b_del = 0 AND id_yer = " + year + " AND " +
                        "fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass) + " AND " +
                        "fk_bpr_bpr_n = " + bizPartner + " AND fk_dps_inv_n = " + dpsKey[0] + " " +
                        (bkkNumberKey_n == null ? "" : "AND (NOT (fk_bkk_yer_n = " + bkkNumberKey_n[0] + " AND fk_bkk_num_n = " + bkkNumberKey_n[1] + ") OR fk_sys_mov_cl = " + DModSysConsts.FS_SYS_MOV_CL_YO + ")");
                resultSet = session.getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    amount = new DTrnAmount(resultSet.getDouble(1), resultSet.getDouble(2), cur, session.getSessionCustom().getCurrencyCode(new int[] { cur }));
                }
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return amount;
    }

    public static DDbIog createIogForSupply(final DGuiSession session, final DDbDps dps) {
        DDbIog iog = new DDbIog();
        int[] iogTypeKey = getIogTypeByDpsClass(dps.getDpsClassKey());

        iog.setPkIogId(0);
        iog.setDate(dps.getDate());
        iog.setNumber(DTrnUtils.getNextNumberForIog(session, iogTypeKey[0], dps.getBranchWarehouseKey_n()));
        iog.setValue_r(0);
        iog.setAudited(false);
        iog.setDeleted(false);
        iog.setSystem(true);
        iog.setFkIogCategoryId(iogTypeKey[0]);
        iog.setFkIogClassId(iogTypeKey[1]);
        iog.setFkIogTypeId(iogTypeKey[2]);
        iog.setFkWarehouseBizPartnerId(dps.getBranchWarehouseKey_n()[0]);
        iog.setFkWarehouseBranchId(dps.getBranchWarehouseKey_n()[1]);
        iog.setFkWarehouseWarehouseId(dps.getBranchWarehouseKey_n()[2]);
        iog.setFkBookkeepingYearId_n(dps.getFkBookkeepingYearId_n());
        iog.setFkBookkeepingNumberId_n(dps.getFkBookkeepingNumberId_n());
        iog.setFkSourceDpsId_n(dps.getPkDpsId());
        iog.setFkSourceIogId_n(DLibConsts.UNDEFINED);
        iog.setFkUserAuditedId(DUtilConsts.USR_NA_ID);
        iog.setFkUserInsertId(DUtilConsts.USR_NA_ID);
        iog.setFkUserUpdateId(DUtilConsts.USR_NA_ID);
        iog.setTsUserAudited(null);
        iog.setTsUserInsert(null);
        iog.setTsUserUpdate(null);

        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                DDbIogRow iogRow = new DDbIogRow();

                iogRow.setPkIogId(0);
                iogRow.setPkRowId(0);
                iogRow.setQuantity(dpsRow.getQuantity());
                iogRow.setValueUnitary(dpsRow.getPriceUnitaryCalculated_r());
                iogRow.setValue_r(0);   // set by DDbIog.computeTotal()
                iogRow.setInventoriable(dpsRow.isInventoriable());
                iogRow.setDeleted(false);
                iogRow.setSystem(true);
                iogRow.setFkItemId(dpsRow.getFkRowItemId());
                iogRow.setFkUnitId(dpsRow.getFkRowUnitId());

                if (!dpsRow.isDpsRowAdjustment()) {
                    // Source document is an invoice:

                    iogRow.setFkDpsInvDpsId_n(dpsRow.getPkDpsId());
                    iogRow.setFkDpsInvRowId_n(dpsRow.getPkRowId());
                    iogRow.setFkDpsAdjDpsId_n(DLibConsts.UNDEFINED);
                    iogRow.setFkDpsAdjRowId_n(DLibConsts.UNDEFINED);
                }
                else {
                    // Source document is a debit or credit note:

                    iogRow.setFkDpsInvDpsId_n(dpsRow.getFkSourceDpsId_n());
                    iogRow.setFkDpsInvRowId_n(dpsRow.getFkSourceRowId_n());
                    iogRow.setFkDpsAdjDpsId_n(dpsRow.getPkDpsId());
                    iogRow.setFkDpsAdjRowId_n(dpsRow.getPkRowId());
                }

                iogRow.setFkUserInsertId(DUtilConsts.USR_NA_ID);
                iogRow.setFkUserUpdateId(DUtilConsts.USR_NA_ID);
                iogRow.setTsUserInsert(null);
                iogRow.setTsUserUpdate(null);

                iogRow.setDbItemCode(dpsRow.getItemCode());
                iogRow.setDbItemName(dpsRow.getItemName());
                iogRow.setDbUnitCode(dpsRow.getUnitCode());

                iogRow.getAuxStockMoves().addAll(dpsRow.getAuxStockMoves());

                iog.getChildRows().add(iogRow);
            }
        }

        iog.computeTotal();

        return iog;
    }

    public static Vector<DGridRow> createDpsRowsForAdjustment(final DGuiSession session, final int year, int[] dpsKey) {
        return createDpsRowsForAdjustment(session, year, dpsKey, null);
    }

    public static Vector<DGridRow> createDpsRowsForAdjustment(final DGuiSession session, final int year, int[] dpsKey, final int[] bkkNumberKey_n) {
        int category = DLibConsts.UNDEFINED;
        double quantityInc = 0;
        double quantityDec = 0;
        double totalIncCy = 0;
        double totalDecCy = 0;
        String sql = "";
        Statement statementDps = null;
        Statement statementAdj = null;
        ResultSet resultSetDps = null;
        ResultSet resultSetAdj = null;
        DDbDpsRow row = null;
        DRowDpsRowAdjusted rowAdjusted = null;
        Vector<DGridRow> rows = new Vector<>();

        try {
            statementDps = session.getStatement().getConnection().createStatement();
            statementAdj = session.getStatement().getConnection().createStatement();

            // Read category of DPS to be adjusted:

            sql = "SELECT fk_dps_ct FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " WHERE id_dps = " + dpsKey[0] + " " ;
            resultSetDps = statementDps.executeQuery(sql);
            if (!resultSetDps.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                category = resultSetDps.getInt(1);
            }

            // Read active rows of DPS to be adjusted:

            sql = "SELECT id_row " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " " +
                    "WHERE id_dps = " + dpsKey[0] + " AND b_del = 0 ";
            resultSetDps = statementDps.executeQuery(sql);
            while (resultSetDps.next()) {
                // Get aswell its current adjustments:

                row = (DDbDpsRow) session.readRegistry(DModConsts.T_DPS_ROW, new int[] { dpsKey[0], resultSetDps.getInt(1) });
                rowAdjusted = new DRowDpsRowAdjusted(row.getPrimaryKey(), row.getFkRowItemId(), row.getFkRowUnitId(), row.getSortingPos(), row.getCode(), row.getName(), row.getDbUnitCode());

                quantityInc = 0;
                quantityDec = 0;
                totalIncCy = 0;
                totalDecCy = 0;

                sql = "SELECT dr.id_dps, dr.id_row, dr.qty, dr.tot_cy_r, dr.fk_adj_ct " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " AS dr ON " +
                        "d.id_dps = dr.id_dps AND d.b_del = 0 AND dr.b_del = 0 AND d.fk_dps_st = " + DModSysConsts.TS_DPS_ST_ISS + " " +
                        "WHERE dr.fk_src_dps_n = " + dpsKey[0] + " AND dr.fk_src_row_n = " + resultSetDps.getInt(1) + " AND " +
                        "dr.fk_adj_ct IN (" + DModSysConsts.TS_ADJ_CT_INC + ", " + DModSysConsts.TS_ADJ_CT_DEC + ") " +
                        (bkkNumberKey_n == null ? "" : "AND NOT (d.fk_bkk_yer_n = " + bkkNumberKey_n[0] + " AND d.fk_bkk_num_n = " + bkkNumberKey_n[1] + ") ");
                resultSetAdj = statementAdj.executeQuery(sql);
                while (resultSetAdj.next()) {
                    if (resultSetAdj.getInt("fk_adj_ct") == DModSysConsts.TS_ADJ_CT_INC) {
                        quantityInc += resultSetAdj.getDouble("qty");
                        totalIncCy += resultSetAdj.getDouble("tot_cy_r");
                    }
                    else {
                        quantityDec += resultSetAdj.getDouble("qty");
                        totalDecCy += resultSetAdj.getDouble("tot_cy_r");
                    }
                }

                rowAdjusted.setQuantityOriginal(row.getQuantity());
                rowAdjusted.setQuantityInc(quantityInc);
                rowAdjusted.setQuantityDec(quantityDec);
                rowAdjusted.setTotalOriginalCy(row.getTotalCy_r());
                rowAdjusted.setTotalIncCy(totalIncCy);
                rowAdjusted.setTotalDecCy(totalDecCy);

                // Get aswell its available stock moves:

                if (row.isInventoriable()) {
                    rowAdjusted.setInventoriable(true);

                    if (category == DModSysConsts.TS_DPS_CT_PUR) {
                        sql = "SUM(s.mov_in - s.mov_out) AS f_stk ";
                    }
                    else {
                        sql = "SUM(s.mov_out - s.mov_in) AS f_stk ";
                    }

                    sql = "SELECT s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.snr, s.imp_dec, s.imp_dec_dt_n, l.dt_exp_n, l.lot, " + sql +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " AS l ON s.id_itm = l.id_itm AND s.id_unt = l.id_unt AND s.id_lot = l.id_lot " +
                            "WHERE s.b_del = 0 AND s.id_yer = " + year + " AND s.fk_dps_inv_dps_n = " + dpsKey[0] + " AND s.fk_dps_inv_row_n = " + resultSetDps.getInt(1) + " " +
                            (bkkNumberKey_n == null ? "" : "AND NOT (s.fk_bkk_yer_n = " + bkkNumberKey_n[0] + " AND s.fk_bkk_num_n = " + bkkNumberKey_n[1] + ") ") +
                            "GROUP BY s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.snr, s.imp_dec, s.imp_dec_dt_n, l.dt_exp_n, l.lot " +
                            "HAVING f_stk <> 0 " +
                            "ORDER BY s.snr, s.imp_dec, s.imp_dec_dt_n, l.dt_exp_n, l.lot, s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah ";
                    resultSetAdj = statementAdj.executeQuery(sql);
                    while (resultSetAdj.next()) {
                        rowAdjusted.getStockMovesAvailable().add(
                                new DTrnStockMove(new int[] {
                            resultSetAdj.getInt(1), resultSetAdj.getInt(2), resultSetAdj.getInt(3),
                            resultSetAdj.getInt(4), resultSetAdj.getInt(5), resultSetAdj.getInt(6) },
                            resultSetAdj.getDouble("f_stk"),
                            resultSetAdj.getString("s.snr"),
                            resultSetAdj.getString("s.imp_dec"),
                            resultSetAdj.getDate("s.imp_dec_dt_n"),
                            resultSetAdj.getString("l.lot"),
                            resultSetAdj.getDate("l.dt_exp_n")));
                    }
                }

                rows.add(rowAdjusted);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }
        catch (Exception e) {
            DLibUtils.showException(DTrnUtils.class.getName(), e);
        }

        return rows;
    }

    /**
     * @param dpsCategory Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @return Constants defined in DModSysConsts (BS_BPR_CL_...).
     */
    public static int getBizPartnerClassByDpsCategory(final int dpsCategory) {
        int bprClass = DLibConsts.UNDEFINED;

        switch (dpsCategory) {
            case DModSysConsts.TS_DPS_CT_PUR:
                bprClass = DModSysConsts.BS_BPR_CL_VEN;
                break;
            case DModSysConsts.TS_DPS_CT_SAL:
                bprClass = DModSysConsts.BS_BPR_CL_CUS;
                break;
            default:
        }

        return bprClass;
    }

    /**
     * @param subtype Constants defined in DModSysConsts (CS_MOD_...).
     * @return Constants defined in DModSysConsts (BS_BPR_CL_...).
     */
    public static int getBizPartnerClassByModuleSubtype(final int subtype) {
        int bprClass = DLibConsts.UNDEFINED;

        switch (subtype) {
            case DModSysConsts.CS_MOD_PUR:
                bprClass = DModSysConsts.BS_BPR_CL_VEN;
                break;
            case DModSysConsts.CS_MOD_SAL:
                bprClass = DModSysConsts.BS_BPR_CL_CUS;
                break;
            default:
        }

        return bprClass;
    }

    /**
     * @param subtype Constants defined in DModSysConsts (CS_MOD_...).
     * @return Constants defined in DModSysConsts (TS_DPS_CT_...).
     */
    public static int getDpsCategoryByModuleSubtype(final int subtype) {
        int dpsCategory = DLibConsts.UNDEFINED;

        switch (subtype) {
            case DModSysConsts.CS_MOD_PUR:
                dpsCategory = DModSysConsts.TS_DPS_CT_PUR;
                break;
            case DModSysConsts.CS_MOD_SAL:
                dpsCategory = DModSysConsts.TS_DPS_CT_SAL;
                break;
            default:
        }

        return dpsCategory;
    }

    /**
     * @param subtype Constants defined in DModSysConsts (CS_MOD_...).
     * @return Constants defined in DModSysConsts (TS_DPS_CT_...).
     */
    public static int getDpsCategoryByBizPartnerClass(final int bprClass) {
        int dpsCategory = DLibConsts.UNDEFINED;

        switch (bprClass) {
            case DModSysConsts.BS_BPR_CL_VEN:
                dpsCategory = DModSysConsts.TS_DPS_CT_PUR;
                break;
            case DModSysConsts.BS_BPR_CL_CUS:
                dpsCategory = DModSysConsts.TS_DPS_CT_SAL;
                break;
            default:
        }

        return dpsCategory;
    }

    /**
     * @param subtype Constants defined in DModSysConsts (TS_IOG_CT_...).
     * @return Constants defined in DModSysConsts (TS_DPS_CT_...).
     */
    public static int getDpsCategoryByIogCategory(final int iogCategory) {
        int dpsCategory = DLibConsts.UNDEFINED;

        switch (iogCategory) {
            case DModSysConsts.TS_IOG_CT_IN:
                dpsCategory = DModSysConsts.TS_DPS_CT_PUR;
                break;
            case DModSysConsts.TS_IOG_CT_OUT:
                dpsCategory = DModSysConsts.TS_DPS_CT_SAL;
                break;
            default:
        }

        return dpsCategory;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_DPS_...).
     * @param dpsCategory Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @return Constants defined in DModSysConsts (TS_DPS_CL_...).
     */
    public static int[] getDpsClassByDpsXType(final int xType, final int dpsCategory) {
        int[] key = null;

        switch (xType) {
            case DModConsts.TX_DPS_ORD:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ORD : DModSysConsts.TS_DPS_CL_SAL_ORD;
                break;
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_DOC : DModSysConsts.TS_DPS_CL_SAL_DOC;
                break;
            case DModConsts.TX_DPS_ADJ_INC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ADJ_INC : DModSysConsts.TS_DPS_CL_SAL_ADJ_INC;
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC : DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC;
                break;
            default:
        }

        return key;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_MY_DPS_...).
     * @param dpsCategory Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @return Constants defined in DModSysConsts (TS_DPS_CL_...).
     */
    public static int[] getDpsClassByMyDpsXType(final int myXType, final int dpsCategory) {
        int[] key = null;

        switch (myXType) {
            case DModConsts.TX_MY_DPS_ORD:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ORD : DModSysConsts.TS_DPS_CL_SAL_ORD;
                break;
            case DModConsts.TX_MY_DPS_DOC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_DOC : DModSysConsts.TS_DPS_CL_SAL_DOC;
                break;
            case DModConsts.TX_MY_DPS_ADJ_INC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ADJ_INC : DModSysConsts.TS_DPS_CL_SAL_ADJ_INC;
                break;
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC : DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC;
                break;
            default:
        }

        return key;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_DPS_...).
     * @param dpsCategory Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @return Constants defined in DModSysConsts (TS_DPS_TP_...).
     */
    public static int[] getDpsTypeByDpsXType(final int xType, final int dpsCategory) {
        int[] key = null;

        switch (xType) {
            case DModConsts.TX_DPS_ORD:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_ORD_ORD : DModSysConsts.TS_DPS_TP_SAL_ORD_ORD;
                break;
            case DModConsts.TX_DPS_DOC_INV:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_DOC_INV : DModSysConsts.TS_DPS_TP_SAL_DOC_INV;
                break;
            case DModConsts.TX_DPS_DOC_NOT:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_DOC_NOT : DModSysConsts.TS_DPS_TP_SAL_DOC_NOT;
                break;
            case DModConsts.TX_DPS_DOC_TIC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_DOC_TIC : DModSysConsts.TS_DPS_TP_SAL_DOC_TIC;
                break;
            case DModConsts.TX_DPS_ADJ_INC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_ADJ_INC_DN : DModSysConsts.TS_DPS_TP_SAL_ADJ_INC_DN;
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
                key = dpsCategory == DModSysConsts.TS_DPS_CT_PUR ? DModSysConsts.TS_DPS_TP_PUR_ADJ_DEC_CN : DModSysConsts.TS_DPS_TP_SAL_ADJ_DEC_CN;
                break;
            default:
        }

        return key;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_ADJ_...).
     * @param category Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @return Constants defined in DModSysConsts (TS_ADJ_CL_...).
     */
    public static int[] getAdjustmentClassByAdjustmentXType(final int xType) {
        int[] key = null;

        switch (xType) {
            case DModConsts.TX_ADJ_INC_INC:
                key = DModSysConsts.TS_ADJ_CL_INC_INC;
                break;
            case DModConsts.TX_ADJ_INC_ADD:
                key = DModSysConsts.TS_ADJ_CL_INC_ADD;
                break;
            case DModConsts.TX_ADJ_DEC_DIS:
                key = DModSysConsts.TS_ADJ_CL_DEC_DIS;
                break;
            case DModConsts.TX_ADJ_DEC_RET:
                key = DModSysConsts.TS_ADJ_CL_DEC_RET;
                break;
            default:
        }

        return key;
    }

    /**
     * @param dpsTypeKey Constants defined in DModSysConsts (TS_DPS_TP_...).
     * @return Constants defined in DModConsts (TX_DPS_...).
     */
    public static int getDpsXTypeByDpsType(final int[] dpsTypeKey) {
        int type = DLibConsts.UNDEFINED;

        if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_ORD_ORD, DModSysConsts.TS_DPS_TP_SAL_ORD_ORD })) {
            type = DModConsts.TX_DPS_ORD;
        }
        else if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_DOC_INV, DModSysConsts.TS_DPS_TP_SAL_DOC_INV })) {
            type = DModConsts.TX_DPS_DOC_INV;
        }
        else if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_DOC_NOT, DModSysConsts.TS_DPS_TP_SAL_DOC_NOT })) {
            type = DModConsts.TX_DPS_DOC_NOT;
        }
        else if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_DOC_TIC, DModSysConsts.TS_DPS_TP_SAL_DOC_TIC })) {
            type = DModConsts.TX_DPS_DOC_TIC;
        }
        else if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_ADJ_INC_DN, DModSysConsts.TS_DPS_TP_SAL_ADJ_INC_DN })) {
            type = DModConsts.TX_DPS_ADJ_INC;
        }
        else if (DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_PUR_ADJ_DEC_CN, DModSysConsts.TS_DPS_TP_SAL_ADJ_DEC_CN })) {
            type = DModConsts.TX_DPS_ADJ_DEC;
        }

        return type;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_ADJ_...).
     * @return Constants defined in DModConsts (TX_DPS_...).
     */
    public static int getDpsXTypeByAdjustmentXType(final int xType) {
        int type = DLibConsts.UNDEFINED;

        switch (xType) {
            case DModConsts.TX_ADJ_INC_INC:
            case DModConsts.TX_ADJ_INC_ADD:
                type = DModConsts.TX_DPS_ADJ_INC;
                break;
            case DModConsts.TX_ADJ_DEC_DIS:
            case DModConsts.TX_ADJ_DEC_RET:
                type = DModConsts.TX_DPS_ADJ_DEC;
                break;
            default:
        }

        return type;
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     * @return Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static int[] getIogTypeByDpsClass(final int[] dpsClassKey) {
        int[] key = null;

        if (DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_DOC, DModSysConsts.TS_DPS_CL_PUR_ADJ_INC } )) {
            key = DModSysConsts.TS_IOG_TP_IN_PUR_PUR;
        }
        else if (DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC } )) {
            key = DModSysConsts.TS_IOG_TP_IN_SAL_SAL;
        }
        else if (DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC } )) {
            key = DModSysConsts.TS_IOG_TP_OUT_PUR_PUR;
        }
        else if (DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_SAL_DOC, DModSysConsts.TS_DPS_CL_SAL_ADJ_INC } )) {
            key = DModSysConsts.TS_IOG_TP_OUT_SAL_SAL;
        }

        return key;
    }

    /**
     * @param xType Constants defined in DModConsts (TX_IOG_...).
     * @param dpsCategory Constants defined in DModSysConsts (TS_IOG_CT_...).
     * @return Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static int[] getIogTypeByIogXType(final int xType, final int type, final int iogCategory) {
        int[] key = null;

        switch (xType) {
            case DModConsts.TX_IOG_PUR:
                break;
            case DModConsts.TX_IOG_SAL:
                break;
            case DModConsts.TX_IOG_EXT:
                if (iogCategory == DModSysConsts.TS_IOG_CT_IN) {
                    if (type == DModSysConsts.TS_IOG_TP_IN_EXT_ADJ[2]) {
                        key = DModSysConsts.TS_IOG_TP_IN_EXT_ADJ;
                    }
                    else if (type == DModSysConsts.TS_IOG_TP_IN_EXT_INV[2]) {
                        key = DModSysConsts.TS_IOG_TP_IN_EXT_INV;
                    }
                }
                else {
                    if (type == DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ[2]) {
                        key = DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ;
                    }
                    else if (type == DModSysConsts.TS_IOG_TP_OUT_EXT_INV[2]) {
                        key = DModSysConsts.TS_IOG_TP_OUT_EXT_INV;
                    }
                }
                break;
            case DModConsts.TX_IOG_INT:
                break;
            case DModConsts.TX_IOG_MFG:
                break;
            default:
        }

        return key;
    }

    public static boolean isDpsForPurchase(final DDbDps dps) {
        return isDpsClassForPurchase(dps.getDpsClassKey());
    }

    public static boolean isDpsForSale(final DDbDps dps) {
        return isDpsClassForSale(dps.getDpsClassKey());
    }

    public static boolean isDpsOrder(final DDbDps dps) {
        return isDpsClassOrder(dps.getDpsClassKey());
    }

    public static boolean isDpsDocument(final DDbDps dps) {
        return isDpsClassDocument(dps.getDpsClassKey());
    }

    public static boolean isDpsAdjustment(final DDbDps dps) {
        return isDpsClassAdjustment(dps.getDpsClassKey());
    }

    public static boolean isDpsAdjustmentInc(final DDbDps dps) {
        return isDpsClassAdjustmentInc(dps.getDpsClassKey());
    }

    public static boolean isDpsAdjustmentDec(final DDbDps dps) {
        return isDpsClassAdjustmentDec(dps.getDpsClassKey());
    }

    public static boolean isDpsClassForPurchase(final int[] dpsClassKey) {
        return dpsClassKey[0] == DModSysConsts.TS_DPS_CT_PUR;
    }

    public static boolean isDpsClassForSale(final int[] dpsClassKey) {
        return dpsClassKey[0] == DModSysConsts.TS_DPS_CT_SAL;
    }

    public static boolean isDpsClassForDfr(final int[] dpsClassKey) {
        return isDpsClassForSale(dpsClassKey) && (isDpsClassDocument(dpsClassKey) || isDpsClassAdjustment(dpsClassKey));
    }

    public static boolean isDpsTypeForDfr(final int[] dpsTypeKey) {
        return DLibUtils.belongsTo(dpsTypeKey, new int[][] { DModSysConsts.TS_DPS_TP_SAL_DOC_INV, DModSysConsts.TS_DPS_TP_SAL_ADJ_INC_DN, DModSysConsts.TS_DPS_TP_SAL_ADJ_DEC_CN });
    }

    public static boolean isDpsClassOrder(final int[] dpsClassKey) {
        return DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ORD, DModSysConsts.TS_DPS_CL_SAL_ORD });
    }

    public static boolean isDpsClassDocument(final int[] dpsClassKey) {
        return DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_DOC, DModSysConsts.TS_DPS_CL_SAL_DOC });
    }

    public static boolean isDpsClassAdjustment(final int[] dpsClassKey) {
        return isDpsClassAdjustmentInc(dpsClassKey) || isDpsClassAdjustmentDec(dpsClassKey);
    }

    public static boolean isDpsClassAdjustmentInc(final int[] dpsClassKey) {
        return DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ADJ_INC, DModSysConsts.TS_DPS_CL_SAL_ADJ_INC });
    }

    public static boolean isDpsClassAdjustmentDec(final int[] dpsClassKey) {
        return DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC, DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC });
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     */
    public static boolean isDpsClassForStockIn(final int[] dpsClassKey) {
        return isDpsClassForStockIn(dpsClassKey, true);
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     * @param dpsActive Indicates if DPS is active.
     */
    public static boolean isDpsClassForStockIn(final int[] dpsClassKey, final boolean dpsActive) {
        boolean b = false;

        if (dpsActive) {
            b = DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ORD, DModSysConsts.TS_DPS_CL_PUR_DOC, DModSysConsts.TS_DPS_CL_PUR_ADJ_INC, DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC });
        }
        else {
            b = DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_SAL_ORD, DModSysConsts.TS_DPS_CL_SAL_DOC, DModSysConsts.TS_DPS_CL_SAL_ADJ_INC, DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC });
        }

        return b;
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     */
    public static boolean isDpsClassForStockOut(final int[] dpsClassKey) {
        return isDpsClassForStockOut(dpsClassKey, true);
    }

    /**
     * @param dpsClassKey Constants defined in DModSysConsts (TS_DPS_CL_...).
     * @param dpsActive Indicates if DPS is active.
     */
    public static boolean isDpsClassForStockOut(final int[] dpsClassKey, final boolean dpsActive) {
        boolean b = false;

        if (dpsActive) {
            b = DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_SAL_ORD, DModSysConsts.TS_DPS_CL_SAL_DOC, DModSysConsts.TS_DPS_CL_SAL_ADJ_INC, DModSysConsts.TS_DPS_CL_PUR_ADJ_DEC });
        }
        else {
            b = DLibUtils.belongsTo(dpsClassKey, new int[][] { DModSysConsts.TS_DPS_CL_PUR_ORD, DModSysConsts.TS_DPS_CL_PUR_DOC, DModSysConsts.TS_DPS_CL_PUR_ADJ_INC, DModSysConsts.TS_DPS_CL_SAL_ADJ_DEC });
        }

        return b;
    }

    public static boolean isDpsForStockIn(final DDbDps dps, final boolean dpsActive) {
        return isDpsClassForStockIn(dps.getDpsClassKey(), dpsActive);
    }

    public static boolean isDpsForStockOut(final DDbDps dps, final boolean dpsActive) {
        return isDpsClassForStockOut(dps.getDpsClassKey(), dpsActive);
    }

    /**
     * @param adjClassKey Constans defined in DModSysConsts (TS_ADJ_CL_...).
     */
    public static boolean isAdjustmentForMoney(final int[] adjClassKey) {
        return DLibUtils.belongsTo(adjClassKey, new int[][] { DModSysConsts.TS_ADJ_CL_INC_INC, DModSysConsts.TS_ADJ_CL_DEC_DIS });
    }

    /**
     * @param adjClassKey Constans defined in DModSysConsts (TS_ADJ_CL_...).
     */
    public static boolean isAdjustmentForStock(final int[] adjClassKey) {
        return DLibUtils.belongsTo(adjClassKey, new int[][] { DModSysConsts.TS_ADJ_CL_INC_ADD, DModSysConsts.TS_ADJ_CL_DEC_RET });
    }

    public static boolean isIogForStockIn(final DDbIog iog) {
        return iog.getFkIogCategoryId() == DModSysConsts.TS_IOG_CT_IN;
    }

    public static boolean isIogForStockOut(final DDbIog iog) {
        return iog.getFkIogCategoryId() == DModSysConsts.TS_IOG_CT_OUT;
    }

    /**
     * @param iogTypeKey Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static boolean isIogTypeForTransfer(final int[] iogTypeKey) {
        return DLibUtils.belongsTo(iogTypeKey, new int[][] { DModSysConsts.TS_IOG_TP_OUT_INT_TRA, DModSysConsts.TS_IOG_TP_IN_INT_TRA });
    }

    /**
     * @param iogTypeKey Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static boolean isIogTypeForConversion(final int[] iogTypeKey) {
        return DLibUtils.belongsTo(iogTypeKey, new int[][] { DModSysConsts.TS_IOG_TP_OUT_INT_CNV, DModSysConsts.TS_IOG_TP_IN_INT_CNV });
    }

    /**
     * @param iogTypeKey Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static boolean isIogTypeForSibling(final int[] iogTypeKey) {
        return isIogTypeForTransfer(iogTypeKey) || isIogTypeForConversion(iogTypeKey);
    }

    /**
     * @param iogTypeKey Constants defined in DModSysConsts (TS_IOG_TP_...).
     */
    public static int[] getIogTypeForSibling(final int[] iogTypeKey) {
        int[] key = null;

        if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_INT_TRA)) {
            key = DModSysConsts.TS_IOG_TP_IN_INT_TRA;
        }
        else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_INT_CNV)) {
            key = DModSysConsts.TS_IOG_TP_IN_INT_CNV;
        }
        else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_INT_TRA)) {
            //key = DModSysConsts.TS_IOG_TP_OUT_INT_TRA;
            throw new UnsupportedOperationException("Operación no soportada.");
        }
        else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_INT_CNV)) {
            //key = DModSysConsts.TS_IOG_TP_OUT_INT_CNV;
            throw new UnsupportedOperationException("Operación no soportada.");
        }

        return key;
    }

    /**
     * @param xType Constants defined in DModSysConsts (TX_DPS_...).
     */
    public static boolean isXTypeForDps(final int xType) {
        return DLibUtils.belongsTo(xType, new int[] {
            DModConsts.TX_DPS_ORD,
            DModConsts.TX_DPS_DOC_INV, DModConsts.TX_DPS_DOC_NOT, DModConsts.TX_DPS_DOC_TIC,
            DModConsts.TX_DPS_ADJ_INC, DModConsts.TX_DPS_ADJ_DEC });
    }

    /**
     * @param xType Constants defined in DModSysConsts (TX_IOG_...).
     */
    public static boolean isXTypeForIog(final int xType) {
        return DLibUtils.belongsTo(xType, new int[] {
            DModConsts.TX_IOG_PUR, DModConsts.TX_IOG_SAL,
            DModConsts.TX_IOG_EXT, DModConsts.TX_IOG_INT,
            DModConsts.TX_IOG_MFG });
    }
    
    /** 
     * Get DPS by its UUID.
     * @param session
     * @param uuid
     * @return
     * @throws Exception 
     */
    public static DDbDps getDpsByUuid(final DGuiSession session, final String uuid) throws Exception {
        DDbDps dps = null;
        
        String sql = "SELECT fk_dps_n FROM " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " WHERE uid = '" + uuid + "';";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            dps = (DDbDps) session.readRegistry(DModConsts.T_DPS, new int[] { resultSet.getInt(1) });
        }
        
        return dps;
    }
    
    /**
     * Get DPS paymentes count. Bookkeepig number can be excluded if one provided.
     * @param session
     * @param bizPartnerClass
     * @param dpsId
     * @param bkkNumKey
     * @return 
     * @throws java.lang.Exception 
     */
    public static int getDpsPaymentsCount(final DGuiSession session, final int bizPartnerClass, final int dpsId, final int[] bkkNumKey) throws Exception {
        int count = 0;
        int[] moveType = DFinUtils.getSysMoveTypeForCashPaymentInForBizPartnerClass(bizPartnerClass);
        String sql = "SELECT COUNT(*) "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " "
                + "WHERE fk_dps_inv_n = " + dpsId + " AND NOT b_del AND "
                + "fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass) + " AND "
                + "fk_sys_mov_cl = " + moveType[0] + " AND fk_sys_mov_tp = " + moveType[1] + " "
                + (bkkNumKey == null ? "" : "AND NOT(fk_bkk_yer_n = " + bkkNumKey[0] + " AND fk_bkk_num_n = " + bkkNumKey[1] + ") ");
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
                
        return count;
    }
    
    /**
     * Populate SDbDpsSending instance with info from array of DBprEmail objects.
     * @param dpsSending
     * @param bprEmails
     */
    public static void populateEmails(final DDbDpsSending dpsSending, final ArrayList<DBprEmail> bprEmails) {
        for (int index = 0; index < bprEmails.size() && index < 3; index++) {
            DBprEmail bprEmail = bprEmails.get(index);
            
            switch (index + 1) {
                case 1:
                    dpsSending.setContact1(bprEmail.Contact);
                    dpsSending.setEmail1(bprEmail.Email);
                    break;
                case 2:
                    dpsSending.setContact2(bprEmail.Contact);
                    dpsSending.setEmail2(bprEmail.Email);
                    break;
                case 3:
                    dpsSending.setContact3(bprEmail.Contact);
                    dpsSending.setEmail3(bprEmail.Email);
                    break;
                default:
            }
        }
    }
}
