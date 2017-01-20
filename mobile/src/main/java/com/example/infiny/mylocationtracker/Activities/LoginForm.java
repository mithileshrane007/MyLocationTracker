package com.example.infiny.mylocationtracker.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.example.infiny.mylocationtracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by infiny on 4/1/17.
 */

public class LoginForm extends AppCompatActivity implements View.OnClickListener {

    CircleImageView iv_employee,iv_manager;
    TextInputLayout input_email,input_password;
    AppCompatButton btn_login;
    TextView link_signup;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        setUi();


        iv_manager.setOnClickListener(this);
        iv_employee.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        link_signup.setOnClickListener(this);


    }

    private void setUi() {
        iv_employee= (CircleImageView) findViewById(R.id.iv_employee);
        iv_manager= (CircleImageView) findViewById(R.id.iv_manager);
        input_email= (TextInputLayout) findViewById(R.id.input_email);
        input_password= (TextInputLayout) findViewById(R.id.input_password);
        btn_login= (AppCompatButton) findViewById(R.id.btn_login);
        link_signup= (TextView) findViewById(R.id.link_signup);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_employee:
                iv_employee.setFillColor(ContextCompat.getColor(mContext,R.color.holo_orange_faint));
                iv_manager.setFillColor(ContextCompat.getColor(mContext,android.R.color.transparent));

                break;

            case R.id.iv_manager:
                iv_employee.setFillColor(ContextCompat.getColor(mContext,android.R.color.transparent));
                iv_manager.setFillColor(ContextCompat.getColor(mContext,R.color.holo_orange_faint));

                break;

            case R.id.link_signup:
                break;

            case R.id.btn_login:
                break;
        }
    }
}
