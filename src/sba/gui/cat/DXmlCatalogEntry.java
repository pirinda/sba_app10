/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.gui.cat;

import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public class DXmlCatalogEntry {
    
    static final String Entry = "Entry";
    static final String Id = "id";
    static final String Code = "code";
    static final String Name = "name";
    static final String TermStart = "term_start";
    static final String TermEnd = "term_end";
    static final String ApplyEntityPer = "app_per";
    static final String ApplyEntityOrg = "app_org";
    
    private int mnId;
    private String msCode;
    private String msName;
    private Date mtTermStart;
    private Date mtTermEnd;
    private boolean mbApplyEntityPer;
    private boolean mbApplyEntityOrg;
    
    public DXmlCatalogEntry(int id, String code, String name, Date termStart, Date termEnd) {
        this (id, code, name, termStart, termEnd, false, false);
    }
    
    public DXmlCatalogEntry(int id, String code, String name, Date termStart, Date termEnd, boolean isApplyEntityPer, boolean isApplyEntityOrg) {
        mnId = id;
        msCode = code;
        msName = name;
        mtTermStart = termStart;
        mtTermEnd = termEnd;
        mbApplyEntityPer = isApplyEntityPer;
        mbApplyEntityOrg = isApplyEntityOrg;
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
}
