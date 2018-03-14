package com.alan.aeroplanechess.model.room;

import org.json.JSONObject;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface RoomInfo {
    PlayerInfo getPlayerInfo(int playerId);  //玩家信息
    void addPlayer(PlayerInfo playerInfo);  //添加玩家
    void removePlayer(int playerId);  //移除玩家
    void setRandomSeed(int randomSeed);  //设置随机种子
    int getRandomSeed();  //获取随机种子
    JSONObject toJson();  //转为Json对象
}
