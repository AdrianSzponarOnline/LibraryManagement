package com.librarymanagement.LibraryManagement.user;

public class LoginRequest {
    private String username;
    private String password;

    // gettery i settery
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
