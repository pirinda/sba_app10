/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbTaxGroupConfigRow extends DDbRegistry {

    protected int mnPkTaxGroupId;
    protected int mnPkConfigId;
    protected int mnPkRowId;
    protected double mdPurchaseTaxChargedRate1;
    protected double mdPurchaseTaxChargedRate2;
    protected double mdPurchaseTaxChargedRate3;
    protected double mdPurchaseTaxRetainedRate1;
    protected double mdPurchaseTaxRetainedRate2;
    protected double mdPurchaseTaxRetainedRate3;
    protected double mdSaleTaxChargedRate1;
    protected double mdSaleTaxChargedRate2;
    protected double mdSaleTaxChargedRate3;
    protected double mdSaleTaxRetainedRate1;
    protected double mdSaleTaxRetainedRate2;
    protected double mdSaleTaxRetainedRate3;
    protected int mnFkIdentityTypeId;
    protected int mnFkPurchaseTaxCharged1Id;
    protected int mnFkPurchaseTaxCharged2Id;
    protected int mnFkPurchaseTaxCharged3Id;
    protected int mnFkPurchaseTaxRetained1Id;
    protected int mnFkPurchaseTaxRetained2Id;
    protected int mnFkPurchaseTaxRetained3Id;
    protected int mnFkSaleTaxCharged1Id;
    protected int mnFkSaleTaxCharged2Id;
    protected int mnFkSaleTaxCharged3Id;
    protected int mnFkSaleTaxRetained1Id;
    protected int mnFkSaleTaxRetained2Id;
    protected int mnFkSaleTaxRetained3Id;

    public DDbTaxGroupConfigRow() {
        super(DModConsts.FU_TAX_GRP_CFG_ROW);
        initRegistry();
    }

    public void setPkTaxGroupId(int n) { mnPkTaxGroupId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setPurchaseTaxChargedRate1(double d) { mdPurchaseTaxChargedRate1 = d; }
    public void setPurchaseTaxChargedRate2(double d) { mdPurchaseTaxChargedRate2 = d; }
    public void setPurchaseTaxChargedRate3(double d) { mdPurchaseTaxChargedRate3 = d; }
    public void setPurchaseTaxRetainedRate1(double d) { mdPurchaseTaxRetainedRate1 = d; }
    public void setPurchaseTaxRetainedRate2(double d) { mdPurchaseTaxRetainedRate2 = d; }
    public void setPurchaseTaxRetainedRate3(double d) { mdPurchaseTaxRetainedRate3 = d; }
    public void setSaleTaxChargedRate1(double d) { mdSaleTaxChargedRate1 = d; }
    public void setSaleTaxChargedRate2(double d) { mdSaleTaxChargedRate2 = d; }
    public void setSaleTaxChargedRate3(double d) { mdSaleTaxChargedRate3 = d; }
    public void setSaleTaxRetainedRate1(double d) { mdSaleTaxRetainedRate1 = d; }
    public void setSaleTaxRetainedRate2(double d) { mdSaleTaxRetainedRate2 = d; }
    public void setSaleTaxRetainedRate3(double d) { mdSaleTaxRetainedRate3 = d; }
    public void setFkIdentityTypeId(int n) { mnFkIdentityTypeId = n; }
    public void setFkPurchaseTaxCharged1Id(int n) { mnFkPurchaseTaxCharged1Id = n; }
    public void setFkPurchaseTaxCharged2Id(int n) { mnFkPurchaseTaxCharged2Id = n; }
    public void setFkPurchaseTaxCharged3Id(int n) { mnFkPurchaseTaxCharged3Id = n; }
    public void setFkPurchaseTaxRetained1Id(int n) { mnFkPurchaseTaxRetained1Id = n; }
    public void setFkPurchaseTaxRetained2Id(int n) { mnFkPurchaseTaxRetained2Id = n; }
    public void setFkPurchaseTaxRetained3Id(int n) { mnFkPurchaseTaxRetained3Id = n; }
    public void setFkSaleTaxCharged1Id(int n) { mnFkSaleTaxCharged1Id = n; }
    public void setFkSaleTaxCharged2Id(int n) { mnFkSaleTaxCharged2Id = n; }
    public void setFkSaleTaxCharged3Id(int n) { mnFkSaleTaxCharged3Id = n; }
    public void setFkSaleTaxRetained1Id(int n) { mnFkSaleTaxRetained1Id = n; }
    public void setFkSaleTaxRetained2Id(int n) { mnFkSaleTaxRetained2Id = n; }
    public void setFkSaleTaxRetained3Id(int n) { mnFkSaleTaxRetained3Id = n; }

    public int getPkTaxGroupId() { return mnPkTaxGroupId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public int getPkRowId() { return mnPkRowId; }
    public double getPurchaseTaxChargedRate1() { return mdPurchaseTaxChargedRate1; }
    public double getPurchaseTaxChargedRate2() { return mdPurchaseTaxChargedRate2; }
    public double getPurchaseTaxChargedRate3() { return mdPurchaseTaxChargedRate3; }
    public double getPurchaseTaxRetainedRate1() { return mdPurchaseTaxRetainedRate1; }
    public double getPurchaseTaxRetainedRate2() { return mdPurchaseTaxRetainedRate2; }
    public double getPurchaseTaxRetainedRate3() { return mdPurchaseTaxRetainedRate3; }
    public double getSaleTaxChargedRate1() { return mdSaleTaxChargedRate1; }
    public double getSaleTaxChargedRate2() { return mdSaleTaxChargedRate2; }
    public double getSaleTaxChargedRate3() { return mdSaleTaxChargedRate3; }
    public double getSaleTaxRetainedRate1() { return mdSaleTaxRetainedRate1; }
    public double getSaleTaxRetainedRate2() { return mdSaleTaxRetainedRate2; }
    public double getSaleTaxRetainedRate3() { return mdSaleTaxRetainedRate3; }
    public int getFkIdentityTypeId() { return mnFkIdentityTypeId; }
    public int getFkPurchaseTaxCharged1Id() { return mnFkPurchaseTaxCharged1Id; }
    public int getFkPurchaseTaxCharged2Id() { return mnFkPurchaseTaxCharged2Id; }
    public int getFkPurchaseTaxCharged3Id() { return mnFkPurchaseTaxCharged3Id; }
    public int getFkPurchaseTaxRetained1Id() { return mnFkPurchaseTaxRetained1Id; }
    public int getFkPurchaseTaxRetained2Id() { return mnFkPurchaseTaxRetained2Id; }
    public int getFkPurchaseTaxRetained3Id() { return mnFkPurchaseTaxRetained3Id; }
    public int getFkSaleTaxCharged1Id() { return mnFkSaleTaxCharged1Id; }
    public int getFkSaleTaxCharged2Id() { return mnFkSaleTaxCharged2Id; }
    public int getFkSaleTaxCharged3Id() { return mnFkSaleTaxCharged3Id; }
    public int getFkSaleTaxRetained1Id() { return mnFkSaleTaxRetained1Id; }
    public int getFkSaleTaxRetained2Id() { return mnFkSaleTaxRetained2Id; }
    public int getFkSaleTaxRetained3Id() { return mnFkSaleTaxRetained3Id; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTaxGroupId = pk[0];
        mnPkConfigId = pk[1];
        mnPkRowId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxGroupId, mnPkConfigId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkTaxGroupId = 0;
        mnPkConfigId = 0;
        mnPkRowId = 0;
        mdPurchaseTaxChargedRate1 = 0;
        mdPurchaseTaxChargedRate2 = 0;
        mdPurchaseTaxChargedRate3 = 0;
        mdPurchaseTaxRetainedRate1 = 0;
        mdPurchaseTaxRetainedRate2 = 0;
        mdPurchaseTaxRetainedRate3 = 0;
        mdSaleTaxChargedRate1 = 0;
        mdSaleTaxChargedRate2 = 0;
        mdSaleTaxChargedRate3 = 0;
        mdSaleTaxRetainedRate1 = 0;
        mdSaleTaxRetainedRate2 = 0;
        mdSaleTaxRetainedRate3 = 0;
        mnFkIdentityTypeId = 0;
        mnFkPurchaseTaxCharged1Id = 0;
        mnFkPurchaseTaxCharged2Id = 0;
        mnFkPurchaseTaxCharged3Id = 0;
        mnFkPurchaseTaxRetained1Id = 0;
        mnFkPurchaseTaxRetained2Id = 0;
        mnFkPurchaseTaxRetained3Id = 0;
        mnFkSaleTaxCharged1Id = 0;
        mnFkSaleTaxCharged2Id = 0;
        mnFkSaleTaxCharged3Id = 0;
        mnFkSaleTaxRetained1Id = 0;
        mnFkSaleTaxRetained2Id = 0;
        mnFkSaleTaxRetained3Id = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tax_grp = " + mnPkTaxGroupId + " AND " +
                "id_cfg = " + mnPkConfigId + " AND " +
                "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax_grp = " + pk[0] + " AND " +
                "id_cfg = " + pk[1] + " AND " +
                "id_row = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = DDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkTaxGroupId = resultSet.getInt("id_tax_grp");
            mnPkConfigId = resultSet.getInt("id_cfg");
            mnPkRowId = resultSet.getInt("id_row");
            mdPurchaseTaxChargedRate1 = resultSet.getDouble("pur_tax_cha_rat_1");
            mdPurchaseTaxChargedRate2 = resultSet.getDouble("pur_tax_cha_rat_2");
            mdPurchaseTaxChargedRate3 = resultSet.getDouble("pur_tax_cha_rat_3");
            mdPurchaseTaxRetainedRate1 = resultSet.getDouble("pur_tax_ret_rat_1");
            mdPurchaseTaxRetainedRate2 = resultSet.getDouble("pur_tax_ret_rat_2");
            mdPurchaseTaxRetainedRate3 = resultSet.getDouble("pur_tax_ret_rat_3");
            mdSaleTaxChargedRate1 = resultSet.getDouble("sal_tax_cha_rat_1");
            mdSaleTaxChargedRate2 = resultSet.getDouble("sal_tax_cha_rat_2");
            mdSaleTaxChargedRate3 = resultSet.getDouble("sal_tax_cha_rat_3");
            mdSaleTaxRetainedRate1 = resultSet.getDouble("sal_tax_ret_rat_1");
            mdSaleTaxRetainedRate2 = resultSet.getDouble("sal_tax_ret_rat_2");
            mdSaleTaxRetainedRate3 = resultSet.getDouble("sal_tax_ret_rat_3");
            mnFkIdentityTypeId = resultSet.getInt("fk_idy_tp");
            mnFkPurchaseTaxCharged1Id = resultSet.getInt("fk_pur_tax_cha_1");
            mnFkPurchaseTaxCharged2Id = resultSet.getInt("fk_pur_tax_cha_2");
            mnFkPurchaseTaxCharged3Id = resultSet.getInt("fk_pur_tax_cha_3");
            mnFkPurchaseTaxRetained1Id = resultSet.getInt("fk_pur_tax_ret_1");
            mnFkPurchaseTaxRetained2Id = resultSet.getInt("fk_pur_tax_ret_2");
            mnFkPurchaseTaxRetained3Id = resultSet.getInt("fk_pur_tax_ret_3");
            mnFkSaleTaxCharged1Id = resultSet.getInt("fk_sal_tax_cha_1");
            mnFkSaleTaxCharged2Id = resultSet.getInt("fk_sal_tax_cha_2");
            mnFkSaleTaxCharged3Id = resultSet.getInt("fk_sal_tax_cha_3");
            mnFkSaleTaxRetained1Id = resultSet.getInt("fk_sal_tax_ret_1");
            mnFkSaleTaxRetained2Id = resultSet.getInt("fk_sal_tax_ret_2");
            mnFkSaleTaxRetained3Id = resultSet.getInt("fk_sal_tax_ret_3");

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTaxGroupId + ", " +
                    mnPkConfigId + ", " +
                    mnPkRowId + ", " +
                    mdPurchaseTaxChargedRate1 + ", " +
                    mdPurchaseTaxChargedRate2 + ", " +
                    mdPurchaseTaxChargedRate3 + ", " +
                    mdPurchaseTaxRetainedRate1 + ", " +
                    mdPurchaseTaxRetainedRate2 + ", " +
                    mdPurchaseTaxRetainedRate3 + ", " +
                    mdSaleTaxChargedRate1 + ", " +
                    mdSaleTaxChargedRate2 + ", " +
                    mdSaleTaxChargedRate3 + ", " +
                    mdSaleTaxRetainedRate1 + ", " +
                    mdSaleTaxRetainedRate2 + ", " +
                    mdSaleTaxRetainedRate3 + ", " +
                    mnFkIdentityTypeId + ", " +
                    mnFkPurchaseTaxCharged1Id + ", " +
                    mnFkPurchaseTaxCharged2Id + ", " +
                    mnFkPurchaseTaxCharged3Id + ", " +
                    mnFkPurchaseTaxRetained1Id + ", " +
                    mnFkPurchaseTaxRetained2Id + ", " +
                    mnFkPurchaseTaxRetained3Id + ", " +
                    mnFkSaleTaxCharged1Id + ", " +
                    mnFkSaleTaxCharged2Id + ", " +
                    mnFkSaleTaxCharged3Id + ", " +
                    mnFkSaleTaxRetained1Id + ", " +
                    mnFkSaleTaxRetained2Id + ", " +
                    mnFkSaleTaxRetained3Id + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_tax_grp = " + mnPkTaxGroupId + ", " +
                    "id_cfg = " + mnPkConfigId + ", " +
                    "id_row = " + mnPkRowId + ", " +
                    */
                    "pur_tax_cha_rat_1 = " + mdPurchaseTaxChargedRate1 + ", " +
                    "pur_tax_cha_rat_2 = " + mdPurchaseTaxChargedRate2 + ", " +
                    "pur_tax_cha_rat_3 = " + mdPurchaseTaxChargedRate3 + ", " +
                    "pur_tax_ret_rat_1 = " + mdPurchaseTaxRetainedRate1 + ", " +
                    "pur_tax_ret_rat_2 = " + mdPurchaseTaxRetainedRate2 + ", " +
                    "pur_tax_ret_rat_3 = " + mdPurchaseTaxRetainedRate3 + ", " +
                    "sal_tax_cha_rat_1 = " + mdSaleTaxChargedRate1 + ", " +
                    "sal_tax_cha_rat_2 = " + mdSaleTaxChargedRate2 + ", " +
                    "sal_tax_cha_rat_3 = " + mdSaleTaxChargedRate3 + ", " +
                    "sal_tax_ret_rat_1 = " + mdSaleTaxRetainedRate1 + ", " +
                    "sal_tax_ret_rat_2 = " + mdSaleTaxRetainedRate2 + ", " +
                    "sal_tax_ret_rat_3 = " + mdSaleTaxRetainedRate3 + ", " +
                    "fk_idy_tp = " + mnFkIdentityTypeId + ", " +
                    "fk_pur_tax_cha_1 = " + mnFkPurchaseTaxCharged1Id + ", " +
                    "fk_pur_tax_cha_2 = " + mnFkPurchaseTaxCharged2Id + ", " +
                    "fk_pur_tax_cha_3 = " + mnFkPurchaseTaxCharged3Id + ", " +
                    "fk_pur_tax_ret_1 = " + mnFkPurchaseTaxRetained1Id + ", " +
                    "fk_pur_tax_ret_2 = " + mnFkPurchaseTaxRetained2Id + ", " +
                    "fk_pur_tax_ret_3 = " + mnFkPurchaseTaxRetained3Id + ", " +
                    "fk_sal_tax_cha_1 = " + mnFkSaleTaxCharged1Id + ", " +
                    "fk_sal_tax_cha_2 = " + mnFkSaleTaxCharged2Id + ", " +
                    "fk_sal_tax_cha_3 = " + mnFkSaleTaxCharged3Id + ", " +
                    "fk_sal_tax_ret_1 = " + mnFkSaleTaxRetained1Id + ", " +
                    "fk_sal_tax_ret_2 = " + mnFkSaleTaxRetained2Id + ", " +
                    "fk_sal_tax_ret_3 = " + mnFkSaleTaxRetained3Id + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbTaxGroupConfigRow clone() throws CloneNotSupportedException {
        DDbTaxGroupConfigRow registry = new DDbTaxGroupConfigRow();

        registry.setPkTaxGroupId(this.getPkTaxGroupId());
        registry.setPkConfigId(this.getPkConfigId());
        registry.setPkRowId(this.getPkRowId());
        registry.setPurchaseTaxChargedRate1(this.getPurchaseTaxChargedRate1());
        registry.setPurchaseTaxChargedRate2(this.getPurchaseTaxChargedRate2());
        registry.setPurchaseTaxChargedRate3(this.getPurchaseTaxChargedRate3());
        registry.setPurchaseTaxRetainedRate1(this.getPurchaseTaxRetainedRate1());
        registry.setPurchaseTaxRetainedRate2(this.getPurchaseTaxRetainedRate2());
        registry.setPurchaseTaxRetainedRate3(this.getPurchaseTaxRetainedRate3());
        registry.setSaleTaxChargedRate1(this.getSaleTaxChargedRate1());
        registry.setSaleTaxChargedRate2(this.getSaleTaxChargedRate2());
        registry.setSaleTaxChargedRate3(this.getSaleTaxChargedRate3());
        registry.setSaleTaxRetainedRate1(this.getSaleTaxRetainedRate1());
        registry.setSaleTaxRetainedRate2(this.getSaleTaxRetainedRate2());
        registry.setSaleTaxRetainedRate3(this.getSaleTaxRetainedRate3());
        registry.setFkIdentityTypeId(this.getFkIdentityTypeId());
        registry.setFkPurchaseTaxCharged1Id(this.getFkPurchaseTaxCharged1Id());
        registry.setFkPurchaseTaxCharged2Id(this.getFkPurchaseTaxCharged2Id());
        registry.setFkPurchaseTaxCharged3Id(this.getFkPurchaseTaxCharged3Id());
        registry.setFkPurchaseTaxRetained1Id(this.getFkPurchaseTaxRetained1Id());
        registry.setFkPurchaseTaxRetained2Id(this.getFkPurchaseTaxRetained2Id());
        registry.setFkPurchaseTaxRetained3Id(this.getFkPurchaseTaxRetained3Id());
        registry.setFkSaleTaxCharged1Id(this.getFkSaleTaxCharged1Id());
        registry.setFkSaleTaxCharged2Id(this.getFkSaleTaxCharged2Id());
        registry.setFkSaleTaxCharged3Id(this.getFkSaleTaxCharged3Id());
        registry.setFkSaleTaxRetained1Id(this.getFkSaleTaxRetained1Id());
        registry.setFkSaleTaxRetained2Id(this.getFkSaleTaxRetained2Id());
        registry.setFkSaleTaxRetained3Id(this.getFkSaleTaxRetained3Id());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
