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
public class DDbSysTaxRegime extends DDbRegistrySys {

    protected int mnPkTaxRegimeId;
    protected String msCode;
    protected String msName;
    //protected int mnSortingPos;
    protected boolean mbPerson;
    protected boolean mbOrganization;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserId;
    protected Date mtTsUser;
    */

    public DDbSysTaxRegime() {
        super(DModConsts.CS_TAX_REG);
        initRegistry();
    }

    public int getPkTaxRegimeId() { return mnPkTaxRegimeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isPerson() { return mbPerson; }
    public boolean isOrganization() { return mbOrganization; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxRegimeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTaxRegimeId = 0;
        msCode = "";
        msName = "";
        mnSortingPos = 0;
        mbPerson = false;
        mbOrganization = false;
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
        return "WHERE id_tax_reg = " + mnPkTaxRegimeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax_reg = " + pk[0] + " ";
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
            mnPkTaxRegimeId = resultSet.getInt("id_tax_reg");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnSortingPos = resultSet.getInt("sort");
            mbPerson = resultSet.getBoolean("b_per");
            mbOrganization = resultSet.getBoolean("b_org");
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
    public DDbSysTaxRegime clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
