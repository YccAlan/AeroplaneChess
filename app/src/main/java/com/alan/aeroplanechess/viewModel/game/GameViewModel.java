package com.alan.aeroplanechess.viewModel.game;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.game.GroupResult;
import com.alan.aeroplanechess.model.room.RoomInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface GameViewModel extends GameViewModelOperation{
    enum GameState{ROLL_DICE,DICE_ROLLED,VACANT,BACKGROUND,GAME_OVER}
    MutableLiveData<GameState> getGameState();  //游戏状态
    LiveData<Integer> getCurrentPlayer();  //当前玩家
    LiveData<GameSetting> getGameSetting();  //按钮等设置
    LiveData<Integer> getCountDown();  //倒计时，0时隐藏
    LiveData<Integer> getDice();  //骰子点数
    Map<ChessmanId,ChessmanState> getStates();  //棋盘状态
    RoomInfo getRoomInfo();  //房间信息（玩家名称、位置、组别等）
    MutableLiveData<List<ChessAnimation>> getAnimation();
    List<GroupResult> getResults();  //游戏成绩

    void host();  //托管
    void exit();  //退出
}

interface GameViewModelOperation{
    MutableLiveData<GameSetting> getMutableGameSetting();
    void takeAction(ChessAction action);  //移动棋子
    void rollDice();  //掷骰子
}
