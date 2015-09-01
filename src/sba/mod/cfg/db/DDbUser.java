/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiSession;
import sba.lib.gui.DGuiSessionCustom;
import sba.lib.gui.DGuiUser;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.trn.db.DDbDpsSeriesBranch;
import sba.mod.trn.db.DDbDpsSeriesNumber;

/**
 *
 * @author Sergio Flores
 */
public class DDbUser extends DDbRegistryUser implements DGuiUser {

    public static final int FIELD_PASSWORD = DDbRegistry.FIELD_BASE + 1;

    protected int mnPkUserId;
    protected String msName;
    protected String msPassword;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkUserTypeId;
    protected int mnFkUserCustomTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DDbUserCompany moChildCompany;
    protected Vector<DDbUserPrivilege> mvChildPrivileges;

    protected HashMap<Integer, Integer> moPrivilegesMap;
    protected HashSet<Integer> moModulesSet;

    protected Vector<DDbBranch> mvAuxBranches;
    protected Vector<DDbBranchCash> mvAuxBranchCashes;
    protected Vector<DDbBranchWarehouse> mvAuxBranchWarehouses;
    protected Vector<DDbDpsSeriesBranch> mvAuxBranchDpsSeries;
    protected Vector<DDbDpsSeriesNumber> mvAuxBranchDpsSeriesNumbers;

    public DDbUser() {
        super(DModConsts.CU_USR);
        mvChildPrivileges = new Vector<DDbUserPrivilege>();
        moPrivilegesMap = new HashMap<Integer, Integer>();
        moModulesSet = new HashSet<Integer>();
        mvAuxBranches = new Vector<DDbBranch>();
        mvAuxBranchCashes = new Vector<DDbBranchCash>();
        mvAuxBranchWarehouses = new Vector<DDbBranchWarehouse>();
        mvAuxBranchDpsSeries = new Vector<DDbDpsSeriesBranch>();
        mvAuxBranchDpsSeriesNumbers = new Vector<DDbDpsSeriesNumber>();
        initRegistry();
    }

