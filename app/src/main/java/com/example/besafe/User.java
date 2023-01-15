package com.example.besafe;

public class User {
    String uID , name , Emaiil ;

    public User(String uid, String displayName, String email) {
        this.uID = uid;
        this.name = displayName;
        Emaiil = email;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmaiil() {
        return Emaiil;
    }

    public void setEmaiil(String emaiil) {
        Emaiil = emaiil;
    }


}
