package com.example.android.managers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class logList extends AppCompatActivity {

    int i;
    String username;
    ArrayList<String> emerlist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list);

        final Bundle extras= getIntent().getExtras();
        username = extras.getString("username");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Log/Ambulance/"+username);
        if(extras.get("type").equals("Staff")){
            ref = database.getReference("Log/Staff/"+username);
        }

        final ArrayAdapter adapter = new ArrayAdapter<String>(logList.this, R.layout.listview, R.id.label, emerlist);
        final ListView listView = (ListView) findViewById(R.id.logs);
        listView.setAdapter(adapter);

        ref.addChildEventListener(new ChildEventListener() {
            int no;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                no++;
                timeEnded tE = dataSnapshot.getValue(timeEnded.class);
                emerlist.add(Integer.toString(tE.dates)+" | "+ Integer.toString(tE.months)+" | "+ Integer.toString(tE.years)+" || "+ Integer.toString(tE.hours)+" : "+ Integer.toString(tE.minutes));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(extras.get("type").equals("Staff")){
                    Intent intent = new Intent(getApplicationContext(), detailstaff.class);
                    intent.putExtra("username",username);
                    intent.putExtra("no", position+1);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), details.class);
                    intent.putExtra("username", username);
                    intent.putExtra("no", position + 1);
                    startActivity(intent);
                }
            }
        });

    }
}
