/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rg
 */
public class UserAccountDirectory {

    private final Map<String, UserAccount> byUsername = new HashMap<>();

    /**
     * 
     */
    public void add(UserAccount ua) {
        byUsername.put(ua.getUsername(), ua);
    }

    /**
     * 
     */
    public void remove(String username) {
        byUsername.remove(username);
    }

    /**
     * 
     */
    public UserAccount get(String username) {
        return byUsername.get(username);
    }

    /**
     * 
     */
    public List<UserAccount> list() {
        return new ArrayList<>(byUsername.values());
    }

    /**
     *
     */
    public UserAccount authenticate(String username, String password) {
        UserAccount ua = byUsername.get(username);
        if (ua != null && ua.isActive() && ua.getPassword().equals(password)) {
            return ua;
        }
        return null;
    }

}
