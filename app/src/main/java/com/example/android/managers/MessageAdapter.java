package com.example.android.managers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<User> {
    String cuser;

    public MessageAdapter(Context context, int resource, List<User> objects,String cuser) {

        super(context, resource, objects );
        this.cuser=cuser;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        TextView mName = (TextView)convertView.findViewById(R.id.name);
//        TextView mUsername = (TextView)convertView.findViewById(R.id.username);
        TextView mPassword = (TextView)convertView.findViewById(R.id.password);
        TextView mPhno = (TextView)convertView.findViewById(R.id.phno);
        LinearLayout mSpecialLayout = (LinearLayout)convertView.findViewById(R.id.specialLinearLayout);
        TextView mspecial = (TextView)convertView.findViewById(R.id.special);


        User user = getItem(position);


        assert user != null;
        mName.setText(user.getname());
//        mUsername.setText(""+user.getUsername());
        mPassword.setText(user.getPassword());
        mPhno.setText(user.getPhno().toString());

        if(cuser.equals("doctor")||cuser.equals("other")){
            mspecial.setText(user.getSpeciality());

        }
        else
        mSpecialLayout.setVisibility(View.GONE);

        return convertView;
    }
}

