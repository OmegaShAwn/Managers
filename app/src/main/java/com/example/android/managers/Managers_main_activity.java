package com.example.android.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Managers_main_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        String u=settings.getString("lusername","");
        final String username;

        if(hasLoggedIn)
        {
        }
        else{
            Intent intent=new Intent(Managers_main_activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(!isNetworkAvailable())
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();

        username=settings.getString("lusername","nil");

        if(!u.equals("")) {
            Toast.makeText(Managers_main_activity.this, username, Toast.LENGTH_LONG).show();
        };





        Button addUsers = (Button)findViewById(R.id.addusers);
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddUsers = new Intent(Managers_main_activity.this,AddUsersActivity.class);
                startActivity(intentAddUsers);
            }
        });

        Button viewUser = (Button)findViewById(R.id.viewusers);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentViewUsers = new Intent(Managers_main_activity.this,ViewUsers.class);
                startActivity(intentViewUsers);

           }
        });
        Button emrgencies =(Button)findViewById(R.id.emergencies);
        emrgencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEmer = new Intent(Managers_main_activity.this,EmergencyActivity.class);
                startActivity(intentEmer);
            }
        });
        Button staff =(Button)findViewById(R.id.staff);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentstaff = new Intent(Managers_main_activity.this,staffActivity.class);
                startActivity(intentstaff);
            }
        });
    }

    int k=0;

    @Override
    public void onBackPressed()
    {
        if(k==0) {
            Toast.makeText(getApplicationContext(), "Press Back again to log out", Toast.LENGTH_LONG).show();
            k++;
        }
        else {
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
            SharedPreferences.Editor editor = settings.edit();

//Set "hasLoggedIn" to true
            editor.putBoolean("hasLoggedIn", false);
            editor.putString("lusername","");

// Commit the edits!
            editor.commit();
            startActivity(i);

            super.onBackPressed();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                k=0;
            }
        }, 2000);// code here to show dialog // optional depending on your needs
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
