/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowFindBizPartner implements DGridRow {

    protected int mnFindMode;
    protected int mnPkBizPartnerId;
    protected String msCode;
    protected String msName;
    protected String msFiscalId;
    protected String msNameFiscal;
    protected int mnFkBizPartnerClassId;
    protected int mnFkBizPartnerTypeId;

    public DRowFindBizPartner(int findMode, int bizPartnerId, String code, String name, String fiscalId, String nameFiscal) {
        mnFindMode = findMode;
        mnPkBizPartnerId = bizPartnerId;
        msCode = code;
        msName = name;
        msFiscalId = fiscalId;
        msNameFiscal = nameFiscal;
        mnFkBizPartnerClassId = 0;
        mnFkBizPartnerTypeId = 0;
    }

    public void setFindMode(int n) { mnFindMode = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void ssNameFiscal(String s) { msNameFiscal = s; }
    public void setFkBizPartnerClassId(int n) { mnFkBizPartnerClassId = n; }
    public void setFkBizPartnerTypeId(int n) { mnFkBizPartnerTypeId = n; }

    public int getFindMode() { return mnFindMode; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getFiscalId() { return msFiscalId; }
    public String msNameFiscal() { return msNameFiscal; }
    public int getFkBizPartnerClassId() { return mnFkBizPartnerClassId; }
    public int getFkBizPartnerTypeId() { return mnFkBizPartnerTypeId; }

    public void setBizPartnerTypeKey(int[] key) {
        mnFkBizPartnerClassId = key[0];
        mnFkBizPartnerTypeId = key[1];
    }

    public int[] getBizPartnerClassKey() {
        return new int[] { mnFkBizPartnerClassId };
    }

    public int[] getBizPartnerTypeKey() {
        return new int[] { mnFkBizPartnerClassId, mnFkBizPartnerTypeId };
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                switch (mnFindMode) {
                    case DTrnConsts.FIND_MODE_BY_CODE:
                        value = msCode;
                        break;
                    case DTrnConsts.FIND_MODE_BY_NAME:
                        value = msName;
                        break;
                    default:
                }
                break;

            case 1:
                switch (mnFindMode) {
                    case DTrnConsts.FIND_MODE_BY_CODE:
                        value = msName;
                        break;
                    case DTrnConsts.FIND_MODE_BY_NAME:
                        value = msCode;
                        break;
                    default:
                }
                break;

            case 2:
                value = msFiscalId;
                break;

            case 3:
                value = msNameFiscal;
                break;

            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
