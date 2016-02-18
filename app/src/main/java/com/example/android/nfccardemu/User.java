package com.example.android.nfccardemu;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class User implements Serializable{

    private String ucid;
    private String password;

    public User(){
        ucid = "00000000";
        password = "";
    }

    public User(String u, String p){
        ucid = u;
        password = p;
    }

    public String getUcid(){
        return ucid;
    }

    public String getPassword(){
        return password;
    }

    public void setUcid(String v) {
        ucid = v;
    }

    public void setPassword(String p){
        password = p;
    }

    public String toString(){
        return ucid + " " + password;
    }
}