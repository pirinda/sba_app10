/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DDbBranch extends DDbRegistryUser {

    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected String msCode;
    protected String msName;
    protected String msNote;
    protected boolean mbAddressPrintable;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkAddressFormatTypeId_n;
    protected int mnFkTaxRegionId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<DDbBranchAddress> mvChildAddresses;
    protected Vector<DDbBranchBankAccount> mvChildBankAccounts;

    public DDbBranch() {
        super(DModConsts.BU_BRA);
        mvChildAddresses = new Vector<DDbBranchAddress>();
        mvChildBankAccounts = new Vector<DDbBranchBankAccount>();
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setNote(String s) { msNote = s; }
    public void setAddressPrintable(boolean b) { mbAddressPrintable = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAddressFormatTypeId_n(int n) { mnFkAddressFormatTypeId_n = n; }
    public void setFkTaxRegionId_n(int n) { mnFkTaxRegionId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getNote() { return msNote; }
    public boolean isAddressPrintable() { return mbAddressPrintable; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAddressFormatTypeId_n() { return mnFkAddressFormatTypeId_n; }
    public int getFkTaxRegionId_n() { return mnFkTaxRegionId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<DDbBranchAddress> getChildAddresses() { return mvChildAddresses; }
    public Vector<DDbBranchBankAccount> getChildBankAccounts() { return mvChildBankAccounts; }

    public int getActualFkAddressFormatTypeId(DGuiSession session) { return mnFkAddressFormatTypeId_n != DLibConsts.UNDEFINED ? mnFkAddressFormatTypeId_n : ((DDbConfigCompany) session.getConfigCompany()).getFkAddressFormatTypeId(); }
    public int getActualFkTaxRegionId(DGuiSession session) { return mnFkTaxRegionId_n != DLibConsts.UNDEFINED ? mnFkTaxRegionId_n : ((DDbConfigBranch) session.getConfigBranch()).getFkTaxRegionId(); }

    public DDbBranchAddress getChildDefaultAddress() {
        DDbBranchAddress defaultChild = null;

        for (DDbBranchAddress child : mvChildAddresses) {
            if (child.isDefault()) {
                defaultChild = child;
                break;
            }
        }

        return defaultChild;
    }

    public DDbBranchAddress getChildAddress(int[] key) {
        DDbBranchAddress desiredChild = null;

        for (DDbBranchAddress child : mvChildAddresses) {
            if (DLibUtils.compareKeys(key, child.getPrimaryKey())) {
                desiredChild = child;
                break;
            }
        }

        return desiredChild;
    }

    public DDbBranchBankAccount getChildBankAccount(int[] key) {
        DDbBranchBankAccount desiredChild = null;

        for (DDbBranchBankAccount child : mvChildBankAccounts) {
            if (DLibUtils.compareKeys(key, child.getPrimaryKey())) {
                desiredChild = child;
                break;
            }
        }

        return desiredChild;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBranchId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        msCode = "";
        msName = "";
        msNote = "";
        mbAddressPrintable = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAddressFormatTypeId_n = 0;
        mnFkTaxRegionId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildAddresses.clear();
        mvChildBankAccounts.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND " +
                "id_bra = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBranchId = 0;

        msSql = "SELECT COALESCE(MAX(id_bra), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_bpr = " + mnPkBizPartnerId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBranchId = resultSet.getInt(1);
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msNote = resultSet.getString("note");
            mbAddressPrintable = resultSet.getBoolean("b_add_prt");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAddressFormatTypeId_n = resultSet.getInt("fk_baf_tp_n");
            mnFkTaxRegionId_n = resultSet.getInt("fk_tax_reg_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_add FROM " + DModConsts.TablesMap.get(DModConsts.BU_ADD) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranchAddress child = new DDbBranchAddress();
                child.read(session, new int[] { mnPkBizPartnerId, mnPkBranchId, resultSet.getInt(1) });
                mvChildAddresses.add(child);
            }

            msSql = "SELECT id_bnk FROM " + DModConsts.TablesMap.get(DModConsts.BU_BNK) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranchBankAccount child = new DDbBranchBankAccount();
                child.read(session, new int[] { mnPkBizPartnerId, mnPkBranchId, resultSet.getInt(1) });
                mvChildBankAccounts.add(child);
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
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msNote + "', " +
                    (mbAddressPrintable ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkAddressFormatTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAddressFormatTypeId_n) + ", " +
                    (mnFkTaxRegionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxRegionId_n) + ", " +
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
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "note = '" + msNote + "', " +
                    "b_add_prt = " + (mbAddressPrintable ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_baf_tp_n = " + (mnFkAddressFormatTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAddressFormatTypeId_n) + ", " +
                    "fk_tax_reg_n = " + (mnFkTaxRegionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxRegionId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (DDbBranchAddress child : mvChildAddresses) {
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setPkBranchId(mnPkBranchId);
            child.save(session);
        }

        for (DDbBranchBankAccount child : mvChildBankAccounts) {
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setPkBranchId(mnPkBranchId);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBranch clone() throws CloneNotSupportedException {
        DDbBranch registry = new DDbBranch();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setNote(this.getNote());
        registry.setAddressPrintable(this.isAddressPrintable());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAddressFormatTypeId_n(this.getFkAddressFormatTypeId_n());
        registry.setFkTaxRegionId_n(this.getFkTaxRegionId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (DDbBranchAddress child : mvChildAddresses) {
            registry.getChildAddresses().add(child.clone());
        }

        for (DDbBranchBankAccount child : mvChildBankAccounts) {
            registry.getChildBankAccounts().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbBranchAddress child : mvChildAddresses) {
            child.setRegistryNew(registryNew);
        }

        for (DDbBranchBankAccount child : mvChildBankAccounts) {
            child.setRegistryNew(registryNew);
        }
    }
}
