package com.alan.aeroplanechess.service.impl;

import android.util.Pair;

import com.alan.aeroplanechess.Constants;
import com.alan.aeroplanechess.service.NetworkService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yccalan on 2018/3/19.
 */

public class NetworkCommunicator {
    HashMap<Pair<String,Integer>,String> messages=new HashMap<>();
    HashMap<Pair<String,Integer>,NetworkService.NetworkServiceListener> listeners=new HashMap<>();
    BlockingQueue<SendingTask> sendingTasks= new ArrayBlockingQueue<>(50, true);

    Sender sender=new Sender();
    Receiver receiver=new Receiver();

    public NetworkCommunicator(){
        sender.start();
        receiver.start();
    }

    public void send(String ip,int type,String message){
        sendingTasks.add(new SendingTask(ip,type,message));
    }

    public void receive(String ip, int type, NetworkService.NetworkServiceListener listener){
        synchronized (listeners){
            String message=messages.get(Pair.create(ip,type));
            if (message!=null){
                listener.onReceiveMessage(ip,type,message);
                messages.put(Pair.create(ip,type),null);
            }
            listeners.put(Pair.create(ip,type),listener);
        }
    }

    class SendingTask{
        String ip;
        int type;
        String message;

        public SendingTask(String ip, int type, String message) {
            this.ip = ip;
            this.type = type;
            this.message = message;
        }
    }

    class Sender extends Thread{
        @Override
        public void run() {
            while(true){
                try {
                    SendingTask sendingTask=sendingTasks.take();
                    Socket socket=new Socket(sendingTask.ip, Constants.PORT);
                    socket.setSoTimeout(Constants.NETWORK_TIMEOUT);
                    Writer writer=new OutputStreamWriter(socket.getOutputStream());
                    writer.write(sendingTask.type);
                    writer.write(sendingTask.message);
                    writer.flush();
                    socket.close();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Receiver extends Thread{
        ExecutorService executor=new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(50,true));
        @Override
        public void run() {
            try {
                ServerSocket serverSocket=new ServerSocket(Constants.PORT);
                while (true){
                    executor.submit(new ReceivingTask(serverSocket.accept()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        class ReceivingTask implements Runnable{
            Socket socket;
            ReceivingTask(Socket socket){
                this.socket=socket;
            }
            @Override
            public void run() {
                try {
                    socket.setSoTimeout(Constants.NETWORK_TIMEOUT);
                    Scanner scanner=new Scanner(socket.getInputStream());
                    int type=scanner.nextInt();
                    String message=scanner.next();
                    scanner.close();
                    socket.close();
                    String ip=socket.getInetAddress().getHostName();
                    synchronized (listeners){
                        NetworkService.NetworkServiceListener listener=listeners.get(Pair.create(ip,type));
                        if (listener!=null)
                            listener.onReceiveMessage(ip,type,message);
                        else{
                            messages.put(Pair.create(ip,type),message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
