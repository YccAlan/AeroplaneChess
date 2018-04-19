package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessAnimation;
import com.alan.aeroplanechess.model.game.ChessState;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.game.GroupResult;
import com.alan.aeroplanechess.model.room.PlayerInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANARKH on 2018/3/18.
 */

public class ChessStateImpl implements ChessState {//传入玩家信息List<PlayerInfo>
    List<PlayerInfo> playerInfoList;
    List<ChessmanState> chessmanStateList = new ArrayList<ChessmanState>();
    List<ChessAction> chessActions = new ArrayList<ChessAction>();
    List<ChessAnimation> chessAnimations = new ArrayList<ChessAnimation>();
    HashMap<ChessmanId, Integer> groupHashMap = new HashMap<ChessmanId, Integer>();

    public ChessStateImpl(List<PlayerInfo> playerInfoList){
        this.playerInfoList=playerInfoList;
        for(int i = 0; i < playerInfoList.size(); i++)
            for(int j = 0; j < 4; j++) {
                ChessmanId chessmanId = new ChessmanIdImpl(playerInfoList.get(i).getPlayerId(),j);
                ChessmanState chessmanState = new ChessmanStateImpl(chessmanId);
                chessmanStateList.add(chessmanState);
            }
//        ChessAnimation chessAnimation = new ChessAnimation();
//        chessAnimations.add(chessAnimation);
        for(int i = 0; i < playerInfoList.size(); i++)
            for(int j = 0; j < chessmanStateList.size(); j++)
            {
                if(playerInfoList.get(i).getPlayerId() == chessmanStateList.get(j).getChessmanId().getPlayerId())
                    groupHashMap.put(chessmanStateList.get(j).getChessmanId(), playerInfoList.get(i).getGroupId());
            }
    }

    @Override
    public List<PlayerInfo> getPlayerInfo() {
        return playerInfoList;
    }

    @Override
    public List<GroupResult> getResults() {
        return null;
    }

    public void addAction(ChessAction action){
        if (action.getChessmanId()==null){
            chessActions.add(action);
            return;
        }
        ChessAnimation chessAnimation = new ChessAnimation();
        int i = 0,j = 0;
        //遍历寻找执行动作的棋子
        for(; i<chessmanStateList.size(); i++)
            if(action.getChessmanId() == chessmanStateList.get(i).getChessmanId())
                break;
        if(chessmanStateList.get(i).getState() == ChessmanState.State.HOME && action.getStep() <= 5)
            return;
        else if(chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
            return;
        else if(chessmanStateList.get(i).getState() == ChessmanState.State.HOME && action.getStep() >= 5) {
            chessmanStateList.get(i).set(ChessmanState.State.TERMINAL, 0);
            chessAnimation.event = ChessAnimation.Event.MOVE;
            chessAnimation.id = chessmanStateList.get(i).getChessmanId();
            chessAnimation.state = chessmanStateList.get(i);
            chessActions.add(action);
            return;
        }
        else if(chessmanStateList.get(i).getState() != ChessmanState.State.HOME || chessmanStateList.get(i).getState() != ChessmanState.State.FINISHED) {
            chessmanStateList.set(i, chessmanStateList.get(i).move(action.getStep()));
            chessAnimation.state = chessmanStateList.get(i);
            chessAnimation.id = chessmanStateList.get(i).getChessmanId();
            if(chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED) {
                chessAnimation.event = ChessAnimation.Event.FINISH;
                chessAnimations.add(chessAnimation);
            }
            else {
                int actionid = -1, stayid = -2;
                //遍历查看是否会发生碰撞
                for (; j < chessmanStateList.size(); j++)
                    if (chessmanStateList.get(j).getState() == ChessmanState.State.COMMON_AREA && chessmanStateList.get(j).getPositionNo() == chessmanStateList.get(i).getPositionNo() && j != i)
                        break;
                if (j < chessmanStateList.size()) {
                    if (groupHashMap.get(chessmanStateList.get(i).getChessmanId()) == groupHashMap.get(chessmanStateList.get(j).getChessmanId())) {
                        chessAnimation.event = ChessAnimation.Event.MOVE;
                        chessAnimations.add(chessAnimation);
                        //递归查看是否会加速飞行
                        addAction(action);
                    } else {
                        chessmanStateList.get(j).reset();
                        chessAnimation.event = ChessAnimation.Event.COLLISION;
                        chessAnimations.add(chessAnimation);
                        chessActions.add(action);
                        return;
                    }
                } else {
                    chessAnimation.event = ChessAnimation.Event.MOVE;
                    chessAnimations.add(chessAnimation);
                    chessActions.add(action);
                    return;
                }
            }
        }
    }
    public List<ChessmanState> getStates(){
        return chessmanStateList;
    }
    public List<PlayerInfo> getPlayerInfo(List<PlayerInfo> playerInfos){

        Collections.sort(playerInfos, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo p1, PlayerInfo p2) {
                int i = p1.getPlayerId() - p2.getPlayerId();
                return i;
            }
        });

