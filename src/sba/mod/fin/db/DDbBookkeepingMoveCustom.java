/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBookkeepingMoveCustom extends DDbRegistryUser {

    protected int mnPkBookkeepingYearId;
    protected int mnPkBookkeepingNumberId;
    protected Date mtDate;
    protected int mnFkSystemMoveClassId;
    protected int mnFkSystemMoveTypeId;
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;

    protected DDbBookkeepingNumber moRegNumber;

    protected Vector<DDbBookkeepingMove> mvChildMoves;

    public DDbBookkeepingMoveCustom() {
        super(DModConsts.FX_BKK_CTM);
        mvChildMoves = new Vector<>();
        initRegistry();
    }

    public void setPkBookkeepingYearId(int n) { mnPkBookkeepingYearId = n; }
    public void setPkBookkeepingNumberId(int n) { mnPkBookkeepingNumberId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setFkSystemMoveClassId(int n) { mnFkSystemMoveClassId = n; }
    public void setFkSystemMoveTypeId(int n) { mnFkSystemMoveTypeId = n; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }

    public int getPkBookkeepingYearId() { return mnPkBookkeepingYearId; }
    public int getPkBookkeepingNumberId() { return mnPkBookkeepingNumberId; }
    public Date getDate() { return mtDate; }
    public int getFkSystemMoveClassId() { return mnFkSystemMoveClassId; }
    public int getFkSystemMoveTypeId() { return mnFkSystemMoveTypeId; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }

    public void setRegNumber(DDbBookkeepingNumber o) { moRegNumber = o; }

    public DDbBookkeepingNumber getRegNumber() { return moRegNumber; }

    public Vector<DDbBookkeepingMove> getChildMoves() { return mvChildMoves; }

    public DDbBookkeepingMove getChildMoveBySortingPos(int pos) {
        DDbBookkeepingMove move = null;

        for (DDbBookkeepingMove auxMove : mvChildMoves) {
            if (pos == auxMove.getSortingPos()) {
                move = auxMove;
                break;
            }
        }

        return move;
    }

    public int[] getSystemMoveKey() { return new int[] { mnFkSystemMoveClassId, mnFkSystemMoveTypeId }; }
    public int[] getCompanyKey() { return new int[] { mnFkOwnerBizPartnerId }; }
    public int[] getCompanyBranchKey() { return new int[] { mnFkOwnerBizPartnerId, mnFkOwnerBranchId }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBookkeepingYearId = pk[0];
        mnPkBookkeepingNumberId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBookkeepingYearId, mnPkBookkeepingNumberId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBookkeepingYearId = 0;
        mnPkBookkeepingNumberId = 0;
        mtDate = null;
        mnFkSystemMoveClassId = 0;
        mnFkSystemMoveTypeId = 0;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;

        moRegNumber = null;

        mvChildMoves.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(DModConsts.F_BKK);
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        if (mnPkBookkeepingYearId != DLibConsts.UNDEFINED && mnPkBookkeepingNumberId != DLibConsts.UNDEFINED) {
            moRegNumber = new DDbBookkeepingNumber();
            moRegNumber.read(session, getPrimaryKey());
            moRegNumber.setDeleted(true);
            moRegNumber.save(session);
        }

        moRegNumber = new DDbBookkeepingNumber();
        moRegNumber.setPkYearId(mnPkBookkeepingYearId);
        moRegNumber.save(session);

        mnPkBookkeepingNumberId = moRegNumber.getPkNumberId();
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        DDbBookkeepingMove move = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = DDbConsts.READ_ERROR;

        statement = session.getStatement().getConnection().createStatement();

        mnPkBookkeepingYearId = pk[0];
        mnPkBookkeepingNumberId = pk[1];

        msSql = "SELECT dt, b_del, fk_sys_mov_cl, fk_sys_mov_tp, fk_own_bpr, fk_own_bra " +
                "FROM " + getSqlTable() + " " +
                "WHERE fk_bkk_yer_n = " + mnPkBookkeepingYearId + " AND fk_bkk_num_n = " + mnPkBookkeepingNumberId + " ";
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSystemMoveClassId = resultSet.getInt("fk_sys_mov_cl");
            mnFkSystemMoveTypeId = resultSet.getInt("fk_sys_mov_tp");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
        }

        moRegNumber = new DDbBookkeepingNumber();
        moRegNumber.read(session, getPrimaryKey());

        msSql = "SELECT id_yer, id_mov " +
                "FROM " + getSqlTable() + " " +
                "WHERE fk_bkk_yer_n = " + mnPkBookkeepingYearId + " AND fk_bkk_num_n = " + mnPkBookkeepingNumberId + " " +
                "ORDER BY sort ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            move = new DDbBookkeepingMove();
            move.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            mvChildMoves.add(move);
        }

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        DDbBookkeepingMove child = null;

        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        computePrimaryKey(session); // each time custom registry is saved, new bookkeeping number is needed

        for (int i = 0; i < mvChildMoves.size(); i++) {
            child = mvChildMoves.get(i);
            child.setSortingPos(i + 1);
            child.setDate(mtDate);
            child.setFkBookkeepingYearId_n(mnPkBookkeepingYearId);
            child.setFkBookkeepingNumberId_n(mnPkBookkeepingNumberId);
            child.setFkSystemMoveClassId(mnFkSystemMoveClassId);
            child.setFkSystemMoveTypeId(mnFkSystemMoveTypeId);
            child.setFkOwnerBizPartnerId(mnFkOwnerBizPartnerId);
            child.setFkOwnerBranchId(mnFkOwnerBranchId);

            if (child.isRegistryNew()) {
                child.setFkUserInsertId(session.getUser().getPkUserId());
            }
            else {
                child.setFkUserUpdateId(session.getUser().getPkUserId());
            }

            child.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public void delete(final DGuiSession session) throws SQLException, Exception {
        if (mbDeleted) {
            computePrimaryKey(session); // when registry is deleted, this method will reactivate it
        }

        for (DDbBookkeepingMove child : mvChildMoves) {
            child.setDeleted(!mbDeleted);
            child.setFkBookkeepingYearId_n(mnPkBookkeepingYearId);
            child.setFkBookkeepingNumberId_n(mnPkBookkeepingNumberId);
            child.setFkUserUpdateId(session.getUser().getPkUserId());
            child.save(session);
        }

        mnQueryResultId = DDbConsts.SAVE_OK;

        session.notifySuscriptors(mnRegistryType);
    }

    @Override
    public DDbBookkeepingMoveCustom clone() throws CloneNotSupportedException {
        DDbBookkeepingMoveCustom registry = new DDbBookkeepingMoveCustom();

        registry.setPkBookkeepingYearId(this.getPkBookkeepingYearId());
        registry.setPkBookkeepingNumberId(this.getPkBookkeepingNumberId());
        registry.setFkSystemMoveClassId(this.getFkSystemMoveClassId());
        registry.setFkSystemMoveTypeId(this.getFkSystemMoveTypeId());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());

        registry.setRegNumber(this.getRegNumber() == null ? null : this.getRegNumber().clone());

        for (DDbBookkeepingMove child : mvChildMoves) {
            registry.getChildMoves().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbBookkeepingMove child : mvChildMoves) {
            child.setRegistryNew(registryNew);
        }
    }
}
