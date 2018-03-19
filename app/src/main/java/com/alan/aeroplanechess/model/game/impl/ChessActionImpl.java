package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessmanId;

import org.json.JSONObject;

/**
 * Created by yccalan on 2018/3/19.
 */

public class ChessActionImpl implements ChessAction {
    ChessmanId chessmanId;
    int step;

    public ChessActionImpl(ChessmanId chessmanId,int step){
        this.chessmanId=chessmanId;
        this.step=step;
    }

    @Override
    public ChessmanId getChessmanId() {
        return null;
    }

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
