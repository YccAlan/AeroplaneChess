package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.model.game.AI;
import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.Player;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.viewModel.game.GameOperation;
import com.alan.aeroplanechess.viewModel.game.GameSetting;

import java.util.List;

import static com.alan.aeroplanechess.viewModel.game.GameSetting.AIState.OFF;
import static com.alan.aeroplanechess.viewModel.game.GameSetting.AIState.ON;

/**
 * Created by yccalan on 2018/3/11.
 */

public class LocalPlayer implements Player {
    PlayerInfo playerInfo;
    GameOperation gameOperation;
    AI ai;
    GameSetting gameSetting=new GameSetting();
    int currentDice;

    public LocalPlayer(GameOperation gameOperation, PlayerInfo playerInfo, AI ai){
        this.gameOperation=gameOperation;
        this.playerInfo=playerInfo;
        this.ai=ai;
        gameSetting.aiState= OFF;
    }
    @Override
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    @Override
    public void turnStart() {
        if (gameSetting.aiState==ON){
            gameOperation.rollDice();
            return;
        }
        gameSetting.movableChess=null;
        gameSetting.diceState= GameSetting.DiceState.CLICKABLE;
        gameOperation.getMutableGameSetting().postValue(gameSetting);
        gameOperation.startCountDown(Constants.TURN_TIME);
    }

    @Override
    public void diceRolled(int dice) {
        if (gameSetting.aiState==ON){
            gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),dice));
            return;
        }
        List<ChessmanId> chessmans=gameOperation.getMovableChessman(playerInfo.getPlayerId(),dice);
        if (chessmans.size()>0){
            gameSetting.movableChess=chessmans;
            gameOperation.getMutableGameSetting().postValue(gameSetting);
            currentDice=dice;
        }
        else
            gameOperation.takeAction(new ChessActionImpl(null,dice));
    }

    @Override
    public void turnFinished() {

    }

    @Override
    public void hosted() {
        if (gameSetting.aiState== ON)
            gameSetting.aiState=OFF;
        else{
            gameSetting.aiState=ON;
            if (gameSetting.diceState== GameSetting.DiceState.CLICKABLE){
                gameSetting.diceState= GameSetting.DiceState.UNCLICKABLE;
                gameOperation.getMutableGameSetting().postValue(gameSetting);
                gameOperation.rollDice();
                return;
            }
            if (gameSetting.movableChess!=null){
                gameSetting.movableChess=null;
                gameOperation.getMutableGameSetting().postValue(gameSetting);
                gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),currentDice));
                return;
            }
        }
        gameOperation.getMutableGameSetting().postValue(gameSetting);
        gameOperation.updatePlayerInfo(playerInfo);
    }

    @Override
    public void notifyAction(ChessAction action) {

    }

    @Override
    public void othersExited(int playerId) {

    }
}
