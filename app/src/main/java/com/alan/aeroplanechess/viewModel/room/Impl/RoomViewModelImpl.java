package com.alan.aeroplanechess.viewModel.room.Impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.alan.aeroplanechess.activity.GameActivity;
import com.alan.aeroplanechess.model.room.Impl.PlayerInfoImpl;
import com.alan.aeroplanechess.model.room.Impl.RoomMatcherImpl;
import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.model.room.RoomMatcher;
import com.alan.aeroplanechess.model.room.WifiInvitationManager;
import com.alan.aeroplanechess.model.room.WifiSync;
import com.alan.aeroplanechess.model.room.WifiUser;
import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.util.Utils;
import com.alan.aeroplanechess.viewModel.room.RoomViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.alan.aeroplanechess.viewModel.room.RoomViewModel.RoomState.DISMISSED;
import static com.alan.aeroplanechess.viewModel.room.RoomViewModel.RoomState.GROUPING;
import static com.alan.aeroplanechess.viewModel.room.RoomViewModel.RoomState.WATCHING;

public class RoomViewModelImpl implements RoomViewModel {
    MutableLiveData<RoomInfo> roomInfoLiveData=new MutableLiveData<>();
    MutableLiveData<RoomState> roomState=new MutableLiveData<>();
    MutableLiveData<List<WifiUser>> wifiUsers=new MutableLiveData<>();

    RoomInfo roomInfo;
    int localPlayerCount=1;
    int pendingPlayerCount=0;

    NetworkService networkService;
    RoomMatcher roomMatcher;
    WifiSync wifiSync;
    WifiInvitationManager wifiInvitationManager;

    public RoomViewModelImpl(RoomInfo roomInfo,boolean isMaster,NetworkService networkService){
        this.roomInfo=roomInfo;
        roomInfoLiveData.postValue(roomInfo);
        this.networkService=networkService;
        this.roomMatcher=new RoomMatcherImpl();
        roomInfoLiveData.setValue(roomInfo);
        wifiSync=new WifiSync(roomState,roomInfoLiveData,isMaster,Utils.getSelfIp(),networkService,()->{roomState.postValue(DISMISSED);networkService.startGame();});
        wifiInvitationManager=networkService.getWifiInvitationManager();
        if (isMaster)
            roomState.postValue(GROUPING);
        else
            roomState.postValue(WATCHING);

        wifiInvitationManager.setWifiUserDiscoveredListener(new OnWifiUserDiscoveredListener());
        wifiInvitationManager.setInvitationSuccessListener(new OnWifiInvitationSuccessListener());
    }

    @Override
    public LiveData<RoomInfo> getRoomInfo() {
        return roomInfoLiveData;
    }

    @Override
    public LiveData<RoomState> getRoomState() {
        return roomState;
    }

    @Override
    public void addLocalPlayer() {
        if (roomInfo.getPlayerInfoList().size()==4)
            return;
        PlayerInfo playerInfo=new PlayerInfoImpl(Utils.getSelfName()+localPlayerCount,Utils.getSelfIp(), PlayerInfo.Type.LOCAL_PLAYER);
        localPlayerCount++;
        roomInfo.addPlayer(playerInfo);
        roomInfoLiveData.postValue(roomInfo);
    }

    @Override
    public void addPendingPlayer() {
        if (roomInfo.getPlayerInfoList().size()==4)
            return;
        PlayerInfo playerInfo=new PlayerInfoImpl("PendingPlayer"+ pendingPlayerCount,null, PlayerInfo.Type.PENDING_PLAYER);
        pendingPlayerCount++;
        roomInfo.addPlayer(playerInfo);
        roomInfoLiveData.postValue(roomInfo);
    }

    @Override
    public void inviteWifiPlayer(List<WifiUser> wifiUsers) {
        for (WifiUser i:wifiUsers)
            wifiInvitationManager.invite(i.ip);
    }

    @Override
    public LiveData<List<WifiUser>> getWifiUsers() {
        return wifiUsers;
    }

    @Override
    public void discoverWifiUser() {
        wifiUsers.postValue(new ArrayList<>());
        wifiInvitationManager.discover();
    }

    @Override
    public void changeGroup(PlayerInfo playerInfo) {
        playerInfo.setGroupId((playerInfo.getPlayerId()+1)%4);
        roomInfoLiveData.postValue(roomInfo);
    }

    @Override
    public void removePlayer(PlayerInfo playerInfo) {
        roomInfo.removePlayer(playerInfo);
        roomInfoLiveData.postValue(roomInfo);
        wifiSync.sendRoomInfo();
    }

    @Override
    public void startLocal(Context context) {
        for(PlayerInfo i:roomInfo.getPlayerInfoList())
            if (i.getType()== PlayerInfo.Type.PENDING_PLAYER)
                i.setType(PlayerInfo.Type.AI_PLAYER);
        context.startActivity(new Intent(context, GameActivity.class));
    }

    @Override
    public void startOnline(Context context) {
        roomState.postValue(RoomState.MATCHING);
        roomMatcher.matchRoom(roomInfo,new OnMatchedListener(context));
    }

    @Override
    public void cancelMatch() {
        roomMatcher.cancelMatch();
    }

    @Override
    public void exit() {
        wifiSync.exit();
    }

    class OnMatchedListener implements RoomMatcher.OnMatchedListener{
        Context context;

        OnMatchedListener(Context context){
            this.context=context;
        }
        @Override
        public void onMatched(RoomInfo roomInfo) {
            roomState.postValue(DISMISSED);
            wifiSync.sendRoomInfo();
            wifiSync.start();
            context.startActivity(new Intent(context,GameActivity.class));
        }

        @Override
        public void onFailed() {
            roomState.postValue(GROUPING);
        }
    }

    class OnWifiUserDiscoveredListener implements WifiInvitationManager.WifiUserListener{
        List<WifiUser> wifiUsers=RoomViewModelImpl.this.wifiUsers.getValue();
        @Override
        public void onNewUser(WifiUser wifiUser) {
            wifiUsers.add(wifiUser);
            RoomViewModelImpl.this.wifiUsers.postValue(wifiUsers);
        }
    }

    class OnWifiInvitationSuccessListener implements WifiInvitationManager.WifiUserListener{
        @Override
        public void onNewUser(WifiUser wifiUser) {
            roomInfo.addPlayer(new PlayerInfoImpl(wifiUser.name,wifiUser.ip, PlayerInfo.Type.REMOTE_PLAYER));
            roomInfoLiveData.postValue(roomInfo);
        }
    }
}
