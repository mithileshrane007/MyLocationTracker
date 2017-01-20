package com.example.infiny.mylocationtracker.Activities;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.infiny.mylocationtracker.Helpers.GPSTracker;
import com.example.infiny.mylocationtracker.Helpers.SessionManager;
import com.example.infiny.mylocationtracker.IDataFetch;
import com.example.infiny.mylocationtracker.Interfaces.NetworkResponse;
import com.example.infiny.mylocationtracker.NetworkUtils.ErrorVolleyUtils;
import com.example.infiny.mylocationtracker.NetworkUtils.VolleyUtils;

import org.json.JSONObject;

import java.util.TimeZone;

/**
 * Created by infiny on 3/1/17.
 */

public class MyIntentLocationService extends IntentService {


    private IDataFetch iDataFetch;

    public MyIntentLocationService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
        Log.d("val","in MyIntentLocationService");


    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("val","in onHandleIntent");
        try {


        TimeZone tz = TimeZone.getDefault();
        Log.d("val","TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID()+"  DST :: "+tz.getDSTSavings());
        SessionManager sessionManager=new SessionManager(getApplicationContext());
        GPSTracker gpsTracker=new GPSTracker(getApplicationContext());
        VolleyUtils volleyUtils=new VolleyUtils();

        if(gpsTracker.canGetLocation())
        {
            Log.d("val","in data::"+gpsTracker.getLatitude()+gpsTracker.getLatitude());
            sessionManager.setLocation(gpsTracker.getLatitude(),gpsTracker.getLatitude());
            String GMT="";
            if (tz.getDisplayName(false, TimeZone.SHORT).contains("+"))
            {
                GMT="+"+tz.getDisplayName(false, TimeZone.SHORT).split("\\+")[1];
            }
            else
            {
                GMT="-"+tz.getDisplayName(false, TimeZone.SHORT).split("\\-")[1];
            }

            volleyUtils.sendLocationDetails(gpsTracker.getLatitude(), gpsTracker.getLatitude(),GMT,tz.getID(), new NetworkResponse() {
                @Override
                public void receiveResult(Object result) {
                    Log.d("val","in data:: response success");
                    try {
                        JSONObject jsonObject=new JSONObject(result.toString());

                        switch (jsonObject.getString("error"))
                        {
                            case "0":
                                Toast.makeText(getApplicationContext(),"Success Sended",Toast.LENGTH_SHORT).show();

                                break;
                            case "1002":
                                Toast.makeText(getApplicationContext(),"Success Failed",Toast.LENGTH_SHORT).show();

                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        SessionManager sessionManager=new SessionManager(getApplicationContext());
                        Log.d("val1","in expetion inner");

                        sessionManager.setClicked(false);

                    }


                }
            },new ErrorVolleyUtils(getApplicationContext()));
//            iDataFetch.data(gpsTracker.getLatitude(),gpsTracker.getLatitude());
        }

        gpsTracker.stopUsingGPS();
        }catch (Exception e)
        {
            Log.d("val1","in expetion outer");

            e.printStackTrace();
            SessionManager sessionManager=new SessionManager(getApplicationContext());
            sessionManager.setClicked(false);
        }
    }
}
