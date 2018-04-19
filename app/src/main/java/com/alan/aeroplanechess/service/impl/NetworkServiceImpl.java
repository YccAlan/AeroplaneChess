package com.alan.aeroplanechess.service.impl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.activity.GameActivity;
import com.alan.aeroplanechess.model.room.Impl.PlayerInfoImpl;
import com.alan.aeroplanechess.model.room.Impl.RoomInfoImpl;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.model.room.WifiInvitationManager;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.util.Utils;
import com.alan.aeroplanechess.viewModel.game.GameViewModel;
import com.alan.aeroplanechess.viewModel.game.impl.GameViewModelImpl;
import com.alan.aeroplanechess.viewModel.room.Impl.RoomViewModelImpl;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

public class NetworkServiceImpl extends Service implements NetworkService {
    NetworkCommunicator communicator=new NetworkCommunicator();
    NotificationManager notificationManager;

    WifiInvitationManager wifiInvitationManager=new WifiInvitationManager();
    RoomViewModel roomViewModel;
    GameViewModel gameViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel gamingChannel = new NotificationChannel("GAMING","Gaming Online", NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel turnChannel = new NotificationChannel("TURN","Turn coming", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(gamingChannel);
            notificationManager.createNotificationChannel(turnChannel);
        }
    }

    @Override
    public RoomViewModel getRoomViewModel() {
        if (roomViewModel==null){
            RoomInfo roomInfo=new RoomInfoImpl();
            roomInfo.addPlayer(new PlayerInfoImpl(Utils.getSelfName(),Utils.getSelfIp(), PlayerInfo.Type.LOCAL_PLAYER));
            roomViewModel = new RoomViewModelImpl(roomInfo,true,this);
        }
        return roomViewModel;
    }

    @Override
    public GameViewModel getGameViewModel() {
        if (gameViewModel==null){
            if (roomViewModel!=null)
                gameViewModel=new GameViewModelImpl(roomViewModel.getRoomInfo().getValue(),this);
            roomViewModel=null;
        }
        return gameViewModel;
    }

    @Override
    public WifiInvitationManager getWifiInvitationManager() {
        return wifiInvitationManager;
    }

    @Override
    public void send(String ip, int type, String message) {
        communicator.send(ip,type,message);
    }

    @Override
    public void setListener(String ip, int type, NetworkServiceListener networkServiceListener) {
        communicator.receive(ip,type,networkServiceListener);
    }

    @Override
    public void runInBackground() {
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentTitle("Playing Online Aeroplane Chess")
            .setContentText("Tap to come back to your game")
            .setContentIntent(PendingIntent.getService(this,0,new Intent(this, GameActivity.class),0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId("GAMING");
        startForeground(0,builder.build());
    }

    @Override
    public void notifyTurn() {
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentTitle("Your Turn Comes")
                .setContentText("Tap to finish your turn")
                .setContentIntent(PendingIntent.getService(this,0,new Intent(this, GameActivity.class),0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId("TURN");
            builder.setTimeoutAfter(Constants.TURN_TIME*1000);
        }
        else
            builder.setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void startGame() {
        startActivity(new Intent(this,GameActivity.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        return new NetworkServiceBinder();
    }

    class NetworkServiceBinder extends Binder implements NetworkService.NetworkServiceBinder{
        public NetworkService getNetworkService(){
            return NetworkServiceImpl.this;
        }
    }
}
