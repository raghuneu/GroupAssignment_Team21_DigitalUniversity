/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rg
 */
public class UserAccount {

    private String username;
    private String password;
    private Role role;
    private String personId;
    private Boolean active;

    /**
     * 
     */
    public UserAccount(String username, String password, Role role, String personId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.personId = personId;
        this.active = true;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    public String getPersonId() {
        return personId;
    }


    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
