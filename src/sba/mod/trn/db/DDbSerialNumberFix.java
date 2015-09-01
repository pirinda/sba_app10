/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

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
public class DDbSerialNumberFix extends DDbRegistryUser {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkFixId;
    protected String msSerialNumberOld;
    protected String msSerialNumberNew;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbSerialNumberFix() {
        super(DModConsts.T_SNR_FIX);
        initRegistry();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkFixId(int n) { mnPkFixId = n; }
    public void setSerialNumberOld(String s) { msSerialNumberOld = s; }
    public void setSerialNumberNew(String s) { msSerialNumberNew = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkFixId() { return mnPkFixId; }
    public String getSerialNumberOld() { return msSerialNumberOld; }
    public String getSerialNumberNew() { return msSerialNumberNew; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkUnitId = pk[1];
        mnPkFixId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkFixId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkFixId = 0;
        msSerialNumberOld = "";
        msSerialNumberNew = "";
        mbDeleted = false;
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
        return "WHERE id_itm = " + mnPkItemId + " AND " +
                "id_unt = " + mnPkUnitId + " AND " +
                "id_fix = " + mnPkFixId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_itm = " + pk[0] + " AND " +
                "id_unt = " + pk[1] + " AND " +
                "id_fix = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkFixId = 0;

        msSql = "SELECT COALESCE(MAX(id_fix), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_itm = " + mnPkItemId + " AND " +
                "id_unt = " + mnPkUnitId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFixId = resultSet.getInt(1);
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
            mnPkItemId = resultSet.getInt("id_itm");
            mnPkUnitId = resultSet.getInt("id_unt");
            mnPkFixId = resultSet.getInt("id_fix");
            msSerialNumberOld = resultSet.getString("snr_old");
            msSerialNumberNew = resultSet.getString("snr_new");
            mbDeleted = resultSet.getBoolean("b_del");
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
                    mnPkItemId + ", " +
                    mnPkUnitId + ", " +
                    mnPkFixId + ", " +
                    "'" + msSerialNumberOld + "', " +
                    "'" + msSerialNumberNew + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
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
                    "id_itm = " + mnPkItemId + ", " +
                    "id_unt = " + mnPkUnitId + ", " +
                    "id_fix = " + mnPkFixId + ", " +
                    */
                    "snr_old = '" + msSerialNumberOld + "', " +
                    "snr_new = '" + msSerialNumberNew + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Fix serial number:

        msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.T_STK) + " SET snr = '" + msSerialNumberNew + "' " +
                "WHERE id_itm = " + mnPkItemId + " AND id_unt = " + mnPkUnitId + " AND snr = '" + msSerialNumberOld + "' AND b_del = 0 ";

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbSerialNumberFix clone() throws CloneNotSupportedException {
        DDbSerialNumberFix registry = new DDbSerialNumberFix();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkFixId(this.getPkFixId());
        registry.setSerialNumberOld(this.getSerialNumberOld());
        registry.setSerialNumberNew(this.getSerialNumberNew());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
