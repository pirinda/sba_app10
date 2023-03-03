/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.gui.cat;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public class DXmlCatalogEntry {
    
    public static final String Entry = "Entry";
    public static final String Id = "id";
    public static final String Code = "code";
    public static final String Name = "name";
    public static final String TermStart = "term_start";
    public static final String TermEnd = "term_end";
    public static final String ApplyEntityPer = "app_per";
    public static final String ApplyEntityOrg = "app_org";
    
    public static final String VAL_REQUIRED_NO = "0";
    public static final String VAL_REQUIRED_YES = "1";
    public static final String VAL_REQUIRED_OPT = "0, 1";
    
    public static final String TXT_REQUIRED_NO = "No requerido";
    public static final String TXT_REQUIRED_YES = "Requerido";
    public static final String TXT_REQUIRED_OPT = "Opcional";
    
    public static final int REQUIRED_NO = 0;
    public static final int REQUIRED_YES = 1;
    public static final int REQUIRED_OPT = 2;
    
    private int mnId;
    private String msCode;
    private String msName;
    private Date mtTermStart;
    private Date mtTermEnd;
    private boolean mbApplyEntityPer;
    private boolean mbApplyEntityOrg;
    private String msBelongingCode;
    private String msSearchingCode;
    private HashMap<String, String> moExtraAttributesMap;
    
    public DXmlCatalogEntry(int id, String code, String name, Date termStart, Date termEnd) {
        this(id, code, name, termStart, termEnd, false, false, "", "");
    }
    
    public DXmlCatalogEntry(int id, String code, String name, Date termStart, Date termEnd, boolean isApplyEntityPer, boolean isApplyEntityOrg) {
        this(id, code, name, termStart, termEnd, isApplyEntityPer, isApplyEntityOrg, "", "");
    }
    
    public DXmlCatalogEntry(int id, String code, String name, Date termStart, Date termEnd, boolean isApplyEntityPer, boolean isApplyEntityOrg, String belongingCode, String searchingCode) {
        mnId = id;
        msCode = code;
        msName = name;
        mtTermStart = termStart;
        mtTermEnd = termEnd;
        mbApplyEntityPer = isApplyEntityPer;
        mbApplyEntityOrg = isApplyEntityOrg;
        msBelongingCode = belongingCode;
        msSearchingCode = searchingCode;
        moExtraAttributesMap = new HashMap<>();
    }

    public int getId() {
        return mnId;
    }

    public String getCode() {
        return msCode;
    }

    public String getName() {
        return msName;
    }

    public Date getTermStart() {
        return mtTermStart;
    }

    public Date getTermEnd() {
        return mtTermEnd;
    }

    public boolean isApplyEntityPer() {
        return mbApplyEntityPer;
    }

    public boolean isApplyEntityOrg() {
        return mbApplyEntityOrg;
    }
    
    public String getBelongingCode() {
        return msBelongingCode;
    }
    
    public String getSearchingCode() {
        return msSearchingCode;
    }

    public HashMap<String, String> getExtraAttributesMap() {
        return moExtraAttributesMap;
    }
}
