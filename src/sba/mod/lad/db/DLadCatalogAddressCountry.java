/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.db;

/**
 *
 * @author Sergio Flores
 */
public class DLadCatalogAddressCountry {
    
    public String Code;
    public String Name;
    
    public DLadCatalogAddressCountry(final String code, final String name) {
        Code = code;
        Name = name;
    }

    @Override
    public DLadCatalogAddressCountry clone() throws CloneNotSupportedException {
        return new DLadCatalogAddressCountry(Code, Name);
    }
}
