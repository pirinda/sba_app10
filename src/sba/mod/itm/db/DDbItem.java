/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.itm.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbItem extends DDbRegistryUser {

    protected int mnPkItemId;
    protected String msCode;
    protected String msName;
    protected String msItemCode;
    protected String msItemName;
    protected String msItemPresentation;
    protected String msIngredient;
    protected double mdMeasurementLength;
    protected double mdMeasurementSurface;
    protected double mdMeasurementVolume;
    protected double mdMeasurementMass;
    protected double mdMeasurementTime;
    protected double mdWeightGross;
    protected double mdWeightDelivery;
    protected double mdUnitsVirtual;
    protected double mdUnitsContained;
    protected double mdUnitsPackage;
    protected double mdPriceSrp;
    protected double mdPrice1;
    protected double mdPrice2;
    protected double mdPrice3;
    protected double mdPrice4;
    protected double mdPrice5;
    protected boolean mbBulk;
    protected boolean mbInventoriable;
    protected boolean mbLotApplying;
    protected boolean mbSerialNumberApplying;
    protected boolean mbFreeOfPrice;
    protected boolean mbFreeOfDiscount;
    protected boolean mbFreeOfCommission;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItemGenusId;
    protected int mnFkItemLineId_n;
    protected int mnFkBrandId;
    protected int mnFkManufacturerId;
    protected int mnFkComponentId;
    protected int mnFkDepartmentId;
    protected int mnFkUnitId;
    protected int mnFkUnitVirtualId;
    protected int mnFkUnitContainedId;
    protected int mnFkItemPackageId_n;
    protected int mnFkTaxGroupId_n;
    protected int mnFkTaxRegimeId;
    protected int mnFkAbpItemId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected int mnXtaItemTypeIndex;
    protected int mnXtaItemClassIndex;
    protected int mnXtaItemCategoryIndex;

    protected DDbItemLine moParentLine;
    protected DDbItemGenus moParentGenus;

    protected Vector<DDbItemBarcode> mvChildBarcodes;

    public DDbItem() {
        super(DModConsts.IU_ITM);
        mvChildBarcodes = new Vector<DDbItemBarcode>();
        initRegistry();
    }

    /**
     * @param itemLinkType Constants defined in <code>DModSysConsts</code> (IS_LNK_ITM_TP_...).
     */
    public int getItemLinkId(final int itemLinkType) {
        int link = DLibConsts.UNDEFINED;

        switch (itemLinkType) {
            case DModSysConsts.IS_LNK_ITM_TP_ITM:
                link = mnPkItemId;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_MFR:
                link = mnFkManufacturerId;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_BRD:
                link = mnFkBrandId;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_LIN:
                link = mnFkItemLineId_n;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_GEN:
                link = mnFkItemGenusId;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_FAM:
                link = moParentGenus.getFkItemFamilyId();
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_TP:
                link = mnXtaItemTypeIndex;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CL:
                link = mnXtaItemClassIndex;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CT:
                link = mnXtaItemCategoryIndex;
                break;
            default:
                link = DLibConsts.UNDEFINED;
        }

        return link;
    }

    /**
     * @param session User GUI session.
     * @param itemLinkType Constants defined in <code>DModSysConsts</code> (IS_LNK_ITM_TP_...).
     */
    public String getItemLinkName(final DGuiSession session, final int itemLinkType) {
        String name = "";

        switch (itemLinkType) {
            case DModSysConsts.IS_LNK_ITM_TP_ITM:
                name = msName;
                break;
            case DModSysConsts.IS_LNK_ITM_TP_MFR:
                name = (String) session.readField(DModConsts.IU_MFR, new int[] { mnFkManufacturerId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_BRD:
                name = (String) session.readField(DModConsts.IU_BRD, new int[] { mnFkBrandId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_LIN:
                name = (String) session.readField(DModConsts.IU_LIN, new int[] { mnFkItemLineId_n }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_GEN:
                name = (String) session.readField(DModConsts.IU_GEN, new int[] { mnFkItemGenusId }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_FAM:
                name = (String) session.readField(DModConsts.IU_FAM, new int[] { moParentGenus.getFkItemFamilyId() }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_TP:
                name = (String) session.readField(DModConsts.IX_ITM_TP_BY_IDX, new int[] { mnXtaItemTypeIndex }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CL:
                name = (String) session.readField(DModConsts.IX_ITM_CL_BY_IDX, new int[] { mnXtaItemClassIndex }, DDbRegistry.FIELD_NAME);
                break;
            case DModSysConsts.IS_LNK_ITM_TP_ITM_CT:
                name = (String) session.readField(DModConsts.IS_ITM_CT, new int[] { mnXtaItemCategoryIndex }, DDbRegistry.FIELD_NAME);
                break;
            default:
        }

        return name;
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setItemName(String s) { msItemName = s; }
    public void setItemPresentation(String s) { msItemPresentation = s; }
    public void setIngredient(String s) { msIngredient = s; }
    public void setMeasurementLength(double d) { mdMeasurementLength = d; }
    public void setMeasurementSurface(double d) { mdMeasurementSurface = d; }
    public void setMeasurementVolume(double d) { mdMeasurementVolume = d; }
    public void setMeasurementMass(double d) { mdMeasurementMass = d; }
    public void setMeasurementTime(double d) { mdMeasurementTime = d; }
    public void setWeightGross(double d) { mdWeightGross = d; }
    public void setWeightDelivery(double d) { mdWeightDelivery = d; }
    public void setUnitsVirtual(double d) { mdUnitsVirtual = d; }
    public void setUnitsContained(double d) { mdUnitsContained = d; }
    public void setUnitsPackage(double d) { mdUnitsPackage = d; }
    public void setPriceSrp(double d) { mdPriceSrp = d; }
    public void setPrice1(double d) { mdPrice1 = d; }
    public void setPrice2(double d) { mdPrice2 = d; }
    public void setPrice3(double d) { mdPrice3 = d; }
    public void setPrice4(double d) { mdPrice4 = d; }
    public void setPrice5(double d) { mdPrice5 = d; }
    public void setBulk(boolean b) { mbBulk = b; }
    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setLotApplying(boolean b) { mbLotApplying = b; }
    public void setSerialNumberApplying(boolean b) { mbSerialNumberApplying = b; }
    public void setFreeOfPrice(boolean b) { mbFreeOfPrice = b; }
    public void setFreeOfDiscount(boolean b) { mbFreeOfDiscount = b; }
    public void setFreeOfCommission(boolean b) { mbFreeOfCommission = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemGenusId(int n) { mnFkItemGenusId = n; }
    public void setFkItemLineId_n(int n) { mnFkItemLineId_n = n; }
    public void setFkBrandId(int n) { mnFkBrandId = n; }
    public void setFkManufacturerId(int n) { mnFkManufacturerId = n; }
    public void setFkComponentId(int n) { mnFkComponentId = n; }
    public void setFkDepartmentId(int n) { mnFkDepartmentId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUnitVirtualId(int n) { mnFkUnitVirtualId = n; }
    public void setFkUnitContainedId(int n) { mnFkUnitContainedId = n; }
    public void setFkItemPackageId_n(int n) { mnFkItemPackageId_n = n; }
    public void setFkTaxGroupId_n(int n) { mnFkTaxGroupId_n = n; }
    public void setFkTaxRegimeId(int n) { mnFkTaxRegimeId = n; }
    public void setFkAbpItemId_n(int n) { mnFkAbpItemId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getItemCode() { return msItemCode; }
    public String getItemName() { return msItemName; }
    public String getItemPresentation() { return msItemPresentation; }
    public String getIngredient() { return msIngredient; }
    public double getMeasurementLength() { return mdMeasurementLength; }
    public double getMeasurementSurface() { return mdMeasurementSurface; }
    public double getMeasurementVolume() { return mdMeasurementVolume; }
    public double getMeasurementMass() { return mdMeasurementMass; }
    public double getMeasurementTime() { return mdMeasurementTime; }
    public double getWeightGross() { return mdWeightGross; }
    public double getWeightDelivery() { return mdWeightDelivery; }
    public double getUnitsVirtual() { return mdUnitsVirtual; }
    public double getUnitsContained() { return mdUnitsContained; }
    public double getUnitsPackage() { return mdUnitsPackage; }
    public double getPriceSrp() { return mdPriceSrp; }
    public double getPrice1() { return mdPrice1; }
    public double getPrice2() { return mdPrice2; }
    public double getPrice3() { return mdPrice3; }
    public double getPrice4() { return mdPrice4; }
    public double getPrice5() { return mdPrice5; }
    public boolean isBulk() { return mbBulk; }
    public boolean isInventoriable() { return mbInventoriable; }
    public boolean isLotApplying() { return mbLotApplying; }
    public boolean isSerialNumberApplying() { return mbSerialNumberApplying; }
    public boolean isFreeOfPrice() { return mbFreeOfPrice; }
    public boolean isFreeOfDiscount() { return mbFreeOfDiscount; }
    public boolean isFreeOfCommission() { return mbFreeOfCommission; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemGenusId() { return mnFkItemGenusId; }
    public int getFkItemLineId_n() { return mnFkItemLineId_n; }
    public int getFkBrandId() { return mnFkBrandId; }
    public int getFkManufacturerId() { return mnFkManufacturerId; }
    public int getFkComponentId() { return mnFkComponentId; }
    public int getFkDepartmentId() { return mnFkDepartmentId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUnitVirtualId() { return mnFkUnitVirtualId; }
    public int getFkUnitContainedId() { return mnFkUnitContainedId; }
    public int getFkItemPackageId_n() { return mnFkItemPackageId_n; }
    public int getFkTaxGroupId_n() { return mnFkTaxGroupId_n; }
    public int getFkTaxRegimeId() { return mnFkTaxRegimeId; }
    public int getFkAbpItemId_n() { return mnFkAbpItemId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int getXtaItemTypeIndex() { return mnXtaItemTypeIndex; }
    public int getXtaItemClassIndex() { return mnXtaItemClassIndex; }
    public int getXtaItemCategoryIndex() { return mnXtaItemCategoryIndex; }

    public DDbItemLine getParentLine() { return moParentLine; }
    public DDbItemGenus getParentGenus() { return moParentGenus; }

    public Vector<DDbItemBarcode> getChildBarcodes() { return mvChildBarcodes; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        msCode = "";
        msName = "";
        msItemCode = "";
        msItemName = "";
        msItemPresentation = "";
        msIngredient = "";
        mdMeasurementLength = 0;
        mdMeasurementSurface = 0;
        mdMeasurementVolume = 0;
        mdMeasurementMass = 0;
        mdMeasurementTime = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;
        mdUnitsVirtual = 0;
        mdUnitsContained = 0;
        mdUnitsPackage = 0;
        mdPriceSrp = 0;
        mdPrice1 = 0;
        mdPrice2 = 0;
        mdPrice3 = 0;
        mdPrice4 = 0;
        mdPrice5 = 0;
        mbBulk = false;
        mbInventoriable = false;
        mbLotApplying = false;
        mbSerialNumberApplying = false;
        mbFreeOfPrice = false;
        mbFreeOfDiscount = false;
        mbFreeOfCommission = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemGenusId = 0;
        mnFkItemLineId_n = 0;
        mnFkBrandId = 0;
        mnFkManufacturerId = 0;
        mnFkComponentId = 0;
        mnFkDepartmentId = 0;
        mnFkUnitId = 0;
        mnFkUnitVirtualId = 0;
        mnFkUnitContainedId = 0;
        mnFkItemPackageId_n = 0;
        mnFkTaxGroupId_n = 0;
        mnFkTaxRegimeId = 0;
        mnFkAbpItemId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mnXtaItemTypeIndex = 0;
        mnXtaItemClassIndex = 0;
        mnXtaItemCategoryIndex = 0;

        moParentLine = null;
        moParentGenus = null;

        mvChildBarcodes.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_itm = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_itm = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemId = 0;

        msSql = "SELECT COALESCE(MAX(id_itm), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = DDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkItemId = resultSet.getInt("id_itm");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msItemCode = resultSet.getString("itm_code");
            msItemName = resultSet.getString("itm_name");
            msItemPresentation = resultSet.getString("itm_pres");
            msIngredient = resultSet.getString("ing");
            mdMeasurementLength = resultSet.getDouble("mst_len");
            mdMeasurementSurface = resultSet.getDouble("mst_sur");
            mdMeasurementVolume = resultSet.getDouble("mst_vol");
            mdMeasurementMass = resultSet.getDouble("mst_mas");
            mdMeasurementTime = resultSet.getDouble("mst_tme");
            mdWeightGross = resultSet.getDouble("wgt_grs");
            mdWeightDelivery = resultSet.getDouble("wgt_del");
            mdUnitsVirtual = resultSet.getDouble("unt_vir");
            mdUnitsContained = resultSet.getDouble("unt_con");
            mdUnitsPackage = resultSet.getDouble("unt_pac");
            mdPriceSrp = resultSet.getDouble("prc_srp");
            mdPrice1 = resultSet.getDouble("prc_1");
            mdPrice2 = resultSet.getDouble("prc_2");
            mdPrice3 = resultSet.getDouble("prc_3");
            mdPrice4 = resultSet.getDouble("prc_4");
            mdPrice5 = resultSet.getDouble("prc_5");
            mbBulk = resultSet.getBoolean("b_buk");
            mbInventoriable = resultSet.getBoolean("b_inv");
            mbLotApplying = resultSet.getBoolean("b_lot");
            mbSerialNumberApplying = resultSet.getBoolean("b_snr");
            mbFreeOfPrice = resultSet.getBoolean("b_fre_prc");
            mbFreeOfDiscount = resultSet.getBoolean("b_fre_dsc");
            mbFreeOfCommission = resultSet.getBoolean("b_fre_cmm");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemGenusId = resultSet.getInt("fk_gen");
            mnFkItemLineId_n = resultSet.getInt("fk_lin_n");
            mnFkBrandId = resultSet.getInt("fk_brd");
            mnFkManufacturerId = resultSet.getInt("fk_mfr");
            mnFkComponentId = resultSet.getInt("fk_cmp");
            mnFkDepartmentId = resultSet.getInt("fk_dep");
            mnFkUnitId = resultSet.getInt("fk_unt");
            mnFkUnitVirtualId = resultSet.getInt("fk_unt_vir");
            mnFkUnitContainedId = resultSet.getInt("fk_unt_con");
            mnFkItemPackageId_n = resultSet.getInt("fk_itm_pac_n");
            mnFkTaxGroupId_n = resultSet.getInt("fk_tax_grp_n");
            mnFkTaxRegimeId = resultSet.getInt("fk_tax_reg");
            mnFkAbpItemId_n = resultSet.getInt("fk_abp_itm_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell parent registries:

            if (mnFkItemLineId_n != DLibConsts.UNDEFINED) {
                moParentLine = new DDbItemLine();
                moParentLine.read(session, new int[] { mnFkItemLineId_n });
            }

            moParentGenus = new DDbItemGenus();
            moParentGenus.read(session, new int[] { mnFkItemGenusId });

            // Read aswell extra registries:

            msSql = "SELECT ict.id_itm_ct, icl.idx, itp.idx "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS ig "
                    + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CT) + " AS ict ON "
                    + "ict.id_itm_ct = ig.fk_itm_ct "
                    + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS icl ON "
                    + "icl.id_itm_ct = ig.fk_itm_ct AND icl.id_itm_cl = ig.fk_itm_cl "
                    + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_TP) + " AS itp ON "
                    + "itp.id_itm_ct = ig.fk_itm_ct AND itp.id_itm_cl = ig.fk_itm_cl AND itp.id_itm_tp = ig.fk_itm_tp "
                    + "WHERE ig.id_gen = " + mnFkItemGenusId + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnXtaItemCategoryIndex = resultSet.getInt(1);
                mnXtaItemClassIndex = resultSet.getInt(2);
                mnXtaItemTypeIndex = resultSet.getInt(3);
            }

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_bar FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM_BAR) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbItemBarcode child = new DDbItemBarcode();
                child.read(session, new int[] { mnPkItemId, resultSet.getInt(1) });
                mvChildBarcodes.add(child);
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkItemId + ", " +
                    "'" + (msCode.length() > 0 ? msCode : "" + mnPkItemId) + "', " +
                    "'" + msName + "', " +
                    "'" + msItemCode + "', " +
                    "'" + msItemName + "', " +
                    "'" + msItemPresentation + "', " +
                    "'" + msIngredient + "', " +
                    mdMeasurementLength + ", " +
                    mdMeasurementSurface + ", " +
                    mdMeasurementVolume + ", " +
                    mdMeasurementMass + ", " +
                    mdMeasurementTime + ", " +
                    mdWeightGross + ", " +
                    mdWeightDelivery + ", " +
                    mdUnitsVirtual + ", " +
                    mdUnitsContained + ", " +
                    mdUnitsPackage + ", " +
                    mdPriceSrp + ", " +
                    mdPrice1 + ", " +
                    mdPrice2 + ", " +
                    mdPrice3 + ", " +
                    mdPrice4 + ", " +
                    mdPrice5 + ", " +
                    (mbBulk ? 1 : 0) + ", " +
                    (mbInventoriable ? 1 : 0) + ", " +
                    (mbLotApplying ? 1 : 0) + ", " +
                    (mbSerialNumberApplying ? 1 : 0) + ", " +
                    (mbFreeOfPrice ? 1 : 0) + ", " +
                    (mbFreeOfDiscount ? 1 : 0) + ", " +
                    (mbFreeOfCommission ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItemGenusId + ", " +
                    (mnFkItemLineId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemLineId_n) + ", " +
                    mnFkBrandId + ", " +
                    mnFkManufacturerId + ", " +
                    mnFkComponentId + ", " +
                    mnFkDepartmentId + ", " +
                    mnFkUnitId + ", " +
                    mnFkUnitVirtualId + ", " +
                    mnFkUnitContainedId + ", " +
                    (mnFkItemPackageId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemPackageId_n) + ", " +
                    (mnFkTaxGroupId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxGroupId_n) + ", " +
                    mnFkTaxRegimeId + ", " + 
                    (mnFkAbpItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpItemId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_itm = " + mnPkItemId + ", " +
                    "code = '" + (msCode.length() > 0 ? msCode : "" + mnPkItemId) + "', " +
                    "name = '" + msName + "', " +
                    "itm_code = '" + msItemCode + "', " +
                    "itm_name = '" + msItemName + "', " +
                    "itm_pres = '" + msItemPresentation + "', " +
                    "ing = '" + msIngredient + "', " +
                    "mst_len = " + mdMeasurementLength + ", " +
                    "mst_sur = " + mdMeasurementSurface + ", " +
                    "mst_vol = " + mdMeasurementVolume + ", " +
                    "mst_mas = " + mdMeasurementMass + ", " +
                    "mst_tme = " + mdMeasurementTime + ", " +
                    "wgt_grs = " + mdWeightGross + ", " +
                    "wgt_del = " + mdWeightDelivery + ", " +
                    "unt_vir = " + mdUnitsVirtual + ", " +
                    "unt_con = " + mdUnitsContained + ", " +
                    "unt_pac = " + mdUnitsPackage + ", " +
                    "prc_srp = " + mdPriceSrp + ", " +
                    "prc_1 = " + mdPrice1 + ", " +
                    "prc_2 = " + mdPrice2 + ", " +
                    "prc_3 = " + mdPrice3 + ", " +
                    "prc_4 = " + mdPrice4 + ", " +
                    "prc_5 = " + mdPrice5 + ", " +
                    "b_buk = " + (mbBulk ? 1 : 0) + ", " +
                    "b_inv = " + (mbInventoriable ? 1 : 0) + ", " +
                    "b_lot = " + (mbLotApplying ? 1 : 0) + ", " +
                    "b_snr = " + (mbSerialNumberApplying ? 1 : 0) + ", " +
                    "b_fre_prc = " + (mbFreeOfPrice ? 1 : 0) + ", " +
                    "b_fre_dsc = " + (mbFreeOfDiscount ? 1 : 0) + ", " +
                    "b_fre_cmm = " + (mbFreeOfCommission ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_gen = " + mnFkItemGenusId + ", " +
                    "fk_lin_n = " + (mnFkItemLineId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemLineId_n) + ", " +
                    "fk_brd = " + mnFkBrandId + ", " +
                    "fk_mfr = " + mnFkManufacturerId + ", " +
                    "fk_cmp = " + mnFkComponentId + ", " +
                    "fk_dep = " + mnFkDepartmentId + ", " +
                    "fk_unt = " + mnFkUnitId + ", " +
                    "fk_unt_vir = " + mnFkUnitVirtualId + ", " +
                    "fk_unt_con = " + mnFkUnitContainedId + ", " +
                    "fk_itm_pac_n = " + (mnFkItemPackageId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemPackageId_n) + ", " +
                    "fk_tax_grp_n = " + (mnFkTaxGroupId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxGroupId_n) + ", " +
                    "fk_tax_reg = " + mnFkTaxRegimeId + ", " +
                    "fk_abp_itm_n = " + (mnFkAbpItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpItemId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM_BAR) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbItemBarcode child : mvChildBarcodes) {
            child.setPkItemId(mnPkItemId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbItem clone() throws CloneNotSupportedException {
        DDbItem registry = new DDbItem();

        registry.setPkItemId(this.getPkItemId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setItemCode(this.getItemCode());
        registry.setItemName(this.getItemName());
        registry.setItemPresentation(this.getItemPresentation());
        registry.setIngredient(this.getIngredient());
        registry.setMeasurementLength(this.getMeasurementLength());
        registry.setMeasurementSurface(this.getMeasurementSurface());
        registry.setMeasurementVolume(this.getMeasurementVolume());
        registry.setMeasurementMass(this.getMeasurementMass());
        registry.setMeasurementTime(this.getMeasurementTime());
        registry.setWeightGross(this.getWeightGross());
        registry.setWeightDelivery(this.getWeightDelivery());
        registry.setUnitsVirtual(this.getUnitsVirtual());
        registry.setUnitsContained(this.getUnitsContained());
        registry.setUnitsPackage(this.getUnitsPackage());
        registry.setPriceSrp(this.getPriceSrp());
        registry.setPrice1(this.getPrice1());
        registry.setPrice2(this.getPrice2());
        registry.setPrice3(this.getPrice3());
        registry.setPrice4(this.getPrice4());
        registry.setPrice5(this.getPrice5());
        registry.setBulk(this.isBulk());
        registry.setInventoriable(this.isInventoriable());
        registry.setLotApplying(this.isLotApplying());
        registry.setSerialNumberApplying(this.isSerialNumberApplying());
        registry.setFreeOfPrice(this.isFreeOfPrice());
        registry.setFreeOfDiscount(this.isFreeOfDiscount());
        registry.setFreeOfCommission(this.isFreeOfCommission());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemGenusId(this.getFkItemGenusId());
        registry.setFkItemLineId_n(this.getFkItemLineId_n());
        registry.setFkBrandId(this.getFkBrandId());
        registry.setFkManufacturerId(this.getFkManufacturerId());
        registry.setFkComponentId(this.getFkComponentId());
        registry.setFkDepartmentId(this.getFkDepartmentId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkUnitVirtualId(this.getFkUnitVirtualId());
        registry.setFkUnitContainedId(this.getFkUnitContainedId());
        registry.setFkItemPackageId_n(this.getFkItemPackageId_n());
        registry.setFkTaxGroupId_n(this.getFkTaxGroupId_n());
        registry.setFkTaxRegimeId(this.getFkTaxRegimeId());
        registry.setFkAbpItemId_n(this.getFkAbpItemId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (DDbItemBarcode child : mvChildBarcodes) {
            registry.getChildBarcodes().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbItemBarcode child : mvChildBarcodes) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public boolean canSave(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        ResultSet resultSet = null;

        if (can) {
            msSql = "SELECT COUNT(*) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " " +
                    "WHERE code = '" + msCode + "' AND " +
                    "id_itm <> " + mnPkItemId + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    can = false;
                    msQueryResult = DDbConsts.ERR_MSG_UNIQUE_CODE + " (" + msCode + ")";
                }
                else {
                    msSql = "SELECT COUNT(*) " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " " +
                            "WHERE name = '" + msName + "' AND " +
                            "id_itm <> " + mnPkItemId + " ";
                    resultSet = session.getStatement().executeQuery(msSql);
                    if (resultSet.next()) {
                        if (resultSet.getInt(1) > 0) {
                            can = false;
                            msQueryResult = DDbConsts.ERR_MSG_UNIQUE_NAME + " (" + msName + ")";
                        }
                    }
                }
            }
        }

        return can;
    }

    @Override
    public boolean canDisable(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDisable(session);

        if (can) {
            if (mbDisabled) {
                can = true;
            }
            else {
                if (DTrnUtils.getStockForItemUnit(session, session.getSystemYear(), mnPkItemId, mnFkUnitId, null) > 0) {
                    can = false;
                    msQueryResult = "\nEl ítem '" + msName + "' tiene existencias.";
                }
            }
        }

        return can;
    }

    @Override
    public boolean canDelete(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            if (mbDeleted) {
                can = true;
            }
            else {
                if (DTrnUtils.getStockForItemUnit(session, session.getSystemYear(), mnPkItemId, mnFkUnitId, null) > 0) {
                    can = false;
                    msQueryResult = "\nEl ítem '" + msName + "' tiene existencias.";
                }
            }
        }

        return can;
    }
}
