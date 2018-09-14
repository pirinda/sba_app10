/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.ver33.crp10.DElementPagosPago;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowDfrPayment implements DGridRow {
    
    private final DElementPagosPago moPago;
    
    public DRowDfrPayment(DElementPagosPago pago) {
        moPago = pago;
    }
    
    public DElementPagosPago getPago() {
        return moPago;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return null;
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return true;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = moPago.getAttFechaPago().getDatetime();
                break;
            case 1:
                value = moPago.getAttFormaDePagoP().getString();
                break;
            case 2:
                value = moPago.getAttMonto().getDouble();
                break;
            case 3:
                value = moPago.getAttMonedaP().getString();
                break;
            case 4:
                value = moPago.getAttTipoCambioP().getDouble();
                break;
            case 5:
                value = moPago.getAttNumOperacion().getString();
                break;
            case 6:
                value = moPago.getAttRfcEmisorCtaBen().getString();
                break;
            case 7:
                value = moPago.getAttCtaBeneficiario().getString();
                break;
            case 8:
                value = moPago.getAttRfcEmisorCtaOrd().getString();
                break;
            case 9:
                value = moPago.getAttCtaOrdenante().getString();
                break;
            case 10:
                value = moPago.getAttNomBancoOrdExt().getString();
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
