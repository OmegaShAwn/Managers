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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.managers.R.id.emergencyMessageListView;


public class EmergencyActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,usramb;
    private ListView mMessageListView;
    private EmergenciesAdapter mEmergenciesAdapter;


    ArrayList<Emergencies> emergencies = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_option:
                Intent i=new Intent(getApplicationContext(),logNames.class);
                i.putExtra("type","Ambulance");
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        mMessageListView = (ListView) findViewById(emergencyMessageListView);
        setAdapter();

        myRef = database.getReference("Emergencies");
        usramb = database.getReference("UserCategories/AmbulanceDrivers");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if(dataSnapshot!=null) {
                    Emergencies user = dataSnapshot.getValue(Emergencies.class);
                    emergencies.add(user);
                    mEmergenciesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    Emergencies user = dataSnapshot.getValue(Emergencies.class);
                    for (int i = 0; i < emergencies.size(); i++)
                        if (emergencies.get(i).emergencyDetails.getUsername().equals(user.emergencyDetails.getUsername()))
                            emergencies.remove(i);
                    mEmergenciesAdapter.notifyDataSetChanged();
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
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/RajagiriLog/Emergencies.txt");
        if(!file.exists()){
            try {
                FileOutputStream stream = new FileOutputStream(file, true);
                String string = String.format("%-15s%-15s%-20s%-15s%-15s%-30s%-10s%-10s%-30s%-30s%-30s%-30s%-30s", "S.DATE","S.TIME","NAME","E.DATE","E.TIME","TYPE","SEVERITY","NUMBER","S.LATITUDE","S.LONGITUDE","E.LATITUDE","E.LONGITUDE","END DEST");
                stream.write(string.getBytes());
            } catch (IOException e) {}

        }
        usramb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                final User user = snapshot.getValue(User.class);
                final DatabaseReference log = database.getReference("Log/Ambulance/" + user.getname());
                log.addChildEventListener(new ChildEventListener() {
                    int no = 0;
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        no++;
                        timeEnded te = dataSnapshot.getValue(timeEnded.class);
                        if (te.db == 1) {
                            try{
                                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/RajagiriLog/Emergencies.txt");
                                FileOutputStream stream = new FileOutputStream(file,true);
                                try {
                                    String times = String.format("%d:%d",te.hours,te.minutes);
                                    String timee = String.format("%d:%d",te.hour,te.minute);
                                    String dates = String.format("%d|%d|%d",te.dates,te.months,te.years);
                                    String datee = String.format("%d|%d|%d",te.date,te.month,te.year);
                                    String Severity = "Not Specified";
                                    String Type = "Not Specified";
                                    String Number = "Not Specified";
                                    String Dest = "Admitted at Rajagiri";
                                    if(te.si == 1)
                                        Severity ="Low";
                                    else if(te.si == 2)
                                        Severity ="Low";
                                    else if(te.si == 3)
                                        Severity ="Low";
                                    if(te.ti == 1)
                                        Type = "Neural";
                                    else if(te.ti == 2)
                                        Type = "Pregnancy";
                                    else if(te.ti == 3)
                                        Type = "Vehicle Accident";
                                    else if(te.ti == 4)
                                        Type = "Heart Attack";
                                    else if(te.ti == 5)
                                        Type = "Head Injury";
                                    else if(te.ti == 6)
                                        Type = "Other";
                                    if(te.no != 0)
                                        Number = String.valueOf(te.no);
                                    if(te.dest ==1)
                                        Dest = "Admitted Elsewhere";
                                    String string = String.format("\n%-15s%-15s%-20s%-15s%-15s%-30s%-10s%-10s%-30f%-30f%-30f%-30f%-30s", dates,times,user.getname(),datee,timee,Type,Severity,Number,te.lat,te.lon,te.late,te.lone,Dest);
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


    }

    public void setAdapter(){


        mEmergenciesAdapter = new EmergenciesAdapter(this, R.layout.item_emergency_message, emergencies);

        mMessageListView.setAdapter(mEmergenciesAdapter);

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EmergencyActivity.this,Locate.class);
                TextView username =(TextView)view.findViewById(R.id.EmergenciesUsername);
                intent.putExtra("user",username.getText().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}
