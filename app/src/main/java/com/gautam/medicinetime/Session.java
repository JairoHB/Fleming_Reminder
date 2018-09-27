package com.gautam.medicinetime;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public String getusername() {
        String usename = prefs.getString("usename","");
        return usename;
    }

    public void settype(String tipo){
        prefs.edit().putString("tipo", tipo).commit();
    }

    public String gettype(){
        String tipo = prefs.getString("tipo","");
        return tipo;
    }

    public void setid(String id){ prefs.edit().putString("id", id).commit(); }

    public String getid(){
        String id = prefs.getString("id","");
        return id;
    }

    public void settel(String tel){ prefs.edit().putString("tel", tel).commit(); }

    public String gettel(){
        String tel = prefs.getString("tel","");
        return tel;
    }
}