/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

/**
 *
 * @author Sergio Flores
 */
public class DTrnEmissionSettings {
    
    public int SignatureProviderId;
    public boolean RequestAllowed;
    public int[] SignatureCompanyBranchKey;
    public int StampsAvailable;
    
    public DTrnEmissionSettings() {
        SignatureProviderId = 0;
        RequestAllowed = false;
        SignatureCompanyBranchKey = null;
        StampsAvailable = 0;
    }
}
