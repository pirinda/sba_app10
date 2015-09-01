/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistrySys;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbSysCurrencyDenomination extends DDbRegistrySys {

    protected int mnPkCurrencyId;
    protected int mnPkDenominationId;
    protected double mdDenomination;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    */

    public DDbSysCurrencyDenomination() {
        super(DModConsts.CS_CUR_DEN);
        initRegistry();
    }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public int getPkDenominationId() { return mnPkDenominationId; }
    public double getDenomination() { return mdDenomination; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCurrencyId, mnPkDenominationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCurrencyId = 0;
        mnPkDenominationId = 0;
        mdDenomination = 0;
        mbDeleted = false;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cur = " + mnPkCurrencyId + " AND " +
                "id_den = " + mnPkDenominationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cur = " + pk[0] + " AND " +
                "id_den = " + pk[1] + " ";
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
            mnPkCurrencyId = resultSet.getInt("id_cur");
            mnPkDenominationId = resultSet.getInt("id_den");
            mdDenomination = resultSet.getDouble("den");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbSysCurrencyDenomination clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
