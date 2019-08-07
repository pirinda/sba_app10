/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.bpr.db;

/**
 *
 * @author Sergio Flores
 */
public class DBprEmail {
    public String Contact;
    public String Email;

    public DBprEmail(String contact, String email) {
        Contact = contact;
        Email = email;
    }
    
    public String composeEmail() {
        String email = "";
        
        if (!Contact.isEmpty()) {
            email = "\"" + Contact + "\" <" + Email + ">";
        }
        else {
            email = Email;
        }
        
        return email;
    }
}
