package com.example.infiny.mylocationtracker.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.infiny.mylocationtracker.R;

/**
 * Created by infiny on 4/1/17.
 */

public class Signup extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        setUi();


    }

    private void setUi() {

    }
}
