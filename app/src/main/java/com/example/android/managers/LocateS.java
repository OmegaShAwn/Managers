package com.example.android.managers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocateS extends Activity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap googleMap;
    String username;
    int firstTime=0;
    LocationDetails loc;
    ChildEventListener listen = null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Staff");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        Bundle extras = getIntent().getExtras();
        username= extras.getString("user");

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        listen = myRef.child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                loc = dataSnapshot.getValue(LocationDetails.class);
                if(loc!=null)
                    setMap();
                else
                    Toast.makeText(getApplicationContext(),"Location not received yet",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                loc = dataSnapshot.getValue(LocationDetails.class);
                if(loc!=null)
                    setMap();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), username+" logged out", Toast.LENGTH_SHORT).show();
                myRef.removeEventListener(listen);
//                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setMap(){
        if(googleMap!=null){
            googleMap.clear();
            LatLng coordinate = new LatLng(loc.getLatitude(),loc.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(coordinate));

            if(firstTime == 0) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 17.0f);
                googleMap.moveCamera(cameraUpdate);
                firstTime++;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop(){
        finish();
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }
    @Override
    public void onBackPressed() {
        myRef.removeEventListener(listen);
        finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }
}
