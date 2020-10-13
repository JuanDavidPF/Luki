package com.example.luki.model;

public class User {

    private String id;
    private String name;
    private int id_document;
    private int birth_date;
    private int id_exp_date;
    private String email;
    private String pass;
    private String user_type;


    public User(String id, String name, int id_document, int birth_date, int id_exp_date, String email, String pass, String user_type) {
        this.id = id;
        this.name = name;
        this.id_document = id_document;
        this.birth_date = birth_date;
        this.id_exp_date = id_exp_date;
        this.email = email;
        this.pass = pass;
        this.user_type = user_type;
    }

    public User() {
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId_document() {
        return id_document;
    }

    public void setId_document(int id_document) {
        this.id_document = id_document;
    }

    public int getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(int birth_date) {
        this.birth_date = birth_date;
    }

    public int getId_exp_date() {
        return id_exp_date;
    }

    public void setId_exp_date(int id_exp_date) {
        this.id_exp_date = id_exp_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}//closes user class
