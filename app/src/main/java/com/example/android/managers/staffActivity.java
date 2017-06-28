package com.example.android.managers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class staffActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,usramb;
    String username;



ArrayList<String> staffArray = new ArrayList<String>();


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


        final ArrayAdapter adapter = new ArrayAdapter<String>(staffActivity.this, R.layout.listview, R.id.label, staffArray);

        ListView listView = (ListView) findViewById(R.id.staff);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                staffArray.clear();

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
                  staffArray.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        File Dir = new File(Environment.getExternalStorageDirectory().getPath()+"/RajagiriLog");
        if (!Dir.exists()){
            Dir.mkdir();
            Toast.makeText(this,"RajagiriLog folder has been created.",Toast.LENGTH_SHORT).show();
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/RajagiriLog/Staff.txt");
        if(!file.exists()){
            try {
                FileOutputStream stream = new FileOutputStream(file, true);
                String string = String.format("%-15s%-15s%-20s%-15s%-15s%-30s%-30s%-30s%-30s", "S.DATE","S.TIME","NAME","E.DATE","E.TIME","S.LATITUDE","S.LONGITUDE","E.LATITUDE","E.LONGITUDE");
                stream.write(string.getBytes());
            } catch (IOException e) {}

        }
        usramb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                final User user = snapshot.getValue(User.class);
                final DatabaseReference log = database.getReference("Log/Staff/" + user.getname());
                log.addChildEventListener(new ChildEventListener() {
                    int no = 0;
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        no++;
                        timeEnded te = dataSnapshot.getValue(timeEnded.class);
                        if (te.db == 1) {
                            try{
                                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/RajagiriLog/Staff.txt");
                                FileOutputStream stream = new FileOutputStream(file,true);
                                try {
                                    String times = String.format("%d:%d",te.hours,te.minutes);
                                    String timee = String.format("%d:%d",te.hour,te.minute);
                                    String dates = String.format("%d|%d|%d",te.dates,te.months,te.years);
                                    String datee = String.format("%d|%d|%d",te.date,te.month,te.year);
                                    String string = String.format("\n%-15s%-15s%-20s%-15s%-15s%-30f%-30f%-30f%-30f", dates,times,user.getname(),datee,timee,te.lat,te.lon,te.late,te.lone);
                                    stream.write(string.getBytes());
                                } finally {
                                    stream.close();
                                }
                            } catch (IOException e) {
                            }
                            te.db = 0;
                            log.child(String.valueOf(no)).setValue(te);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
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
