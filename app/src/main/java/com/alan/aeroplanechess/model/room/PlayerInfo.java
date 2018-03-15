package com.alan.aeroplanechess.model.room;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface PlayerInfo {
    enum Type{LOCAL_PLAYER,AI_PLAYER,REMOTE_PLAYER}
    enum State{ONLINE,AI,OFFLINE}
    Type getType();  //玩家类型
    int getPlayerId();  //玩家id，决定座位
    int getGroupId();  //小组id
    String getName();  //玩家名
    String getIp();  //Ip地址

    State getState();  //状态
    void setGroupId(int groupId);
    void setState(State state);
}
