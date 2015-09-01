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
}
