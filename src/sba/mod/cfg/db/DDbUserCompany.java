/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbUserCompany extends DDbRegistry {

    protected int mnPkUserId;
    protected int mnPkBizPartnerId;
    protected boolean mbUniversal;

    protected Vector<DDbUserBranch> mvChildBranches;

    public DDbUserCompany() {
        super(DModConsts.CU_USR_CO);
        mvChildBranches = new Vector<>();
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setUniversal(boolean b) { mbUniversal = b; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public boolean isUniversal() { return mbUniversal; }

    public Vector<DDbUserBranch> getChildBranches() { return mvChildBranches; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkBizPartnerId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnPkBizPartnerId = 0;
        mbUniversal = false;

        mvChildBranches.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND " +
                "id_bpr = " + pk[1] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mbUniversal = resultSet.getBoolean("b_unv");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_bra FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_BRA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbUserBranch child = new DDbUserBranch();
                child.read(session, new int[] { mnPkUserId, mnPkBizPartnerId, resultSet.getInt(1) });
                mvChildBranches.add(child);
            }

            // Finish registry reading:

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
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkUserId + ", " +
                    mnPkBizPartnerId + ", " +
                    (mbUniversal ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_usr = " + mnPkUserId + ", " +
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    */
                    "b_unv = " + (mbUniversal ? 1 : 0) + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (DDbUserBranch child : mvChildBranches) {
            child.setPkUserId(mnPkUserId);
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbUserCompany clone() throws CloneNotSupportedException {
        DDbUserCompany registry = new DDbUserCompany();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setUniversal(this.isUniversal());

        for (DDbUserBranch child : mvChildBranches) {
            registry.getChildBranches().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
