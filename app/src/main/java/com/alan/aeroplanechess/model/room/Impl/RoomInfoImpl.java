package com.alan.aeroplanechess.model.room.Impl;

import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomInfoImpl implements RoomInfo {
    List<PlayerInfo> playerInfoList=new ArrayList<>(4);
    long randomSeed;

    @Override
    public List<PlayerInfo> getPlayerInfoList() {
        return playerInfoList;
    }

    @Override
    public void addPlayer(PlayerInfo playerInfo) {
        playerInfoList.add(playerInfo);
    }

    @Override
    public void removePlayer(PlayerInfo playerInfo) {
        for (PlayerInfo i:playerInfoList)
            if (i.getUniqueId()==playerInfo.getUniqueId()){
                playerInfoList.remove(i);
                break;
        }
    }

    @Override
    public boolean isOnline() {
        for (PlayerInfo i:playerInfoList)
            if (i.getType()== PlayerInfo.Type.REMOTE_PLAYER)
                return true;
        return false;
    }

    @Override
    public void setRandomSeed(long randomSeed) {
        this.randomSeed=randomSeed;
    }

    @Override
    public long getRandomSeed() {
        return randomSeed;
    }

    @Override
    public JSONObject toJson() {
//        JSONObject json=new JSONObject();
//        try {
//            json.put("RANDOM_SEED",randomSeed);
//
//            JSONArray playerArray=new JSONArray();
//            for (PlayerInfo i:playerInfoList)
//                playerArray.put(i.toJson());
//
//            json.put("PLAYER_ARRAY",playerArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RoomInfoImpl fromJson(JSONObject json){
        return new Gson().fromJson(json.toString(),RoomInfoImpl.class);
    }
}
