package com.alan.aeroplanechess.model.game;

import java.util.Map;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface Ai {  //传入各玩家类
    ChessAction calcAction(Map<ChessmanId,ChessmanState> state, int playerId, int step);  //计算行动
}
