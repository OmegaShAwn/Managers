package com.example.android.managers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";


    EditText username ;
    EditText password ;

    boolean usernameFound;
    boolean passwordmatched;

    String currentUserName;
    String currentUserPassword;

    ArrayList<User> users = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("UserCategories/Managers");


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit"+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username =(EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);


        Button signin =(Button)findViewById(R.id.signin);




        Log.v("main",""+1);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.v("main",""+2);
                    User user = postSnapshot.getValue(User.class);
                    Log.v("main",""+3);
                    users.add(user);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameFound=false;
                passwordmatched=false;
                currentUserName = username.getText().toString();
                currentUserPassword = password.getText().toString();
                int i;
                int f;
                f=users.size();
                for( i=0;i<users.size();i++){
                    if ((users.get(i).getUsername()).equals(currentUserName)){
                        usernameFound=true;
                        //Toast.makeText(createAccountActivity.this,"Username already exists",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(usernameFound)
                    if((users.get(i).getPassword().equals(currentUserPassword))){
                        passwordmatched=true;
                    }

                if(usernameFound){
                    if(passwordmatched){
//User has successfully logged in, save this information
// We need an Editor object to make preference changes.
                        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
                        SharedPreferences.Editor editor = settings.edit();

//Set "hasLoggedIn" to true
                        editor.putBoolean("hasLoggedIn", true);
                        editor.putString("lusername",currentUserName);

// Commit the edits!
                        editor.commit();
                        Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,Managers_main_activity.class);
                        intent.putExtra("username",currentUserName);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(MainActivity.this,"Incorrect Password",Toast.LENGTH_SHORT).show();

                }
                else if(f<=0)
                    Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"Invalid Username",Toast.LENGTH_SHORT).show();

            }
        });
    }
}

