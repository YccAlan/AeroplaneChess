package com.alan.aeroplanechess.model.room;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface PlayerInfo {
    enum Type{LOCAL_PLAYER,AI_PLAYER,REMOTE_PLAYER,PENDING_PLAYER}
    enum State{ONLINE,AI,OFFLINE}
    Type getType();  //玩家类型
    int getPlayerId();  //玩家id，决定座位
    int getGroupId();  //小组id
    String getName();  //玩家名
    Bitmap getAvatar();
    String getIp();  //Ip地址
    int getUniqueId();

    State getState();  //状态
    void setGroupId(int groupId);
    void setState(State state);
    void setType(Type type);

    JSONObject toJson();
}
