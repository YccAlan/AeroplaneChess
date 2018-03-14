package com.alan.aeroplanechess.viewModel.game;

import android.arch.lifecycle.LiveData;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.room.RoomInfo;

import java.util.Map;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface GameViewModel {
    enum GameState{ROLL_DICE,MOVE_CHESS,WAIT,GAME_OVER}
    LiveData<GameState> getGameState();  //游戏状态
    LiveData<GameSetting> getGameSetting();  //按钮等设置
    LiveData<Integer> getCountDown();
    LiveData<Integer> getDice();
    RoomInfo getRoomInfo();  //房间信息（玩家名称、位置、组别等）
    Map<ChessmanId,ChessmanState> getStates();  //棋的初始位置

    void takeAction(ChessAction action);  //移动棋子
    void rollDice();  //掷骰子
    void host();  //托管
    void exit();  //退出
}
