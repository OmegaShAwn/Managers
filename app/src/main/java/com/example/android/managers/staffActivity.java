package com.example.android.managers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class staffActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,usramb;
    String username;



ArrayList<String> staffArray = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_option:
                Intent i=new Intent(getApplicationContext(),logNames.class);
                i.putExtra("type","Staff");
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);


        myRef = database.getReference("Staff");
        usramb = database.getReference("UserCategories/Otheruser");


        final ArrayAdapter adapter = new ArrayAdapter<>(staffActivity.this, R.layout.listview, R.id.label, staffArray);

        ListView listView = (ListView) findViewById(R.id.staff);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                staffArray.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                   username = postSnapshot.getKey();
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
                  staffArray.clear();
                    adapter.notifyDataSetChanged();
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
            }
        });
    }
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
