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
    
public class DTrnAnnulParams {

    public int AnnulAction;
    public boolean RetryCancel;
    public String AnnulReasonCode;
    public String AnnulRelatedUuid;
    public boolean ConfirmCancel;

    /**
     * Crate new parameters for document cancellation.
     * @param annulAction Constants defined in DTrnEmissionConsts.ACTION_...
     * @param retryCancel Retry a new cancel even if receptor rejected the very last cancel request.
     * @param confirmCancel Confirm a pending finished cancel request.
     */
    public DTrnAnnulParams(final int annulAction, final boolean retryCancel, final boolean confirmCancel) {
        this(annulAction, retryCancel, "", "", confirmCancel);
    }
    
    /**
     * Crate new parameters for document cancellation.
     * @param annulAction Constants defined in DTrnEmissionConsts.ACTION_...
     * @param retryCancel Retry a new cancel even if receptor rejected the very last cancel request.
     * @param annulReasonCode SAT annul reason code.
     * @param annulRelatedUuid SAT related UUID.
     * @param confirmCancel Confirm a pending finished cancel request.
     */
    public DTrnAnnulParams(final int annulAction, final boolean retryCancel, final String annulReasonCode, final String annulRelatedUuid, final boolean confirmCancel) {
        AnnulAction = annulAction;
        RetryCancel = retryCancel;
        AnnulReasonCode = annulReasonCode;
        AnnulRelatedUuid = annulRelatedUuid;
        ConfirmCancel = confirmCancel;
    }
}
