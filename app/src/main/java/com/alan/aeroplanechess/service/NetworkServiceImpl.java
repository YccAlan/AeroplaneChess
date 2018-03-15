package com.alan.aeroplanechess.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NetworkServiceImpl extends Service implements NetworkService{
    public NetworkServiceImpl() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class NetworkServiceBinder extends Binder implements NetworkService.NetworkServiceBinder{
        public NetworkService getNetworkService(){
            return NetworkServiceImpl.this;
        }
    }
}
