/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DDbBranchAddress extends DDbRegistryUser implements DGridRow {

    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected int mnPkAddressId;
    protected String msName;
    protected String msAddress1;
    protected String msAddress2;
    protected String msAddress3;
    protected String msNumberExterior;
    protected String msNumberInterior;
    protected String msLocality;
    protected String msCounty;
    protected String msState;
    protected String msZipCode;
    protected String msPostOfficeBox;
    protected String msTelecommDevice1;
    protected String msTelecommDevice2;
    protected String msTelecommDevice3;
    protected String msContact1;
    protected String msContact2;
    protected String msTelecommElectronic1;
    protected String msTelecommElectronic2;
    protected String msNote;
    protected boolean mbDefault;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkCountryId_n;
    protected int mnFkTelecommDevice1TypeId;
    protected int mnFkTelecommDevice2TypeId;
    protected int mnFkTelecommDevice3TypeId;
    protected int mnFkTelecommElectronic1TypeId;
    protected int mnFkTelecommElectronic2TypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbBranchAddress() {
        super(DModConsts.BU_ADD);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkAddressId(int n) { mnPkAddressId = n; }
    public void setName(String s) { msName = s; }
    public void setAddress1(String s) { msAddress1 = s; }
    public void setAddress2(String s) { msAddress2 = s; }
    public void setAddress3(String s) { msAddress3 = s; }
    public void setNumberExterior(String s) { msNumberExterior = s; }
    public void setNumberInterior(String s) { msNumberInterior = s; }
    public void setLocality(String s) { msLocality = s; }
    public void setCounty(String s) { msCounty = s; }
    public void setState(String s) { msState = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setPostOfficeBox(String s) { msPostOfficeBox = s; }
    public void setTelecommDevice1(String s) { msTelecommDevice1 = s; }
    public void setTelecommDevice2(String s) { msTelecommDevice2 = s; }
    public void setTelecommDevice3(String s) { msTelecommDevice3 = s; }
    public void setContact1(String s) { msContact1 = s; }
    public void setContact2(String s) { msContact2 = s; }
    public void setTelecommElectronic1(String s) { msTelecommElectronic1 = s; }
    public void setTelecommElectronic2(String s) { msTelecommElectronic2 = s; }
    public void setNote(String s) { msNote = s; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkCountryId_n(int n) { mnFkCountryId_n = n; }
    public void setFkTelecommDevice1TypeId(int n) { mnFkTelecommDevice1TypeId = n; }
    public void setFkTelecommDevice2TypeId(int n) { mnFkTelecommDevice2TypeId = n; }
    public void setFkTelecommDevice3TypeId(int n) { mnFkTelecommDevice3TypeId = n; }
    public void setFkTelecommElectronic1TypeId(int n) { mnFkTelecommElectronic1TypeId = n; }
    public void setFkTelecommElectronic2TypeId(int n) { mnFkTelecommElectronic2TypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkAddressId() { return mnPkAddressId; }
    public String getName() { return msName; }
    public String getAddress1() { return msAddress1; }
    public String getAddress2() { return msAddress2; }
    public String getAddress3() { return msAddress3; }
    public String getNumberExterior() { return msNumberExterior; }
    public String getNumberInterior() { return msNumberInterior; }
    public String getLocality() { return msLocality; }
    public String getCounty() { return msCounty; }
    public String getState() { return msState; }
    public String getZipCode() { return msZipCode; }
    public String getPostOfficeBox() { return msPostOfficeBox; }
    public String getTelecommDevice1() { return msTelecommDevice1; }
    public String getTelecommDevice2() { return msTelecommDevice2; }
    public String getTelecommDevice3() { return msTelecommDevice3; }
    public String getContact1() { return msContact1; }
    public String getContact2() { return msContact2; }
    public String getTelecommElectronic1() { return msTelecommElectronic1; }
    public String getTelecommElectronic2() { return msTelecommElectronic2; }
    public String getNote() { return msNote; }
    public boolean isDefault() { return mbDefault; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkCountryId_n() { return mnFkCountryId_n; }
    public int getFkTelecommDevice1TypeId() { return mnFkTelecommDevice1TypeId; }
    public int getFkTelecommDevice2TypeId() { return mnFkTelecommDevice2TypeId; }
    public int getFkTelecommDevice3TypeId() { return mnFkTelecommDevice3TypeId; }
    public int getFkTelecommElectronic1TypeId() { return mnFkTelecommElectronic1TypeId; }
    public int getFkTelecommElectronic2TypeId() { return mnFkTelecommElectronic2TypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int getActualFkCountryId_n(DGuiSession session) { return mnFkCountryId_n != DLibConsts.UNDEFINED ? mnFkCountryId_n : ((DDbConfigCompany) session.getConfigCompany()).getFkCountryId(); }

    public String getTelecommDevices() {
        int i = 0;
        String s = "";

        if (!msTelecommDevice1.isEmpty()) {
            i++;
            s = msTelecommDevice1;
        }
        if (!msTelecommDevice2.isEmpty()) {
            i++;
            s += (s.isEmpty() ? "" : ", ") + msTelecommDevice2;
        }
        if (!msTelecommDevice3.isEmpty()) {
            i++;
            s += (s.isEmpty() ? "" : ", ") + msTelecommDevice3;
        }

        return (s.isEmpty() ? "" : (i == 1 ? "TEL. " : "TELS. ")) + s;
    }

    public String getTelecommElectronics() {
        String s = "";

        if (!msTelecommElectronic1.isEmpty()) {
            s = msTelecommElectronic1;
        }
        if (!msTelecommElectronic2.isEmpty()) {
            s += (s.isEmpty() ? "" : ", ") + msTelecommElectronic2;
        }

        return s;
    }

    public String composeLocality(DGuiSession session) {
        String country = (String) session.readField(DModConsts.CS_CTY, new int[] { getActualFkCountryId_n(session) }, DDbRegistry.FIELD_NAME);
        
        return msLocality +
                (msCounty.isEmpty() || msCounty.compareTo(msLocality) == 0 ? "" : ", " + msCounty) +
                (msState.isEmpty() ? "" : ", " + msState) +
                (country.isEmpty() ? "" : ", " + country);
    }
    
    public String composeAddress(DGuiSession session, int formatType) {
        String address = "";
        String country = "";

        if (mnFkCountryId_n == DLibConsts.UNDEFINED || mnFkCountryId_n == ((DDbConfigCompany) session.getConfigCompany()).getFkCountryId()) {
            country = "";
        }
        else {
            country = (String) session.readField(DModConsts.CS_CTY, new int[] { mnFkCountryId_n }, DDbRegistry.FIELD_NAME);
        }

        switch (formatType) {
            case DModSysConsts.BS_BAF_TP_STD:
                address = msAddress1 + (msNumberExterior.length() == 0 ? "" : " " + msNumberExterior) + (msNumberInterior.length() == 0 ? "" : " " + msNumberInterior);
                address += (address.length() == 0 ? "" : "\n") + msAddress2 + (msAddress3.length() == 0 ? "" : ", " + msAddress3);
                address += (address.length() == 0 ? "" : "\n") + msZipCode + (msLocality.length() == 0 ? "" : " " + msLocality) + (msCounty.length() == 0 ? "" : ", " + msCounty) + (msState.length() == 0 ? "" : ", " + msState);
                address += address.length() == 0 ? "" : (country.isEmpty() ? "" : "\n" + country);
                break;
            case DModSysConsts.BS_BAF_TP_US:
                address = msNumberExterior + (msNumberInterior.length() == 0 ? "" : " " + msNumberInterior) + (msAddress1.length() == 0 ? "" : " " + msAddress1);
                address += (address.length() == 0 ? "" : "\n") + msAddress2 + (msAddress3.length() == 0 ? "" : " " + msAddress3);
                address += (address.length() == 0 ? "" : "\n") + msLocality + (msCounty.length() == 0 ? "" : " " + msCounty) + (msState.length() == 0 ? "" : " " + msState) + (msZipCode.length() == 0 ? "" : " " + msZipCode);
                address += address.length() == 0 ? "" : (country.isEmpty() ? "" : "\n" + country);
                break;
            default:
        }

        return address;
    }
    
    

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBranchId = pk[1];
        mnPkAddressId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkAddressId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        mnPkAddressId = 0;
        msName = "";
        msAddress1 = "";
        msAddress2 = "";
        msAddress3 = "";
        msNumberExterior = "";
        msNumberInterior = "";
        msLocality = "";
        msCounty = "";
        msState = "";
        msZipCode = "";
        msPostOfficeBox = "";
        msTelecommDevice1 = "";
        msTelecommDevice2 = "";
        msTelecommDevice3 = "";
        msContact1 = "";
        msContact2 = "";
        msTelecommElectronic1 = "";
        msTelecommElectronic2 = "";
        msNote = "";
        mbDefault = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkCountryId_n = 0;
        mnFkTelecommDevice1TypeId = 0;
        mnFkTelecommDevice2TypeId = 0;
        mnFkTelecommDevice3TypeId = 0;
        mnFkTelecommElectronic1TypeId = 0;
        mnFkTelecommElectronic2TypeId = 0;
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
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " AND " +
                "id_add = " + mnPkAddressId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND " +
                "id_bra = " + pk[1] + " AND " +
                "id_add = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAddressId = 0;

        msSql = "SELECT COALESCE(MAX(id_add), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAddressId = resultSet.getInt(1);
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");
            mnPkAddressId = resultSet.getInt("id_add");
            msName = resultSet.getString("name");
            msAddress1 = resultSet.getString("add_1");
            msAddress2 = resultSet.getString("add_2");
            msAddress3 = resultSet.getString("add_3");
            msNumberExterior = resultSet.getString("num_ext");
            msNumberInterior = resultSet.getString("num_int");
            msLocality = resultSet.getString("loc");
            msCounty = resultSet.getString("cou");
            msState = resultSet.getString("ste");
            msZipCode = resultSet.getString("zip");
            msPostOfficeBox = resultSet.getString("pob");
            msTelecommDevice1 = resultSet.getString("tcd_1");
            msTelecommDevice2 = resultSet.getString("tcd_2");
            msTelecommDevice3 = resultSet.getString("tcd_3");
            msContact1 = resultSet.getString("con_1");
            msContact2 = resultSet.getString("con_2");
            msTelecommElectronic1 = resultSet.getString("tce_1");
            msTelecommElectronic2 = resultSet.getString("tce_2");
            msNote = resultSet.getString("note");
            mbDefault = resultSet.getBoolean("b_def");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkCountryId_n = resultSet.getInt("fk_cty_n");
            mnFkTelecommDevice1TypeId = resultSet.getInt("fk_tcd_1_tp");
            mnFkTelecommDevice2TypeId = resultSet.getInt("fk_tcd_2_tp");
            mnFkTelecommDevice3TypeId = resultSet.getInt("fk_tcd_3_tp");
            mnFkTelecommElectronic1TypeId = resultSet.getInt("fk_tce_1_tp");
            mnFkTelecommElectronic2TypeId = resultSet.getInt("fk_tce_2_tp");
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
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    mnPkAddressId + ", " +
                    "'" + msName + "', " +
                    "'" + msAddress1 + "', " +
                    "'" + msAddress2 + "', " +
                    "'" + msAddress3 + "', " +
                    "'" + msNumberExterior + "', " +
                    "'" + msNumberInterior + "', " +
                    "'" + msLocality + "', " +
                    "'" + msCounty + "', " +
                    "'" + msState + "', " +
                    "'" + msZipCode + "', " +
                    "'" + msPostOfficeBox + "', " +
                    "'" + msTelecommDevice1 + "', " +
                    "'" + msTelecommDevice2 + "', " +
                    "'" + msTelecommDevice3 + "', " +
                    "'" + msContact1 + "', " +
                    "'" + msContact2 + "', " +
                    "'" + msTelecommElectronic1 + "', " +
                    "'" + msTelecommElectronic2 + "', " +
                    "'" + msNote + "', " +
                    (mbDefault ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkCountryId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCountryId_n) + ", " +
                    mnFkTelecommDevice1TypeId + ", " +
                    mnFkTelecommDevice2TypeId + ", " +
                    mnFkTelecommDevice3TypeId + ", " +
                    mnFkTelecommElectronic1TypeId + ", " +
                    mnFkTelecommElectronic2TypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    "id_bra = " + mnPkBranchId + ", " +
                    "id_add = " + mnPkAddressId + ", " +
                    */
                    "name = '" + msName + "', " +
                    "add_1 = '" + msAddress1 + "', " +
                    "add_2 = '" + msAddress2 + "', " +
                    "add_3 = '" + msAddress3 + "', " +
                    "num_ext = '" + msNumberExterior + "', " +
                    "num_int = '" + msNumberInterior + "', " +
                    "loc = '" + msLocality + "', " +
                    "cou = '" + msCounty + "', " +
                    "ste = '" + msState + "', " +
                    "zip = '" + msZipCode + "', " +
                    "pob = '" + msPostOfficeBox + "', " +
                    "tcd_1 = '" + msTelecommDevice1 + "', " +
                    "tcd_2 = '" + msTelecommDevice2 + "', " +
                    "tcd_3 = '" + msTelecommDevice3 + "', " +
                    "con_1 = '" + msContact1 + "', " +
                    "con_2 = '" + msContact2 + "', " +
                    "tce_1 = '" + msTelecommElectronic1 + "', " +
                    "tce_2 = '" + msTelecommElectronic2 + "', " +
                    "note = '" + msNote + "', " +
                    "b_def = " + (mbDefault ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_cty_n = " + (mnFkCountryId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCountryId_n) + ", " +
                    "fk_tcd_1_tp = " + mnFkTelecommDevice1TypeId + ", " +
                    "fk_tcd_2_tp = " + mnFkTelecommDevice2TypeId + ", " +
                    "fk_tcd_3_tp = " + mnFkTelecommDevice3TypeId + ", " +
                    "fk_tce_1_tp = " + mnFkTelecommElectronic1TypeId + ", " +
                    "fk_tce_2_tp = " + mnFkTelecommElectronic2TypeId + ", " +
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
    public DDbBranchAddress clone() throws CloneNotSupportedException {
        DDbBranchAddress registry = new DDbBranchAddress();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkAddressId(this.getPkAddressId());
        registry.setName(this.getName());
        registry.setAddress1(this.getAddress1());
        registry.setAddress2(this.getAddress2());
        registry.setAddress3(this.getAddress3());
        registry.setNumberExterior(this.getNumberExterior());
        registry.setNumberInterior(this.getNumberInterior());
        registry.setLocality(this.getLocality());
        registry.setCounty(this.getCounty());
        registry.setState(this.getState());
        registry.setZipCode(this.getZipCode());
        registry.setPostOfficeBox(this.getPostOfficeBox());
        registry.setTelecommDevice1(this.getTelecommDevice1());
        registry.setTelecommDevice2(this.getTelecommDevice2());
        registry.setTelecommDevice3(this.getTelecommDevice3());
        registry.setContact1(this.getContact1());
        registry.setContact2(this.getContact2());
        registry.setTelecommElectronic1(this.getTelecommElectronic1());
        registry.setTelecommElectronic2(this.getTelecommElectronic2());
        registry.setNote(this.getNote());
        registry.setDefault(this.isDefault());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkCountryId_n(this.getFkCountryId_n());
        registry.setFkTelecommDevice1TypeId(this.getFkTelecommDevice1TypeId());
        registry.setFkTelecommDevice2TypeId(this.getFkTelecommDevice2TypeId());
        registry.setFkTelecommDevice3TypeId(this.getFkTelecommDevice3TypeId());
        registry.setFkTelecommElectronic1TypeId(this.getFkTelecommElectronic1TypeId());
        registry.setFkTelecommElectronic2TypeId(this.getFkTelecommElectronic2TypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
    }

    @Override
    public boolean isRowSystem() {
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msName;
                break;
            case 1:
                value = msAddress1 + (msNumberExterior.length() == 0 ? "" : " " + msNumberExterior) + (msNumberInterior.length() == 0 ? "" : " " + msNumberInterior);
                break;
            case 2:
                value = msAddress2 + (msAddress3.length() == 0 ? "" : " " + msAddress3);
                break;
            case 3:
                value = msLocality + (msState.length() == 0 ? "" : ", " + msState);
                break;
            case 4:
                value = msZipCode;
                break;
            case 5:
                value = mbDefault;
                break;
            case 6:
                value = mbDisabled;
                break;
            case 7:
                value = mbDeleted;
                break;
            case 8:
                value = mbSystem;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
