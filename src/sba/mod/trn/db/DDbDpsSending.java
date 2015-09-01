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
public class DDbDpsSending extends DDbRegistryUser {

    protected int mnPkDpsId;
    protected int mnPkSendingId;
    protected String msContact1;
    protected String msContact2;
    protected String msEmail1;
    protected String msEmail2;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected boolean mbHasDpsChanged;

    public DDbDpsSending() {
        super(DModConsts.T_DPS_SND);
        initRegistry();
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkSendingId(int n) { mnPkSendingId = n; }
    public void setContact1(String s) { msContact1 = s; }
    public void setContact2(String s) { msContact2 = s; }
    public void setEmail1(String s) { msEmail1 = s; }
    public void setEmail2(String s) { msEmail2 = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkSendingId() { return mnPkSendingId; }
    public String getContact1() { return msContact1; }
    public String getContact2() { return msContact2; }
    public String getEmail1() { return msEmail1; }
    public String getEmail2() { return msEmail2; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkSendingId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkSendingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        mnPkSendingId = 0;
        msContact1 = "";
        msContact2 = "";
        msEmail1 = "";
        msEmail2 = "";
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
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_snd = " + mnPkSendingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_snd = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSendingId = 0;

        msSql = "SELECT COALESCE(MAX(id_snd), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSendingId = resultSet.getInt(1);
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
            mnPkDpsId = resultSet.getInt("id_dps");
            mnPkSendingId = resultSet.getInt("id_snd");
            msContact1 = resultSet.getString("con_1");
            msContact2 = resultSet.getString("con_2");
            msEmail1 = resultSet.getString("ema_1");
            msEmail2 = resultSet.getString("ema_2");
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
        DDbDps dps = null;

        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    mnPkSendingId + ", " +
                    "'" + msContact1 + "', " +
                    "'" + msContact2 + "', " +
                    "'" + msEmail1 + "', " +
                    "'" + msEmail2 + "', " +
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
                    "id_dps = " + mnPkDpsId + ", " +
                    "id_snd = " + mnPkSendingId + ", " +
                    */
                    "con_1 = '" + msContact1 + "', " +
                    "con_2 = '" + msContact2 + "', " +
                    "ema_1 = '" + msEmail1 + "', " +
                    "ema_2 = '" + msEmail2 + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public DDbDpsSending clone() throws CloneNotSupportedException {
        DDbDpsSending registry = new DDbDpsSending();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkSendingId(this.getPkSendingId());
        registry.setContact1(this.getContact1());
        registry.setContact2(this.getContact2());
        registry.setEmail1(this.getEmail1());
        registry.setEmail2(this.getEmail2());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
