/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.srv.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbReparation extends DDbRegistryUser {

    protected int mnPkReparationId;
    protected int mnNumber;
    protected Date mtDate;
    protected Date mtDateEstimate;
    protected Date mtDateCommitment;
    protected Date mtDateFinished_n;
    protected Date mtDateDelivery_n;
    protected String msEquipmentModel;
    protected String msEquipmentSerialNumber;
    protected String msFailure;
    protected String msNoteEquipment;
    protected String msNoteReparation;
    protected double mdEstimate;
    protected int mnWarranty;
    //protected boolean mbDeleted;
    protected int mnFkServiceTypeId;
    protected int mnFkServiceStatusId;
    protected int mnFkBizPartnerBizPartnerId;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkBizPartnerAddressId;
    protected int mnFkEquipmentId;
    protected int mnFkBrandId;
    protected int mnFkDpsId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Date mtAuxDateLog;

    protected int mnLastFkServiceTypeId;
    protected int mnLastFkServiceStatusId;

    public DDbReparation() {
        super(DModConsts.S_REP);
        initRegistry();
    }

    public void setPkReparationId(int n) { mnPkReparationId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateEstimate(Date t) { mtDateEstimate = t; }
    public void setDateCommitment(Date t) { mtDateCommitment = t; }
    public void setDateFinished_n(Date t) { mtDateFinished_n = t; }
    public void setDateDelivery_n(Date t) { mtDateDelivery_n = t; }
    public void setEquipmentModel(String s) { msEquipmentModel = s; }
    public void setEquipmentSerialNumber(String s) { msEquipmentSerialNumber = s; }
    public void setFailure(String s) { msFailure = s; }
    public void setNoteEquipment(String s) { msNoteEquipment = s; }
    public void setNoteReparation(String s) { msNoteReparation = s; }
    public void setEstimate(double d) { mdEstimate = d; }
    public void setWarranty(int n) { mnWarranty = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkServiceTypeId(int n) { mnFkServiceTypeId = n; }
    public void setFkServiceStatusId(int n) { mnFkServiceStatusId = n; }
    public void setFkBizPartnerBizPartnerId(int n) { mnFkBizPartnerBizPartnerId = n; }
    public void setFkBizPartnerBranchId(int n) { mnFkBizPartnerBranchId = n; }
    public void setFkBizPartnerAddressId(int n) { mnFkBizPartnerAddressId = n; }
    public void setFkEquipmentId(int n) { mnFkEquipmentId = n; }
    public void setFkBrandId(int n) { mnFkBrandId = n; }
    public void setFkDpsId_n(int n) { mnFkDpsId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkReparationId() { return mnPkReparationId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public Date getDateEstimate() { return mtDateEstimate; }
    public Date getDateCommitment() { return mtDateCommitment; }
    public Date getDateFinished_n() { return mtDateFinished_n; }
    public Date getDateDelivery_n() { return mtDateDelivery_n; }
    public String getEquipmentModel() { return msEquipmentModel; }
    public String getEquipmentSerialNumber() { return msEquipmentSerialNumber; }
    public String getFailure() { return msFailure; }
    public String getNoteEquipment() { return msNoteEquipment; }
    public String getNoteReparation() { return msNoteReparation; }
    public double getEstimate() { return mdEstimate; }
    public int getWarranty() { return mnWarranty; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkServiceTypeId() { return mnFkServiceTypeId; }
    public int getFkServiceStatusId() { return mnFkServiceStatusId; }
    public int getFkBizPartnerBizPartnerId() { return mnFkBizPartnerBizPartnerId; }
    public int getFkBizPartnerBranchId() { return mnFkBizPartnerBranchId; }
    public int getFkBizPartnerAddressId() { return mnFkBizPartnerAddressId; }
    public int getFkEquipmentId() { return mnFkEquipmentId; }
    public int getFkBrandId() { return mnFkBrandId; }
    public int getFkDpsId_n() { return mnFkDpsId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxDateLog(Date t) { mtAuxDateLog = t; }

    public Date getAuxDateLog() { return mtAuxDateLog; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReparationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReparationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkReparationId = 0;
        mnNumber = 0;
        mtDate = null;
        mtDateEstimate = null;
        mtDateCommitment = null;
        mtDateFinished_n = null;
        mtDateDelivery_n = null;
        msEquipmentModel = "";
        msEquipmentSerialNumber = "";
        msFailure = "";
        msNoteEquipment = "";
        msNoteReparation = "";
        mdEstimate = 0;
        mnWarranty = 0;
        mbDeleted = false;
        mnFkServiceTypeId = 0;
        mnFkServiceStatusId = 0;
        mnFkBizPartnerBizPartnerId = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkBizPartnerAddressId = 0;
        mnFkEquipmentId = 0;
        mnFkBrandId = 0;
        mnFkDpsId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mtAuxDateLog = null;

        mnLastFkServiceTypeId = 0;
        mnLastFkServiceStatusId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rep = " + mnPkReparationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkReparationId = 0;

        msSql = "SELECT COALESCE(MAX(id_rep), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkReparationId = resultSet.getInt(1);
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
            mnPkReparationId = resultSet.getInt("id_rep");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mtDateEstimate = resultSet.getDate("dt_est");
            mtDateCommitment = resultSet.getDate("dt_cmt");
            mtDateFinished_n = resultSet.getDate("dt_fin_n");
            mtDateDelivery_n = resultSet.getDate("dt_dvy_n");
            msEquipmentModel = resultSet.getString("mdl");
            msEquipmentSerialNumber = resultSet.getString("snr");
            msFailure = resultSet.getString("fail");
            msNoteEquipment = resultSet.getString("note_equ");
            msNoteReparation = resultSet.getString("note_rep");
            mdEstimate = resultSet.getDouble("est");
            mnWarranty = resultSet.getInt("war");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkServiceTypeId = resultSet.getInt("fk_srv_tp");
            mnFkServiceStatusId = resultSet.getInt("fk_srv_st");
            mnFkBizPartnerBizPartnerId = resultSet.getInt("fk_bpr_bpr");
            mnFkBizPartnerBranchId = resultSet.getInt("fk_bpr_bra");
            mnFkBizPartnerAddressId = resultSet.getInt("fk_bpr_add");
            mnFkEquipmentId = resultSet.getInt("fk_equ");
            mnFkBrandId = resultSet.getInt("fk_brd");
            mnFkDpsId_n = resultSet.getInt("fk_dps_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mnLastFkServiceTypeId = mnFkServiceTypeId;
            mnLastFkServiceStatusId = mnFkServiceStatusId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        DDbReparationLog log = null;

        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkReparationId + ", " +
                    mnNumber + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateEstimate) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateCommitment) + "', " +
                    (mtDateFinished_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateFinished_n) + "'") + ", " +
                    (mtDateDelivery_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "'") + ", " +
                    "'" + msEquipmentModel + "', " +
                    "'" + msEquipmentSerialNumber + "', " +
                    "'" + msFailure + "', " +
                    "'" + msNoteEquipment + "', " +
                    "'" + msNoteReparation + "', " +
                    mdEstimate + ", " +
                    mnWarranty + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkServiceTypeId + ", " +
                    mnFkServiceStatusId + ", " +
                    mnFkBizPartnerBizPartnerId + ", " +
                    mnFkBizPartnerBranchId + ", " +
                    mnFkBizPartnerAddressId + ", " +
                    mnFkEquipmentId + ", " +
                    mnFkBrandId + ", " +
                    (mnFkDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_rep = " + mnPkReparationId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "dt_est = '" + DLibUtils.DbmsDateFormatDate.format(mtDateEstimate) + "', " +
                    "dt_cmt = '" + DLibUtils.DbmsDateFormatDate.format(mtDateCommitment) + "', " +
                    "dt_fin_n = " + (mtDateFinished_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateFinished_n) + "'") + ", " +
                    "dt_dvy_n = " + (mtDateDelivery_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "'") + ", " +
                    "mdl = '" + msEquipmentModel + "', " +
                    "snr = '" + msEquipmentSerialNumber + "', " +
                    "fail = '" + msFailure + "', " +
                    "note_equ = '" + msNoteEquipment + "', " +
                    "note_rep = '" + msNoteReparation + "', " +
                    "est = " + mdEstimate + ", " +
                    "war = " + mnWarranty + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_srv_tp = " + mnFkServiceTypeId + ", " +
                    "fk_srv_st = " + mnFkServiceStatusId + ", " +
                    "fk_bpr_bpr = " + mnFkBizPartnerBizPartnerId + ", " +
                    "fk_bpr_bra = " + mnFkBizPartnerBranchId + ", " +
                    "fk_bpr_add = " + mnFkBizPartnerAddressId + ", " +
                    "fk_equ = " + mnFkEquipmentId + ", " +
                    "fk_brd = " + mnFkBrandId + ", " +
                    "fk_dps_n = " + (mnFkDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        if (mnLastFkServiceTypeId != mnFkServiceTypeId || mnLastFkServiceStatusId != mnFkServiceStatusId) {
            log = new DDbReparationLog();
            log.setPkReparationId(mnPkReparationId);
            log.setDate(mtAuxDateLog != null ? mtAuxDateLog : mtDate);
            log.setFkServiceTypeId(mnFkServiceTypeId);
            log.setFkServiceStatusId(mnFkServiceStatusId);
            log.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbReparation clone() throws CloneNotSupportedException {
        DDbReparation registry = new DDbReparation();

        registry.setPkReparationId(this.getPkReparationId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setDateEstimate(this.getDateEstimate());
        registry.setDateCommitment(this.getDateCommitment());
        registry.setDateFinished_n(this.getDateFinished_n());
        registry.setDateDelivery_n(this.getDateDelivery_n());
        registry.setEquipmentModel(this.getEquipmentModel());
        registry.setEquipmentSerialNumber(this.getEquipmentSerialNumber());
        registry.setFailure(this.getFailure());
        registry.setNoteEquipment(this.getNoteEquipment());
        registry.setNoteReparation(this.getNoteReparation());
        registry.setEstimate(this.getEstimate());
        registry.setWarranty(this.getWarranty());
        registry.setDeleted(this.isDeleted());
        registry.setFkServiceTypeId(this.getFkServiceTypeId());
        registry.setFkServiceStatusId(this.getFkServiceStatusId());
        registry.setFkBizPartnerBizPartnerId(this.getFkBizPartnerBizPartnerId());
        registry.setFkBizPartnerBranchId(this.getFkBizPartnerBranchId());
        registry.setFkBizPartnerAddressId(this.getFkBizPartnerAddressId());
        registry.setFkEquipmentId(this.getFkEquipmentId());
        registry.setFkBrandId(this.getFkBrandId());
        registry.setFkDpsId_n(this.getFkDpsId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxDateLog(this.getAuxDateLog());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    public void print(final DGuiSession session) throws Exception {
        DSrvUtils.printReparation(session, mnPkReparationId);
    }
}
