package com.example.infiny.mylocationtracker.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.infiny.mylocationtracker.Helpers.ConnectivityReceiver;
import com.example.infiny.mylocationtracker.Helpers.GPSTracker;
import com.example.infiny.mylocationtracker.Helpers.SessionManager;
import com.example.infiny.mylocationtracker.R;
import com.example.infiny.mylocationtracker.Utils.SystemPermissionsMarshmallowUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class SplashActivity extends AppCompatActivity {



    SystemPermissionsMarshmallowUtils sysPermissionsMarshmallowUtils;
    private Context mContext;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    SessionManager sessionManager;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.splash_screen);

        mContext=this;
        sessionManager=new SessionManager(this);


        sysPermissionsMarshmallowUtils=new SystemPermissionsMarshmallowUtils(SplashActivity.this);
        if (checkPlayServices()) {

        }else {
            return;
        }


//        Intent intent = FoursquareOAuth.getConnectIntent(mContext, getResources().getString(R.string.CLENTID_FOURSQUARE));
//        startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);

    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("dfdjks", "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(mContext,"onResume",Toast.LENGTH_SHORT).show();

        gpsTracker=new GPSTracker(this);
        if ( (Build.VERSION.SDK_INT>=23)?sysPermissionsMarshmallowUtils.checkPermission():true && ConnectivityReceiver.isConnected() && gpsTracker.canGetLocation())
        {

        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT>=23)
                        if (!sysPermissionsMarshmallowUtils.checkPermission() )
                            sysPermissionsMarshmallowUtils.requestPermission();
                    if (!ConnectivityReceiver.isConnected() )
                        showDialog();
                    if (!gpsTracker.canGetLocation())
                        gpsTracker.showSettingsAlertMaterial();
                }
            },3000);


        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                if (sysPermissionsMarshmallowUtils.checkPermission() && ConnectivityReceiver.isConnected() && gpsTracker.canGetLocation())
//                {
//                    startActivity(new Intent(SplashActivity.this,NewLocation.class));
//                    finish();
//                }

                if (Build.VERSION.SDK_INT>=23){
                    if (sysPermissionsMarshmallowUtils.checkPermission() && ConnectivityReceiver.isConnected() && gpsTracker.canGetLocation())
                    {
                        startActivity(new Intent(SplashActivity.this,NewLocation.class));
                        sessionManager.setLocation(gpsTracker.getLatitude(),gpsTracker.getLongitude());
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this,NewLocation.class));
                    sessionManager.setLocation(gpsTracker.getLatitude(),gpsTracker.getLongitude());

                    finish();

                }
            }
        },5000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SystemPermissionsMarshmallowUtils.PERMISSION_REQUEST_CODE:
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        gps = new GPSTracker(SplashScreen.this);

                onResume();
//                        EZRent.gps=new GPSTracker(HomeCategory.this);
//                        address1= LocationHelper.getLoc(EZRent.gps.getLatitude(),EZRent.gps.getLongitude(),HomeCategory.this);
//                        try {
//                            EZRent.address=address1.get(0).getLocality();
//                        }catch (Exception e)
//                        {
//                            showSettingsAlert();
//                        }
//                        Toast.makeText(getApplicationContext(), "Your Location is -"+GPSTracker.main+" \nLat: " + EZRent.gps.getLatitude() + "\nLong: " + EZRent.gps.getLongitude(), Toast.LENGTH_LONG).show();
//                        pd.dismiss();
//                    }
//                }, 5000);

                break;


        }
    }

    public void showDialog() {
        MaterialDialog materialDialog=  new MaterialDialog.Builder(mContext)
                .title(R.string.no_internet_connection)
                .positiveText("Settings")
                .negativeColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark))
                .positiveColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark))

                .negativeText("Exit")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
//                        Toast.makeText(mContext,"sett",Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();

                        finishAffinity();


                    }
                }).show();
        materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                Toast.makeText(mContext,"setOnDismissListener",Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        materialDialog.setCancelable(false);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (sysPermissionsMarshmallowUtils.checkPermission() && ConnectivityReceiver.isConnected() && gpsTracker.canGetLocation()) {
                    onResume();
                }
                else {
                    if (!sysPermissionsMarshmallowUtils.checkPermission() )
                        sysPermissionsMarshmallowUtils.requestPermission();
                    if (!ConnectivityReceiver.isConnected() )
                        showDialog();
                    if (!gpsTracker.canGetLocation())
                        gpsTracker.showSettingsAlertMaterial();
                }
                break;
        }
    }
}
