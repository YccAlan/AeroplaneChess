package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessmanId {
    int getGroupId();  //小组id
    int getPlayerId();  //玩家id
    int getPlaneId();  //飞机id

    int getId();  //唯一id
}
