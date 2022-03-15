package com.example.teachingonline.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.teachingonline.Activity.Dashboard;
import com.example.teachingonline.Activity.Home;
import com.example.teachingonline.Activity.Login;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String FULLNAME = "FULLNAME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }
    public void createSession( String email, String id) {
        editor.putBoolean(LOGIN,true);
        editor.putString(EMAIL,email);
        editor.putString(ID,id);
        editor.apply();
    }
    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkSLogin() {
        if(!this.isLoggin()){
            Intent i = new Intent(context, Login.class);
            context.startActivity(i);;
            ((Home) context).finish();
        }
    }
    public void checkILogin() {
        if(!this.isLoggin()){
            Intent i = new Intent(context,Login.class);
            context.startActivity(i);;
            ((Dashboard) context).finish();
        }
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(FULLNAME, sharedPreferences.getString(FULLNAME,null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL,null));
        user.put(ID, sharedPreferences.getString(ID,null));
        return user;
    }

    public void ilogout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,Login.class);
        context.startActivity(i);;
        ((Dashboard) context).finish();
    }

    public void slogout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,Login.class);
        context.startActivity(i);;
        ((Home) context).finish();
    }
}
