package com.alan.aeroplanechess.model.room.Impl;

import com.alan.aeroplanechess.model.room.RoomInfo;
import com.alan.aeroplanechess.model.room.RoomMatcher;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zzj on 2018/4/12.
 */

public class RoomMatcherImpl implements RoomMatcher {
    private Socket socket;
    private DataOutputStream writer;
    @Override
    public void matchRoom (RoomInfo roomInfo, RoomMatcher.OnMatchedListener onMatchedListener) {

        try {
            socket=new Socket("192.168.0.1",6666);
            writer = new DataOutputStream(socket.getOutputStream());
            JSONObject json=new JSONObject();
            json.put("type",0);
            long time=System.currentTimeMillis();
            json.put("time",time);
            json.put("roomInfo",roomInfo.toJson());
            writer.writeUTF(json.toString());
            DataInputStream inputStream=new DataInputStream(socket.getInputStream());
            if(inputStream.readUTF()=="1") {
                writer.writeUTF("1");
                inputStream = new DataInputStream(socket.getInputStream());
                JSONObject json_s=new JSONObject(inputStream.toString());
                RoomInfo matchedRoomInfo= RoomInfoImpl.fromJson(json_s);
                onMatchedListener.onMatched(matchedRoomInfo);
            }
            socket.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    @Override
    public void cancelMatch() {
        try {
            writer.writeUTF("3");
        }catch (IOException e){
            System.out.println(e);
        }

    }
//    class OnMatchedListenerImpl implements OnMatchedListener {
//        @Override
//        public void onMatched(RoomInfo roomInfo) {
//
//        }
//
//        @Override
//        public void onFailed() {
//
//        }
//    }
}
