package com.alan.aeroplanechess.model.room;

/**
 * Created by yccalan on 2018/3/14.
 */

public interface WifiRoommate {
    void sendRoomInfo(RoomInfo roomInfo);
    void kick(PlayerInfo playerInfo);
    void start();

    interface StateListener{
        void receivedRoomInfo(RoomInfo roomInfo);
        void kicked();
        void started();
    }
}
