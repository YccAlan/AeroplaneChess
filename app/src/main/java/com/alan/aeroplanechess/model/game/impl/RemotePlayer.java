package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.model.game.AI;
import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.Player;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.viewModel.game.GameOperation;
import com.alan.aeroplanechess.viewModel.game.GameSetting;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alan.aeroplanechess.viewModel.game.GameSetting.AIState.OFF;
import static com.alan.aeroplanechess.viewModel.game.GameSetting.AIState.ON;

/**
 * Created by yccalan on 2018/3/11.
 */

public class RemotePlayer implements Player {
    PlayerInfo playerInfo;
    GameOperation gameOperation;
    AI ai;
    GameSetting gameSetting=new GameSetting();
    int currentDice;
    NetworkService networkService;

    public RemotePlayer(GameOperation gameOperation, PlayerInfo playerInfo, AI ai, NetworkService networkService){
        this.gameOperation=gameOperation;
        this.playerInfo=playerInfo;
        this.ai=ai;
        this.networkService=networkService;
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
        gameSetting.diceState= GameSetting.DiceState.UNCLICKABLE;
        gameOperation.getMutableGameSetting().postValue(gameSetting);
        gameOperation.startCountDown(Constants.TURN_TIME+3000);
    }

    @Override
    public void diceRolled(int dice) {
        if (gameSetting.aiState==ON){
            gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),dice));
            return;
        }
        currentDice=dice;
        networkService.setListener(playerInfo.getIp(),Constants.NET_GAME,(ip, type, message) -> {
            try {
                JSONObject remoteAction=new JSONObject(message);
                if (remoteAction.getString("STATE").equals("EXIT")){
                    gameSetting.aiState=ON;
                    gameOperation.getMutableGameSetting().postValue(gameSetting);
                    networkService.setListener(playerInfo.getIp(),Constants.NET_GAME,null);
                    gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),dice));
                    return;
                }
                if (remoteAction.getInt("ID")==gameOperation.getCurrentStep()){
                    networkService.setListener(playerInfo.getIp(),Constants.NET_GAME,null);
                    ChessAction action=ChessActionImpl.fromJSON(remoteAction.getJSONObject("ACTION"));
                    gameOperation.takeAction(action);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void turnFinished() {
    }

    @Override
    public void hosted() {
        gameSetting.aiState=ON;
        gameOperation.getMutableGameSetting().postValue(gameSetting);
        networkService.setListener(playerInfo.getIp(),Constants.NET_GAME,null);
        gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),currentDice));
        playerInfo.setState(PlayerInfo.State.OFFLINE);
        gameOperation.updatePlayerInfo(playerInfo);
    }

    @Override
    public void notifyAction(ChessAction action) {
        try {
            JSONObject remoteAction=new JSONObject();
            remoteAction.put("STATE","NEW_ACTION");
            remoteAction.put("ID",gameOperation.getCurrentStep());
            remoteAction.put("ACTION",action.toJson());
            networkService.send(playerInfo.getIp(),Constants.NET_GAME,remoteAction.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void othersExited(int playerId) {
        JSONObject remoteAction=new JSONObject();
        try {
            remoteAction.put("STATE","EXIT");
            networkService.send(playerInfo.getIp(),Constants.NET_GAME,remoteAction.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
