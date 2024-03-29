/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

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
public class DDbTrailer extends DDbRegistryUser {

    protected int mnPkTrailerId;
    protected String msCode;
    protected String msName;
    protected String msPlate;
    protected String msTrailerSubtypeCode;
    protected String msTrailerSubtypeName;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbTrailer() {
        super(DModConsts.LU_TRAIL);
        initRegistry();
    }

    public void setPkTrailerId(int n) { mnPkTrailerId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setTrailerSubtypeCode(String s) { msTrailerSubtypeCode = s; }
    public void setTrailerSubtypeName(String s) { msTrailerSubtypeName = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkTrailerId() { return mnPkTrailerId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPlate() { return msPlate; }
    public String getTrailerSubtypeCode() { return msTrailerSubtypeCode; }
    public String getTrailerSubtypeName() { return msTrailerSubtypeName; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTrailerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTrailerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTrailerId = 0;
        msCode = "";
        msName = "";
        msPlate = "";
        msTrailerSubtypeCode = "";
        msTrailerSubtypeName = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
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
        return "WHERE id_trail = " + mnPkTrailerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_trail = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTrailerId = 0;

        msSql = "SELECT COALESCE(MAX(id_trail), 0) + 1 "
                + "FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTrailerId = resultSet.getInt(1);
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
            mnPkTrailerId = resultSet.getInt("id_trail");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPlate = resultSet.getString("plate");
            msTrailerSubtypeCode = resultSet.getString("trail_stp_code");
            msTrailerSubtypeName = resultSet.getString("trail_stp_name");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
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
                    mnPkTrailerId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msPlate + "', " + 
                    "'" + msTrailerSubtypeCode + "', " + 
                    "'" + msTrailerSubtypeName + "', " + 
                    (mbUpdatable ? 1 : 0) + ", " + 
                    (mbDisableable ? 1 : 0) + ", " + 
                    (mbDeletable ? 1 : 0) + ", " + 
                    (mbDisabled ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_trail = " + mnPkTrailerId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "plate = '" + msPlate + "', " +
                    "trail_stp_code = '" + msTrailerSubtypeCode + "', " +
                    "trail_stp_name = '" + msTrailerSubtypeName + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
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
    public DDbTrailer clone() throws CloneNotSupportedException {
        DDbTrailer registry = new DDbTrailer();

        registry.setPkTrailerId(this.getPkTrailerId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPlate(this.getPlate());
        registry.setTrailerSubtypeCode(this.getTrailerSubtypeCode());
        registry.setTrailerSubtypeName(this.getTrailerSubtypeName());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
