/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBolTransportFigure extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkTransportFigureId;
    protected String msName;
    protected String msFiscalId;
    protected String msForeignId;
    protected String msDriverLicense;
    protected String msAddressStreet;
    protected String msAddressNumberExt;
    protected String msAddressNumberInt;
    protected String msAddressDistrictCode;
    protected String msAddressDistrictName;
    protected String msAddressLocalityCode;
    protected String msAddressLocalityName;
    protected String msAddressReference;
    protected String msAddressCountyCode;
    protected String msAddressCountyName;
    protected String msAddressStateCode;
    protected String msAddressStateName;
    protected String msAddressZipCode;
    protected int mnFkTransportFigureTypeId;
    protected int mnFkTransportFigureId;
    protected int mnFkFigureCountryId;
    protected int mnFkAddressCountryId;
    
    protected ArrayList<DDbBolTransportFigureTransportPart> maChildTransportParts;
    
    protected DDbTransportFigure moOwnTransportFigure;
    
    protected String msDbmsTransportFigureTypeCode;
    protected String msDbmsTransportFigureTypeName;
    
    protected boolean mbBolUpdateOwnRegistry;
    protected int mnBolSortingPos;

    public DDbBolTransportFigure() {
        super(DModConsts.L_BOL_TPT_FIGURE);
        maChildTransportParts = new ArrayList<>();
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkTransportFigureId(int n) { mnPkTransportFigureId = n; }
    public void setName(String s) { msName = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void setForeignId(String s) { msForeignId = s; }
    public void setDriverLicense(String s) { msDriverLicense = s; }
    public void setAddressStreet(String s) { msAddressStreet = s; }
    public void setAddressNumberExt(String s) { msAddressNumberExt = s; }
    public void setAddressNumberInt(String s) { msAddressNumberInt = s; }
    public void setAddressDistrictCode(String s) { msAddressDistrictCode = s; }
    public void setAddressDistrictName(String s) { msAddressDistrictName = s; }
    public void setAddressLocalityCode(String s) { msAddressLocalityCode = s; }
    public void setAddressLocalityName(String s) { msAddressLocalityName = s; }
    public void setAddressReference(String s) { msAddressReference = s; }
    public void setAddressCountyCode(String s) { msAddressCountyCode = s; }
    public void setAddressCountyName(String s) { msAddressCountyName = s; }
    public void setAddressStateCode(String s) { msAddressStateCode = s; }
    public void setAddressStateName(String s) { msAddressStateName = s; }
    public void setAddressZipCode(String s) { msAddressZipCode = s; }
    public void setFkTransportFigureTypeId(int n) { mnFkTransportFigureTypeId = n; }
    public void setFkTransportFigureId(int n) { mnFkTransportFigureId = n; }
    public void setFkFigureCountryId(int n) { mnFkFigureCountryId = n; }
    public void setFkAddressCountryId(int n) { mnFkAddressCountryId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkTransportFigureId() { return mnPkTransportFigureId; }
    public String getName() { return msName; }
    public String getFiscalId() { return msFiscalId; }
    public String getForeignId() { return msForeignId; }
    public String getDriverLicense() { return msDriverLicense; }
    public String getAddressStreet() { return msAddressStreet; }
    public String getAddressNumberExt() { return msAddressNumberExt; }
    public String getAddressNumberInt() { return msAddressNumberInt; }
    public String getAddressDistrictCode() { return msAddressDistrictCode; }
    public String getAddressDistrictName() { return msAddressDistrictName; }
    public String getAddressLocalityCode() { return msAddressLocalityCode; }
    public String getAddressLocalityName() { return msAddressLocalityName; }
    public String getAddressReference() { return msAddressReference; }
    public String getAddressCountyCode() { return msAddressCountyCode; }
    public String getAddressCountyName() { return msAddressCountyName; }
    public String getAddressStateCode() { return msAddressStateCode; }
    public String getAddressStateName() { return msAddressStateName; }
    public String getAddressZipCode() { return msAddressZipCode; }
    public int getFkTransportFigureTypeId() { return mnFkTransportFigureTypeId; }
    public int getFkTransportFigureId() { return mnFkTransportFigureId; }
    public int getFkFigureCountryId() { return mnFkFigureCountryId; }
    public int getFkAddressCountryId() { return mnFkAddressCountryId; }
    
    public ArrayList<DDbBolTransportFigureTransportPart> getChildTransportParts() { return maChildTransportParts; }
    
    public void setOwnTransportFigure(final DDbTransportFigure o) { moOwnTransportFigure = o; updateFromOwnTransportFigure(); }
    
    public DDbTransportFigure getOwnTransportFigure() { return moOwnTransportFigure; }
    
    public void setDbmsTransportFigureTypeCode(String s) { msDbmsTransportFigureTypeCode = s; }
    public void setDbmsTransportFigureTypeName(String s) { msDbmsTransportFigureTypeName = s; }
    
    public String getDbmsTransportFigureTypeCode() { return msDbmsTransportFigureTypeCode; }
    public String getDbmsTransportFigureTypeName() { return msDbmsTransportFigureTypeName; }
    
    @Override
    public void setBolUpdateOwnRegistry(boolean update) { mbBolUpdateOwnRegistry = update; }
    @Override
    public void setBolSortingPos(int n) { mnBolSortingPos = n; }
    
    @Override
    public boolean isBolUpdateOwnRegistry() { return mbBolUpdateOwnRegistry; }
    @Override
    public int getBolSortingPos() { return mnBolSortingPos; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkTransportFigureId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkTransportFigureId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkTransportFigureId = 0;
        msName = "";
        msFiscalId = "";
        msForeignId = "";
        msDriverLicense = "";
        msAddressStreet = "";
        msAddressNumberExt = "";
        msAddressNumberInt = "";
        msAddressDistrictCode = "";
        msAddressDistrictName = "";
        msAddressLocalityCode = "";
        msAddressLocalityName = "";
        msAddressReference = "";
        msAddressCountyCode = "";
        msAddressCountyName = "";
        msAddressStateCode = "";
        msAddressStateName = "";
        msAddressZipCode = "";
        mnFkTransportFigureTypeId = 0;
        mnFkTransportFigureId = 0;
        mnFkFigureCountryId = 0;
        mnFkAddressCountryId = 0;
        
        maChildTransportParts.clear();
        
        moOwnTransportFigure = null;
        
        msDbmsTransportFigureTypeCode = "";
        msDbmsTransportFigureTypeName = "";
        
        mbBolUpdateOwnRegistry = false;
        mnBolSortingPos = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_tpt_figure = " + mnPkTransportFigureId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_tpt_figure = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTransportFigureId = 0;

        msSql = "SELECT COALESCE(MAX(id_tpt_figure), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTransportFigureId = resultSet.getInt(1);
        }
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
            mnPkBolId = resultSet.getInt("id_bol");
            mnPkTransportFigureId = resultSet.getInt("id_tpt_figure");
            msName = resultSet.getString("name");
            msFiscalId = resultSet.getString("fis_id");
            msForeignId = resultSet.getString("frg_id");
            msDriverLicense = resultSet.getString("drv_lic");
            msAddressStreet = resultSet.getString("add_str");
            msAddressNumberExt = resultSet.getString("add_num_ext");
            msAddressNumberInt = resultSet.getString("add_num_int");
            msAddressDistrictCode = resultSet.getString("add_dist_code");
            msAddressDistrictName = resultSet.getString("add_dist_name");
            msAddressLocalityCode = resultSet.getString("add_loc_code");
            msAddressLocalityName = resultSet.getString("add_loc_name");
            msAddressReference = resultSet.getString("add_ref");
            msAddressCountyCode = resultSet.getString("add_cou_code");
            msAddressCountyName = resultSet.getString("add_cou_name");
            msAddressStateCode = resultSet.getString("add_ste_code");
            msAddressStateName = resultSet.getString("add_ste_name");
            msAddressZipCode = resultSet.getString("add_zip");
            mnFkTransportFigureTypeId = resultSet.getInt("fk_tpt_figure_tp");
            mnFkTransportFigureId = resultSet.getInt("fk_tpt_figure");
            mnFkFigureCountryId = resultSet.getInt("fk_figure_cty");
            mnFkAddressCountryId = resultSet.getInt("fk_add_cty");
            
            // read as well child transport parts:
            
            msSql = "SELECT id_tpt_part "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE_TPT_PART) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolTransportFigureTransportPart transportPart = (DDbBolTransportFigureTransportPart) session.readRegistry(DModConsts.L_BOL_TPT_FIGURE_TPT_PART, new int[] { mnPkBolId, mnPkTransportFigureId, resultSet.getInt("id_tpt_part") });
                    maChildTransportParts.add(transportPart);
                }
            }
            
            moOwnTransportFigure = (DDbTransportFigure) session.readRegistry(DModConsts.LU_TPT_FIGURE, new int[] { mnFkTransportFigureId });
            
            msDbmsTransportFigureTypeCode = (String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { mnFkTransportFigureTypeId }, DDbRegistry.FIELD_CODE);
            msDbmsTransportFigureTypeName = (String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { mnFkTransportFigureTypeId }, DDbRegistry.FIELD_NAME);
            
            mnBolSortingPos = mnPkTransportFigureId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // prepare dependencies:
        
        if (moOwnTransportFigure == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (Own " + DDbTransportFigure.class.getName() + ")");
        }
        else if (moOwnTransportFigure.isRegistryNew() || (moOwnTransportFigure.isRegistryEdited() && mbBolUpdateOwnRegistry)) {
            moOwnTransportFigure.save(session);
        }
        
        mnFkTransportFigureId = moOwnTransportFigure.getPkTransportFigureId();
        
        // save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkTransportFigureId + ", " + 
                    "'" + msName + "', " + 
                    "'" + msFiscalId + "', " + 
                    "'" + msForeignId + "', " + 
                    "'" + msDriverLicense + "', " + 
                    "'" + msAddressStreet + "', " + 
                    "'" + msAddressNumberExt + "', " + 
                    "'" + msAddressNumberInt + "', " + 
                    "'" + msAddressDistrictCode + "', " + 
                    "'" + msAddressDistrictName + "', " + 
                    "'" + msAddressLocalityCode + "', " + 
                    "'" + msAddressLocalityName + "', " + 
                    "'" + msAddressReference + "', " + 
                    "'" + msAddressCountyCode + "', " + 
                    "'" + msAddressCountyName + "', " + 
                    "'" + msAddressStateCode + "', " + 
                    "'" + msAddressStateName + "', " + 
                    "'" + msAddressZipCode + "', " + 
                    mnFkTransportFigureTypeId + ", " + 
                    mnFkTransportFigureId + ", " + 
                    mnFkFigureCountryId + ", " + 
                    mnFkAddressCountryId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_tpt_figure = " + mnPkTransportFigureId + ", " +
                    "name = '" + msName + "', " +
                    "fis_id = '" + msFiscalId + "', " +
                    "frg_id = '" + msForeignId + "', " +
                    "drv_lic = '" + msDriverLicense + "', " +
                    "add_str = '" + msAddressStreet + "', " +
                    "add_num_ext = '" + msAddressNumberExt + "', " +
                    "add_num_int = '" + msAddressNumberInt + "', " +
                    "add_dist_code = '" + msAddressDistrictCode + "', " +
                    "add_dist_name = '" + msAddressDistrictName + "', " +
                    "add_loc_code = '" + msAddressLocalityCode + "', " +
                    "add_loc_name = '" + msAddressLocalityName + "', " +
                    "add_ref = '" + msAddressReference + "', " +
                    "add_cou_code = '" + msAddressCountyCode + "', " +
                    "add_cou_name = '" + msAddressCountyName + "', " +
                    "add_ste_code = '" + msAddressStateCode + "', " +
                    "add_ste_name = '" + msAddressStateName + "', " +
                    "add_zip = '" + msAddressZipCode + "', " +
                    "fk_tpt_figure_tp = " + mnFkTransportFigureTypeId + ", " +
                    "fk_tpt_figure = " + mnFkTransportFigureId + ", " +
                    "fk_figure_cty = " + mnFkFigureCountryId + ", " +
                    "fk_add_cty = " + mnFkAddressCountryId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child transport parts:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE_TPT_PART) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolTransportFigureTransportPart transportPart : maChildTransportParts) {
            transportPart.setPkBolId(mnPkBolId);
            transportPart.setPkTransportFigureId(mnPkTransportFigureId);
            transportPart.setRegistryNew(true);
            transportPart.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolTransportFigure clone() throws CloneNotSupportedException {
        DDbBolTransportFigure registry = new DDbBolTransportFigure();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkTransportFigureId(this.getPkTransportFigureId());
        registry.setName(this.getName());
        registry.setFiscalId(this.getFiscalId());
        registry.setForeignId(this.getForeignId());
        registry.setDriverLicense(this.getDriverLicense());
        registry.setAddressStreet(this.getAddressStreet());
        registry.setAddressNumberExt(this.getAddressNumberExt());
        registry.setAddressNumberInt(this.getAddressNumberInt());
        registry.setAddressDistrictCode(this.getAddressDistrictCode());
        registry.setAddressDistrictName(this.getAddressDistrictName());
        registry.setAddressLocalityCode(this.getAddressLocalityCode());
        registry.setAddressLocalityName(this.getAddressLocalityName());
        registry.setAddressReference(this.getAddressReference());
        registry.setAddressCountyCode(this.getAddressCountyCode());
        registry.setAddressCountyName(this.getAddressCountyName());
        registry.setAddressStateCode(this.getAddressStateCode());
        registry.setAddressStateName(this.getAddressStateName());
        registry.setAddressZipCode(this.getAddressZipCode());
        registry.setFkTransportFigureTypeId(this.getFkTransportFigureTypeId());
        registry.setFkTransportFigureId(this.getFkTransportFigureId());
        registry.setFkFigureCountryId(this.getFkFigureCountryId());
        registry.setFkAddressCountryId(this.getFkAddressCountryId());
        
        // clone as well child transport parts:

        for (DDbBolTransportFigureTransportPart transportPart : maChildTransportParts) {
            registry.getChildTransportParts().add(transportPart.clone());
        }
        
        registry.setOwnTransportFigure(this.getOwnTransportFigure()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());
        
        registry.setDbmsTransportFigureTypeCode(this.getDbmsTransportFigureTypeCode());
        registry.setDbmsTransportFigureTypeName(this.getDbmsTransportFigureTypeName());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnTransportFigure() {
        if (moOwnTransportFigure != null) {
            setName(moOwnTransportFigure.getName());
            setFiscalId(moOwnTransportFigure.getFiscalId());
            setForeignId(moOwnTransportFigure.getForeignId());
            setDriverLicense(moOwnTransportFigure.getDriverLicense());
            setAddressStreet(moOwnTransportFigure.getAddressStreet());
            setAddressNumberExt(moOwnTransportFigure.getAddressNumberExt());
            setAddressNumberInt(moOwnTransportFigure.getAddressNumberInt());
            setAddressDistrictCode(moOwnTransportFigure.getAddressDistrictCode());
            setAddressDistrictName(moOwnTransportFigure.getAddressDistrictName());
            setAddressLocalityCode(moOwnTransportFigure.getAddressLocalityCode());
            setAddressLocalityName(moOwnTransportFigure.getAddressLocalityName());
            setAddressReference(moOwnTransportFigure.getAddressReference());
            setAddressCountyCode(moOwnTransportFigure.getAddressCountyCode());
            setAddressCountyName(moOwnTransportFigure.getAddressCountyName());
            setAddressStateCode(moOwnTransportFigure.getAddressStateCode());
            setAddressStateName(moOwnTransportFigure.getAddressStateName());
            setAddressZipCode(moOwnTransportFigure.getAddressZipCode());
            
            setFkTransportFigureTypeId(moOwnTransportFigure.getFkTransportFigureTypeId());
            setFkTransportFigureId(moOwnTransportFigure.getPkTransportFigureId());
            setFkFigureCountryId(moOwnTransportFigure.getFkFigureCountryId());
            setFkAddressCountryId(moOwnTransportFigure.getFkAddressCountryId());
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnTransportFigure != null ? moOwnTransportFigure.getCode() : "";
    }

    @Override
    public String getRowName() {
        return moOwnTransportFigure != null ? moOwnTransportFigure.getName() : "";
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
        return mbRegistryEdited;
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRegistryEdited = edited;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = mnBolSortingPos;
                break;
            case 1:
                value = msName;
                break;
            case 2:
                value = msFiscalId;
                break;
            case 3:
                value = msForeignId;
                break;
            case 4:
                value = msDriverLicense;
                break;
            case 5:
                value = msDbmsTransportFigureTypeCode + " - " + msDbmsTransportFigureTypeName;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
