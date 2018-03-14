package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessmanState;

/**
 * Created by yccalan on 2018/3/11.
 */

public class ChessmanStateImpl implements ChessmanState {
    static int width;
    static int height;
    static void initializeChessBoardSize(int width,int height){
        ChessmanStateImpl.width=width;
        ChessmanStateImpl.height=height;
    }

}
