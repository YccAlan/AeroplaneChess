package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;

/**
 * Created by yccalan on 2018/3/11.
 */

public class ChessmanStateImpl implements ChessmanState {//传入地图宽高初始化地图，传入ID得到某一chessman相关信息
    static int width;//地图宽度
    static int height;//地图高度
    int now_pos;//当前相对位置编号，默认在家
    State state;
    ChessmanId chessmanId = null;

    ChessmanStateImpl(ChessmanId id){
        state = State.HOME;
        chessmanId = id;
        now_pos = -1;
    }

    public void set(State s, int pos){
        state = s;
        now_pos = pos;
    }

    public void reset()
    {
        state = State.HOME;
        now_pos = -1;
    }

    //初始化地图尺寸
    public static void initializeChessBoardSize(int width,int height){
        ChessmanStateImpl.width=width;
        ChessmanStateImpl.height=height;
    }

    @Override
    //调用chessmanid的函数得到返回值？？？？？
    public ChessmanId getChessmanId()
    {
        return chessmanId;
    }

    @Override
    //顺时针玩家颜色分别为红1、黄2、绿3、蓝4，以红色棋子出发点为0号位置
    public int getPositionNo()
    {
        int positionno = 0;
        if(now_pos >= 1 && now_pos <=50) {
            if (chessmanId.getPlayerId() == 0)
                positionno = now_pos;
            if (chessmanId.getPlayerId() == 1)
                positionno = (now_pos + 13 + (now_pos + 13) / 53) % 53;
            if (chessmanId.getPlayerId() == 2)
                positionno = (now_pos + 26 + (now_pos + 26) / 53) % 53;
            if (chessmanId.getPlayerId() == 3)
                positionno = (now_pos + 39 + (now_pos + 39) / 53) % 53;
        }
        if(now_pos >= 51) {
            if (chessmanId.getPlayerId() == 0)
                positionno = now_pos + 2;
            if (chessmanId.getPlayerId() == 1)
                positionno = now_pos + 8;
            if (chessmanId.getPlayerId() == 2)
                positionno = now_pos + 14;
            if (chessmanId.getPlayerId() == 3)
                positionno = now_pos + 20;
        }
        return positionno;
    }

    @Override
    public int getRelativePosition()
    {
        return now_pos;
    }

    @Override
    public ChessmanState move(int step)
    {
        ChessmanStateImpl chessmanState = new ChessmanStateImpl(this.chessmanId);
        now_pos += step;
        if(now_pos >= 1 && now_pos <= 49) {
            if ((now_pos - 2) % 4 == 0 && now_pos != 18)
                now_pos += 4;
            if (now_pos == 18)
                now_pos += 12;
        }
        if(now_pos > 56)
            now_pos = 112 - now_pos - step;
        chessmanState.set(this.getState(), now_pos);
        return chessmanState;
        //前进step后的位置
    }
    //0号在停机坪，-1在家，1-50在公共区域，51-55在私有区域，56完成
    public State getState()
    {
        if(now_pos == -1)
            state = State.HOME;
        else if(now_pos == 0)
            state = State.TERMINAL;
        else if(now_pos >= 1 && now_pos <=50)
            state = State.COMMON_AREA;
        else if(now_pos >= 51 && now_pos <= 55)
            state = State.PRIVATE_AREA;
        else if(now_pos == 56)
            state = State.FINISHED;
        //棋子状态
        return state;
    }

