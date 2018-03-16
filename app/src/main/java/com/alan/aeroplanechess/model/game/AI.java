package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface AI {  //构造器传入ChessState
    ChessAction calcAction(int playerId, int step);  //计算行动
}
