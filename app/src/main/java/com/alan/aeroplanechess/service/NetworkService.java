package com.alan.aeroplanechess.service;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface NetworkService {
    void send(String ip,int type,String message);  //发送数据
    String receive(String ip,int type);  //接收数据
    void registerListener(String ip,int type,NetworkServiceListener networkServiceListener);  //添加消息监听器
    void clearListener(String ip,int type);  //移除监听器
    void runInBackground();  //后台保持运行
    void notifyTurn();  //提醒玩家操作

    interface NetworkServiceBinder{
        NetworkService getNetworkService();
    }
}

interface NetworkServiceListener {
    void onReceiveMessage(String message);
}