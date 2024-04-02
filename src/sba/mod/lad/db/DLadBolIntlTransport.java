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
public class DLadBolIntlTransport {

    public String IntlTransportDirection;
    public int IntlTransportCountryId;
    public int IntlWayTransportTypeId;
    
    public DLadBolIntlTransport() {
        IntlTransportDirection = "";
        IntlTransportCountryId = 0;
        IntlWayTransportTypeId = 0;
    }
}
