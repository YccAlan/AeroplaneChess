package com.alan.aeroplanechess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.activity.room.RoomActivity;
import com.alan.aeroplanechess.service.impl.NetworkServiceImpl;
import com.alan.aeroplanechess.util.Avatar;
import com.alan.aeroplanechess.util.Utils;

public class MainActivity extends AppCompatActivity {
    Button startRoomButton;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Avatar.init(this);
        Utils.init(this);

        startRoomButton=findViewById(R.id.startRoom);
        startRoomButton.setOnClickListener((view)->{ startActivity(new Intent(this,RoomActivity.class)); });

        startService(new Intent(this, NetworkServiceImpl.class));
    }

}
