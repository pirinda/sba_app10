/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbCertificate extends DDbRegistryUser {

    protected int mnPkCertificateId;
    protected String msCode;
    protected String msName;
    protected String msCertificateNumber;
    protected java.sql.Blob moCertificateKeyPrivate_n;
    protected java.sql.Blob moCertificateKeyPublic_n;
    protected java.sql.Blob moCertificatePemKeyPrivate_n;
    protected java.sql.Blob moCertificatePemKeyPublic_n;
    protected Date mtCertificateDateStart;
    protected Date mtCertificateDateEnd;
    protected String msPasswordPrivate;
    protected String msPasswordRevoke;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected byte[] mabXtaCertificateKeyPrivate_n;
    protected byte[] mabXtaCertificateKeyPublic_n;
    protected byte[] mabXtaCertificatePemKeyPrivate_n;
    protected byte[] mabXtaCertificatePemKeyPublic_n;
    
    public DDbCertificate() {
        super(DModConsts.CU_CER);
        initRegistry();
    }

    public void setPkCertificateId(int n) { mnPkCertificateId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setCertificateNumber(String s) { msCertificateNumber = s; }
    public void setCertificateKeyPrivate_n(java.sql.Blob o) { moCertificateKeyPrivate_n = o; }
    public void setCertificateKeyPublic_n(java.sql.Blob o) { moCertificateKeyPublic_n = o; }
    public void setCertificatePemKeyPrivate_n(java.sql.Blob o) { moCertificatePemKeyPrivate_n = o; }
    public void setCertificatePemKeyPublic_n(java.sql.Blob o) { moCertificatePemKeyPublic_n = o; }
    public void setCertificateDateStart(Date t) { mtCertificateDateStart = t; }
    public void setCertificateDateEnd(Date t) { mtCertificateDateEnd = t; }
    public void setPasswordPrivate(String s) { msPasswordPrivate = s; }
    public void setPasswordRevoke(String s) { msPasswordRevoke = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCertificateId() { return mnPkCertificateId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getCertificateNumber() { return msCertificateNumber; }
    public java.sql.Blob getCertificateKeyPrivate_n() { return moCertificateKeyPrivate_n; }
    public java.sql.Blob getCertificateKeyPublic_n() { return moCertificateKeyPublic_n; }
    public java.sql.Blob getCertificatePemKeyPrivate_n() { return moCertificatePemKeyPrivate_n; }
    public java.sql.Blob getCertificatePemKeyPublic_n() { return moCertificatePemKeyPublic_n; }
    public Date getCertificateDateStart() { return mtCertificateDateStart; }
    public Date getCertificateDateEnd() { return mtCertificateDateEnd; }
    public String getPasswordPrivate() { return msPasswordPrivate; }
    public String getPasswordRevoke() { return msPasswordRevoke; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaCertificateKeyPrivate_n(byte[] ab) { mabXtaCertificateKeyPrivate_n = ab; }
    public void setXtaCertificateKeyPublic_n(byte[] ab) { mabXtaCertificateKeyPublic_n = ab; }
    public void setXtaCertificatePemKeyPrivate_n(byte[] ab) { mabXtaCertificatePemKeyPrivate_n = ab; }
    public void setXtaCertificatePemKeyPublic_n(byte[] ab) { mabXtaCertificatePemKeyPublic_n = ab; }
    
    public byte[] getXtaCertificateKeyPrivate_n() { return mabXtaCertificateKeyPrivate_n; }
    public byte[] getXtaCertificateKeyPublic_n() { return mabXtaCertificateKeyPublic_n; }
    public byte[] getXtaCertificatePemKeyPrivate_n() { return mabXtaCertificatePemKeyPrivate_n; }
    public byte[] getXtaCertificatePemKeyPublic_n() { return mabXtaCertificatePemKeyPublic_n; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCertificateId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCertificateId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCertificateId = 0;
        msCode = "";
        msName = "";
        msCertificateNumber = "";
        moCertificateKeyPrivate_n = null;
        moCertificateKeyPublic_n = null;
        moCertificatePemKeyPrivate_n = null;
        moCertificatePemKeyPublic_n = null;
        mtCertificateDateStart = null;
        mtCertificateDateEnd = null;
        msPasswordPrivate = "";
        msPasswordRevoke = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mabXtaCertificateKeyPrivate_n = null;
        mabXtaCertificateKeyPublic_n = null;
        mabXtaCertificatePemKeyPrivate_n = null;
        mabXtaCertificatePemKeyPublic_n = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cer = " + mnPkCertificateId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cer = " + pk[0] + " ";
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
            mnPkCertificateId = resultSet.getInt("id_cer");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msCertificateNumber = resultSet.getString("cer_num");
            moCertificateKeyPrivate_n = resultSet.getBlob("cer_pri_n");
            moCertificateKeyPublic_n = resultSet.getBlob("cer_pub_n");
            moCertificatePemKeyPrivate_n = resultSet.getBlob("cer_pem_pri_n");
            moCertificatePemKeyPublic_n = resultSet.getBlob("cer_pem_pub_n");
            mtCertificateDateStart = resultSet.getDate("cer_dt_sta");
            mtCertificateDateEnd = resultSet.getDate("cer_dt_end");
            msPasswordPrivate = resultSet.getString("pswd_pri");
            msPasswordRevoke = resultSet.getString("pswd_rev");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            if (moCertificateKeyPrivate_n != null) {
                mabXtaCertificateKeyPrivate_n = DLibUtils.convertBlobToBytes(moCertificateKeyPrivate_n);
            }

            if (moCertificateKeyPublic_n != null) {
                mabXtaCertificateKeyPublic_n = DLibUtils.convertBlobToBytes(moCertificateKeyPublic_n);
            }

            if (moCertificatePemKeyPrivate_n != null) {
                mabXtaCertificatePemKeyPrivate_n = DLibUtils.convertBlobToBytes(moCertificatePemKeyPrivate_n);
            }

            if (moCertificatePemKeyPublic_n != null) {
                mabXtaCertificatePemKeyPublic_n = DLibUtils.convertBlobToBytes(moCertificatePemKeyPublic_n);
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = false;
            
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCertificateId + ", " +
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msCertificateNumber + "', " +
                    //moCertificateKeyPrivate_n + ", " +
                    "NULL, " +
                    //moCertificateKeyPublic_n + ", " +
                    "NULL, " +
                    //moCertificatePemKeyPrivate_n + ", " + 
                    "NULL, " +
                    //moCertificatePemKeyPublic_n + ", " + 
                    "NULL, " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtCertificateDateStart) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtCertificateDateEnd) + "', " +
                    "'" + msPasswordPrivate + "', " +
                    "'" + msPasswordRevoke + "', " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_cer = " + mnPkCertificateId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "cer_num = '" + msCertificateNumber + "', " +
                    //"cer_pri_n = " + moCertificateKeyPrivate_n + ", " +
                    //"cer_pub_n = " + moCertificateKeyPublic_n + ", " +
                    //"cer_pem_pri_n = " + moCertificatePemKeyPrivate_n + ", " +
                    //"cer_pem_pub_n = " + moCertificatePemKeyPublic_n + ", " +
                    "cer_dt_sta = '" + DLibUtils.DbmsDateFormatDate.format(mtCertificateDateStart) + "', " +
                    "cer_dt_end = '" + DLibUtils.DbmsDateFormatDate.format(mtCertificateDateEnd) + "', " +
                    "pswd_pri = '" + msPasswordPrivate + "', " +
                    "pswd_rev = '" + msPasswordRevoke + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbCertificate clone() throws CloneNotSupportedException {
        DDbCertificate registry = new DDbCertificate();

        registry.setPkCertificateId(this.getPkCertificateId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setCertificateNumber(this.getCertificateNumber());
        registry.setCertificateKeyPrivate_n(this.getCertificateKeyPrivate_n());
        registry.setCertificateKeyPublic_n(this.getCertificateKeyPublic_n());
        registry.setCertificatePemKeyPrivate_n(this.getCertificatePemKeyPrivate_n());
        registry.setCertificatePemKeyPublic_n(this.getCertificatePemKeyPublic_n());
        registry.setCertificateDateStart(this.getCertificateDateStart());
        registry.setCertificateDateEnd(this.getCertificateDateEnd());
        registry.setPasswordPrivate(this.getPasswordPrivate());
        registry.setPasswordRevoke(this.getPasswordRevoke());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
