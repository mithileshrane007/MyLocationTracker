package com.example.infiny.mylocationtracker.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by infiny on 21/12/16.
 */

public class SessionManager {
    private static final String CLICKED = "clicked";
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Near";


    //Keys names
    private String CURRENT_LOC_LAT="latit";
    private String CURRENT_LOC_LONG="long";
    private String STORE_TIME="str_time";
    private String TIMERSTARTTIME="timerStartTime";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLocation(double latit,double longi)
    {

        editor.putFloat(CURRENT_LOC_LAT, ((float) latit));
        editor.putFloat(CURRENT_LOC_LONG, ((float) longi));
        editor.commit();
    }
    public LatLng getLocation()
    {
        LatLng latLng=new LatLng(pref.getFloat(CURRENT_LOC_LAT,0f),pref.getFloat(CURRENT_LOC_LONG,0f));
        return latLng;
    }


    public void clear()
    {
        editor.clear();
        editor.commit();
    }




    public void setClicked(boolean clicked) {
        editor.putBoolean(CLICKED,clicked);
        editor.commit();
    }
    public boolean getClicked() {
        return pref.getBoolean(CLICKED,false);
    }


    public void setTimerStartTime(long timerStartTime) {
        editor.putLong(TIMERSTARTTIME,timerStartTime);
        editor.commit();
    }
    public long  getTimerStartTime() {
        return pref.getLong(TIMERSTARTTIME,0);
    }


    public void clearTimer() {
        editor.putString(TIMERSTARTTIME,"");
        editor.commit();
    }
}
