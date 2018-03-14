package com.alan.aeroplanechess.model.game;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessState {
    boolean addAction(ChessAction action);  //下一步棋
    Map<ChessmanId,ChessmanState> getStates();  //所有棋子状态
    int getCurrentActionCount();  //当前步数
    List<ChessmanId> getMovableChessman(int playerId,int step);  //可移动棋子
    List<ChessAnimation> getAnimations(int beginning);  //从beginning到当前操作的动画序列
    List<GroupResult> getResult();  //成绩

    void save(File file);
    void load(File file);
}
