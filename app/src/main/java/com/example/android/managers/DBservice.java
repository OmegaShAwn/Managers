package com.example.android.managers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ShAwn on 06-04-2017.
 */



public class DBservice extends Service {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eref = database.getReference("UserCategories/AmbulanceDrivers");
    DatabaseReference sref = database.getReference("UserCategories/Otheruser");
    private SQLiteDatabase db,dbs;

    public DBservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void onCreate() {

        File Dir = new File(Environment.getExternalStorageDirectory().getPath()+"/Rajagiri Log");
        if (!Dir.exists()){
            Dir.mkdir();
            Toast.makeText(this,"Rajagiri Log folder has been created.",Toast.LENGTH_SHORT).show();
        }

        db=openOrCreateDatabase(Environment.getExternalStorageDirectory().getPath()+"/Rajagiri Log/Emergencies.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS EMERGENCIES(START_DATE DATE, END_DATE DATE, NAME VARCHAR, TYPE VARCHAR, SEVERITY VARCHAR, NUMBER_OF_PATIENTS INTEGER, START_LATITUDE FLOAT, START_LONGITUDE FLOAT, END_LATITUDE FLOAT, END_LONGITUDE FLOAT, FINAL_DESTINATION VARCHAR);");

        eref.addChildEventListener(new ChildEventListener() {
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
                            int year = te.years;
                            int month = te.months;
                            int day = te.dates;
                            int hour = te.hours;
                            int min = te.minutes;
                            Date dates = new GregorianCalendar(year, month, day, hour, min).getTime();
                            year = te.year;
                            month = te.month;
                            day = te.date;
                            hour = te.hour;
                            min = te.minute;
                            Date datee = new GregorianCalendar(year, month, day, hour, min).getTime();
                            String Severity = "Not Specified";
                            String Type = "Not Specified";
                            String Number = "Not Specified";
                            String Dest = "Rajagiri Hospital";
                            if(te.si == 1)
                                Severity ="Low";
                            else if(te.si == 2)
                                Severity ="Medium";
                            else if(te.si == 3)
                                Severity ="High";
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
                            String query = "INSERT INTO EMERGENCIES (START_DATE, END_DATE, NAME, TYPE, SEVERITY, NUMBER_OF_PATIENTS, START_LATITUDE, START_LONGITUDE, END_LATITUDE, END_LONGITUDE, FINAL_DESTINATION) VALUES('"+dates+"', '"+datee+"', '"+user.getname()+"', '"+Type+"', '"+Severity+"', '"+Number+"', '"+te.lat+"', '"+te.lon+"', '"+te.late+"', '"+te.lone+"', '"+Dest+"');";
                            db.execSQL(query);
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


        dbs=openOrCreateDatabase(Environment.getExternalStorageDirectory().getPath()+"/Rajagiri Log/Staff.db", Context.MODE_PRIVATE, null);
        dbs.execSQL("CREATE TABLE IF NOT EXISTS STAFF(START_DATE DATE, END_DATE DATE, NAME VARCHAR, START_LATITUDE FLOAT, START_LONGITUDE FLOAT, END_LATITUDE FLOAT, END_LONGITUDE FLOAT);");

        sref.addChildEventListener(new ChildEventListener() {
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
                            int year = te.years;
                            int month = te.months;
                            int day = te.dates;
                            int hour = te.hours;
                            int min = te.minutes;
                            Date dates = new GregorianCalendar(year, month, day, hour, min).getTime();
                            year = te.year;
                            month = te.month;
                            day = te.date;
                            hour = te.hour;
                            min = te.minute;
                            Date datee = new GregorianCalendar(year, month, day, hour, min).getTime();
                            String query = "INSERT INTO STAFF (START_DATE, END_DATE, NAME, START_LATITUDE, START_LONGITUDE, END_LATITUDE, END_LONGITUDE) VALUES('"+dates+"', '"+datee+"', '"+user.getname()+"', '"+te.lat+"', '"+te.lon+"', '"+te.late+"', '"+te.lone+"');";
                            dbs.execSQL(query);
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

        super.onCreate();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}