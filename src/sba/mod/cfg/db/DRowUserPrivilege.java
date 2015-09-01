/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import sba.lib.grid.DGridRowCustom;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DRowUserPrivilege extends DGridRowCustom {

    public int[] PrivilegeKey;
    public String Privilege;
    public String Module;
    public boolean IsSelected;
    public boolean IsLevelApplying;
    public int[] LevelKey;

    public DRowUserPrivilege() {
        super(null, "", "");

        PrivilegeKey = null;
        Privilege = "";
        Module = "";
        IsSelected = false;
        IsLevelApplying = false;
        LevelKey = new int[] { DModSysConsts.CS_LEV_NA };
    }

    @Override
    public boolean isCellEditable(int col) {
        boolean editable = false;

        switch (col) {
            case 0:
            case 1:
                break;
            case 2:
                editable = true;
                break;
            case 3:
                editable = IsLevelApplying;
                break;
            default:
        }

        return editable;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = Module;
                break;
            case 1:
                value = Privilege;
                break;
            case 2:
                value = IsSelected;
                break;
            case 3:
                value = LevelKey;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 0:
            case 1:
                break;
            case 2:
                IsSelected = (Boolean) value;
                break;
            case 3:
                LevelKey = (int[]) value;
                break;
            default:
        }
    }
}
