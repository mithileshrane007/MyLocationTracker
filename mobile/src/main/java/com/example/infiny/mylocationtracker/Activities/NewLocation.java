package com.example.infiny.mylocationtracker.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infiny.mylocationtracker.Helpers.SessionManager;
import com.example.infiny.mylocationtracker.IDataFetch;
import com.example.infiny.mylocationtracker.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewLocation extends AppCompatActivity implements View.OnClickListener,IDataFetch{
    AlarmManager alarm;
    TextInputLayout tvv_enter_quote;
    Button btn_trackme,btn_stopme,btn_check;
    TextView tv_total_time,tv_timer;
    private Context mContext;
    IDataFetch iDataFetch;
    PendingIntent pintent;
    MenuItem add_new;
    SessionManager sessionManager;

    Timer timer;
    TimerTask timerTask;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int hours=0;
    int milliseconds = 0;
    Handler handler = new Handler();
    ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_new_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Location Tracker");
        mContext=this;
        sessionManager=new SessionManager(mContext);
        Log.d("val1","in onCreate");


        setUpUi();
        iDataFetch=this;

        btn_trackme.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        btn_stopme.setOnClickListener(this);




    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("val1","in onResume");

        Log.d("val1","in sessionManager.getClicked()"+sessionManager.getClicked());
        Log.d("val1","inddd "+tv_timer.getText().toString());

        if (sessionManager.getClicked()) {
            btn_stopme.setVisibility(View.VISIBLE);
            tv_timer.setVisibility(View.VISIBLE);
            Log.d("val1","indddsdsd "+tv_timer.getText().toString());

            tvv_enter_quote.setVisibility(View.INVISIBLE);
            btn_trackme.setVisibility(View.INVISIBLE);


        }
        else {
            btn_trackme.setVisibility(View.VISIBLE);
            tvv_enter_quote.setVisibility(View.VISIBLE);
            tv_timer.setVisibility(View.INVISIBLE);
            tv_total_time.setVisibility(View.VISIBLE);
            btn_stopme.setVisibility(View.INVISIBLE);
        }
    }


    void  checkAlarm()
    {
        Intent intent = new Intent(getApplicationContext(), MyIntentLocationService.class);

        boolean alarmUp = (PendingIntent.getService(mContext, 0,
                intent,
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp)
        {
            Log.d("val", "Alarm is already active");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_act_add, menu);
        add_new= menu.findItem(R.id.add_new);
        add_new.setEnabled(true);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_new:
                Toast.makeText(mContext,"Clciked add",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void setUpUi() {
        tvv_enter_quote= (TextInputLayout) findViewById(R.id.tvv_enter_quote);
        btn_trackme= (Button) findViewById(R.id.btn_trackme);
        tv_total_time= (TextView) findViewById(R.id.tv_total_time);
        tv_timer= (TextView) findViewById(R.id.tv_timer);

        btn_check= (Button) findViewById(R.id.btn_check);
        btn_check.setVisibility(View.INVISIBLE);
        tv_total_time.setVisibility(View.INVISIBLE);

        btn_stopme= (Button) findViewById(R.id.btn_stopme);
        Log.d("val1","in "+tv_timer.getVisibility());

        Log.d("val1","in "+tv_timer.getText().toString());

        Log.d("val1","in sessionManager.getClicked()"+sessionManager.getClicked());

        if (sessionManager.getClicked()) {
            btn_stopme.setVisibility(View.VISIBLE);
            tv_timer.setVisibility(View.VISIBLE);

            tvv_enter_quote.setVisibility(View.INVISIBLE);
            btn_trackme.setVisibility(View.INVISIBLE);
            startTimer();
//            handler.postDelayed(updateTimer,0);

        }
        else {
            btn_trackme.setVisibility(View.VISIBLE);
            tvv_enter_quote.setVisibility(View.VISIBLE);
            tv_timer.setVisibility(View.INVISIBLE);
            tv_total_time.setVisibility(View.INVISIBLE);
            btn_stopme.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_stopme:
                try {
                    sessionManager.setClicked(false);


                    btn_stopme.setVisibility(View.INVISIBLE);
                    btn_trackme.setVisibility(View.VISIBLE);
                    tvv_enter_quote.getEditText().setText("");
                    tvv_enter_quote.setVisibility(View.VISIBLE);


                    //timer will stop
                    tv_timer.setTextColor(Color.BLUE);
                    tv_total_time.setTextColor(Color.BLUE);
                    stoptimertask(tv_timer);

//                        handler.removeCallbacks(updateTimer);



                    //timer will stop

                    long millis=Calendar.getInstance().getTime().getTime()-sessionManager.getTimerStartTime();
                    Log.d("val","in time ed::"+millis+"\n fdffd:"+String.format("%s h, %s m",
                            String.valueOf(TimeUnit.MILLISECONDS.toHours(millis)),
                            String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis))));
                    tv_timer.setVisibility(View.INVISIBLE);
                    tv_total_time.setVisibility(View.VISIBLE);
//                    tv_total_time.setText(String.format("%s h, %s m",
//                            String.valueOf(TimeUnit.MILLISECONDS.toHours(millis)),
//                            String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis))
////                            TimeUnit.MILLISECONDS.toSeconds(millis) -
////                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//                    ));


                    long updated = timeSwapBuff + millis;
                    int updatedsecs = (int) (updated / 1000);
                    int updatedmins = secs / 60;
                    updatedsecs = secs % 60;
                    int seconds = (int) (updated / 1000) % 60 ;
                    int minutes = (int) ((updated / (1000*60)) % 60);
                    int updatedhours   = (int) ((updated / (1000*60*60)) % 24);
                    tv_total_time.setText(updatedhours+"h : "+minutes +"m");
                    Intent intent = new Intent(getBaseContext(), MyIntentLocationService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();

                    sessionManager.clearTimer();


//                    alarm.cancel(pintent);
//                    pintent.cancel();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exceptionMith","in excetion stop me");
                }

                break;

            case R.id.btn_trackme:

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                tv_total_time.setText("");
                tv_total_time.setVisibility(View.INVISIBLE);

                try {
                    if (tvv_enter_quote.getEditText().getText().toString().trim().equals("") )
                    {
                        tvv_enter_quote.getEditText().setError("Enter Code");
//                        Toast.makeText(mContext, "Enter code",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        if (!tvv_enter_quote.getEditText().getText().toString().trim().equals("12345") )
                        {
                            tvv_enter_quote.getEditText().setError("Code mismatch");

//                            Toast.makeText(mContext, "Code mismatch",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            tvv_enter_quote.setVisibility(View.INVISIBLE);


                            //timer will start
//                                starttime = SystemClock.uptimeMillis();
                            starttime = Calendar.getInstance().getTime().getTime();

                            sessionManager.setTimerStartTime(starttime);
                            sessionManager.setClicked(true);
                            startTimer();




//                                handler.postDelayed(updateTimer, 0);
                            //timer will start

                            tv_timer.setVisibility(View.VISIBLE);

//                            long starttime = Calendar.getInstance().getTime().getTime();

                            Log.d("val", "in time star::" + starttime);


//                GPSTracker gpsTracker=new GPSTracker(mContext);
//                if(gpsTracker.canGetLocation())
//                {
//                    Log.d("val","in data::"+gpsTracker.getLatitude()+gpsTracker.getLatitude());
//                    sessionManager.setLocation(gpsTracker.getLatitude(),gpsTracker.getLatitude());
////            iDataFetch.data(gpsTracker.getLatitude(),gpsTracker.getLatitude());
//                    VolleyUtils volleyUtils=new VolleyUtils();
//                    volleyUtils.sendLocationDetails(gpsTracker.getLatitude(), gpsTracker.getLatitude(), new NetworkResponse() {
//                        @Override
//                        public void receiveResult(Object result) {
//                            Log.d("val","in data:: response success");
//                            try {
//                                JSONObject jsonObject=new JSONObject(result.toString());
//
//                                switch (jsonObject.getString("error"))
//                                {
//                                    case "0":
//                                        Toast.makeText(mContext,"Success Sended",Toast.LENGTH_SHORT).show();
//
//                                        break;
//                                    case "1002":
//                                        Toast.makeText(mContext,"Success Failed",Toast.LENGTH_SHORT).show();
//
//                                        break;
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    },new ErrorVolleyUtils(getApplicationContext()));
//                }
                            btn_stopme.setVisibility(View.VISIBLE);
                            btn_trackme.setVisibility(View.INVISIBLE);
                            Calendar cur_cal = Calendar.getInstance();
                            cur_cal.setTimeInMillis(System.currentTimeMillis());
                            cur_cal.add(Calendar.SECOND, 5);
                            Log.d("val", "Calender Set time:" + cur_cal.getTime());
                            Intent intent = new Intent(getApplicationContext(), MyIntentLocationService.class);
                            pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
                            alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(), 300000, pintent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exceptionMith","in excetion trck me");

                }

//                    Intent intent=new Intent(mContext,MyIntentLocationService.class);
//                    startService(intent);

                break;

            case R.id.btn_check:
//                VolleyUtils volleyUtils=new VolleyUtils();
//                volleyUtils.check(new NetworkResponse() {
//                    @Override
//                    public void receiveResult(Object result) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(result.toString());
//                            Toast.makeText(mContext,jsonObject.toString(),Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },new ErrorVolleyUtils(mContext));

//                startActivity(new Intent(NewLocation.this,LoginForm.class));

                checkAlarm();
                break;
        }
    }


    @Override
    protected void onDestroy() {




        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

//        try {
//            sessionManager.setTimerStartTime(updatedtime);
//        } catch (Exception e) {
//            Log.d("exceptionMith","in excetion stop;");
//
//            e.printStackTrace();
//        }

    }

    @Override
    public void data(double lat, double longi) {
        Log.d("val","in data::"+lat+longi);

    }
//    class MyTimerTask extends TimerTask {
//
//        @Override
//        public void run() {
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat simpleDateFormat =
//                    new SimpleDateFormat("HH:mm");
//            final String strDate = simpleDateFormat.format(calendar.getTime());
//
//            runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//                    tv_timer.setText(strDate);
//                }});
//        }
//
//    }

//    public Runnable updateTimer = new Runnable() {
//        public void run() {
//            Calendar c = Calendar.getInstance();
//
//
//            if (sessionManager.getClicked())
//            {
//                starttime=sessionManager.getTimerStartTime();
//            }
//
//            timeInMilliseconds = Calendar.getInstance().getTime().getTime() - starttime;
//            updatedtime = timeSwapBuff + timeInMilliseconds;
//            secs = (int) (updatedtime / 1000);
//            mins = secs / 60;
//            secs = secs % 60;
//            int seconds = (int) (updatedtime / 1000) % 60 ;
//            int minutes = (int) ((updatedtime / (1000*60)) % 60);
//            hours   = (int) ((updatedtime / (1000*60*60)) % 24);
//            milliseconds = (int) (updatedtime % 1000);
////            Log.d("timer","updatedtime"+updatedtime+"  min"+mins+"  secs"+secs+"  min"+mins+"  milliseconds"+milliseconds+"\n");
//            Log.d("timer2","hr "+hours+" min "+minutes+" sec "+seconds +"\ntimeInMilliseconds::"+timeInMilliseconds);
//
//
//            tv_timer.setText("" + hours +"h"+ " : "+"" + minutes +"m"+ " : " + String.format("%02d", seconds)+"s"/* + ":"
//                    + String.format("%03d", milliseconds)*/);
//            handler.postDelayed(this, 0);
//        }};








    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp



                        if (sessionManager.getClicked())
                        {
                            starttime=sessionManager.getTimerStartTime();
                        }

                        timeInMilliseconds = Calendar.getInstance().getTime().getTime() - starttime;
                        updatedtime = timeSwapBuff + timeInMilliseconds;
                        secs = (int) (updatedtime / 1000);
                        mins = secs / 60;
                        secs = secs % 60;
                        int seconds = (int) (updatedtime / 1000) % 60 ;
                        int minutes = (int) ((updatedtime / (1000*60)) % 60);
                        hours   = (int) ((updatedtime / (1000*60*60)) % 24);
                        milliseconds = (int) (updatedtime % 1000);
//            Log.d("timer","updatedtime"+updatedtime+"  min"+mins+"  secs"+secs+"  min"+mins+"  milliseconds"+milliseconds+"\n");
                        Log.d("timer2","hr "+hours+" min "+minutes+" sec "+seconds +"\ntimeInMilliseconds::"+timeInMilliseconds);

                        tv_timer.setTextColor(Color.RED);
                        tv_timer.setText("" + hours +"h"+ " : "+"" + minutes +"m"+ " : " + String.format("%02d", seconds)+"s"/* + ":"
                    + String.format("%03d", milliseconds)*/);
                    }
                });
            }
        };
    }



    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 1000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



}
