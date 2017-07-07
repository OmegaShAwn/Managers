package com.example.android.managers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Managers_main_activity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        String u=settings.getString("lusername","");
        final String username;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("UserCategories/Managers");

        if(!hasLoggedIn)
        {
            Intent intent=new Intent(Managers_main_activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(!isNetworkAvailable())
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        username=settings.getString("lusername","");

        if(!u.equals("")) {
            Toast.makeText(Managers_main_activity.this, username, Toast.LENGTH_SHORT).show();
            /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent s = new Intent(getApplicationContext(), DBservice.class);
                startService(s);
            }*/
        }


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(username)){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                    SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putBoolean("hasLoggedIn",false);
                    editor.putString("lusername","");
                    editor.apply();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button addUsers = (Button)findViewById(R.id.addusers);
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                else {
                    Intent intentAddUsers = new Intent(Managers_main_activity.this, AddUsersActivity.class);
                    startActivity(intentAddUsers);
                }
            }
        });

        Button viewUser = (Button)findViewById(R.id.viewusers);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                else {
                    Intent intentViewUsers = new Intent(Managers_main_activity.this, ViewUsers.class);
                    startActivity(intentViewUsers);
                }
           }
        });
        Button emrgencies =(Button)findViewById(R.id.emergencies);
        emrgencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                else {
                    Intent intentEmer = new Intent(Managers_main_activity.this, EmergencyActivity.class);
                    startActivity(intentEmer);
                }
            }
        });
        Button staff =(Button)findViewById(R.id.staff);
        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable())
                    Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                else {
                    Intent intentstaff = new Intent(Managers_main_activity.this, staffActivity.class);
                    startActivity(intentstaff);
                }
            }
        });


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

    }


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putBoolean("hasLoggedIn", false);
                        editor.putString("lusername","");

                        editor.apply();
                        stopService(new Intent(getApplicationContext(),DBservice.class));
                        startActivity(i);

                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
