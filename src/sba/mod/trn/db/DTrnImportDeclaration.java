/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public final class DTrnImportDeclaration {

    private String msNumber;
    private Date mtDate;

    public DTrnImportDeclaration(String number, Date date) {
        msNumber = number;
        mtDate = date;
    }

    public void setNumber(String s) { msNumber = s; }
    public void setDate(Date t) { mtDate = t; }

    public String getNumber() { return msNumber; }
    public Date getDate() { return mtDate; }
    
    /**
     * Gets number formatted according to this:
     * index:  012345678901234
     * number: 104738078003832
     * number formatted:
     * 10  47  3807  8003832
    */
    public String getNumberFormatted() {
        return msNumber.length() != 15 ? "" : 
                msNumber.substring(0, 2) + "  " +
                msNumber.substring(2, 4) + "  " +
                msNumber.substring(4, 8) + "  " +
                msNumber.substring(8, 15);
    }
}
