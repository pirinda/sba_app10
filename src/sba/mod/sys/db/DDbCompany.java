/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.sys.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbCompany extends DDbRegistry {

    protected int mnPkCompanyId;
    protected String msCompany;
    protected String msDatabase;
    protected boolean mbDeleted;
    protected Date mtTsInsert;
    protected Date mtTsUpdate;

    public DDbCompany() {
        super(DModConsts.SU_CO);
        initRegistry();
    }

    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setCompany(String s) { msCompany = s; }
    public void setDatabase(String s) { msDatabase = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setTsInsert(Date t) { mtTsInsert = t; }
    public void setTsUpdate(Date t) { mtTsUpdate = t; }

    public int getPkCompanyId() { return mnPkCompanyId; }
    public String getCompany() { return msCompany; }
    public String getDatabase() { return msDatabase; }
    public boolean isDeleted() { return mbDeleted; }
    public Date getTsInsert() { return mtTsInsert; }
    public Date getTsUpdate() { return mtTsUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkCompanyId = 0;
        msCompany = "";
        msDatabase = "";
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
        return "WHERE id_co = " + mnPkCompanyId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_co = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(final DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCompanyId = 0;

        msSql = "SELECT COALESCE(MAX(id_co), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCompanyId = resultSet.getInt(1);
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
            mnPkCompanyId = resultSet.getInt("id_co");
            msCompany = resultSet.getString("co");
            msDatabase = resultSet.getString("db");
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
                    mnPkCompanyId + ", " +
                    "'" + msCompany + "', " +
                    "'" + msDatabase + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_co = " + mnPkCompanyId + ", " +
                    "co = '" + msCompany + "', " +
                    "db = '" + msDatabase + "', " +
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
    public DDbCompany clone() throws CloneNotSupportedException {
        DDbCompany registry = new DDbCompany();

        registry.setPkCompanyId(this.getPkCompanyId());
        registry.setCompany(this.getCompany());
        registry.setDatabase(this.getDatabase());
        registry.setDeleted(this.isDeleted());
        registry.setTsInsert(this.getTsInsert());
        registry.setTsUpdate(this.getTsUpdate());

        registry.setRegistryNew(this.isRegistryEdited());
        return registry;
    }
}
