package com.example.android.managers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewSelectedUsers extends AppCompatActivity {

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    List<User> users = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;


    int flagdup=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_users);



        Bundle bundle = getIntent().getExtras();
        final String userinfo=bundle.getString("user");



        if(userinfo.equals("ambulance")){

            myRef = database.getReference("UserCategories/AmbulanceDrivers");

        }
        else if(userinfo.equals("doctor")){
            myRef = database.getReference("UserCategories/Doctors");
        }
        else{
            myRef = database.getReference("UserCategories/Otheruser");
        }

        mMessageListView = (ListView) findViewById(R.id.messageListView);

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);

                    flagdup = 0;


                            for (int i = 0; i < users.size(); i++)
                            if (users.get(i).getUsername().equals(user.getUsername())) {
                                flagdup = 1;
                                break;
                            }



                            if (flagdup == 0) {
                                users.add(user);
                            }


                    mMessageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            int i;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                i++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                i--;
                if(i==0)
                {
                    Intent s=new Intent(ViewSelectedUsers.this,ViewUsers.class);
                    startActivity(s);
                    finish();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, users,userinfo);

        mMessageListView.setAdapter(mMessageAdapter);

        mMessageListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String selected = ((TextView) view.findViewById(R.id.name)).getText().toString();

//                Toast toast = Toast.makeText(getApplicationContext(), myRef.child(selected).getKey(), Toast.LENGTH_SHORT);
//                toast.show();


                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSelectedUsers.this);
                builder.setMessage("Are you sure you want to remove "+myRef.child(selected).getKey()+"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myRef.child(selected).removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.show();
                // Create the AlertDialog object and return it




            }
        });




    }



}
