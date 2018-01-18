/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DLockUtils {
    
    /**
     * Checks if registry is available, that is, if it is not already locked.
     * @param session GUI session.
     * @param registryType Registry type.
     * @param registryId Registry ID.
     * @return <code>true</code> if registry is availaboe, otherwise <code>false</code>.
     * @throws Exception 
     */
    private static boolean isRegistryAvailable(final DGuiSession session, final int registryType, final int registryId) throws Exception {
        String sql = "SELECT id_lock, lock_tout, lock_ts, fk_usr_ins, "
                + "ADDTIME(lock_ts, CONCAT('00:', lock_tout)) AS _tout, "
                + "TIMEDIFF(ADDTIME(lock_ts, CONCAT('00:', lock_tout)), NOW()) AS _tout_time "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.C_LOCK) + " "
                + "WHERE reg_tp_id = " + registryType + " AND reg_id = " + registryId + " AND NOT b_del AND lock_st = " + DLockConsts.LOCK_ST_ACTIVE + " AND "
                + "ADDTIME(lock_ts, CONCAT('00:', lock_tout)) >= NOW() "
                + "ORDER BY id_lock;";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            throw new Exception("El registro solicitado está bloqueado con el folio de acceso exclusivo #" + resultSet.getInt("id_lock") + " por el usuario '" + (String) session.readField(DModConsts.CU_USR, new int[] { resultSet.getInt("fk_usr_ins") }, DDbRegistry.FIELD_NAME) + "'.\n"
                    + "El bloqueo de " + resultSet.getInt("lock_tout") + " min. fue realizado el " + DLibUtils.DateFormatDate.format(resultSet.getTimestamp("lock_ts")) + " a las " + DLibUtils.DateFormatTime.format(resultSet.getTimestamp("lock_ts")) + ", "
                    + "y vence el " + DLibUtils.DateFormatDate.format(resultSet.getTimestamp("_tout")) + " a las " + DLibUtils.DateFormatTime.format(resultSet.getTimestamp("_tout")) + ", en " + DLibUtils.DateFormatTime.format(resultSet.getTime("_tout_time")) + ".");
        }
        
        return true;
    }
    
    /**
     * Creates an exclusive-access lock, if possible.
     * @param session GUI session.
     * @param registryType Registry type to lock.
     * @param registryId Registry ID to lock.
     * @param timeout Timeout of lock in minutes.
     * @return Created lock.
     * @throws Exception 
     */
    public static DDbLock createLock(final DGuiSession session, final int registryType, final int registryId, final int timeout) throws Exception {
        DDbLock lock = null;
        
        if (isRegistryAvailable(session, registryType, registryId)) {
            lock = new DDbLock();
            //lock.setPkLockId(...);
            lock.setRegistryTypeId(registryType);
            lock.setRegistryId(registryId);
            lock.setLockTimeout(timeout);
            //lock.setLockTimestamp(...);
            lock.setLockStatus(DLockConsts.LOCK_ST_ACTIVE);
            //lock.setDeleted(...);
            //lock.setFkUserInsertId(...);
            //lock.setFkUserUpdateId(...);
            //lock.setTsUserInsert(...);
            //lock.setTsUserUpdate(...);
            lock.save(session);
        }
        
        return lock;
    }
    
    /**
     * Validates if lock is already active, otherwise attempts to recover it, if possible.
     * @param session GUI session.
     * @param lock Lock to validate.
     * @throws Exception 
     */
    public static void validateLock(final DGuiSession session, final DDbLock lock) throws Exception {
        //check if lock is still active:
        String sql = "SELECT TIMEDIFF(ADDTIME(lock_ts, CONCAT('00:', lock_tout)), NOW()) > 0 AS _active,"
                + "TIMEDIFF(NOW(), ADDTIME(lock_ts, CONCAT('00:', lock_tout))) AS _tout_time "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.C_LOCK) + " "
                + "WHERE id_lock = " + lock.getPkLockId() + ";";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            if (!resultSet.getBoolean("_active")) {
                int count = 0;
                Date timeout = resultSet.getTimestamp("_tout_time");
                
                //lock is not active, try to recover it.
                
                //check if there are registry updates done after current lock:
                sql = "SELECT COUNT(*) "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.C_LOCK) + " "
                        + "WHERE reg_tp_id = " + lock.getRegistryTypeId() + " AND reg_id = " + lock.getRegistryId() + " AND NOT b_del AND lock_st = " + DLockConsts.LOCK_ST_FREED_UPDATE + " AND "
                        + "id_lock > " + lock.getPkLockId() + " "
                        + "ORDER BY id_lock;";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
                
                if (count > 0) {
                    throw new Exception("El registro solicitado ha sido modificado " + DLibUtils.DecimalFormatInteger.format(count) + " " + (count == 1 ? "vez" : "veces") + " "
                            + "después de haberse perdido el bloqueo actual hace " + DLibUtils.DateFormatTime.format(timeout) + ".\n"
                            + "La captura actual del registro no podrá ser guardada, y se debe cancelar.");
                }
                else {
                    //recover lock if registry is available:
                    try {
                        if (isRegistryAvailable(session, lock.getRegistryTypeId(), lock.getRegistryId())) {
                            lock.setAuxUpdateLockTimestamp(true);
                            lock.save(session);
                        }
                    }
                    catch (Exception e) {
                        throw new Exception("No se pudo recuperar el bloqueo del registro solicitado:\n" + e);
                    }
                }
            }
        }
    }
    
    /**
     * Frees lock with desired freed lock-status.
     * @param session GUI session.
     * @param lock Lock.
     * @param freedLockStatus Freed lock-status.
     * @throws Exception 
     */
    public static void freeLock(final DGuiSession session, final DDbLock lock, final int freedLockStatus) throws Exception {
        if (!DLibUtils.belongsTo(freedLockStatus, new int[] { DLockConsts.LOCK_ST_FREED_CANCEL, DLockConsts.LOCK_ST_FREED_UPDATE })) {
            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        else {
            lock.setLockStatus(freedLockStatus);
            lock.save(session);
        }
    }
    
    /**
     * Deletes active locks of current user.
     * @param session GUI session.
     * @throws Exception 
     */
    public static void deleteActiveLocks(final DGuiSession session) throws Exception {
        String sql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.C_LOCK) + " "
                + "SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                + "WHERE fk_usr_ins = " + session.getUser().getPkUserId() + " AND NOT b_del AND lock_st = " + DLockConsts.LOCK_ST_ACTIVE + " ";
        session.getStatement().execute(sql);
    }
    
    /**
     * Deletes all active locks from all users.
     * @param session GUI session.
     * @throws Exception 
     */
    public static void deleteAllActiveLocks(final DGuiSession session) throws Exception {
        String sql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.C_LOCK) + " "
                + "SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                + "WHERE NOT b_del AND lock_st = " + DLockConsts.LOCK_ST_ACTIVE + " ";
        session.getStatement().execute(sql);
    }
}
