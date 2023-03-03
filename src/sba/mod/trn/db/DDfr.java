/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

/**
 * Mantiene en memoria los valores de atributos y nodos CFD que no están en la BD.
 * @author Sergio Flores
 */
public class DDfr {
    
    protected String msCfdType;
    protected String msVersion; // eventhough is a decimal, handle it as string
    protected String msPlaceOfIssue;
    protected String msMethodOfPayment;
    protected String msPaymentTerms;
    protected String msConfirmation;
    protected String msIssuerTaxRegime;
    protected String msReceiverTaxRegime;
    protected String msReceiverFiscalAddress;
    protected String msCfdUsage;
    protected String msGlobalPeriodicity;
    protected String msGlobalMonths;
    protected int mnGlobalYear;
    protected DDfrCfdRelations moCfdRelations;

    public DDfr() {
        reset();
    }
    
    private void reset() {
        msCfdType = "";
        msPlaceOfIssue = "";
        msVersion = "";
        msMethodOfPayment = "";
        msPaymentTerms = "";
        msConfirmation = "";
        msIssuerTaxRegime = "";
        msReceiverTaxRegime = "";
        msReceiverFiscalAddress = "";
        msCfdUsage = "";
        msGlobalPeriodicity = "";
        msGlobalMonths = "";
        mnGlobalYear = 0;
        moCfdRelations = null;
    }
    
    public void setCfdType(String s) { msCfdType = s; }
    public void setPlaceOfIssue(String s) { msPlaceOfIssue = s; }
    public void setVersion(String s) { msVersion = s; }
    public void setMethodOfPayment(String s) { msMethodOfPayment = s; }
    public void setPaymentTerms(String s) { msPaymentTerms = s; }
    public void setConfirmation(String s) { msConfirmation = s; }
    public void setIssuerTaxRegime(String s) { msIssuerTaxRegime = s; }
    public void setReceiverTaxRegime(String s) { msReceiverTaxRegime = s; }
    public void setReceiverFiscalAddress(String s) { msReceiverFiscalAddress = s; }
    public void setCfdUsage(String s) { msCfdUsage = s; }
    public void setGlobalPeriodicity(String s) { msGlobalPeriodicity = s; }
    public void setGlobalMonths(String s) { msGlobalMonths = s; }
    public void setGlobalYear(int n) { mnGlobalYear = n; }
    public void setCfdRelations(DDfrCfdRelations o) { moCfdRelations = o; }
    
    public String getCfdType() { return msCfdType; }
    public String getVersion() { return msVersion; }
    public String getPlaceOfIssue() { return msPlaceOfIssue; }
    public String getMethodOfPayment() { return msMethodOfPayment; }
    public String getPaymentTerms() { return msPaymentTerms; }
    public String getConfirmation() { return msConfirmation; }
    public String getIssuerTaxRegime() { return msIssuerTaxRegime; }
    public String getReceiverTaxRegime() { return msReceiverTaxRegime; }
    public String getReceiverFiscalAddress() { return msReceiverFiscalAddress; }
    public String getCfdUsage() { return msCfdUsage; }
    public String getGlobalPeriodicity() { return msGlobalPeriodicity; }
    public String getGlobalMonths() { return msGlobalMonths; }
    public int getGlobalYear() { return mnGlobalYear; }
    public DDfrCfdRelations getCfdRelations() { return moCfdRelations; }
    
    public boolean isGlobal() { return !msGlobalPeriodicity.isEmpty(); }
    
    @Override
    public DDfr clone() throws CloneNotSupportedException {
        DDfr clone = new DDfr();
        
        clone.setCfdType(msCfdType);
        clone.setVersion(msVersion);
        clone.setPlaceOfIssue(msPlaceOfIssue);
        clone.setMethodOfPayment(msMethodOfPayment);
        clone.setPaymentTerms(msPaymentTerms);
        clone.setConfirmation(msConfirmation);
        clone.setIssuerTaxRegime(msIssuerTaxRegime);
        clone.setReceiverTaxRegime(msReceiverTaxRegime);
        clone.setReceiverFiscalAddress(msReceiverFiscalAddress);
        clone.setCfdUsage(msCfdUsage);
        clone.setGlobalPeriodicity(msGlobalPeriodicity);
        clone.setGlobalMonths(msGlobalMonths);
        clone.setGlobalYear(mnGlobalYear);
        clone.setCfdRelations(moCfdRelations == null ? null : moCfdRelations.clone());
        
        return clone;
    }
}
