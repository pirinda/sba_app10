/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

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
public class DDbAbpItem extends DDbRegistryUser {

    protected int mnPkAbpItemId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkAccountInPurchaseId;
    protected int mnFkAccountInPurchaseChangeId;
    protected int mnFkAccountInPurchaseWarrantyId;
    protected int mnFkAccountInPurchaseConsignationId;
    protected int mnFkAccountInSaleId;
    protected int mnFkAccountInSaleChangeId;
    protected int mnFkAccountInSaleWarrantyId;
    protected int mnFkAccountInSaleConsignationId;
    protected int mnFkAccountInAdjustmentId;
    protected int mnFkAccountInInventoryId;
    protected int mnFkAccountOutPurchaseId;
    protected int mnFkAccountOutPurchaseChangeId;
    protected int mnFkAccountOutPurchaseWarrantyId;
    protected int mnFkAccountOutPurchaseConsignationId;
    protected int mnFkAccountOutSaleId;
    protected int mnFkAccountOutSaleChangeId;
    protected int mnFkAccountOutSaleWarrantyId;
    protected int mnFkAccountOutSaleConsignationId;
    protected int mnFkAccountOutAdjustmentId;
    protected int mnFkAccountOutInventoryId;
    protected int mnFkAccountPurchaseId;
    protected int mnFkAccountPurchaseIncIncrementId;
    protected int mnFkAccountPurchaseIncAditionId;
    protected int mnFkAccountPurchaseDecDiscountId;
    protected int mnFkAccountPurchaseDecReturnId;
    protected int mnFkAccountSaleId;
    protected int mnFkAccountSaleIncIncrementId;
    protected int mnFkAccountSaleIncAdditionId;
    protected int mnFkAccountSaleDecDiscountId;
    protected int mnFkAccountSaleDecReturnId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbAbpItem() {
        super(DModConsts.F_ABP_ITM);
        initRegistry();
    }

