package com.alan.aeroplanechess.service;

import com.alan.aeroplanechess.model.room.WifiInvitationManager;
import com.alan.aeroplanechess.viewModel.game.GameViewModel;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface NetworkService {
    void send(String ip, int type, String message);  //发送数据
//    String receive(String ip,int type);  //接收数据
    void setListener(String ip,int type,NetworkServiceListener networkServiceListener);  //设置消息监听器
    void runInBackground();  //后台保持运行
    void notifyTurn();  //提醒玩家操作
    void startGame();
    RoomViewModel getRoomViewModel();
    GameViewModel getGameViewModel();
    WifiInvitationManager getWifiInvitationManager();

    interface NetworkServiceBinder{
        NetworkService getNetworkService();
    }

    interface NetworkServiceListener {
        void onReceiveMessage(String ip,int type,String message);
    }
}