    private void computePrivilegesCollections(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        moPrivilegesMap.clear();
        moModulesSet.clear();

        msSql = "SELECT u.id_prv, u.fk_lev, p.fk_mod " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_PRV) + " AS u " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_PRV) + " AS p ON " +
                "u.id_prv = p.id_prv " +
                "WHERE u.id_usr = " + mnPkUserId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {
            moPrivilegesMap.put(resultSet.getInt(1), resultSet.getInt(2));
            moModulesSet.add(resultSet.getInt(3));
        }
    }

    private boolean isPrivilegeForSupervisor(int privilege) {
        return privilege == DModSysConsts.CS_PRV_CFG_USR || privilege == DModSysConsts.CS_PRV_CFG_SYS;
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setName(String s) { msName = s; }
    public void setPassword(String s) { msPassword = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserTypeId(int n) { mnFkUserTypeId = n; }
    public void setFkUserCustomTypeId(int n) { mnFkUserCustomTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkUserId() { return mnPkUserId; }
    public String getName() { return msName; }
    public String getPassword() { return msPassword; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserTypeId() { return mnFkUserTypeId; }
    public int getFkUserCustomTypeId() { return mnFkUserCustomTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setChildCompany(DDbUserCompany o) { moChildCompany = o; }

    public DDbUserCompany getChildCompany() { return moChildCompany; }
    public Vector<DDbUserPrivilege> getChildPrivileges() { return mvChildPrivileges; }

    public DDbUserBranch getChildCompanyBranch(int [] branchKey) {
        DDbUserBranch userBranch = null;

        for (DDbUserBranch child : moChildCompany.getChildBranches()) {
            if (DLibUtils.compareKeys(branchKey, child.getBranchKey())) {
                userBranch = child;
                break;
            }
        }

        return userBranch;
    }

    public Vector<DDbBranch> getAuxBranches() { return mvAuxBranches; }
    public Vector<DDbBranchCash> getAuxBranchCashes() { return mvAuxBranchCashes; }
    public Vector<DDbBranchWarehouse> getAuxBranchWarehouses() { return mvAuxBranchWarehouses; }
    public Vector<DDbDpsSeriesBranch> getAuxBranchDpsSeries() { return mvAuxBranchDpsSeries; }
    public Vector<DDbDpsSeriesNumber> getAuxBranchDpsSeriesNumbers() { return mvAuxBranchDpsSeriesNumbers; }

    public DDbBranch getAuxBranch(int[] branchKey) {
        DDbBranch branch = null;

        for (DDbBranch aux : mvAuxBranches) {
            if (DLibUtils.compareKeys(branchKey, aux.getPrimaryKey())) {
                branch = aux;
                break;
            }
        }
        return branch;
    }

    public Vector<DDbBranchCash> getAuxBranchCashes(int[] branchKey) {
        Vector<DDbBranchCash> entities = new Vector<DDbBranchCash>();

        for (DDbBranchCash entity : mvAuxBranchCashes) {
            if (DLibUtils.compareKeys(branchKey, entity.getBranchKey())) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public Vector<DDbBranchWarehouse> getAuxBranchWarehouses(int[] branchKey) {
        Vector<DDbBranchWarehouse> entities = new Vector<DDbBranchWarehouse>();

        for (DDbBranchWarehouse entity : mvAuxBranchWarehouses) {
            if (DLibUtils.compareKeys(branchKey, entity.getBranchKey())) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public Vector<DDbDpsSeriesBranch> getAuxBranchDpsSeries(int[] branchKey) {
        Vector<DDbDpsSeriesBranch> entities = new Vector<DDbDpsSeriesBranch>();

        for (DDbDpsSeriesBranch entity : mvAuxBranchDpsSeries) {
            if (DLibUtils.compareKeys(branchKey, entity.getBranchKey())) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public Vector<DDbDpsSeriesBranch> getAuxBranchDpsSeries(int[] branchKey, int[] dpsTypeKey) {
        Vector<DDbDpsSeriesBranch> entities = new Vector<DDbDpsSeriesBranch>();

        for (DDbDpsSeriesBranch entity : mvAuxBranchDpsSeries) {
            if (DLibUtils.compareKeys(branchKey, entity.getBranchKey()) && DLibUtils.compareKeys(dpsTypeKey, entity.getDbDpsTypeKey())) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public Vector<DDbDpsSeriesNumber> getAuxBranchDpsSeriesNumbers(int series) {
        Vector<DDbDpsSeriesNumber> entities = new Vector<DDbDpsSeriesNumber>();

        for (DDbDpsSeriesNumber entity : mvAuxBranchDpsSeriesNumbers) {
            if (entity.getPkSeriesId() == series) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public boolean hasUniversalAccess() {
        return isAdministrator() || (moChildCompany != null && moChildCompany.isUniversal());
    }

    public boolean hasUniversalAccessToBranchCashes(int[] branchKey) {
        boolean hasAccess = hasUniversalAccess();
        DDbUserBranch userBranch = null;

        if (!hasAccess) {
            userBranch = getChildCompanyBranch(branchKey);
            if (userBranch != null) {
                hasAccess = userBranch.isUniversalCash();
            }
        }

        return hasAccess;
    }

    public boolean hasUniversalAccessToBranchWarehouses(int[] branchKey) {
        boolean hasAccess = hasUniversalAccess();
        DDbUserBranch userBranch = null;

        if (!hasAccess) {
            userBranch = getChildCompanyBranch(branchKey);
            if (userBranch != null) {
                hasAccess = userBranch.isUniversalWarehouse();
            }
        }

        return hasAccess;
    }

    public boolean hasUniversalAccessToBranchDpsSeries(int[] branchKey) {
        boolean hasAccess = hasUniversalAccess();
        DDbUserBranch userBranch = null;

        if (!hasAccess) {
            userBranch = getChildCompanyBranch(branchKey);
            if (userBranch != null) {
                hasAccess = userBranch.isUniversalDpsSeries();
            }
        }

        return hasAccess;
    }

    @Override
    public boolean isAdministrator() {
        return isSupervisor() || mnFkUserTypeId == DModSysConsts.CS_USR_TP_ADM;
    }

    @Override
    public boolean isSupervisor() {
        return mnFkUserTypeId == DModSysConsts.CS_USR_TP_SUP;
    }

    @Override
    public boolean hasModuleAccess(final int module) {
        return isAdministrator() || moModulesSet.contains(module);
    }

    @Override
    public boolean hasPrivilege(final int privilege) {
        boolean hasPrivilege = false;

        if (isSupervisor()) {
            hasPrivilege = true;
        }
        else if (isAdministrator() && !isPrivilegeForSupervisor(privilege)) {
            hasPrivilege = true;
        }
        else {
            hasPrivilege = moPrivilegesMap.get(privilege) != null;
        }

        return hasPrivilege;
    }

    @Override
    public boolean hasPrivilege(final int[] privileges) {
        boolean hasPrivilege = false;

        for (int privilege : privileges) {
            if (hasPrivilege(privilege)) {
                hasPrivilege = true;
                break;
            }
        }

        return hasPrivilege;
    }

    @Override
    public int getPrivilegeLevel(final int privilege) {
        int level = DLibConsts.UNDEFINED;

        if (isSupervisor()) {
            level = DModSysConsts.CS_LEV_EDT;
        }
        else if (isAdministrator() && !isPrivilegeForSupervisor(privilege)) {
            level = DModSysConsts.CS_LEV_EDT;
        }
        else {
            if (moPrivilegesMap.get(privilege) != null) {
                level = moPrivilegesMap.get(privilege);
            }
        }

        return level;
    }

    @Override
    public HashMap<Integer, Integer> getPrivilegesMap() {
        return moPrivilegesMap;
    }

    @Override
    public HashSet<Integer> getModulesSet() {
        return moModulesSet;
    }

    @Override
    public void computeAccess(DGuiSession session) throws SQLException, Exception {
        Statement statement = null;
        Statement statementAux = null;
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;

        /*
         * Read auxiliar objects:
         * a) All available branches for current user.
         * b) All available branch entities for current user.
         */

        statement = session.getStatement().getConnection().createStatement();
        statementAux = session.getStatement().getConnection().createStatement();

        if (hasUniversalAccess()) {
            // Read all available branches:

            msSql = "SELECT bb.id_bpr, bb.id_bra " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb " +
                    "WHERE bb.b_dis = 0 AND bb.b_del = 0 AND bb.id_bpr = " + DUtilConsts.BPR_CO_ID + " " +
                    "ORDER BY bb.name, bb.id_bpr, bb.id_bra ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranch branch = new DDbBranch();
                branch.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                mvAuxBranches.add(branch);
            }

            // Read all available branch cashes:

            msSql = "SELECT e.id_bpr, e.id_bra, e.id_csh " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS e " +
                    "WHERE e.b_dis = 0 AND e.b_del = 0 " +
                    "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_csh ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranchCash entity = new DDbBranchCash();
                entity.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                mvAuxBranchCashes.add(entity);
            }

            // Read all available branch warehouses:

            msSql = "SELECT e.id_bpr, e.id_bra, e.id_wah " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS e " +
                    "WHERE e.b_dis = 0 AND e.b_del = 0 " +
                    "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_wah ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranchWarehouse entity = new DDbBranchWarehouse();
                entity.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                mvAuxBranchWarehouses.add(entity);
            }

            // Read all available branch DPS series:

            msSql = "SELECT e.id_ser, e.id_bpr, e.id_bra " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS e " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON e.id_ser = s.id_ser " +
                    "WHERE s.b_dis = 0 AND s.b_del = 0 " +
                    "ORDER BY s.fk_dps_ct, s.fk_dps_cl, s.fk_dps_tp, s.ser, e.id_ser, e.id_bpr, e.id_bra ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsSeriesBranch entity = new DDbDpsSeriesBranch();
                entity.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                mvAuxBranchDpsSeries.add(entity);
            }

            // Read all available branch DPS series numbers:

            msSql = "SELECT sn.id_ser, sn.id_num " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS e " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON e.id_ser = s.id_ser " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER_NUM) + " AS sn ON s.id_ser = sn.id_ser " +
                    "WHERE s.b_dis = 0 AND s.b_del = 0 AND sn.b_dis = 0 AND sn.b_del = 0 " +
                    "ORDER BY s.fk_dps_ct, s.fk_dps_cl, s.fk_dps_tp, s.ser, sn.num_sta, sn.id_ser, sn.id_num ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsSeriesNumber entity = new DDbDpsSeriesNumber();
                entity.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                mvAuxBranchDpsSeriesNumbers.add(entity);
            }
        }
        else {
            DDbUserBranch userBranch = null;

            // Read user available branches:

            msSql = "SELECT bb.id_bpr, bb.id_bra " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS bb " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_BRA) + " AS ub ON " +
                    "bb.id_bpr = ub.id_bpr AND bb.id_bra = ub.id_bra AND ub.id_usr = " + mnPkUserId + " " +
                    "WHERE bb.b_dis = 0 AND bb.b_del = 0 AND ub.id_bpr = " + DUtilConsts.BPR_CO_ID + " " +
                    "ORDER BY bb.name, bb.id_bpr, bb.id_bra ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranch branch = new DDbBranch();
                branch.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                mvAuxBranches.add(branch);

                userBranch = getChildCompanyBranch(branch.getPrimaryKey());

                if (userBranch != null) {
                    // Get all available branch cashes in current branch:

                    msSql = "SELECT e.id_bpr, e.id_bra, e.id_csh " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS e " +
                            (userBranch.isUniversalCash() ? "" : " " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_CSH) + " AS ue ON " +
                            "e.id_bpr = ue.id_bpr AND e.id_bra = ue.id_bra AND e.id_csh = ue.id_csh AND ue.id_usr = " + mnPkUserId + " ") +
                            "WHERE e.b_dis = 0 AND e.b_del = 0 AND " +
                            "e.id_bpr = " + branch.getPkBizPartnerId() + " AND e.id_bra = " + branch.getPkBranchId() + " " +
                            "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_csh ";
                    resultSetAux = statementAux.executeQuery(msSql);
                    while (resultSetAux.next()) {
                        DDbBranchCash entity = new DDbBranchCash();
                        entity.read(session, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                        mvAuxBranchCashes.add(entity);
                    }

                    // Get all available branch warehouses in current branch:

                    msSql = "SELECT e.id_bpr, e.id_bra, e.id_wah " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS e " +
                            (userBranch.isUniversalWarehouse() ? "" : " " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_WAH) + " AS ue ON " +
                            "e.id_bpr = ue.id_bpr AND e.id_bra = ue.id_bra AND e.id_wah = ue.id_wah AND ue.id_usr = " + mnPkUserId + " ") +
                            "WHERE e.b_dis = 0 AND e.b_del = 0 AND " +
                            "e.id_bpr = " + branch.getPkBizPartnerId() + " AND e.id_bra = " + branch.getPkBranchId() + " " +
                            "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_wah ";
                    resultSetAux = statementAux.executeQuery(msSql);
                    while (resultSetAux.next()) {
                        DDbBranchWarehouse entity = new DDbBranchWarehouse();
                        entity.read(session, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                        mvAuxBranchWarehouses.add(entity);
                    }

                    // Get all available branch DPS series in current branch:

                    msSql = "SELECT e.id_ser, e.id_bpr, e.id_bra " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS e " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON e.id_ser = s.id_ser " +
                            (userBranch.isUniversalDpsSeries() ? "" : " " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " AS ue ON " +
                            "e.id_ser = ue.id_ser AND e.id_bpr = ue.id_bpr AND e.id_bra = ue.id_bra AND ue.id_usr = " + mnPkUserId + " ") +
                            "WHERE s.b_dis = 0 AND s.b_del = 0 AND " +
                            "e.id_bpr = " + branch.getPkBizPartnerId() + " AND e.id_bra = " + branch.getPkBranchId() + " " +
                            "ORDER BY s.fk_dps_ct, s.fk_dps_cl, s.fk_dps_tp, s.ser, e.id_ser, e.id_bpr, e.id_bra ";
                    resultSetAux = statementAux.executeQuery(msSql);
                    while (resultSetAux.next()) {
                        DDbDpsSeriesBranch entity = new DDbDpsSeriesBranch();
                        entity.read(session, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                        mvAuxBranchDpsSeries.add(entity);
                    }

                    // Get all available branch DPS series numbers in current branch:

                    msSql = "SELECT sn.id_ser, sn.id_num " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS e " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON e.id_ser = s.id_ser " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER_NUM) + " AS sn ON s.id_ser = sn.id_ser " +
                            (userBranch.isUniversalDpsSeries() ? "" : " " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " AS ue ON " +
                            "e.id_ser = ue.id_ser AND e.id_bpr = ue.id_bpr AND e.id_bra = ue.id_bra AND ue.id_usr = " + mnPkUserId + " ") +
                            "WHERE s.b_dis = 0 AND s.b_del = 0 AND sn.b_dis = 0 AND sn.b_del = 0 AND " +
                            "e.id_bpr = " + branch.getPkBizPartnerId() + " AND e.id_bra = " + branch.getPkBranchId() + " " +
                            "ORDER BY s.fk_dps_ct, s.fk_dps_cl, s.fk_dps_tp, s.ser, sn.num_sta, sn.id_ser, sn.id_num ";
                    resultSetAux = statementAux.executeQuery(msSql);
                    while (resultSetAux.next()) {
                        DDbDpsSeriesNumber entity = new DDbDpsSeriesNumber();
                        entity.read(session, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2) });
                        mvAuxBranchDpsSeriesNumbers.add(entity);
                    }
                }
            }
        }
    }

    @Override
    public DGuiSessionCustom createDefaultUserSession(final DGuiClient client) {
        return createDefaultUserSession(client, 0);
    }

    @Override
    public DGuiSessionCustom createDefaultUserSession(final DGuiClient client, final int terminal) {
        int[] branchKey = null;
        Vector<DDbBranchCash> branchCashes = null;
        Vector<DDbBranchWarehouse> branchWarehouses = null;
        Vector<DDbDpsSeriesBranch> branchDpsSeriesBranches = null;
        DGuiClientSessionCustom sessionCustom = new DGuiClientSessionCustom(client);

        if (mvAuxBranches.size() == 1) {
            branchKey = mvAuxBranches.get(0).getPrimaryKey();
            sessionCustom.setBranchKey(branchKey);
        }

        branchCashes = getAuxBranchCashes(branchKey);
        if (branchCashes.size() == 1) {
            sessionCustom.setBranchCashKey(branchCashes.get(0).getPrimaryKey());
        }

        branchWarehouses = getAuxBranchWarehouses(branchKey);
        if (branchWarehouses.size() == 1) {
            sessionCustom.setBranchWarehouseKey(branchWarehouses.get(0).getPrimaryKey());
        }

        branchDpsSeriesBranches = getAuxBranchDpsSeries(branchKey);
        if (branchDpsSeriesBranches.size() == 1) {
            sessionCustom.setBranchDpsSeriesKey(branchDpsSeriesBranches.get(0).getPrimaryKey());
        }

        sessionCustom.setTerminal(terminal);

        return sessionCustom;
    }

    @Override
    public boolean showUserSessionConfigOnLogin() {
        boolean show = false;
        int[] branchKey = null;
        Vector<DDbBranchCash> branchCashes = null;
        Vector<DDbBranchWarehouse> branchWarehouses = null;

        if (mvAuxBranches.size() != 1) {
            show = true;
        }
        else {
            branchKey = mvAuxBranches.get(0).getPrimaryKey();

            branchCashes = getAuxBranchCashes(branchKey);
            branchWarehouses = getAuxBranchWarehouses(branchKey);

            if (branchCashes.size() > 1 || branchWarehouses.size() > 1) {
                show = true;
            }
        }

        return show;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        msName = "";
        msPassword = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserTypeId = 0;
        mnFkUserCustomTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moChildCompany = null;
        mvChildPrivileges.clear();

        moPrivilegesMap.clear();
        moModulesSet.clear();

        mvAuxBranches.clear();
        mvAuxBranchCashes.clear();
        mvAuxBranchWarehouses.clear();
        mvAuxBranchDpsSeries.clear();
        mvAuxBranchDpsSeriesNumbers.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkUserId = 0;

        msSql = "SELECT COALESCE(MAX(id_usr), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkUserId = resultSet.getInt(1);
        }
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
            msName = resultSet.getString("name");
            //msPassword = resultSet.getString("pswd");     // stored value is a string digestion, so it is useless
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserTypeId = resultSet.getInt("fk_usr_tp");
            mnFkUserCustomTypeId = resultSet.getInt("fk_usr_ctm_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_bpr FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_CO) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moChildCompany = new DDbUserCompany();
                moChildCompany.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
            }

            msSql = "SELECT id_prv FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_PRV) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbUserPrivilege child = new DDbUserPrivilege();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1) });
                mvChildPrivileges.add(child);
            }

            computePrivilegesCollections(session);

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
                    mnPkUserId + ", " +
                    "'" + msName + "', " +
                    "PASSWORD('" + msPassword + "'), " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserTypeId + ", " +
                    mnFkUserCustomTypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_usr = " + mnPkUserId + ", " +
                    "name = '" + msName + "', " +
                    (msPassword.length() == 0 ? "" : "pswd = PASSWORD('" + msPassword + "'), ") +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_usr_tp = " + mnFkUserTypeId + ", " +
                    "fk_usr_ctm_tp = " + mnFkUserCustomTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_CSH) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_WAH) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_BRA) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_CO) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_PRV) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        moChildCompany.setPkUserId(mnPkUserId);
        moChildCompany.setRegistryNew(true);
        moChildCompany.save(session);

        for (DDbUserPrivilege child : mvChildPrivileges) {
            child.setPkUserId(mnPkUserId);
            child.setRegistryNew(true);
            child.save(session);
        }

        computePrivilegesCollections(session);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbUser clone() throws CloneNotSupportedException {
        DDbUser registry = new DDbUser();

        registry.setPkUserId(this.getPkUserId());
        registry.setName(this.getName());
        registry.setPassword(this.getPassword());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserTypeId(this.getFkUserTypeId());
        registry.setFkUserCustomTypeId(this.getFkUserCustomTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setChildCompany(moChildCompany == null ? null : moChildCompany.clone());

        for (DDbUserPrivilege child : mvChildPrivileges) {
            registry.getChildPrivileges().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_PASSWORD:
                msSql += "pswd = PASSWORD('" + value + "') ";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = DDbConsts.SAVE_OK;
    }
}
