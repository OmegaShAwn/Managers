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


        assert userinfo != null;
        switch (userinfo) {
            case "ambulance":
                myRef = database.getReference("UserCategories/AmbulanceDrivers");
                break;
            case "doctor":
                myRef = database.getReference("UserCategories/Doctors");
                break;
            default:
                myRef = database.getReference("UserCategories/Otheruser");
                break;
        }

        ListView mMessageListView = (ListView) findViewById(R.id.messageListView);

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

                    users.clear();
                    mMessageAdapter.notifyDataSetChanged();
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
                final String selectedpass = ((TextView) view.findViewById(R.id.password)).getText().toString();
                final String selectedspec = ((TextView) view.findViewById(R.id.special)).getText().toString();
                final Long selectedphno = Long.valueOf(((TextView) view.findViewById(R.id.phno)).getText().toString());

//                Toast toast = Toast.makeText(getApplicationContext(), myRef.child(selected).getKey(), Toast.LENGTH_SHORT);
//                toast.show();


                /*AlertDialog.Builder builder = new AlertDialog.Builder(ViewSelectedUsers.this);
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
                        });*/

                CharSequence options[] = new CharSequence[] {"EDIT", "DELETE", "CANCEL"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSelectedUsers.this);
                builder.setTitle(myRef.child(selected).getKey())
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on colors[which]
                                if(which==0){
                                    Intent i=new Intent(ViewSelectedUsers.this,CreateUserActivity.class);
                                    i.putExtra("edit",true);
                                    i.putExtra("user",userinfo);
                                    i.putExtra("username",myRef.child(selected).getKey());
                                    i.putExtra("userpass",selectedpass);
                                    i.putExtra("userphno",selectedphno);
                                    i.putExtra("userspec",selectedspec);
                                    startActivity(i);
                                }
                                if(which==1){
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
                                }
                            }
                        });

                builder.show();
                // Create the AlertDialog object and return it




            }
        });




    }



}
