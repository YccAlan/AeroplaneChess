package com.alan.aeroplanechess.viewModel.game;

import com.alan.aeroplanechess.model.game.ChessmanId;

import java.util.List;

/**
 * Created by yccalan on 2018/3/13.
 */

public class GameSetting {
    int currentPlayer;
    List<ChessmanId> movableChess;  //可移动的棋子

    enum AIState{ON,OFF,DISABLED};
    AIState aiState;  //托管状态

    enum DiceState{CLICKABLE,UNCLICKABLE};
    DiceState diceState;  //骰子状态
}
