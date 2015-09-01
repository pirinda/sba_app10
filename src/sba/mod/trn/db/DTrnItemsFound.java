/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.util.Vector;
import sba.lib.DLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class DTrnItemsFound {

    protected int mnFoundBy;
    protected double mdQuantity;
    protected String msCode;
    protected String msBarcode;
    protected String msSerialNumber;
    protected String msTextToFind;
    protected Vector<int[]> mvItemsFoundKeys;

    public DTrnItemsFound() {
        mnFoundBy = DLibConsts.UNDEFINED;
        mdQuantity = 1;
        msCode = "";
        msBarcode = "";
        msSerialNumber = "";
        msTextToFind = "";
        mvItemsFoundKeys = new Vector<int[]>();
    }

    /**
     * @param n Field that matched text to find items. Constants defined in DTrnConsts (FOUND_BY_...).
     */
    public void setFoundBy(int n) { mnFoundBy = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setCode(String s) { msCode = s; }
    public void setBarcode(String s) { msBarcode = s; }
    public void setSerialNumber(String s) { msSerialNumber = s; }
    public void setTextToFind(String s) { msTextToFind = s; }

    public int getFoundBy() { return mnFoundBy; }
    public double getQuantity() { return mdQuantity; }
    public String getCode() { return msCode; }
    public String getBarcode() { return msBarcode; }
    public String getSerialNumber() { return msSerialNumber; }
    public String getTextToFind() { return msTextToFind; }

    public Vector<int[]> getItemsFoundKeys() { return mvItemsFoundKeys; }
}