        return playerInfos;
    }
    public int getCurrentActionCount(){
        return chessActions.size();
    }
    public List<ChessmanId> getMovableChessman(int playerId, int step){
        List<ChessmanId> chessmanIdList = new ArrayList<ChessmanId>();
        for(int i = 0; i < chessmanStateList.size(); i++)
            if(chessmanStateList.get(i).getChessmanId().getPlayerId() == playerId) {
                if(chessmanStateList.get(i).getState() == ChessmanState.State.HOME && step >= 5)
                    chessmanIdList.add(chessmanStateList.get(i).getChessmanId());
                else if(chessmanStateList.get(i).getState() == ChessmanState.State.TERMINAL || chessmanStateList.get(i).getState() == ChessmanState.State.COMMON_AREA || chessmanStateList.get(i).getState() == ChessmanState.State.PRIVATE_AREA)
                    chessmanIdList.add(chessmanStateList.get(i).getChessmanId());
            }
        return chessmanIdList;
    }
    public List<ChessAnimation> getAnimations(int beginning){  //bug:一个action可能对应多段动画或没有动画
        return chessAnimations.subList(beginning, chessAnimations.size() - 1);
    }
    public boolean isFinished(){
        int j = 0;
        int[] count = new int[4];
        for(int i = 0; i < chessmanStateList.size(); i++) {
            if (chessmanStateList.get(i).getChessmanId().getPlayerId() == 0)
                if (chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
                    count[0]++;
            if (chessmanStateList.get(i).getChessmanId().getPlayerId() == 1)
                if (chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
                    count[1]++;
            if (chessmanStateList.get(i).getChessmanId().getPlayerId() == 2)
                if (chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
                    count[2]++;
            if (chessmanStateList.get(i).getChessmanId().getPlayerId() == 3)
                if (chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
                    count[3]++;
        }
        for(j = 0;j < 4; j++)
        {
            if(count[j] == 4) {
                break;
            }
        }
        if(j == 4)
            return false;
        else
            return true;
    }
    public List<GroupResult> getResults(List<PlayerInfo> playerInfos){
        List<GroupResult> groupResults = new ArrayList<GroupResult>();
        List<PlayerInfo> playerInfoList = new ArrayList<PlayerInfo>();
        GroupResult groupResult = new GroupResult();
        int[] count = new int[4];
        for(int i = 0; i < 4; i++)
            count[i] = 0;
        for(int i = 0; i < chessmanStateList.size(); i++)
        {
            if(chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED){
                count[groupHashMap.get(chessmanStateList.get(i).getChessmanId())]++;
            }
        }
        for(int i = 0; i < 4; i++){
            groupResult.groupId = i;
            groupResult.finishedPlane = count[i];
            for(int j = 0; j < playerInfos.size(); j++)
            {
                if(playerInfos.get(j).getGroupId() == i)
                    playerInfoList.add(playerInfos.get(j));
            }
            groupResult.players = playerInfoList;
            groupResults.add(groupResult);
        }
        return groupResults;
        //成绩
    }

    public void save(File file){

    }
    public void load(File file){

    }
}
