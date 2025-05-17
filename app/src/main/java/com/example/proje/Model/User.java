package com.example.proje.Model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private String oldUsername; // Eski kullanıcı adı için

    // Constructor
    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = false;
        this.oldUsername = username;
    }

    public User(String username, String password, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.oldUsername = username;
    }

    // Getter ve Setter'lar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getOldUsername() {
        return oldUsername != null ? oldUsername : username;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }
}
