/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.cfg.db;

/**
 *
 * @author Sergio Flores
 */
public abstract class DLockConsts {
    
    public static final int LOCK_ST_ACTIVE = 1; // lock active
    public static final int LOCK_ST_FREED_CANCEL = 2; // lock freed by cancellation
    public static final int LOCK_ST_FREED_UPDATE = 3; // lock freed by update
}
