package com.alan.aeroplanechess.viewModel.game;

import android.arch.lifecycle.MutableLiveData;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.room.PlayerInfo;

import java.util.List;

public interface GameOperation {
    MutableLiveData<GameSetting> getMutableGameSetting();
    int getCurrentStep();
    void updatePlayerInfo(PlayerInfo playerInfo);
    List<ChessmanId> getMovableChessman(int playerId, int step);  //可移动棋子
    void takeAction(ChessAction action);  //移动棋子
    void rollDice();  //掷骰子
    void startCountDown(int time);  //开始倒计时
}
