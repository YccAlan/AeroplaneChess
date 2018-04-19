package com.alan.aeroplanechess.viewModel.room;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.alan.aeroplanechess.model.room.PlayerInfo;
import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.model.room.WifiUser;

import java.util.List;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface RoomViewModel {
    enum RoomState{GROUPING,MATCHING,WATCHING,DISMISSED}
    LiveData<RoomInfo> getRoomInfo();
    LiveData<RoomState> getRoomState();
    LiveData<List<WifiUser>> getWifiUsers();

    void addLocalPlayer();
    void addPendingPlayer();
    void inviteWifiPlayer(List<WifiUser> wifiUsers);
    void discoverWifiUser();
    void changeGroup(PlayerInfo playerInfo);
    void removePlayer(PlayerInfo playerInfo);

    void startLocal(Context context);
    void startOnline(Context context);
    void cancelMatch();
    void exit();
}
