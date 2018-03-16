package com.alan.aeroplanechess.model.game;

/**
 * Created by yccalan on 2018/3/11.
 */

public class ChessAnimation {
    public enum Event{MOVE,FINISH,COLLISION}
    public ChessmanId id;  //棋子
    public ChessmanState state;  //棋子的目标状态
    public Event event;  //事件（动画类型：移动、到终点、碰撞）
}
