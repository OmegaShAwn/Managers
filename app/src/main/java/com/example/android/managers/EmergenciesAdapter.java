package com.example.android.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by RoshanJoy on 20-03-2017.
 */

public class EmergenciesAdapter  extends ArrayAdapter<Emergencies>{

    public EmergenciesAdapter(Context context, int resource, List<Emergencies> objects) {

        super(context, resource, objects );
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_emergency_message, parent, false);
        }

        TextView mName = (TextView)convertView.findViewById(R.id.EmergenciesName);
        TextView mUsername = (TextView)convertView.findViewById(R.id.EmergenciesUsername);
        TextView mseverity = (TextView)convertView.findViewById(R.id.EmergenciesSeverity);
        TextView mtype = (TextView)convertView.findViewById(R.id.EmergenciesType);


        final Emergencies emergencies = getItem(position);


        mName.setText(""+emergencies.emergencyDetails.getUsername());
        Log.v("emer","");
        mUsername.setText(""+emergencies.emergencyDetails.getUsername());
        Log.v("emer","");
        mseverity.setText(""+emergencies.emergencyDetails.getSi());
        Log.v("emer","");
        mtype.setText(""+emergencies.emergencyDetails.getTi());
        Log.v("emer","");





        return convertView;
    }
}
