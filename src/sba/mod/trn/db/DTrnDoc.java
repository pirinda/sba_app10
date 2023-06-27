/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import java.util.Date;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.cfg.db.DDbLock;

/**
 *
 * @author Sergio Flores
 */
public interface DTrnDoc {
    
    public String getDocName();
    public String getDocNumber();
    public Date getDocDate();
    public int getDocStatus();
    public int[] getCompanyBranchKey();
    public int getBizPartnerCategory();
    public int[] getBizPartnerKey();
    public int[] getBizPartnerBranchAddressKey();
    public DDbDfr getDfr();
    public DDbRegistry createDocSending();
    public void printDfr(DGuiSession session) throws Exception;
    public boolean canAnnul(final DGuiSession session) throws Exception;
    public boolean checkAvailability(final DGuiSession session) throws Exception;
    public DDbLock assureLock(final DGuiSession session) throws Exception;
    public void freeLock(final DGuiSession session, final int freedLockStatus) throws Exception;
}
