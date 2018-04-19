package com.alan.aeroplanechess.model.watchBack;

import com.alan.aeroplanechess.model.game.ChessState;
import com.alan.aeroplanechess.model.game.GroupResult;
import com.alan.aeroplanechess.model.room.RoomInfo;

import java.util.List;

public class HistoryGameState {
    public long time;
    public List<GroupResult> result;
    public RoomInfo roomInfo;
    public ChessState chessState;
}
