package com.example.android.managers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                    mMessageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, users,userinfo);

        mMessageListView.setAdapter(mMessageAdapter);

        mMessageListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected = ((TextView) view.findViewById(R.id.name)).getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), myRef.child(selected).getKey(), Toast.LENGTH_SHORT);
                toast.show();

//                myRef.child(selected).removeValue();

            }
        });

    }



}
