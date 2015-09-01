/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistrySys;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbSysCurrency extends DDbRegistrySys {

    protected int mnPkCurrencyId;
    protected String msCode;
    protected String msName;
    /*
    protected int mnSortingPos;
    */
    protected String msCurrencySingular;
    protected String msCurrencyPlural;
    protected String msCurrencyPrefix;
    protected String msCurrencySuffix;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    */

    protected Vector<DDbSysCurrencyDenomination> mvChildDenominations;

    public DDbSysCurrency() {
        super(DModConsts.CS_CUR);
        mvChildDenominations = new Vector<DDbSysCurrencyDenomination>();
        initRegistry();
    }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getSortingPos() { return mnSortingPos; }
    public String getCurrencySingular() { return msCurrencySingular; }
    public String getCurrencyPlural() { return msCurrencyPlural; }
    public String getCurrencyPrefix() { return msCurrencyPrefix; }
    public String getCurrencySuffix() { return msCurrencySuffix; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    public Vector<DDbSysCurrencyDenomination> getChildDenominations() { return mvChildDenominations; }

    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCurrencyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCurrencyId = 0;
        msCode = "";
        msName = "";
        mnSortingPos = 0;
        msCurrencySingular = "";
        msCurrencyPlural = "";
        msCurrencyPrefix = "";
        msCurrencySuffix = "";
        mbDeleted = false;
        mnFkUserId = 0;
        mtTsUser = null;

        mvChildDenominations.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cur = " + mnPkCurrencyId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cur = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkCurrencyId = resultSet.getInt("id_cur");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnSortingPos = resultSet.getInt("sort");
            msCurrencySingular = resultSet.getString("cur_sng");
            msCurrencyPlural = resultSet.getString("cur_plr");
            msCurrencyPrefix = resultSet.getString("cur_pref");
            msCurrencySuffix = resultSet.getString("cur_suff");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_den FROM " + DModConsts.TablesMap.get(DModConsts.CS_CUR_DEN) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbSysCurrencyDenomination child = new DDbSysCurrencyDenomination();
                child.read(session, new int[] { mnPkCurrencyId, resultSet.getInt(1) });
                mvChildDenominations.add(child);
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbSysCurrency clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
