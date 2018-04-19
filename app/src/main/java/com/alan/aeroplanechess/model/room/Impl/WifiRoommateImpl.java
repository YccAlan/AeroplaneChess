//package com.alan.aeroplanechess.model.room.Impl;
//
//import com.alan.aeroplanechess.model.room.RoomInfo;
//import com.alan.aeroplanechess.model.room.WifiRoommate;
//import com.alan.aeroplanechess.model.room.WifiUser;
//import com.alan.aeroplanechess.service.NetworkService;
//
//import java.util.List;
//
//public class WifiRoommateImpl implements WifiRoommate {
//    StateListener stateListener;
//    WifiUser master;
//    List<WifiUser> members;
//
//    NetworkService networkService;
//
//    public static WifiRoommateImpl asMaster(List<WifiUser> members,NetworkService networkService){
//        WifiRoommateImpl wifiRoommate=new WifiRoommateImpl();
//        wifiRoommate.members=members;
//        return wifiRoommate;
//    }
//
//    public static WifiRoommateImpl asMember(WifiUser master,StateListener stateListener,NetworkService networkService){
//        WifiRoommateImpl wifiRoommate=new WifiRoommateImpl();
//        wifiRoommate.master=master;
//        wifiRoommate.stateListener=stateListener;
//        return wifiRoommate;
//    }
//
//    @Override
//    public void sendRoomInfo(RoomInfo roomInfo) {
//
//    }
//
//    @Override
//    public void kick() {
//
//    }
//
//    @Override
//    public void start() {
//
//    }
//}
