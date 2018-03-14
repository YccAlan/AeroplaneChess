package com.alan.aeroplanechess.model;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface OnlineService {
    void send(String ip,int type,String message);  //发送数据
    String receive(String ip,int type);  //接收数据
    void registerListener(String ip,int type,OnlineServiceListener onlineServiceListener);  //添加消息监听器
    void clearListener(String ip,int type);  //移除监听器
}

interface OnlineServiceListener{
    void onReceiveMessage(String message);
}