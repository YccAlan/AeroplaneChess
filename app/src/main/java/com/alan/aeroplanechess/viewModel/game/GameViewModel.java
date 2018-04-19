package com.alan.aeroplanechess.viewModel.game;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.game.GroupResult;
import com.alan.aeroplanechess.model.room.PlayerInfo;

import java.util.List;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface GameViewModel {
    enum GameState{ROLL_DICE,DICE_ROLLED,VACANT,BACKGROUND,GAME_OVER}
    MutableLiveData<GameState> getGameState();  //游戏状态
    LiveData<Integer> getCurrentPlayer();  //当前玩家
    LiveData<GameSetting> getGameSetting();  //按钮等设置
    LiveData<Integer> getCountDown();  //倒计时，0时隐藏
    LiveData<Integer> getDice();  //骰子点数
    List<ChessmanState> getStates();  //棋盘状态
    List<LiveData<PlayerInfo>> getPlayerInfoList();  //玩家信息（玩家名称、位置、组别等）
    MutableLiveData<List<ChessAnimation>> getAnimations();
    List<GroupResult> getResults();  //游戏成绩

    void takeAction(ChessAction action);  //移动棋子
    void rollDice();  //掷骰子
    void host();  //托管
    void exit();  //退出
}

