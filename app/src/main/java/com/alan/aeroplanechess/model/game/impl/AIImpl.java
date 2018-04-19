package com.alan.aeroplanechess.model.game.impl;

import com.alan.aeroplanechess.model.game.AI;
import com.alan.aeroplanechess.model.game.ChessAction;
import com.alan.aeroplanechess.model.game.ChessState;
import com.alan.aeroplanechess.model.game.ChessmanId;
import com.alan.aeroplanechess.model.game.ChessmanState;
import com.alan.aeroplanechess.model.room.PlayerInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sujianshen on 2018/3/18.
 */

public class AIImpl implements AI {
    public static <ChessmanState> List<ChessmanState> deepCopy(List<ChessmanState> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<ChessmanState> dest = (List<ChessmanState>) in.readObject();
        return dest;
    }
    ChessState chessState1;
    public AIImpl(ChessState chessState)
    {
    chessState1=chessState;
    }
    public ChessAction calcAction(int playerId, int step){
        ChessAction chessAction;
        List<ChessmanId>chessmanIds=new ArrayList<>();
        List<ChessmanState>chessmanStateList1=new ArrayList<>();
        List<ChessmanState>chessmanStateList=new ArrayList<>();
        List<ChessmanState>gropList=new ArrayList<>();
        List<ChessmanState>beforeState=new ArrayList<>();
        List<ChessmanState>afterState=new ArrayList<>();
        List<PlayerInfo>playerInfoList=chessState1.getPlayerInfo();
        chessmanIds=chessState1.getMovableChessman(playerId, step);
        chessmanStateList1=chessState1.getStates();
        try {
             chessmanStateList = deepCopy(chessmanStateList1);
        }
        catch(IOException |ClassNotFoundException e){
         e.printStackTrace();
        }
        Map<Integer,Integer> map=new HashMap();
        if(chessmanIds.size()==0)
        {
            return new ChessActionImpl(null,step);
        }
        else {
            for (int i = 0; i < playerInfoList.size(); i++) {
                map.put(playerInfoList.get(i).getPlayerId(), playerInfoList.get(i).getGroupId());
            }
            Integer ln=chessmanStateList.size();
            for (int i = 0; i <ln; i++) {
                for (int j = 0; j < chessmanIds.size(); j++) {
                    if (chessmanStateList.get(i).getChessmanId() == chessmanIds.get(j)) {
                        beforeState.add(chessmanStateList.get(i));
                        afterState.add(chessmanStateList.get(i).move(step));
                       // chessmanStateList.remove(i);
                    } else {
                        if (chessmanStateList.get(i).getState() == ChessmanState.State.HOME || chessmanStateList.get(i).getState() == ChessmanState.State.FINISHED)
                            chessmanStateList.remove(i);
                        else
                            if (map.get(chessmanStateList.get(i).getChessmanId().getPlayerId()) == map.get(playerId))
                            {
                                gropList.add(chessmanStateList.get(i));
                                chessmanStateList.remove(i);
                            }
                    }
                }
            }
            Map<Integer, Float> score = new HashMap<>();
            Map<Integer, Float> movedScore = new HashMap<>();
            for (int i = 0; i < beforeState.size(); i++) {
                score.put(beforeState.get(i).getChessmanId().getId(), 0f);
                movedScore.put(afterState.get(i).getChessmanId().getId(), 0f);
            }
            boolean t=true;
            while(t) {
                t=false;
                for (int i = 0; i < afterState.size(); i++) {
                    for (int j = 0; j < gropList.size(); j++) {
                        if (map.get(afterState.get(i).getChessmanId().getPlayerId()) == map.get(gropList.get(j).getChessmanId().getPlayerId())) {
                            ChessmanState chess = afterState.get(i).move(step);
                            afterState.set(i, chess);
                            Float after = movedScore.get(afterState.get(i).getChessmanId().getId());
                            after += step;
                            movedScore.put(beforeState.get(i).getChessmanId().getId(), after);
                            t=true;

                        }
                    }
                }
            }
            for (int i = 0; i < chessmanStateList.size(); i++) {
                for (int j = 1; j <= 6; j++) {
                    ChessmanState chessmanState = chessmanStateList.get(i).move(j);
                    for (int m = 0; m < beforeState.size(); m++) {
                        if (chessmanState.getPositionNo() == beforeState.get(m).getPositionNo()) {
                            Float before = score.get(beforeState.get(m).getChessmanId().getId());
                            before += -1f/ 6 * chessmanState.getRelativePosition();
                            score.put(beforeState.get(m).getChessmanId().getId(), before);
                        }
                        if (chessmanState.getPositionNo() == afterState.get(m).getPositionNo()) {
                            Float after = movedScore.get(afterState.get(m).getChessmanId().getId());
                            after += -1f / 6 * chessmanState.getRelativePosition();
                            movedScore.put(beforeState.get(m).getChessmanId().getId(), after);
                        }

                    }
                }
                ChessmanState chessmanState1 = chessmanStateList.get(i);
                for (int n = 0; n < afterState.size(); n++) {
                    if (chessmanState1.getPositionNo() == afterState.get(n).getPositionNo()) {
                        Float after = movedScore.get(afterState.get(n).getChessmanId().getId());
                        after += chessmanState1.getRelativePosition() * 1f / 2;
                        movedScore.put(beforeState.get(n).getChessmanId().getId(), after);
                    }
                }
            }
            for (int n = 0; n < afterState.size(); n++) {
                if (afterState.get(n).getState() == ChessmanState.State.FINISHED) {
                    Float after = movedScore.get(afterState.get(n).getChessmanId().getId());
                    after += 6f;
                    movedScore.put(beforeState.get(n).getChessmanId().getId(), after);
                }
                if (afterState.get(n).getState() == ChessmanState.State.TERMINAL) {
                    Float after = movedScore.get(afterState.get(n).getChessmanId().getId());
                    after += 5f;
                    movedScore.put(beforeState.get(n).getChessmanId().getId(), after);
                }
            }
            Map<ChessmanId, Float> Subtraction = new HashMap<>();
            for (int i = 0; i < beforeState.size(); i++) {
                Float sub;
                sub = movedScore.get(beforeState.get(i).getChessmanId().getId()) - score.get(beforeState.get(i).getChessmanId().getId());
                Subtraction.put((beforeState.get(i).getChessmanId()), sub);
            }
            Iterator iter = Subtraction.entrySet().iterator();
            Float max = -200f;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Float values = (Float) entry.getValue();
                if (values > max)
                    max = values;
            }
            Iterator iter1 = Subtraction.entrySet().iterator();
            Integer finalId = 0;
            ChessmanId chessmanId=null;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Float values = (Float) entry.getValue();
                if (Math.abs(values - max) < 0.001f)
                    chessmanId = (ChessmanId) entry.getKey();
            }

           /*
            for (int i = 0; i < beforeState.size(); i++) {
                if (beforeState.get(i).getChessmanId().getPlayerId() == finalId)
                    chessmanId = beforeState.get(i).getChessmanId();
            }*/
           return new ChessActionImpl(chessmanId,step);
        }
    }
}
