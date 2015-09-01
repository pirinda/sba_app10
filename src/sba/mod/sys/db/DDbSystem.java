/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.sys.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbSystem extends DDbRegistry {

    public static final int FIELD_VERSION = 1;

    protected int mnPkSystemId;
    protected int mnVersion;
    protected Date mtVersionTs;
    protected boolean mbDeleted;
    protected Date mtTsInsert;
    protected Date mtTsUpdate;

    public DDbSystem() {
        super(DModConsts.SU_SYS);
        initRegistry();
    }

    public void setPkSystemId(int n) { mnPkSystemId = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(Date t) { mtVersionTs = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setTsInsert(Date t) { mtTsInsert = t; }
    public void setTsUpdate(Date t) { mtTsUpdate = t; }

    public int getPkSystemId() { return mnPkSystemId; }
    public int getVersion() { return mnVersion; }
    public Date getVersionTs() { return mtVersionTs; }
    public boolean isDeleted() { return mbDeleted; }
    public Date getTsInsert() { return mtTsInsert; }
    public Date getTsUpdate() { return mtTsUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSystemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSystemId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkSystemId = 0;
        mnVersion = 0;
        mtVersionTs = null;
        mbDeleted = false;
        mtTsInsert = null;
        mtTsUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_sys = " + mnPkSystemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sys = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(final DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSystemId = 0;

        msSql = "SELECT COALESCE(MAX(id_sys), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSystemId = resultSet.getInt(1);
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
            mnPkSystemId = resultSet.getInt("id_sys");
            mnVersion = resultSet.getInt("ver");
            mtVersionTs = resultSet.getTimestamp("ver_ts");
            mbDeleted = resultSet.getBoolean("b_del");
            mtTsInsert = resultSet.getTimestamp("ts_ins");
            mtTsUpdate = resultSet.getTimestamp("ts_upd");

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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSystemId + ", " +
                    mnVersion + ", " +
                    "NOW()" + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_sys = " + mnPkSystemId + ", " +
                    //"ver = " + mnVersion + ", " +
                    //"ver_ts = " + "NOW()" + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"ts_ins = " + "NOW()" + ", " +
                    "ts_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbSystem clone() throws CloneNotSupportedException {
        DDbSystem registry = new DDbSystem();

        registry.setPkSystemId(this.getPkSystemId());
        registry.setVersion(this.getVersion());
        registry.setVersionTs(this.getVersionTs());
        registry.setDeleted(this.isDeleted());
        registry.setTsInsert(this.getTsInsert());
        registry.setTsUpdate(this.getTsUpdate());

        registry.setRegistryNew(this.isRegistryEdited());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_VERSION:
                msSql += "ver = " + value + ", ver_ts = NOW() ";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = DDbConsts.SAVE_OK;
    }
}
