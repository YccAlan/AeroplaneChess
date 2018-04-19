package com.alan.aeroplanechess.model.room.Impl;

import android.graphics.Bitmap;

import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.util.Avatar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class PlayerInfoImpl implements PlayerInfo {
    int uniqueId;
    int playerId;
    int groupId;
    String name;
    String ip;
    Type type;
    State state;

    public PlayerInfoImpl(String name, String ip, Type type){
        this.name=name;
        this.ip=ip;
        this.type=type;
        this.uniqueId= new Random(System.currentTimeMillis()).nextInt();
    }

    private PlayerInfoImpl() {
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public int getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setGroupId(int groupId) {
        this.groupId=groupId;
    }

    @Override
    public void setState(State state) {
        this.state=state;
    }

    @Override
    public void setType(Type type) {
        this.type=type;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json=new JSONObject();
        try {
            json.put("PLAYER_ID",playerId)
                    .put("GROUP_ID",groupId)
                    .put("NAME",name)
                    .put("IP",ip)
                    .put("TYPE",type)
                    .put("STATE",state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public Bitmap getAvatar() {
        return Avatar.getAvatar(name);
    }

    public static PlayerInfoImpl fromJson(JSONObject json){
        PlayerInfoImpl playerInfo=new PlayerInfoImpl();
        try {
            playerInfo.playerId=json.getInt("PLAYER_ID");
            playerInfo.groupId=json.getInt("GROUP_ID");
            playerInfo.name=json.getString("NAME");
            playerInfo.ip=json.getString("IP");
            playerInfo.type=Type.values()[json.getInt("TYPE")];
            playerInfo.state=State.values()[json.getInt("STATE")];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playerInfo;
    }
}
