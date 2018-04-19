package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.google.gson.Gson;

import org.json.JSONException;
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
        return chessmanId;
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public JSONObject toJson() {
        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static ChessActionImpl fromJSON(JSONObject json){
        return new Gson().fromJson(json.toString(),ChessActionImpl.class);
    }
}
