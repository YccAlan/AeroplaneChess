package com.alan.aeroplanechess.model.room;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.model.room.Impl.RoomInfoImpl;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.alan.aeroplanechess.viewModel.room.RoomViewModel.RoomState.DISMISSED;

public class WifiSync {
    MutableLiveData<RoomInfo> roomInfo;
    MutableLiveData<RoomViewModel.RoomState> roomState;
    Set<PlayerInfo> players=new HashSet<>();
    String selfIp;
    boolean isMaster;
    NetworkService networkService;
    OnReceivedRoomInfo onReceivedRoomInfo=new OnReceivedRoomInfo();
    WifiSyncListener wifiSyncListener;

    public WifiSync(MutableLiveData<RoomViewModel.RoomState> roomState,MutableLiveData<RoomInfo> roomInfo, boolean isMaster, String selfIp, NetworkService networkService,WifiSyncListener wifiSyncListener){
        this.roomState=roomState;
        this.roomInfo=roomInfo;
        this.selfIp = selfIp;
        this.networkService=networkService;
        this.wifiSyncListener=wifiSyncListener;
        players.addAll(roomInfo.getValue().getPlayerInfoList());
        if (isMaster)
            roomInfo.observeForever(new SendRoomInfo());
    }

    void updateNetworkListener(Collection<PlayerInfo> oldPlayers, Collection<PlayerInfo> newPlayers){
        if (oldPlayers!=null)
            for (PlayerInfo i:oldPlayers)
                if (i.getIp()!=null&&i.getIp().equals(selfIp)){
                    networkService.setListener(i.getIp(),Constants.NET_ROOMINFO,null);
                }
        if (newPlayers!=null)
            for (PlayerInfo i:newPlayers)
                if (i.getIp()!=null&&i.getIp().equals(selfIp)){
                    networkService.setListener(i.getIp(),Constants.NET_ROOMINFO,onReceivedRoomInfo);
                }
    }

    public void sendRoomInfo(){
        try {
            JSONObject json=new JSONObject();
            json.put("TYPE","ROOMINFO");
            json.put("ROOMINFO",roomInfo.getValue().toJson());
            String roomInfoString=json.toString();
            for (PlayerInfo i:players)
                if (i.getIp()!=null&&!i.getIp().equals(selfIp)){
                    networkService.send(i.getIp(), Constants.NET_ROOMINFO,roomInfoString);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            JSONObject json=new JSONObject();
            json.put("TYPE","START");
            String roomInfoString=json.toString();
            for (PlayerInfo i:players)
                if (i.getIp()!=null&&!i.getIp().equals(selfIp)){
                    networkService.send(i.getIp(), Constants.NET_ROOMINFO,roomInfoString);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        JSONObject json=new JSONObject();
        try{
            if (isMaster){
                json.put("TYPE","DISMISSED");
            }
            else
                json.put("TYPE","EXIT");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    class SendRoomInfo implements Observer<RoomInfo>{
        @Override
        public void onChanged(@Nullable RoomInfo roomInfo) {
            players.addAll(roomInfo.getPlayerInfoList());
            sendRoomInfo();
            updateNetworkListener(players,roomInfo.getPlayerInfoList());
            players.clear();
            players.addAll(roomInfo.getPlayerInfoList());
        }
    }

    class OnReceivedRoomInfo implements NetworkService.NetworkServiceListener {
        @Override
        public void onReceiveMessage(String ip, int type, String message) {
            try {
                JSONObject json = new JSONObject(message);
                if (json.getString("TYPE").equals("DISMISSED")) {
                    roomState.postValue(DISMISSED);
                    return;
                }
                RoomInfo newRoomInfo;
                if (json.getString("TYPE").equals("EXIT")) {
                    newRoomInfo=roomInfo.getValue();
                    for (PlayerInfo i : newRoomInfo.getPlayerInfoList())
                        if (ip.equals(i.getIp())) {
                            newRoomInfo.getPlayerInfoList().remove(i);
                            break;
                        }
                    roomInfo.postValue(newRoomInfo);
                    updateNetworkListener(players, newRoomInfo.getPlayerInfoList());
                    players.clear();
                    players.addAll(newRoomInfo.getPlayerInfoList());
                    return;
                }
                if (json.getString("TYPE").equals("ROOMINFO")) {
                    newRoomInfo = RoomInfoImpl.fromJson(json.getJSONObject("ROOMINFO"));
                    for (PlayerInfo i : newRoomInfo.getPlayerInfoList())
                        if (selfIp.equals(i.getIp())) {
                            roomInfo.postValue(newRoomInfo);
                            updateNetworkListener(players, newRoomInfo.getPlayerInfoList());
                            players.clear();
                            players.addAll(newRoomInfo.getPlayerInfoList());
                            return;
                        }
                    roomState.postValue(DISMISSED);
                    players.clear();
                    return;
                }
                if (json.getString("TYPE").equals("START")){
                    wifiSyncListener.onStart();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface WifiSyncListener{
        void onStart();
    }
}
