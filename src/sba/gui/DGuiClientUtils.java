/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.gui;

import java.util.Date;
import org.bouncycastle.util.encoders.Base64;
import sba.lib.DLibTimeUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class DGuiClientUtils {
    
    public static String generateStampCode(final Date date, final Integer quantity) {
        String code = new String(Base64.encode(("" + quantity + DLibTimeUtils.convertToDateOnly(date).getTime()).getBytes()));
        
        return code.length() <= 8 ? code : code.substring(0, 8);
    }
}
