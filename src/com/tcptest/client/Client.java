package com.tcptest.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client {

    private Socket server;

    private Thread thread;
    private final int ticks;

    private final HashMap<Integer, ReceiveDataClient> packets = new HashMap<>();

    public Client(String host, int port, int ticks, ReceiveDataClient... packets){
        try {
            server = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ReceiveDataClient packet : packets){
            this.packets.put(this.packets.size()+1, packet);
        }

        this.ticks = ticks;

        startClient();

    }

    private void startClient(){
        this.thread = new Thread(() -> {
            while(true){
                try {
                    DataInputStream stream = new DataInputStream(server.getInputStream());
                    if(stream.available()== 0) continue;
                    this.packets.get(stream.readInt()).processData(stream, this);

                    Thread.sleep(1000/ticks);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        System.out.println("Client started! Connected to : "+server.getLocalSocketAddress());
    }

    public void closeConnection(){
        if(thread.isAlive()) thread.stop();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Socket getSocket() {
        return server;
    }

    public void sendInt(int num){
        try {
            new DataOutputStream(server.getOutputStream()).writeInt(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDouble(double num){
        try {
            new DataOutputStream(server.getOutputStream()).writeDouble(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendString(String num){
        try {
            new DataOutputStream(server.getOutputStream()).writeUTF(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBoolean(boolean num){

        try {
            new DataOutputStream(server.getOutputStream()).writeBoolean(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
