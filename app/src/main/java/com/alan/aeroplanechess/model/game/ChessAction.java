package com.alan.aeroplanechess.model.game;

import org.json.JSONObject;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessAction {
    ChessmanId getId();  //棋子id
    int getStep();  //步数
    JSONObject toJson();
}
