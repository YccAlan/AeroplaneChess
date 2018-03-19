package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessmanId;

/**
 * Created by yccalan on 2018/3/19.
 */

public class ChessmanIdImpl implements ChessmanId {
    int chessmanId;

    public ChessmanIdImpl(int chessmanId){
        this.chessmanId=chessmanId;
    }

    @Override
    public int getPlayerId() {
        return chessmanId/4;
    }

    @Override
    public int getPlaneId() {
        return chessmanId%4;
    }

    @Override
    public int getId() {
        return chessmanId;
    }
}
