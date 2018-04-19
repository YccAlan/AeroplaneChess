package com.alan.aeroplanechess.activity.room;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.alan.aeroplanechess.R;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.service.impl.NetworkServiceImpl;

public class RoomActivity extends AppCompatActivity {
    RoomFragment roomFragment;
    NetworkService networkService;
    NetworkServiceConnection networkServiceConnection=new NetworkServiceConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        roomFragment=(RoomFragment) getSupportFragmentManager().findFragmentById(R.id.roomFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,NetworkServiceImpl.class),networkServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(networkServiceConnection);
    }

    class NetworkServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            networkService=((NetworkService.NetworkServiceBinder)iBinder).getNetworkService();
            roomFragment.bindViewModel(networkService.getRoomViewModel());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            networkService=null;
        }
    }
}
