package com.alan.aeroplanechess.model.game;

import com.alan.aeroplanechess.model.room.PlayerInfo;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface Player {  //玩家接口，将操作更新在ViewModel上，ViewModel会在事件发生时调用对应函数
    PlayerInfo getPlayerInfo();
    void turnStart();  //回合开始
    void diceRolled(int dice);  //掷完骰子
    void turnFinished();  //回合结束
    void hosted();  //被托管
    void notifyAction(ChessAction action);  //其他玩家采取操作
    void othersExited(int playerId);  //其他玩家退出游戏
}