    public float getX(){
        int little = width / 15;
        double X = 0;

        if(now_pos == 0 && chessmanId.getPlayerId() == 0)
            X = 0.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 1)
            X = 11.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 2)
            X = 14.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 3)
            X = 3.5 * little;
        else if(getPositionNo() >= 1 && getPositionNo() <= 4)
            X = (getPositionNo() - 1) + 0.5 * little;
        else if(getPositionNo() >= 5 && getPositionNo() <= 7)
            X = 4.5 * little;
        else if(getPositionNo() >= 8 && getPositionNo() <= 14)
            X = (getPositionNo() - 4) + 0.5 * little;
        else if(getPositionNo() >= 15 && getPositionNo() <= 17)
            X = 10.5 * little;
        else if(getPositionNo() >= 18 && getPositionNo() <= 21)
            X = (getPositionNo() - 7) + 0.5 * little;
        else if(getPositionNo() >= 22 && getPositionNo() <= 27)
            X = 14.5 * little;
        else if(getPositionNo() >= 28 && getPositionNo() <= 30)
            X = (41 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 31 && getPositionNo() <= 34)
            X = 10.5 * little;
        else if(getPositionNo() >= 35 && getPositionNo() <= 40)
            X = (44 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 41 && getPositionNo() <= 43)
            X = 4.5 * little;
        else if(getPositionNo() >= 44 && getPositionNo() <= 47)
            X = (47 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 48 && getPositionNo() <= 52)
            X = 0.5 *little;
        else if(getPositionNo() >= 53 && getPositionNo() <= 58)
            X = (getPositionNo() - 52) + 0.5 * little;
        else if(getPositionNo() >= 59 && getPositionNo() <= 64)
            X = 7.5 * little;
        else if(getPositionNo() >= 65 && getPositionNo() <= 70)
            X = (78 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 71 && getPositionNo() <= 76)
            X = 7.5 * little;

        return (float) X;
    }
    public float getY(){
        int little = height / 15;
        double Y = 0;

        if(now_pos == 0 && chessmanId.getPlayerId() == 0)
            Y = 3.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 1)
            Y = 0.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 2)
            Y = 11.5 * little;
        else if(now_pos == 0 && chessmanId.getPlayerId() == 3)
            Y = 14.5 * little;
        else if(getPositionNo() >= 1 && getPositionNo() <= 4)
            Y = 4.5 * little;
        else if(getPositionNo() >= 5 && getPositionNo() <= 7)
            Y = (8 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 8 && getPositionNo() <= 14)
            Y = 0.5 * little;
        else if(getPositionNo() >= 15 && getPositionNo() <= 17)
            Y = (getPositionNo() - 14) + 0.5 * little;
        else if(getPositionNo() >= 18 && getPositionNo() <= 21)
            Y = 4.5 * little;
        else if(getPositionNo() >= 22 && getPositionNo() <= 27)
            Y = (getPositionNo() - 17) + 0.5 * little;
        else if(getPositionNo() >= 28 && getPositionNo() <= 30)
            Y = 10.5 * little;
        else if(getPositionNo() >= 31 && getPositionNo() <= 34)
            Y = (getPositionNo() - 20) + 0.5 * little;
        else if(getPositionNo() >= 35 && getPositionNo() <= 40)
            Y = 14.5 * little;
        else if(getPositionNo() >= 41 && getPositionNo() <= 43)
            Y = (54 - getPositionNo()) + 0.5 * little;
        else if(getPositionNo() >= 44 && getPositionNo() <= 47)
            Y = 10.5 * little;
        else if(getPositionNo() >= 48 && getPositionNo() <= 52)
            Y = (57 - getPositionNo()) + 0.5 *little;
        else if(getPositionNo() >= 53 && getPositionNo() <= 58)
            Y = 7.5 * little;
        else if(getPositionNo() >= 59 && getPositionNo() <= 64)
            Y = (getPositionNo() - 58) + 0.5 * little;
        else if(getPositionNo() >= 65 && getPositionNo() <= 70)
            Y = 7.5 * little;
        else if(getPositionNo() >= 71 && getPositionNo() <= 76)
            Y = (84 - getPositionNo()) + 0.5 * little;

        return (float) Y;
    }
    public int getAngle() {//棋子朝向，1左，2上，3右，4下
        int angle = 0;

        if(now_pos == 0)
            angle = 0;
        else if(getPositionNo() >= 1 && getPositionNo() <= 4)
            angle = 3;
        else if(getPositionNo() >= 5 && getPositionNo() <= 7)
            angle = 2;
        else if(getPositionNo() >= 8 && getPositionNo() <= 14)
            angle = 3;
        else if(getPositionNo() >= 15 && getPositionNo() <= 17)
            angle = 4;
        else if(getPositionNo() >= 18 && getPositionNo() <= 21)
            angle = 3;
        else if(getPositionNo() >= 22 && getPositionNo() <= 27)
            angle = 4;
        else if(getPositionNo() >= 28 && getPositionNo() <= 30)
            angle = 1;
        else if(getPositionNo() >= 31 && getPositionNo() <= 34)
            angle = 4;
        else if(getPositionNo() >= 35 && getPositionNo() <= 40)
            angle = 1;
        else if(getPositionNo() >= 41 && getPositionNo() <= 43)
            angle = 2;
        else if(getPositionNo() >= 44 && getPositionNo() <= 47)
            angle = 1;
        else if(getPositionNo() >= 48 && getPositionNo() <= 52)
            angle = 2;
        else if(getPositionNo() >= 53 && getPositionNo() <= 58)
            angle = 3;
        else if(getPositionNo() >= 59 && getPositionNo() <= 64)
            angle = 4;
        else if(getPositionNo() >= 65 && getPositionNo() <= 70)
            angle = 1;
        else if(getPositionNo() >= 71 && getPositionNo() <= 76)
            angle = 2;

        return angle;
    }
}
