package com.example.women_healthcare;

public class Relative {
    private String id;
    private String name;
    private String phone;
    private String email;

    public Relative() {
        // Required empty constructor for Firebase
    }

    public Relative(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
