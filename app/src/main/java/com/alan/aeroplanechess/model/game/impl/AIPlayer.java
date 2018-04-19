package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.AI;
import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.Player;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.viewModel.game.GameOperation;

/**
 * Created by yccalan on 2018/3/11.
 */

public class AIPlayer implements Player {
    PlayerInfo playerInfo;
    AI ai;

    GameOperation gameOperation;

    public AIPlayer(GameOperation gameOperation,PlayerInfo playerInfo, AI ai){
        this.gameOperation=gameOperation;
        this.playerInfo=playerInfo;
        this.ai=ai;
    }
    @Override
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    @Override
    public void turnStart() {
        gameOperation.rollDice();
    }

    @Override
    public void diceRolled(int dice) {
        gameOperation.takeAction(ai.calcAction(playerInfo.getPlayerId(),dice));
    }

    @Override
    public void turnFinished() {

    }

    @Override
    public void hosted() {

    }

    @Override
    public void notifyAction(ChessAction action) {

    }

    @Override
    public void othersExited(int playerId) {

    }
}