    public void setPkAbpItemId(int n) { mnPkAbpItemId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountInPurchaseId(int n) { mnFkAccountInPurchaseId = n; }
    public void setFkAccountInPurchaseChangeId(int n) { mnFkAccountInPurchaseChangeId = n; }
    public void setFkAccountInPurchaseWarrantyId(int n) { mnFkAccountInPurchaseWarrantyId = n; }
    public void setFkAccountInPurchaseConsignationId(int n) { mnFkAccountInPurchaseConsignationId = n; }
    public void setFkAccountInSaleId(int n) { mnFkAccountInSaleId = n; }
    public void setFkAccountInSaleChangeId(int n) { mnFkAccountInSaleChangeId = n; }
    public void setFkAccountInSaleWarrantyId(int n) { mnFkAccountInSaleWarrantyId = n; }
    public void setFkAccountInSaleConsignationId(int n) { mnFkAccountInSaleConsignationId = n; }
    public void setFkAccountInAdjustmentId(int n) { mnFkAccountInAdjustmentId = n; }
    public void setFkAccountInInventoryId(int n) { mnFkAccountInInventoryId = n; }
    public void setFkAccountOutPurchaseId(int n) { mnFkAccountOutPurchaseId = n; }
    public void setFkAccountOutPurchaseChangeId(int n) { mnFkAccountOutPurchaseChangeId = n; }
    public void setFkAccountOutPurchaseWarrantyId(int n) { mnFkAccountOutPurchaseWarrantyId = n; }
    public void setFkAccountOutPurchaseConsignationId(int n) { mnFkAccountOutPurchaseConsignationId = n; }
    public void setFkAccountOutSaleId(int n) { mnFkAccountOutSaleId = n; }
    public void setFkAccountOutSaleChangeId(int n) { mnFkAccountOutSaleChangeId = n; }
    public void setFkAccountOutSaleWarrantyId(int n) { mnFkAccountOutSaleWarrantyId = n; }
    public void setFkAccountOutSaleConsignationId(int n) { mnFkAccountOutSaleConsignationId = n; }
    public void setFkAccountOutAdjustmentId(int n) { mnFkAccountOutAdjustmentId = n; }
    public void setFkAccountOutInventoryId(int n) { mnFkAccountOutInventoryId = n; }
    public void setFkAccountPurchaseId(int n) { mnFkAccountPurchaseId = n; }
    public void setFkAccountPurchaseIncIncrementId(int n) { mnFkAccountPurchaseIncIncrementId = n; }
    public void setFkAccountPurchaseIncAditionId(int n) { mnFkAccountPurchaseIncAditionId = n; }
    public void setFkAccountPurchaseDecDiscountId(int n) { mnFkAccountPurchaseDecDiscountId = n; }
    public void setFkAccountPurchaseDecReturnId(int n) { mnFkAccountPurchaseDecReturnId = n; }
    public void setFkAccountSaleId(int n) { mnFkAccountSaleId = n; }
    public void setFkAccountSaleIncIncrementId(int n) { mnFkAccountSaleIncIncrementId = n; }
    public void setFkAccountSaleIncAdditionId(int n) { mnFkAccountSaleIncAdditionId = n; }
    public void setFkAccountSaleDecDiscountId(int n) { mnFkAccountSaleDecDiscountId = n; }
    public void setFkAccountSaleDecReturnId(int n) { mnFkAccountSaleDecReturnId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpItemId() { return mnPkAbpItemId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountInPurchaseId() { return mnFkAccountInPurchaseId; }
    public int getFkAccountInPurchaseChangeId() { return mnFkAccountInPurchaseChangeId; }
    public int getFkAccountInPurchaseWarrantyId() { return mnFkAccountInPurchaseWarrantyId; }
    public int getFkAccountInPurchaseConsignationId() { return mnFkAccountInPurchaseConsignationId; }
    public int getFkAccountInSaleId() { return mnFkAccountInSaleId; }
    public int getFkAccountInSaleChangeId() { return mnFkAccountInSaleChangeId; }
    public int getFkAccountInSaleWarrantyId() { return mnFkAccountInSaleWarrantyId; }
    public int getFkAccountInSaleConsignationId() { return mnFkAccountInSaleConsignationId; }
    public int getFkAccountInAdjustmentId() { return mnFkAccountInAdjustmentId; }
    public int getFkAccountInInventoryId() { return mnFkAccountInInventoryId; }
    public int getFkAccountOutPurchaseId() { return mnFkAccountOutPurchaseId; }
    public int getFkAccountOutPurchaseChangeId() { return mnFkAccountOutPurchaseChangeId; }
    public int getFkAccountOutPurchaseWarrantyId() { return mnFkAccountOutPurchaseWarrantyId; }
    public int getFkAccountOutPurchaseConsignationId() { return mnFkAccountOutPurchaseConsignationId; }
    public int getFkAccountOutSaleId() { return mnFkAccountOutSaleId; }
    public int getFkAccountOutSaleChangeId() { return mnFkAccountOutSaleChangeId; }
    public int getFkAccountOutSaleWarrantyId() { return mnFkAccountOutSaleWarrantyId; }
    public int getFkAccountOutSaleConsignationId() { return mnFkAccountOutSaleConsignationId; }
    public int getFkAccountOutAdjustmentId() { return mnFkAccountOutAdjustmentId; }
    public int getFkAccountOutInventoryId() { return mnFkAccountOutInventoryId; }
    public int getFkAccountPurchaseId() { return mnFkAccountPurchaseId; }
    public int getFkAccountPurchaseIncIncrementId() { return mnFkAccountPurchaseIncIncrementId; }
    public int getFkAccountPurchaseIncAditionId() { return mnFkAccountPurchaseIncAditionId; }
    public int getFkAccountPurchaseDecDiscountId() { return mnFkAccountPurchaseDecDiscountId; }
    public int getFkAccountPurchaseDecReturnId() { return mnFkAccountPurchaseDecReturnId; }
    public int getFkAccountSaleId() { return mnFkAccountSaleId; }
    public int getFkAccountSaleIncIncrementId() { return mnFkAccountSaleIncIncrementId; }
    public int getFkAccountSaleIncAdditionId() { return mnFkAccountSaleIncAdditionId; }
    public int getFkAccountSaleDecDiscountId() { return mnFkAccountSaleDecDiscountId; }
    public int getFkAccountSaleDecReturnId() { return mnFkAccountSaleDecReturnId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpItemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpItemId = 0;
        msName = "";
        mbDeleted = false;
        mnFkAccountInPurchaseId = 0;
        mnFkAccountInPurchaseChangeId = 0;
        mnFkAccountInPurchaseWarrantyId = 0;
        mnFkAccountInPurchaseConsignationId = 0;
        mnFkAccountInSaleId = 0;
        mnFkAccountInSaleChangeId = 0;
        mnFkAccountInSaleWarrantyId = 0;
        mnFkAccountInSaleConsignationId = 0;
        mnFkAccountInAdjustmentId = 0;
        mnFkAccountInInventoryId = 0;
        mnFkAccountOutPurchaseId = 0;
        mnFkAccountOutPurchaseChangeId = 0;
        mnFkAccountOutPurchaseWarrantyId = 0;
        mnFkAccountOutPurchaseConsignationId = 0;
        mnFkAccountOutSaleId = 0;
        mnFkAccountOutSaleChangeId = 0;
        mnFkAccountOutSaleWarrantyId = 0;
        mnFkAccountOutSaleConsignationId = 0;
        mnFkAccountOutAdjustmentId = 0;
        mnFkAccountOutInventoryId = 0;
        mnFkAccountPurchaseId = 0;
        mnFkAccountPurchaseIncIncrementId = 0;
        mnFkAccountPurchaseIncAditionId = 0;
        mnFkAccountPurchaseDecDiscountId = 0;
        mnFkAccountPurchaseDecReturnId = 0;
        mnFkAccountSaleId = 0;
        mnFkAccountSaleIncIncrementId = 0;
        mnFkAccountSaleIncAdditionId = 0;
        mnFkAccountSaleDecDiscountId = 0;
        mnFkAccountSaleDecReturnId = 0;
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
        return "WHERE id_abp_itm = " + mnPkAbpItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_itm = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpItemId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_itm), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpItemId = resultSet.getInt(1);
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
            mnPkAbpItemId = resultSet.getInt("id_abp_itm");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountInPurchaseId = resultSet.getInt("fk_acc_in_pur");
            mnFkAccountInPurchaseChangeId = resultSet.getInt("fk_acc_in_pur_chg");
            mnFkAccountInPurchaseWarrantyId = resultSet.getInt("fk_acc_in_pur_war");
            mnFkAccountInPurchaseConsignationId = resultSet.getInt("fk_acc_in_pur_csg");
            mnFkAccountInSaleId = resultSet.getInt("fk_acc_in_sal");
            mnFkAccountInSaleChangeId = resultSet.getInt("fk_acc_in_sal_chg");
            mnFkAccountInSaleWarrantyId = resultSet.getInt("fk_acc_in_sal_war");
            mnFkAccountInSaleConsignationId = resultSet.getInt("fk_acc_in_sal_csg");
            mnFkAccountInAdjustmentId = resultSet.getInt("fk_acc_in_adj");
            mnFkAccountInInventoryId = resultSet.getInt("fk_acc_in_inv");
            mnFkAccountOutPurchaseId = resultSet.getInt("fk_acc_out_pur");
            mnFkAccountOutPurchaseChangeId = resultSet.getInt("fk_acc_out_pur_chg");
            mnFkAccountOutPurchaseWarrantyId = resultSet.getInt("fk_acc_out_pur_war");
            mnFkAccountOutPurchaseConsignationId = resultSet.getInt("fk_acc_out_pur_csg");
            mnFkAccountOutSaleId = resultSet.getInt("fk_acc_out_sal");
            mnFkAccountOutSaleChangeId = resultSet.getInt("fk_acc_out_sal_chg");
            mnFkAccountOutSaleWarrantyId = resultSet.getInt("fk_acc_out_sal_war");
            mnFkAccountOutSaleConsignationId = resultSet.getInt("fk_acc_out_sal_csg");
            mnFkAccountOutAdjustmentId = resultSet.getInt("fk_acc_out_adj");
            mnFkAccountOutInventoryId = resultSet.getInt("fk_acc_out_inv");
            mnFkAccountPurchaseId = resultSet.getInt("fk_acc_pur");
            mnFkAccountPurchaseIncIncrementId = resultSet.getInt("fk_acc_pur_inc_inc");
            mnFkAccountPurchaseIncAditionId = resultSet.getInt("fk_acc_pur_inc_add");
            mnFkAccountPurchaseDecDiscountId = resultSet.getInt("fk_acc_pur_dec_dis");
            mnFkAccountPurchaseDecReturnId = resultSet.getInt("fk_acc_pur_dec_ret");
            mnFkAccountSaleId = resultSet.getInt("fk_acc_sal");
            mnFkAccountSaleIncIncrementId = resultSet.getInt("fk_acc_sal_inc_inc");
            mnFkAccountSaleIncAdditionId = resultSet.getInt("fk_acc_sal_inc_add");
            mnFkAccountSaleDecDiscountId = resultSet.getInt("fk_acc_sal_dec_dis");
            mnFkAccountSaleDecReturnId = resultSet.getInt("fk_acc_sal_dec_ret");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpItemId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkAccountInPurchaseId + ", " +
                    mnFkAccountInPurchaseChangeId + ", " +
                    mnFkAccountInPurchaseWarrantyId + ", " +
                    mnFkAccountInPurchaseConsignationId + ", " +
                    mnFkAccountInSaleId + ", " +
                    mnFkAccountInSaleChangeId + ", " +
                    mnFkAccountInSaleWarrantyId + ", " +
                    mnFkAccountInSaleConsignationId + ", " +
                    mnFkAccountInAdjustmentId + ", " +
                    mnFkAccountInInventoryId + ", " +
                    mnFkAccountOutPurchaseId + ", " +
                    mnFkAccountOutPurchaseChangeId + ", " +
                    mnFkAccountOutPurchaseWarrantyId + ", " +
                    mnFkAccountOutPurchaseConsignationId + ", " +
                    mnFkAccountOutSaleId + ", " +
                    mnFkAccountOutSaleChangeId + ", " +
                    mnFkAccountOutSaleWarrantyId + ", " +
                    mnFkAccountOutSaleConsignationId + ", " +
                    mnFkAccountOutAdjustmentId + ", " +
                    mnFkAccountOutInventoryId + ", " +
                    mnFkAccountPurchaseId + ", " +
                    mnFkAccountPurchaseIncIncrementId + ", " +
                    mnFkAccountPurchaseIncAditionId + ", " +
                    mnFkAccountPurchaseDecDiscountId + ", " +
                    mnFkAccountPurchaseDecReturnId + ", " +
                    mnFkAccountSaleId + ", " +
                    mnFkAccountSaleIncIncrementId + ", " +
                    mnFkAccountSaleIncAdditionId + ", " +
                    mnFkAccountSaleDecDiscountId + ", " +
                    mnFkAccountSaleDecReturnId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_abp_itm = " + mnPkAbpItemId + ", " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc_in_pur = " + mnFkAccountInPurchaseId + ", " +
                    "fk_acc_in_pur_chg = " + mnFkAccountInPurchaseChangeId + ", " +
                    "fk_acc_in_pur_war = " + mnFkAccountInPurchaseWarrantyId + ", " +
                    "fk_acc_in_pur_csg = " + mnFkAccountInPurchaseConsignationId + ", " +
                    "fk_acc_in_sal = " + mnFkAccountInSaleId + ", " +
                    "fk_acc_in_sal_chg = " + mnFkAccountInSaleChangeId + ", " +
                    "fk_acc_in_sal_war = " + mnFkAccountInSaleWarrantyId + ", " +
                    "fk_acc_in_sal_csg = " + mnFkAccountInSaleConsignationId + ", " +
                    "fk_acc_in_adj = " + mnFkAccountInAdjustmentId + ", " +
                    "fk_acc_in_inv = " + mnFkAccountInInventoryId + ", " +
                    "fk_acc_out_pur = " + mnFkAccountOutPurchaseId + ", " +
                    "fk_acc_out_pur_chg = " + mnFkAccountOutPurchaseChangeId + ", " +
                    "fk_acc_out_pur_war = " + mnFkAccountOutPurchaseWarrantyId + ", " +
                    "fk_acc_out_pur_csg = " + mnFkAccountOutPurchaseConsignationId + ", " +
                    "fk_acc_out_sal = " + mnFkAccountOutSaleId + ", " +
                    "fk_acc_out_sal_chg = " + mnFkAccountOutSaleChangeId + ", " +
                    "fk_acc_out_sal_war = " + mnFkAccountOutSaleWarrantyId + ", " +
                    "fk_acc_out_sal_csg = " + mnFkAccountOutSaleConsignationId + ", " +
                    "fk_acc_out_adj = " + mnFkAccountOutAdjustmentId + ", " +
                    "fk_acc_out_inv = " + mnFkAccountOutInventoryId + ", " +
                    "fk_acc_pur = " + mnFkAccountPurchaseId + ", " +
                    "fk_acc_pur_inc_inc = " + mnFkAccountPurchaseIncIncrementId + ", " +
                    "fk_acc_pur_inc_add = " + mnFkAccountPurchaseIncAditionId + ", " +
                    "fk_acc_pur_dec_dis = " + mnFkAccountPurchaseDecDiscountId + ", " +
                    "fk_acc_pur_dec_ret = " + mnFkAccountPurchaseDecReturnId + ", " +
                    "fk_acc_sal = " + mnFkAccountSaleId + ", " +
                    "fk_acc_sal_inc_inc = " + mnFkAccountSaleIncIncrementId + ", " +
                    "fk_acc_sal_inc_add = " + mnFkAccountSaleIncAdditionId + ", " +
                    "fk_acc_sal_dec_dis = " + mnFkAccountSaleDecDiscountId + ", " +
                    "fk_acc_sal_dec_ret = " + mnFkAccountSaleDecReturnId + ", " +
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
    public DDbAbpItem clone() throws CloneNotSupportedException {
        DDbAbpItem registry = new DDbAbpItem();

        registry.setPkAbpItemId(this.getPkAbpItemId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountInPurchaseId(this.getFkAccountInPurchaseId());
        registry.setFkAccountInPurchaseChangeId(this.getFkAccountInPurchaseChangeId());
        registry.setFkAccountInPurchaseWarrantyId(this.getFkAccountInPurchaseWarrantyId());
        registry.setFkAccountInPurchaseConsignationId(this.getFkAccountInPurchaseConsignationId());
        registry.setFkAccountInSaleId(this.getFkAccountInSaleId());
        registry.setFkAccountInSaleChangeId(this.getFkAccountInSaleChangeId());
        registry.setFkAccountInSaleWarrantyId(this.getFkAccountInSaleWarrantyId());
        registry.setFkAccountInSaleConsignationId(this.getFkAccountInSaleConsignationId());
        registry.setFkAccountInAdjustmentId(this.getFkAccountInAdjustmentId());
        registry.setFkAccountInInventoryId(this.getFkAccountInInventoryId());
        registry.setFkAccountOutPurchaseId(this.getFkAccountOutPurchaseId());
        registry.setFkAccountOutPurchaseChangeId(this.getFkAccountOutPurchaseChangeId());
        registry.setFkAccountOutPurchaseWarrantyId(this.getFkAccountOutPurchaseWarrantyId());
        registry.setFkAccountOutPurchaseConsignationId(this.getFkAccountOutPurchaseConsignationId());
        registry.setFkAccountOutSaleId(this.getFkAccountOutSaleId());
        registry.setFkAccountOutSaleChangeId(this.getFkAccountOutSaleChangeId());
        registry.setFkAccountOutSaleWarrantyId(this.getFkAccountOutSaleWarrantyId());
        registry.setFkAccountOutSaleConsignationId(this.getFkAccountOutSaleConsignationId());
        registry.setFkAccountOutAdjustmentId(this.getFkAccountOutAdjustmentId());
        registry.setFkAccountOutInventoryId(this.getFkAccountOutInventoryId());
        registry.setFkAccountPurchaseId(this.getFkAccountPurchaseId());
        registry.setFkAccountPurchaseIncIncrementId(this.getFkAccountPurchaseIncIncrementId());
        registry.setFkAccountPurchaseIncAditionId(this.getFkAccountPurchaseIncAditionId());
        registry.setFkAccountPurchaseDecDiscountId(this.getFkAccountPurchaseDecDiscountId());
        registry.setFkAccountPurchaseDecReturnId(this.getFkAccountPurchaseDecReturnId());
        registry.setFkAccountSaleId(this.getFkAccountSaleId());
        registry.setFkAccountSaleIncIncrementId(this.getFkAccountSaleIncIncrementId());
        registry.setFkAccountSaleIncAdditionId(this.getFkAccountSaleIncAdditionId());
        registry.setFkAccountSaleDecDiscountId(this.getFkAccountSaleDecDiscountId());
        registry.setFkAccountSaleDecReturnId(this.getFkAccountSaleDecReturnId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
