/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
import sba.mod.lad.form.DFormBolUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbBolMerchandise extends DDbRegistryUser implements DGridRow, DBolGridRow {
    
    private static final int SEGMENTS_DIMS = 3; // regex: /([0-9]{1,3}[/]){2}([0-9]{1,3})(cm|plg)/
    private static final int SEGMENTS_IMP_REQ = 4; // regex: /[0-9]{2}  [0-9]{2}  [0-9]{4}  [0-9]{7}/
    private static final String SEPARATOR_DIMS = "/"; // slash
    private static final String SEPARATOR_IMP_REQ = "  "; // double blank

    protected int mnPkBolId;
    protected int mnPkMerchandiseId;
    protected String msDescriptionItem;
    protected String msDescriptionUnit;
    protected double mdQuantity;
    protected String msDimensions;
    protected boolean mbHazardousMaterial;
    protected String msHazardousMaterial;
    protected String msHazardousMaterialCode;
    protected String msHazardousMaterialName;
    protected String msPackagingCode;
    protected String msPackagingName;
    protected double mdWeightKg;
    protected double mdValue;
    protected String msTariff;
    protected String msImportRequest;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkCurrencyId;
    
    protected ArrayList<DDbBolMerchandiseMove> maChildMoves;
    
    protected DDbItem moOwnItem;
    protected DDbUnit moOwnUnit;
    
    protected int mnBolSortingPos;
    
    protected double mdAuxQuantityMoved;

    public DDbBolMerchandise() {
        super(DModConsts.L_BOL_MERCH);
        maChildMoves = new ArrayList<>();
        initRegistry();
    }

    private void sanitize() {
        if (!mbHazardousMaterial) {
            msHazardousMaterial = "";
            msHazardousMaterialCode = "";
            msHazardousMaterialName = "";
            msPackagingCode = "";
            msPackagingName = "";
        }
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkMerchandiseId(int n) { mnPkMerchandiseId = n; }
    public void setDescriptionItem(String s) { msDescriptionItem = s; }
    public void setDescriptionUnit(String s) { msDescriptionUnit = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setDimensions(String s) { msDimensions = s; }
    public void setHazardousMaterial(boolean b) { mbHazardousMaterial = b; }
    public void setHazardousMaterial(String s) { msHazardousMaterial = s; }
    public void setHazardousMaterialCode(String s) { msHazardousMaterialCode = s; }
    public void setHazardousMaterialName(String s) { msHazardousMaterialName = s; }
    public void setPackagingCode(String s) { msPackagingCode = s; }
    public void setPackagingName(String s) { msPackagingName = s; }
    public void setWeightKg(double d) { mdWeightKg = d; }
    public void setValue(double d) { mdValue = d; }
    public void setTariff(String s) { msTariff = s; }
    public void setImportRequest(String s) { msImportRequest = s; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkMerchandiseId() { return mnPkMerchandiseId; }
    public String getDescriptionItem() { return msDescriptionItem; }
    public String getDescriptionUnit() { return msDescriptionUnit; }
    public double getQuantity() { return mdQuantity; }
    public String getDimensions() { return msDimensions; }
    public boolean isHazardousMaterial() { return mbHazardousMaterial; }
    public String getHazardousMaterial() { return msHazardousMaterial; }
    public String getHazardousMaterialCode() { return msHazardousMaterialCode; }
    public String getHazardousMaterialName() { return msHazardousMaterialName; }
    public String getPackagingCode() { return msPackagingCode; }
    public String getPackagingName() { return msPackagingName; }
    public double getWeightKg() { return mdWeightKg; }
    public double getValue() { return mdValue; }
    public String getTariff() { return msTariff; }
    public String getImportRequest() { return msImportRequest; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    
    public ArrayList<DDbBolMerchandiseMove> getChildMoves() { return maChildMoves; }

    public void setOwnItem(final DDbItem o) { moOwnItem = o; }
    public void setOwnUnit(final DDbUnit o) { moOwnUnit = o; }

    public DDbItem getOwnItem() { return moOwnItem; }
    public DDbUnit getOwnUnit() { return moOwnUnit; }
    
    @Override
    public void setBolUpdateOwnRegistry(boolean update) { }
    @Override
    public void setBolSortingPos(int n) { mnBolSortingPos = n; }
    
    @Override
    public boolean isBolUpdateOwnRegistry() { return false; }
    @Override
    public int getBolSortingPos() { return mnBolSortingPos; }
    
    public void setAuxQuantityMoved(double d) { mdAuxQuantityMoved = d; }
    
    public double getAuxQuantityMoved() { return mdAuxQuantityMoved; }
    
    public Dimensions createDimensions() throws Exception {
        Dimensions dimensions = null;
        
        if (!msDimensions.isEmpty()) {
            String[] sections = msDimensions.split(SEPARATOR_DIMS);

            if (sections.length == SEGMENTS_DIMS) {
                int length = DLibUtils.parseInt(sections[0]);
                int height = DLibUtils.parseInt(sections[1]);
                int width = 0;
                String unit = "";

                if (sections[2].contains(DCfdi40Catalogs.CcpDimensiónCm)) {
                    width = DLibUtils.parseInt(sections[2].substring(0, sections[2].indexOf(DCfdi40Catalogs.CcpDimensiónCm)));
                    unit = DCfdi40Catalogs.CcpDimensiónCm;
                }
                else if (sections[2].contains(DCfdi40Catalogs.CcpDimensiónPlg)) {
                    width = DLibUtils.parseInt(sections[2].substring(0, sections[2].indexOf(DCfdi40Catalogs.CcpDimensiónPlg)));
                    unit = DCfdi40Catalogs.CcpDimensiónPlg;
                }
                else {
                    throw new Exception("Las dimensiones no tienen una unidad de medida conocida: " + msDimensions + ".");
                }

                dimensions = new Dimensions(length, height, width, unit);
            }
            else {
                throw new Exception("Las dimensiones no están integradas de " + SEGMENTS_DIMS + " secciones: " + msDimensions + ".");
            }
        }
        
        return dimensions;
    }
    
    public static String composeDimensions(final int length, final int height, final int width, final String unit) {
        String dimensions = "";
        
        if (length != 0 && height != 0 && width != 0 && !unit.isEmpty()) {
            dimensions = "" + length + SEPARATOR_DIMS + height + SEPARATOR_DIMS + width + unit;
        }
        
        return dimensions;
    }
    
    public ImporRequest createImporRequest() throws Exception {
        ImporRequest imporRequest = null;
        
        if (!msImportRequest.isEmpty()) {
            String[] sections = msImportRequest.split(SEPARATOR_IMP_REQ);

            if (sections.length == SEGMENTS_IMP_REQ) {
                imporRequest = new ImporRequest(sections[0], sections[1], sections[2], sections[3]);
            }
            else {
                throw new Exception("El pedimento de importación no está integrado de " + SEGMENTS_IMP_REQ + " secciones: " + msImportRequest + ".");
            }
        }
        
        return imporRequest;
    }
    
    public static String composeImportRequest(final String section1, final String section2, final String section3, final String section4) {
        String importRequest = "";
        int n1 = DLibUtils.parseInt(section1);
        int n2 = DLibUtils.parseInt(section2);
        int n3 = DLibUtils.parseInt(section3);
        int n4 = DLibUtils.parseInt(section4);
        
        if (n1 != 0 && n2 != 0 && n3 != 0 && n4 != 0) {
            importRequest = DFormBolUtils.FormatSegmentImportRequest1.format(DLibUtils.parseInt(section1)) + SEPARATOR_IMP_REQ +
                    DFormBolUtils.FormatSegmentImportRequest2.format(DLibUtils.parseInt(section2)) + SEPARATOR_IMP_REQ +
                    DFormBolUtils.FormatSegmentImportRequest3.format(DLibUtils.parseInt(section3)) + SEPARATOR_IMP_REQ +
                    DFormBolUtils.FormatSegmentImportRequest4.format(DLibUtils.parseInt(section4));
        }
        
        return importRequest;
    }
    
    public void computeQuantityMoved() {
        mdAuxQuantityMoved = 0;
        
        for (DDbBolMerchandiseMove move : maChildMoves) {
            mdAuxQuantityMoved += move.getQuantity();
        }
    }
    
    public boolean isHazardousMaterialYes() {
        return mbHazardousMaterial && msHazardousMaterial.equals(DDbItem.HAZARDOUS_MATERIAL_Y);
    }
    
    public boolean isHazardousMaterialNo() {
        return mbHazardousMaterial && msHazardousMaterial.equals(DDbItem.HAZARDOUS_MATERIAL_N);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkMerchandiseId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkMerchandiseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkMerchandiseId = 0;
        msDescriptionItem = "";
        msDescriptionUnit = "";
        mdQuantity = 0;
        msDimensions = "";
        mbHazardousMaterial = false;
        msHazardousMaterial = "";
        msHazardousMaterialCode = "";
        msHazardousMaterialName = "";
        msPackagingCode = "";
        msPackagingName = "";
        mdWeightKg = 0;
        mdValue = 0;
        msTariff = "";
        msImportRequest = "";
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkCurrencyId = 0;
        
        maChildMoves.clear();
        
        moOwnItem = null;
        moOwnUnit = null;
        
        mnBolSortingPos = 0;
        
        mdAuxQuantityMoved = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_merch = " + mnPkMerchandiseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_merch = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMerchandiseId = 0;

        msSql = "SELECT COALESCE(MAX(id_merch), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMerchandiseId = resultSet.getInt(1);
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
            mnPkMerchandiseId = resultSet.getInt("id_merch");
            msDescriptionItem = resultSet.getString("descrip_itm");
            msDescriptionUnit = resultSet.getString("descrip_unt");
            mdQuantity = resultSet.getDouble("qty");
            msDimensions = resultSet.getString("dim");
            mbHazardousMaterial = resultSet.getBoolean("b_hazard_mat");
            msHazardousMaterial = resultSet.getString("hazard_mat");
            msHazardousMaterialCode = resultSet.getString("hazard_mat_code");
            msHazardousMaterialName = resultSet.getString("hazard_mat_name");
            msPackagingCode = resultSet.getString("pack_code");
            msPackagingName = resultSet.getString("pack_name");
            mdWeightKg = resultSet.getDouble("weight_kg");
            mdValue = resultSet.getDouble("value");
            msTariff = resultSet.getString("tariff");
            msImportRequest = resultSet.getString("imp_request");
            mnFkItemId = resultSet.getInt("fk_itm");
            mnFkUnitId = resultSet.getInt("fk_unt");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            
            // read as well child moves:
            
            msSql = "SELECT id_move "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH_MOVE) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolMerchandiseMove move = (DDbBolMerchandiseMove) session.readRegistry(DModConsts.L_BOL_MERCH_MOVE, new int[] { mnPkBolId, mnPkMerchandiseId, resultSet.getInt("id_move") });
                    maChildMoves.add(move);
                }
            }
            
            moOwnItem = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { mnFkItemId });
            moOwnUnit = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { mnFkUnitId});
            
            mnBolSortingPos = mnPkMerchandiseId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        sanitize();

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkMerchandiseId + ", " + 
                    "'" + msDescriptionItem + "', " + 
                    "'" + msDescriptionUnit + "', " + 
                    mdQuantity + ", " + 
                    "'" + msDimensions + "', " + 
                    (mbHazardousMaterial ? 1 : 0) + ", " + 
                    "'" + msHazardousMaterial + "', " + 
                    "'" + msHazardousMaterialCode + "', " + 
                    "'" + msHazardousMaterialName + "', " + 
                    "'" + msPackagingCode + "', " + 
                    "'" + msPackagingName + "', " + 
                    mdWeightKg + ", " + 
                    mdValue + ", " + 
                    "'" + msTariff + "', " + 
                    "'" + msImportRequest + "', " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkCurrencyId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_merch = " + mnPkMerchandiseId + ", " +
                    "descrip_itm = '" + msDescriptionItem + "', " +
                    "descrip_unt = '" + msDescriptionUnit + "', " +
                    "qty = " + mdQuantity + ", " +
                    "dim = '" + msDimensions + "', " +
                    "b_hazard_mat = " + (mbHazardousMaterial ? 1 : 0) + ", " +
                    "hazard_mat = '" + msHazardousMaterial + "', " +
                    "hazard_mat_code = '" + msHazardousMaterialCode + "', " +
                    "hazard_mat_name = '" + msHazardousMaterialName + "', " +
                    "pack_code = '" + msPackagingCode + "', " +
                    "pack_name = '" + msPackagingName + "', " +
                    "weight_kg = " + mdWeightKg + ", " +
                    "value = " + mdValue + ", " +
                    "tariff = '" + msTariff + "', " +
                    "imp_request = '" + msImportRequest + "', " +
                    "fk_itm = " + mnFkItemId + ", " +
                    "fk_unt = " + mnFkUnitId + ", " +
                    "fk_cur = " + mnFkCurrencyId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child moves:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH_MOVE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolMerchandiseMove move : maChildMoves) {
            move.setPkBolId(mnPkBolId);
            move.setPkMerchandiseId(mnPkMerchandiseId);
            move.setRegistryNew(true);
            move.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolMerchandise clone() throws CloneNotSupportedException {
        DDbBolMerchandise registry = new DDbBolMerchandise();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkMerchandiseId(this.getPkMerchandiseId());
        registry.setDescriptionItem(this.getDescriptionItem());
        registry.setDescriptionUnit(this.getDescriptionUnit());
        registry.setQuantity(this.getQuantity());
        registry.setDimensions(this.getDimensions());
        registry.setHazardousMaterial(this.isHazardousMaterial());
        registry.setHazardousMaterial(this.getHazardousMaterial());
        registry.setHazardousMaterialCode(this.getHazardousMaterialCode());
        registry.setHazardousMaterialName(this.getHazardousMaterialName());
        registry.setPackagingCode(this.getPackagingCode());
        registry.setPackagingName(this.getPackagingName());
        registry.setWeightKg(this.getWeightKg());
        registry.setValue(this.getValue());
        registry.setTariff(this.getTariff());
        registry.setImportRequest(this.getImportRequest());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        
        // clone as well child moves:

        for (DDbBolMerchandiseMove move : maChildMoves) {
            registry.getChildMoves().add(move.clone());
        }
        
        registry.setOwnItem(this.getOwnItem()); // clone shares the same "read-only" object
        registry.setOwnUnit(this.getOwnUnit()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());
        
        registry.setAuxQuantityMoved(this.getAuxQuantityMoved());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnItem != null ? moOwnItem.getCode() : "";
    }

    @Override
    public String getRowName() {
        return moOwnItem != null ? moOwnItem.getName() : "";
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
                value = msDescriptionItem;
                break;
            case 2:
                value = moOwnItem != null ? moOwnItem.getActualCfdItemKey() : "";
                break;
            case 3:
                value = mdQuantity;
                break;
            case 4:
                value = msDescriptionUnit;
                break;
            case 5:
                value = moOwnUnit != null ? moOwnUnit.getCfdUnitKey() : "";
                break;
            case 6:
                value = mdWeightKg;
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
    
    public class Dimensions {
        
        public int Length;
        public int Height;
        public int Width;
        public String Unit;
        
        public Dimensions(final int length, final int height, final int width, final String unit) {
            Length = length;
            Height = height;
            Width = width;
            Unit = unit;
        }
        
        /**
         * Compose dimensions.
         * @return Dimensions according to regex /([0-9]{1,3}[/]){2}([0-9]{1,3})(cm|plg)/.
         */
        public String compose() {
            return DDbBolMerchandise.composeDimensions(Length, Height, Width, Unit);
        }
    }
    
    public class ImporRequest {
        
        public String Section1;
        public String Section2;
        public String Section3;
        public String Section4;
        
        public ImporRequest(final String section1, final String section2, final String section3, final String section4) {
            Section1 = DFormBolUtils.FormatSegmentImportRequest1.format(DLibUtils.parseInt(section1));
            Section2 = DFormBolUtils.FormatSegmentImportRequest2.format(DLibUtils.parseInt(section2));
            Section3 = DFormBolUtils.FormatSegmentImportRequest3.format(DLibUtils.parseInt(section3));
            Section4 = DFormBolUtils.FormatSegmentImportRequest4.format(DLibUtils.parseInt(section4));
        }
        
        /**
         * Compose import request.
         * @return Dimensions according to regex /[0-9]{2}  [0-9]{2}  [0-9]{4}  [0-9]{7}/.
         */
        public String compose() {
            return DDbBolMerchandise.composeImportRequest(Section1, Section2, Section3, Section4);
        }
    }
}
