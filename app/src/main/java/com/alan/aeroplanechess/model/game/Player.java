package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface Player {  //玩家接口，将操作更新在ViewModel上，ViewModel会在事件发生时调用对应函数
    void turnStart();  //回合开始
    void diceRolled(int dice);  //掷完骰子（远程玩家和AI玩家不掷骰子，此方法不会被调用）
    void turnFinished();  //回合结束
    void othersFinished();  //其他玩家的回合结束
    void othersExited();  //其他玩家退出游戏
}
