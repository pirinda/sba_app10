/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnConsts {

    public static final int ITEM_FILTER_ALL = 1;
    public static final int ITEM_FILTER_INV = 2;
    public static final int ITEM_FILTER_INV_PAC = 3;

    public static final int FOUND_BY_CODE = 1;
    public static final int FOUND_BY_BARCODE = 2;
    public static final int FOUND_BY_SNR = 3;

    public static final int FIND_MODE_BY_CODE = 1;
    public static final int FIND_MODE_BY_NAME = 2;
    public static final int FIND_MODE_BY_BRD = 3;
    public static final int FIND_MODE_BY_MFR = 4;
    public static final int FIND_MODE_BY_CMP = 5;
    public static final int FIND_MODE_BY_DEP = 6;

    public static final String TXT_ITEM_FILTER_ALL = "Todos";
    public static final String TXT_ITEM_FILTER_INV = "Inventariables";
    public static final String TXT_ITEM_FILTER_INV_PAC = "Convertibles";

    public static final String TXT_DPS_DOC = "F";
    public static final String TXT_DPS_ADJ_INC = "ND";
    public static final String TXT_DPS_ADJ_DEC = "NC";

    public static final String MSG_CNF_REG_VALUELESS = "¿Está seguro que desea agregar un registro sin valor?";
    public static final String MSG_CNF_ROW_VALUELESS = "¿Está seguro que desea agregar una partida sin valor?";

    public static final String ERR_MSG_BKK_MOVES = "El documento tiene movimientos contables.";
    
    public static final String ERR_MSG_NOT_PROCEED = "No se puede proceder con la acción solicitada.";
}
