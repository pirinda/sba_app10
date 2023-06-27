/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.ver4.DCfdVer4Consts;
import java.util.ArrayList;

/**
 * Data structure to exchange CFD relations between editing dialog and CFD form.
 * @author Sergio Flores
 */
public class DDfrMateRelations {
    
    public static final String UUID_SEPARATOR = ",";
    public static final String RELATED_CFD_SEPARATOR = "/ ";
    
    protected ArrayList<RelatedCfd> maRelatedCfds;
    
    /**
     * Create CFD relations.
     */
    public DDfrMateRelations() {
        maRelatedCfds = new ArrayList<>();
    }
    
    /**
     * Get all related CFD.
     * @return 
     */
    public ArrayList<RelatedCfd> getRelatedCfds() {
        return maRelatedCfds;
    }
    
    /**
     * Get all matching related CFD.
     * @param relationCode Code of relation type to match.
     * @return 
     */
    public ArrayList<RelatedCfd> getRelatedCfds(final String relationCode) {
        ArrayList<RelatedCfd> relatedCfds = new ArrayList<>();
        
        for (RelatedCfd relatedCfd : maRelatedCfds) {
            if (relatedCfd.RelationCode.equals(relationCode)) {
                relatedCfds.add(relatedCfd);
            }
        }
        
        return relatedCfds;
    }
    
    /**
     * Add related CFD if not already added.
     * @param relationCode Code of relation type.
     * @param uuid Related CFD to add.
     * @return <code>true</code> if added.
     * @throws Exception 
     */
    public boolean addRelatedCfd(final String relationCode, final String uuid) throws Exception {
        boolean added = false;
        String uuidToAdd = parseUuids(uuid).get(0);
        ArrayList<RelatedCfd> relatedCfds = getRelatedCfds(relationCode);
        
        if (relatedCfds.isEmpty()) {
            maRelatedCfds.add(new RelatedCfd(relationCode, uuidToAdd));
            added = true;
        }
        else {
            boolean found = false;
            
            for (RelatedCfd relatedCfd : relatedCfds) {
                if (relatedCfd.Uuids.contains(uuid)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                relatedCfds.get(0).Uuids.add(uuid);
                added = true;
            }
        }
        
        return added;
    }
    
    /**
     * Remove all related CFD if already added.
     * @param relationCode Code of relation type.
     * @param uuid Related CFD to remove. All occurrences will be removed.
     * @return <code>true</code> if removed.
     * @throws Exception 
     */
    public boolean removeRelatedCfd(final String relationCode, final String uuid) throws Exception {
        boolean removed = false;
        String uuidToRemove = parseUuids(uuid).get(0);
        ArrayList<RelatedCfd> relatedCfds = getRelatedCfds(relationCode);
        ArrayList<RelatedCfd> relatedCfdsToRemove = new ArrayList<>();
        
        if (!relatedCfds.isEmpty()) {
            for (RelatedCfd relatedCfd : relatedCfds) {
                while (relatedCfd.Uuids.remove(uuidToRemove)) {
                    removed = true;
                }
                
                if (relatedCfd.Uuids.isEmpty()) {
                    relatedCfdsToRemove.add(relatedCfd);
                }
            }
            
            maRelatedCfds.removeAll(relatedCfdsToRemove); // discard related CFD without UUID
        }
        
        return removed;
    }
    
    /**
     * Get object info as String in format:
     * related_cfd_1/ related_cfd_2/ ... related_cfd_n
     * @return String representation of object.
     */
    @Override
    public String toString() {
        String string = "";
        
        for (RelatedCfd relatedCfd : maRelatedCfds) {
            string += (string.isEmpty() ? "" : RELATED_CFD_SEPARATOR) + relatedCfd.toString();
        }
        
        return string;
    }
    
    @Override
    public DDfrMateRelations clone() throws CloneNotSupportedException  {
        DDfrMateRelations clone = new DDfrMateRelations();
        
        for(RelatedCfd relatedCfd : maRelatedCfds) {
            clone.getRelatedCfds().add(relatedCfd.clone());
        }
        
        return clone;
    }
    
    /**
     * Parse UUID's delimited with default separator, comma.
     * @param plainUuids Plain list of UUID's.
     * @return UUID's list.
     * @throws Exception 
     */
    public static ArrayList<String> parseUuids(final String plainUuids) throws Exception {
        ArrayList<String> list = new ArrayList<>();
        
        String[] uuids = plainUuids.replaceAll(" ", "").split(UUID_SEPARATOR); // remove whitespaces before splitting
        
        for (String uuid : uuids) {
            if (!uuid.isEmpty()) {
                if (uuid.length() != DCfdVer4Consts.LEN_UUID) {
                    throw new Exception("El UUID '" + uuid + "' debe tener " + DCfdVer4Consts.LEN_UUID + " caracteres.");
                }
                
                if (list.contains(uuid)) {
                    throw new Exception("El UUID '" + uuid + "' está repetido.");
                }
                
                list.add(uuid);
            }
        }
        
        return list;
    }
    
    /**
     * Normalize UUID's delimited with default separator, comma.
     * Remove useless commas, when existing.
     * @param plainUuids Plain list of UUID's.
     * @return Normalized UUID's plain list.
     * @throws Exception 
     */
    public static String normalizeUuids(final String plainUuids) throws Exception {
        String string = "";
        ArrayList<String> uuids = parseUuids(plainUuids);
        
        for (String uuid : uuids) {
            string += (string.isEmpty() ? "" : UUID_SEPARATOR + " ") + uuid;
        }
        
        return string;
    }
    
    /**
     * Individual node of related CFD's.
     */
    public final class RelatedCfd {
        
        public static final String COLON = ": ";
        
        public String RelationCode;
        public ArrayList<String> Uuids;
        
        /**
         * Create Related CFD from plain list of UUID's delimited with default separator, comma.
         * @param relationCode Code of relation type.
         * @param plainUuids Plain list of UUID's.
         * @throws Exception 
         */
        public RelatedCfd(final String relationCode, final String plainUuids) throws Exception {
            RelationCode = relationCode;
            Uuids = parseUuids(plainUuids);
        }
        
        /**
         * Create Related CFD.
         * @param relationCode Code of relation type.
         * @param uuids List of UUID's.
         */
        private RelatedCfd(final String relationCode, final ArrayList<String> uuids) {
            RelationCode = relationCode;
            Uuids = new ArrayList<>();
            Uuids.addAll(uuids);
        }
        
        /**
         * Get plain list of UUID's.
         * @return Plain list of UUID's.
         */
        public String getUuids() {
            return Uuids.toString().replace("[", "").replace("]", "");
        }
        
        /**
         * Get object info as String in format:
         * relation_code: uuid1, uuid2, uuid_n
         * @return String representation of object.
         */
        @Override
        public String toString() {
            String string = "";
            
            for (String uuid : Uuids) {
                string += (string.isEmpty() ? "" : UUID_SEPARATOR + " ") + uuid;
            }
            
            string = RelationCode + ": " + string;
            
            return string;
        }
        
        @Override
        public RelatedCfd clone() throws CloneNotSupportedException {
            RelatedCfd clone = new RelatedCfd(RelationCode, Uuids);
            
            return clone;
        }
    }
}
