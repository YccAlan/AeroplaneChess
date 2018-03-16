package com.alan.aeroplanechess.model.game;

import com.alan.aeroplanechess.model.room.PlayerInfo;

import java.io.File;
import java.util.List;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessState {  //传入玩家信息List<PlayerInfo>
    void addAction(ChessAction action);  //下一步棋
    List<ChessmanState> getStates();  //所有棋子状态
    List<PlayerInfo> getPlayerInfo();  //玩家信息，按id排序
    int getCurrentActionCount();  //当前步数
    List<ChessmanId> getMovableChessman(int playerId,int step);  //可移动棋子
    List<ChessAnimation> getAnimations(int beginning);  //从beginning到当前操作的动画序列
    boolean isFinished();  //是否结束
    List<GroupResult> getResults();  //成绩

    void save(File file);
    void load(File file);
}
