package com.alan.aeroplanechess.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {
    static SharedPreferences sharedPreferences;
    static int screenWidth;
    public static void init(Context context){
        sharedPreferences=context.getSharedPreferences("SETTINGS",0);
        screenWidth=context.getResources().getDisplayMetrics().widthPixels;
    }
    public static String getSelfName(){
        return sharedPreferences.getString("NAME","Player");
    }
    public static String getSelfIp(){
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static int getScreenWidth(){
        return screenWidth;
    }
}
