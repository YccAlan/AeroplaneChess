package com.alan.aeroplanechess.model.room;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WifiInvitationManager {
    enum State{VACANT,GROUPING,GAMING};
    State state;
    DatagramSocket datagramSocket;
    WifiUserListener wifiUserListener;
    WifiUserListener invitationSuccessListener;
    WifiUserListener invitationReceivedListener;

    public void WifiInvitation(){
        try {
            datagramSocket=new DatagramSocket(Constants.PORT,InetAddress.getByName("0.0.0.0"));
            datagramSocket.setBroadcast(true);
            datagramSocket.setSoTimeout(0);
            new Reply().start();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setWifiUserDiscoveredListener(WifiUserListener wifiUserListener){
        this.wifiUserListener=wifiUserListener;
    }

    public void setInvitationSuccessListener(WifiUserListener invitationSuccessListener){
        this.invitationSuccessListener=invitationSuccessListener;
    }

    public void setInvitationReceivedListener(WifiUserListener invitationReceivedListener){
        this.invitationReceivedListener=invitationReceivedListener;
    }

    public void discover(){
        new Discover().start();
    }

    public void invite(String ip){
        new Thread(()->{
            try{
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("IP",Utils.getSelfIp());
                jsonObject.put("NAME",Utils.getSelfName());
                String json=jsonObject.toString();
                datagramSocket.send(new DatagramPacket(json.getBytes(),json.length(),InetAddress.getByName(ip),Constants.PORT));
            } catch (JSONException | IOException e) {
                    e.printStackTrace();
            }
        }).start();
    }

//    class OnInvitingSuccess implements NetworkService.NetworkServiceListener{
//        long invitationTime=System.currentTimeMillis();
//        MutableLiveData<RoomInfo> roomInfo;
//        OnInvitingSuccess(MutableLiveData<RoomInfo> roomInfo){
//            this.roomInfo=roomInfo;
//        }
//        @Override
//        public void onReceiveMessage(String ip, int type, String message) {
//            if (System.currentTimeMillis()-invitationTime>60000)
//                networkService.setListener(ip,type,null);
//            if (state==State.GROUPING){
//                try{
//                JSONObject jsonObject=new JSONObject(message);
//                RoomInfo roomInfo1=roomInfo.getValue();
//                roomInfo1.addPlayer(new PlayerInfoImpl(jsonObject.getString("NAME"),ip,PlayerInfoImpl.Type.REMOTE_PLAYER));
//                roomInfo.postValue(roomInfo1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public interface WifiUserListener{
        void onNewUser(WifiUser wifiUser);
    }

    class Discover extends Thread{
        @Override
        public void run() {
            try {
                JSONObject json=new JSONObject();
                json.put("TYPE", "ECHO_REQUEST");
                String jsonString=json.toString();
                datagramSocket.send(new DatagramPacket(jsonString.getBytes(),jsonString.length()));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Reply extends Thread{
        DatagramPacket echoReply;
        Reply(){
            try{
                JSONObject json=new JSONObject();
                json.put("TYPE","ECHO_REPLY");
                json.put("IP",Utils.getSelfIp());
                json.put("NAME",Utils.getSelfName());
                String jsonString=json.toString();
                echoReply=new DatagramPacket(jsonString.getBytes(),jsonString.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            DatagramPacket datagramPacket=new DatagramPacket(new byte[100],100);
            try{
            while(true){
                datagramSocket.receive(datagramPacket);
                JSONObject json=new JSONObject(Arrays.toString(datagramPacket.getData()));
                switch (json.getString("TYPE")){
                    case "ECHO_REQUEST":
                        datagramSocket.send(echoReply);
                        break;
                    case "ECHO_REPLY":
                        if (wifiUserListener!=null)
                            wifiUserListener.onNewUser(new WifiUser(json.getString("NAME"),json.getString("IP")));
                        break;
                    case "INVITATION_REQUEST":
                        if (invitationReceivedListener!=null)
                            invitationReceivedListener.onNewUser(new WifiUser(json.getString("NAME"),json.getString("IP")));
                        break;
                    case "INVITATION_ACCEPT":
                        if (invitationSuccessListener!=null)
                            invitationSuccessListener.onNewUser(new WifiUser(json.getString("NAME"),json.getString("IP")));
                        break;
                }
            }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
