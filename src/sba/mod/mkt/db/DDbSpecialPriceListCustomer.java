/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

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
public class DDbSpecialPriceListCustomer extends DDbRegistryUser {

    protected int mnPkLinkCustomerTypeId;
    protected int mnPkReferenceCustomerId;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkSpecialPriceListId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbSpecialPriceListCustomer() {
        super(DModConsts.M_SPE_CUS);
        initRegistry();
    }

    public void setPkLinkCustomerTypeId(int n) { mnPkLinkCustomerTypeId = n; }
    public void setPkReferenceCustomerId(int n) { mnPkReferenceCustomerId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkSpecialPriceListId(int n) { mnFkSpecialPriceListId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLinkCustomerTypeId() { return mnPkLinkCustomerTypeId; }
    public int getPkReferenceCustomerId() { return mnPkReferenceCustomerId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkSpecialPriceListId() { return mnFkSpecialPriceListId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLinkCustomerTypeId = pk[0];
        mnPkReferenceCustomerId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLinkCustomerTypeId, mnPkReferenceCustomerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLinkCustomerTypeId = 0;
        mnPkReferenceCustomerId = 0;
        mbDeleted = false;
        mnFkSpecialPriceListId = 0;
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
        return "WHERE id_lnk_cus_tp = " + mnPkLinkCustomerTypeId + " AND " +
                "id_ref_cus = " + mnPkReferenceCustomerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lnk_cus_tp = " + pk[0] + " AND " +
                "id_ref_cus = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkLinkCustomerTypeId = resultSet.getInt("id_lnk_cus_tp");
            mnPkReferenceCustomerId = resultSet.getInt("id_ref_cus");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSpecialPriceListId = resultSet.getInt("fk_spe");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLinkCustomerTypeId + ", " +
                    mnPkReferenceCustomerId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkSpecialPriceListId + ", " +
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
                    "id_lnk_cus_tp = " + mnPkLinkCustomerTypeId + ", " +
                    "id_ref_cus = " + mnPkReferenceCustomerId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_spe = " + mnFkSpecialPriceListId + ", " +
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
    public DDbSpecialPriceListCustomer clone() throws CloneNotSupportedException {
        DDbSpecialPriceListCustomer registry = new DDbSpecialPriceListCustomer();

        registry.setPkLinkCustomerTypeId(this.getPkLinkCustomerTypeId());
        registry.setPkReferenceCustomerId(this.getPkReferenceCustomerId());
        registry.setDeleted(this.isDeleted());
        registry.setFkSpecialPriceListId(this.getFkSpecialPriceListId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
