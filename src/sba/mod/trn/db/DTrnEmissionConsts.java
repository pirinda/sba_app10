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
public abstract class DTrnEmissionConsts {
    
    public static final int ANNUL = 1;
    public static final int ANNUL_CANCEL = 2;
    
    public static final String UUID_ANNUL = "201";
    public static final String UUID_ANNUL_PREV = "202";
    public static final String UUID_ANNUL_UUID_UNEXIST = "205";
    
    public static final String PAC = "Proveedor Autorizado de Certificación";
    
    public static final String MSG_DENIED_CHANGE = "No se puede cambiar el tipo del documento:\n";
    public static final String MSG_DENIED_SIGN = "No se puede timbrar el documento:\n";
    public static final String MSG_DENIED_CANCEL = "No se puede cancelar el documento:\n";
    public static final String MSG_DENIED_SEND = "No se puede enviar el documento:\n";
}
