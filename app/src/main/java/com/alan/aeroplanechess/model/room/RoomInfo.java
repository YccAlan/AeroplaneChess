package com.alan.aeroplanechess.model.room;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface RoomInfo {
    List<PlayerInfo> getPlayerInfoList();  //玩家信息，按id排序
    void addPlayer(PlayerInfo playerInfo);  //添加玩家
    void removePlayer(PlayerInfo playerInfo);  //移除玩家
    boolean isOnline();  //是否为在线房间
    void setRandomSeed(long randomSeed);  //设置随机种子
    long getRandomSeed();  //获取随机种子
    JSONObject toJson();  //转为Json对象
}
