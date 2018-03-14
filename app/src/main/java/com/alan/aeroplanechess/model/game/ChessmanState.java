package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public interface ChessmanState {  //先通过静态方法传入地图尺寸
    int getPositionNo();  //位置编号
    int getX();  //地图上的X坐标
    int getY();  //地图上的Y坐标
    int getAngle();  //棋子朝向
    boolean isFinished();  //是否完成飞行
}
