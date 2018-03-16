package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessmanState {  //先通过静态方法传入地图尺寸
    enum State{HOME,TERMINAL,COMMON_AREA,PRIVATE_AREA,FINISHED}  //在家，停机坪，公共区域，私有区域，完成
    ChessmanId getChessmanId();
    int getPositionNo();  //位置编号
    int getRelativePosition();  //当前玩家而言的已走步数
    ChessmanState move(int step);  //前进step后的位置
    State getState();  //棋子状态

    int getX();  //地图上的X坐标
    int getY();  //地图上的Y坐标
    int getAngle();  //棋子朝向
}
