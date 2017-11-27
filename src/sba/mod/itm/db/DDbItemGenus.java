/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.itm.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbItemGenus extends DDbRegistryUser {

    public static final int LEN_SNR = 25;

    protected int mnPkItemGenusId;
    protected String msCode;
    protected String msName;
    protected int mnCodeOrderItemGenus;
    protected int mnCodeOrderItemLine;
    protected int mnCodeOrderBrand;
    protected int mnCodeOrderManufacturer;
    protected int mnCodeOrderItem;
    protected int mnNameOrderItemGenus;
    protected int mnNameOrderItemLine;
    protected int mnNameOrderBrand;
    protected int mnNameOrderManufacturer;
    protected int mnNameOrderItem;
    protected String msSerialNumberName;
    protected int mnSerialNumberLength;
    protected String msCfdItemKey;
    protected boolean mbCodeEditable;
    protected boolean mbNameEditable;
    protected boolean mbIngredientApplying;
    protected boolean mbItemLineApplying;
    protected boolean mbMeasurementLengthApplying;
    protected boolean mbMeasurementSurfaceApplying;
    protected boolean mbMeasurementVolumeApplying;
    protected boolean mbMeasurementMassApplying;
    protected boolean mbMeasurementTimeApplying;
    protected boolean mbWeightGrossApplying;
    protected boolean mbWeightDeliveryApplying;
    protected boolean mbUnitsVirtualApplying;
    protected boolean mbUnitsContainedApplying;
    protected boolean mbUnitsPackageApplying;
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
    protected int mnFkItemFamilyId;
    protected int mnFkItemCategoryId;
    protected int mnFkItemClassId;
    protected int mnFkItemTypeId;
    protected int mnFkSerialNumberTypeId;
    protected int mnFkTaxGroupId;
    protected int mnFkAbpItemId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbItemGenus() {
        super(DModConsts.IU_GEN);
        initRegistry();
    }

    public void setPkItemGenusId(int n) { mnPkItemGenusId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setCodeOrderItemGenus(int n) { mnCodeOrderItemGenus = n; }
    public void setCodeOrderItemLine(int n) { mnCodeOrderItemLine = n; }
    public void setCodeOrderBrand(int n) { mnCodeOrderBrand = n; }
    public void setCodeOrderManufacturer(int n) { mnCodeOrderManufacturer = n; }
    public void setCodeOrderItem(int n) { mnCodeOrderItem = n; }
    public void setNameOrderItemGenus(int n) { mnNameOrderItemGenus = n; }
    public void setNameOrderItemLine(int n) { mnNameOrderItemLine = n; }
    public void setNameOrderBrand(int n) { mnNameOrderBrand = n; }
    public void setNameOrderManufacturer(int n) { mnNameOrderManufacturer = n; }
    public void setNameOrderItem(int n) { mnNameOrderItem = n; }
    public void setSerialNumberName(String s) { msSerialNumberName = s; }
    public void setSerialNumberLength(int n) { mnSerialNumberLength = n; }
    public void setCfdItemKey(String s) { msCfdItemKey = s; }
    public void setCodeEditable(boolean b) { mbCodeEditable = b; }
    public void setNameEditable(boolean b) { mbNameEditable = b; }
    public void setIngredientApplying(boolean b) { mbIngredientApplying = b; }
    public void setItemLineApplying(boolean b) { mbItemLineApplying = b; }
    public void setMeasurementLengthApplying(boolean b) { mbMeasurementLengthApplying = b; }
    public void setMeasurementSurfaceApplying(boolean b) { mbMeasurementSurfaceApplying = b; }
    public void setMeasurementVolumeApplying(boolean b) { mbMeasurementVolumeApplying = b; }
    public void setMeasurementMassApplying(boolean b) { mbMeasurementMassApplying = b; }
    public void setMeasurementTimeApplying(boolean b) { mbMeasurementTimeApplying = b; }
    public void setWeightGrossApplying(boolean b) { mbWeightGrossApplying = b; }
    public void setWeightDeliveryApplying(boolean b) { mbWeightDeliveryApplying = b; }
    public void setUnitsVirtualApplying(boolean b) { mbUnitsVirtualApplying = b; }
    public void setUnitsContainedApplying(boolean b) { mbUnitsContainedApplying = b; }
    public void setUnitsPackageApplying(boolean b) { mbUnitsPackageApplying = b; }
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
    public void setFkItemFamilyId(int n) { mnFkItemFamilyId = n; }
    public void setFkItemCategoryId(int n) { mnFkItemCategoryId = n; }
    public void setFkItemClassId(int n) { mnFkItemClassId = n; }
    public void setFkItemTypeId(int n) { mnFkItemTypeId = n; }
    public void setFkSerialNumberTypeId(int n) { mnFkSerialNumberTypeId = n; }
    public void setFkTaxGroupId(int n) { mnFkTaxGroupId = n; }
    public void setFkAbpItemId(int n) { mnFkAbpItemId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemGenusId() { return mnPkItemGenusId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getCodeOrderItemGenus() { return mnCodeOrderItemGenus; }
    public int getCodeOrderItemLine() { return mnCodeOrderItemLine; }
    public int getCodeOrderBrand() { return mnCodeOrderBrand; }
    public int getCodeOrderManufacturer() { return mnCodeOrderManufacturer; }
    public int getCodeOrderItem() { return mnCodeOrderItem; }
    public int getNameOrderItemGenus() { return mnNameOrderItemGenus; }
    public int getNameOrderItemLine() { return mnNameOrderItemLine; }
    public int getNameOrderBrand() { return mnNameOrderBrand; }
    public int getNameOrderManufacturer() { return mnNameOrderManufacturer; }
    public int getNameOrderItem() { return mnNameOrderItem; }
    public String getSerialNumberName() { return msSerialNumberName; }
    public int getSerialNumberLength() { return mnSerialNumberLength; }
    public String getCfdItemKey() { return msCfdItemKey; }
    public boolean isCodeEditable() { return mbCodeEditable; }
    public boolean isNameEditable() { return mbNameEditable; }
    public boolean isIngredientApplying() { return mbIngredientApplying; }
    public boolean isItemLineApplying() { return mbItemLineApplying; }
    public boolean isMeasurementLengthApplying() { return mbMeasurementLengthApplying; }
    public boolean isMeasurementSurfaceApplying() { return mbMeasurementSurfaceApplying; }
    public boolean isMeasurementVolumeApplying() { return mbMeasurementVolumeApplying; }
    public boolean isMeasurementMassApplying() { return mbMeasurementMassApplying; }
    public boolean isMeasurementTimeApplying() { return mbMeasurementTimeApplying; }
    public boolean isWeightGrossApplying() { return mbWeightGrossApplying; }
    public boolean isWeightDeliveryApplying() { return mbWeightDeliveryApplying; }
    public boolean isUnitsVirtualApplying() { return mbUnitsVirtualApplying; }
    public boolean isUnitsContainedApplying() { return mbUnitsContainedApplying; }
    public boolean isUnitsPackageApplying() { return mbUnitsPackageApplying; }
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
    public int getFkItemFamilyId() { return mnFkItemFamilyId; }
    public int getFkItemCategoryId() { return mnFkItemCategoryId; }
    public int getFkItemClassId() { return mnFkItemClassId; }
    public int getFkItemTypeId() { return mnFkItemTypeId; }
    public int getFkSerialNumberTypeId() { return mnFkSerialNumberTypeId; }
    public int getFkTaxGroupId() { return mnFkTaxGroupId; }
    public int getFkAbpItemId() { return mnFkAbpItemId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemGenusId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemGenusId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemGenusId = 0;
        msCode = "";
        msName = "";
        mnCodeOrderItemGenus = 0;
        mnCodeOrderItemLine = 0;
        mnCodeOrderBrand = 0;
        mnCodeOrderManufacturer = 0;
        mnCodeOrderItem = 0;
        mnNameOrderItemGenus = 0;
        mnNameOrderItemLine = 0;
        mnNameOrderBrand = 0;
        mnNameOrderManufacturer = 0;
        mnNameOrderItem = 0;
        msSerialNumberName = "";
        mnSerialNumberLength = 0;
        msCfdItemKey = "";
        mbCodeEditable = false;
        mbNameEditable = false;
        mbIngredientApplying = false;
        mbItemLineApplying = false;
        mbMeasurementLengthApplying = false;
        mbMeasurementSurfaceApplying = false;
        mbMeasurementVolumeApplying = false;
        mbMeasurementMassApplying = false;
        mbMeasurementTimeApplying = false;
        mbWeightGrossApplying = false;
        mbWeightDeliveryApplying = false;
        mbUnitsVirtualApplying = false;
        mbUnitsContainedApplying = false;
        mbUnitsPackageApplying = false;
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
        mnFkItemFamilyId = 0;
        mnFkItemCategoryId = 0;
        mnFkItemClassId = 0;
        mnFkItemTypeId = 0;
        mnFkSerialNumberTypeId = 0;
        mnFkTaxGroupId = 0;
        mnFkAbpItemId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_gen = " + mnPkItemGenusId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_gen = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemGenusId = 0;

        msSql = "SELECT COALESCE(MAX(id_gen), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemGenusId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
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
            mnPkItemGenusId = resultSet.getInt("id_gen");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnCodeOrderItemGenus = resultSet.getInt("code_pos_gen");
            mnCodeOrderItemLine = resultSet.getInt("code_pos_lin");
            mnCodeOrderBrand = resultSet.getInt("code_pos_brd");
            mnCodeOrderManufacturer = resultSet.getInt("code_pos_mfr");
            mnCodeOrderItem = resultSet.getInt("code_pos_itm");
            mnNameOrderItemGenus = resultSet.getInt("name_pos_gen");
            mnNameOrderItemLine = resultSet.getInt("name_pos_lin");
            mnNameOrderBrand = resultSet.getInt("name_pos_brd");
            mnNameOrderManufacturer = resultSet.getInt("name_pos_mfr");
            mnNameOrderItem = resultSet.getInt("name_pos_itm");
            msSerialNumberName = resultSet.getString("snr_name");
            mnSerialNumberLength = resultSet.getInt("snr_len");
            msCfdItemKey = resultSet.getString("cfd_itm_key");
            mbCodeEditable = resultSet.getBoolean("b_code_edit");
            mbNameEditable = resultSet.getBoolean("b_name_edit");
            mbIngredientApplying = resultSet.getBoolean("b_ing");
            mbItemLineApplying = resultSet.getBoolean("b_lin");
            mbMeasurementLengthApplying = resultSet.getBoolean("b_mst_len");
            mbMeasurementSurfaceApplying = resultSet.getBoolean("b_mst_sur");
            mbMeasurementVolumeApplying = resultSet.getBoolean("b_mst_vol");
            mbMeasurementMassApplying = resultSet.getBoolean("b_mst_mas");
            mbMeasurementTimeApplying = resultSet.getBoolean("b_mst_tme");
            mbWeightGrossApplying = resultSet.getBoolean("b_wgt_grs");
            mbWeightDeliveryApplying = resultSet.getBoolean("b_wgt_del");
            mbUnitsVirtualApplying = resultSet.getBoolean("b_unt_vir");
            mbUnitsContainedApplying = resultSet.getBoolean("b_unt_con");
            mbUnitsPackageApplying = resultSet.getBoolean("b_unt_pac");
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
            mnFkItemFamilyId = resultSet.getInt("fk_fam");
            mnFkItemCategoryId = resultSet.getInt("fk_itm_ct");
            mnFkItemClassId = resultSet.getInt("fk_itm_cl");
            mnFkItemTypeId = resultSet.getInt("fk_itm_tp");
            mnFkSerialNumberTypeId = resultSet.getInt("fk_snr_tp");
            mnFkTaxGroupId = resultSet.getInt("fk_tax_grp");
            mnFkAbpItemId = resultSet.getInt("fk_abp_itm");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
                    mnPkItemGenusId + ", " +
                    "'" + (msCode.length() > 0 ? msCode : "" + mnPkItemGenusId) + "', " +
                    "'" + msName + "', " +
                    mnCodeOrderItemGenus + ", " +
                    mnCodeOrderItemLine + ", " +
                    mnCodeOrderBrand + ", " +
                    mnCodeOrderManufacturer + ", " +
                    mnCodeOrderItem + ", " +
                    mnNameOrderItemGenus + ", " +
                    mnNameOrderItemLine + ", " +
                    mnNameOrderBrand + ", " +
                    mnNameOrderManufacturer + ", " +
                    mnNameOrderItem + ", " +
                    "'" + msSerialNumberName + "', " +
                    mnSerialNumberLength + ", " +
                    "'" + msCfdItemKey + "', " + 
                    (mbCodeEditable ? 1 : 0) + ", " +
                    (mbNameEditable ? 1 : 0) + ", " +
                    (mbIngredientApplying ? 1 : 0) + ", " +
                    (mbItemLineApplying ? 1 : 0) + ", " +
                    (mbMeasurementLengthApplying ? 1 : 0) + ", " +
                    (mbMeasurementSurfaceApplying ? 1 : 0) + ", " +
                    (mbMeasurementVolumeApplying ? 1 : 0) + ", " +
                    (mbMeasurementMassApplying ? 1 : 0) + ", " +
                    (mbMeasurementTimeApplying ? 1 : 0) + ", " +
                    (mbWeightGrossApplying ? 1 : 0) + ", " +
                    (mbWeightDeliveryApplying ? 1 : 0) + ", " +
                    (mbUnitsVirtualApplying ? 1 : 0) + ", " +
                    (mbUnitsContainedApplying ? 1 : 0) + ", " +
                    (mbUnitsPackageApplying ? 1 : 0) + ", " +
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
                    mnFkItemFamilyId + ", " +
                    mnFkItemCategoryId + ", " +
                    mnFkItemClassId + ", " +
                    mnFkItemTypeId + ", " +
                    mnFkSerialNumberTypeId + ", " +
                    mnFkTaxGroupId + ", " +
                    mnFkAbpItemId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_gen = " + mnPkItemGenusId + ", " +
                    "code = '" + (msCode.length() > 0 ? msCode : "" + mnPkItemGenusId) + "', " +
                    "name = '" + msName + "', " +
                    "code_pos_gen = " + mnCodeOrderItemGenus + ", " +
                    "code_pos_lin = " + mnCodeOrderItemLine + ", " +
                    "code_pos_brd = " + mnCodeOrderBrand + ", " +
                    "code_pos_mfr = " + mnCodeOrderManufacturer + ", " +
                    "code_pos_itm = " + mnCodeOrderItem + ", " +
                    "name_pos_gen = " + mnNameOrderItemGenus + ", " +
                    "name_pos_lin = " + mnNameOrderItemLine + ", " +
                    "name_pos_brd = " + mnNameOrderBrand + ", " +
                    "name_pos_mfr = " + mnNameOrderManufacturer + ", " +
                    "name_pos_itm = " + mnNameOrderItem + ", " +
                    "snr_name = '" + msSerialNumberName + "', " +
                    "snr_len = " + mnSerialNumberLength + ", " +
                    "cfd_itm_key = '" + msCfdItemKey + "', " +
                    "b_code_edit = " + (mbCodeEditable ? 1 : 0) + ", " +
                    "b_name_edit = " + (mbNameEditable ? 1 : 0) + ", " +
                    "b_ing = " + (mbIngredientApplying ? 1 : 0) + ", " +
                    "b_lin = " + (mbItemLineApplying ? 1 : 0) + ", " +
                    "b_mst_len = " + (mbMeasurementLengthApplying ? 1 : 0) + ", " +
                    "b_mst_sur = " + (mbMeasurementSurfaceApplying ? 1 : 0) + ", " +
                    "b_mst_vol = " + (mbMeasurementVolumeApplying ? 1 : 0) + ", " +
                    "b_mst_mas = " + (mbMeasurementMassApplying ? 1 : 0) + ", " +
                    "b_mst_tme = " + (mbMeasurementTimeApplying ? 1 : 0) + ", " +
                    "b_wgt_grs = " + (mbWeightGrossApplying ? 1 : 0) + ", " +
                    "b_wgt_del = " + (mbWeightDeliveryApplying ? 1 : 0) + ", " +
                    "b_unt_vir = " + (mbUnitsVirtualApplying ? 1 : 0) + ", " +
                    "b_unt_con = " + (mbUnitsContainedApplying ? 1 : 0) + ", " +
                    "b_unt_pac = " + (mbUnitsPackageApplying ? 1 : 0) + ", " +
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
                    "fk_fam = " + mnFkItemFamilyId + ", " +
                    "fk_itm_ct = " + mnFkItemCategoryId + ", " +
                    "fk_itm_cl = " + mnFkItemClassId + ", " +
                    "fk_itm_tp = " + mnFkItemTypeId + ", " +
                    "fk_snr_tp = " + mnFkSerialNumberTypeId + ", " +
                    "fk_tax_grp = " + mnFkTaxGroupId + ", " +
                    "fk_abp_itm = " + mnFkAbpItemId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbItemGenus clone() throws CloneNotSupportedException {
        DDbItemGenus registry = new DDbItemGenus();

        registry.setPkItemGenusId(this.getPkItemGenusId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setCodeOrderItemGenus(this.getCodeOrderItemGenus());
        registry.setCodeOrderItemLine(this.getCodeOrderItemLine());
        registry.setCodeOrderBrand(this.getCodeOrderBrand());
        registry.setCodeOrderManufacturer(this.getCodeOrderManufacturer());
        registry.setCodeOrderItem(this.getCodeOrderItem());
        registry.setNameOrderItemGenus(this.getNameOrderItemGenus());
        registry.setNameOrderItemLine(this.getNameOrderItemLine());
        registry.setNameOrderBrand(this.getNameOrderBrand());
        registry.setNameOrderManufacturer(this.getNameOrderManufacturer());
        registry.setNameOrderItem(this.getNameOrderItem());
        registry.setSerialNumberName(this.getSerialNumberName());
        registry.setSerialNumberLength(this.getSerialNumberLength());
        registry.setCfdItemKey(this.getCfdItemKey());
        registry.setCodeEditable(this.isCodeEditable());
        registry.setNameEditable(this.isNameEditable());
        registry.setIngredientApplying(this.isIngredientApplying());
        registry.setItemLineApplying(this.isItemLineApplying());
        registry.setMeasurementLengthApplying(this.isMeasurementLengthApplying());
        registry.setMeasurementSurfaceApplying(this.isMeasurementSurfaceApplying());
        registry.setMeasurementVolumeApplying(this.isMeasurementVolumeApplying());
        registry.setMeasurementMassApplying(this.isMeasurementMassApplying());
        registry.setMeasurementTimeApplying(this.isMeasurementTimeApplying());
        registry.setWeightGrossApplying(this.isWeightGrossApplying());
        registry.setWeightDeliveryApplying(this.isWeightDeliveryApplying());
        registry.setUnitsVirtualApplying(this.isUnitsVirtualApplying());
        registry.setUnitsContainedApplying(this.isUnitsContainedApplying());
        registry.setUnitsPackageApplying(this.isUnitsPackageApplying());
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
        registry.setFkItemFamilyId(this.getFkItemFamilyId());
        registry.setFkItemCategoryId(this.getFkItemCategoryId());
        registry.setFkItemClassId(this.getFkItemClassId());
        registry.setFkItemTypeId(this.getFkItemTypeId());
        registry.setFkSerialNumberTypeId(this.getFkSerialNumberTypeId());
        registry.setFkTaxGroupId(this.getFkTaxGroupId());
        registry.setFkAbpItemId(this.getFkAbpItemId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
