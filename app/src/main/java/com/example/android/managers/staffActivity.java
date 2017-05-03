package com.example.android.managers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class staffActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String username;



//    String[] staffArray = {"Android","IPhone","WindowsMobile","Blackberry", "WebOS","Ubuntu","Windows7","Max OS X"};
ArrayList<String> staffArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);


        myRef = database.getReference("Staff");


        final ArrayAdapter adapter = new ArrayAdapter<String>(staffActivity.this, R.layout.listview, R.id.label, staffArray);

        ListView listView = (ListView) findViewById(R.id.staff);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                   username = (String) postSnapshot.getKey();
                    int f = 0;
                    for (int i = 0; i < staffArray.size(); i++) {
                        if (staffArray.get(i).equals(username))
                            f = 1;
                        break;
                    }
                    if (f == 0)
                        staffArray.add(username);
                }
                adapter.notifyDataSetChanged();
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
                if(i==0) {
                    Intent s = new Intent(staffActivity.this, Managers_main_activity.class);
                    Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
                    startActivity(s);
                    finish();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(staffActivity.this, LocateS.class);
                username = staffArray.get(position);
                intent.putExtra("user", username);
                startActivity(intent);
                finish();

//                mEmergenciesAdapter.remove(mEmergenciesAdapter);

            }
        });
    }
}
