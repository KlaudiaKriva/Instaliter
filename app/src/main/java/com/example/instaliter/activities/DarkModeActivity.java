package com.example.instaliter.activities;

import android.content.Context;
import android.content.SharedPreferences;

public class DarkModeActivity  {

    SharedPreferences modePreferences;

    public DarkModeActivity(Context context){
        modePreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setDarkModeState(Boolean state){
        SharedPreferences.Editor editor = modePreferences.edit();
        editor.putBoolean("darkmode",state);
        editor.commit();
    }
    public Boolean loadDarkModeState(){
        Boolean state = modePreferences.getBoolean("darkmode",false);
        return state;
    }
}
