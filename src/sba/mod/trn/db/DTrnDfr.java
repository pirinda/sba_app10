/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import sba.lib.gui.DGuiSession;

/**
 *
 * @author Sergio Flores
 */
public interface DTrnDfr {
    
    public void issueDfr(DGuiSession session) throws Exception;
}
