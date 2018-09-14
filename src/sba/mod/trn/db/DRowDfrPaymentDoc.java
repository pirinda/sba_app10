/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.ver33.crp10.DElementDoctoRelacionado;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowDfrPaymentDoc implements DGridRow {
    
    private final DElementDoctoRelacionado moDoctoRelacionado;
    
    public DRowDfrPaymentDoc(DElementDoctoRelacionado doctoRelacionado) {
        moDoctoRelacionado = doctoRelacionado;
    }
    
    public DElementDoctoRelacionado getDoctoRelacionado() {
        return moDoctoRelacionado;
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
                value = moDoctoRelacionado.getAttSerie().getString();
                break;
            case 1:
                value = moDoctoRelacionado.getAttFolio().getString();
                break;
            case 2:
                value = moDoctoRelacionado.getAttIdDocumento().getString();
                break;
            case 3:
                value = moDoctoRelacionado.getAttImpPagado().getDouble();
                break;
            case 4:
                value = moDoctoRelacionado.getAttImpSaldoAnt().getDouble();
                break;
            case 5:
                value = moDoctoRelacionado.getAttImpSaldoInsoluto().getDouble();
                break;
            case 6:
                value = moDoctoRelacionado.getAttMonedaDR().getString();
                break;
            case 7:
                value = moDoctoRelacionado.getAttNumParcialidad().getInteger();
                break;
            case 8:
                value = moDoctoRelacionado.getAttTipoCambioDR().getDouble();
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
