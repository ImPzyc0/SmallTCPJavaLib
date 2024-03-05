package com.tcptest.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

class Client {

    private final Thread thread;
    private final Socket socket;

    public Client(Socket socket, Thread thread){
        this.socket = socket;
        this.thread = thread;

        thread.start();
    }

    public void stopThread(){
        if(thread.isAlive()) thread.interrupt();
    }

    public Socket getSocket(){
        return socket;
    }
}


public class Server {

    private ServerSocket server;
    private int ticks;

    private Thread thread;

    int clientCounter = 0;

    private final HashMap<Integer, Client> clients = new HashMap<>();
    private final HashMap<Integer, ReceiveDataServer> packets = new HashMap<>();

    public Server(int port, int ticks, ReceiveDataServer... packets){
        try {
            server = new ServerSocket(port);
            this.ticks = ticks;
            server.setSoTimeout(1000000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ReceiveDataServer packet : packets){
            this.packets.put(this.packets.size()+1, packet);
        }

        runServer();
    }

    private void runServer(){
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                acceptClients();
            }
        });
        thread.start();

        System.out.println("Server started! Port: "+server.getLocalPort()+" Ticks: "+ticks);

    }

    private Server getServer(){
        return this;
    }

    public void acceptClients(){
        while(true){
            try {
                Socket client;
                try{
                    client = server.accept();
                }catch (SocketException x){
                    break;
                }
                Socket finalClient = client;
                clientCounter++;
                System.out.println("New Client: "+client.getLocalSocketAddress()+ " ID: "+clientCounter);
                clients.put(clientCounter, new Client(client, new Thread(() -> {
                    while(true){
                        try{
                            DataInputStream input = new DataInputStream(finalClient.getInputStream());
                            if(input.available() == 0) continue;
                            packets.get(input.readInt()).processData(input, clientCounter, getServer());

                            try{
                                Thread.sleep(1000/ticks);
                            }catch(InterruptedException x){
                                //Thread wurde extern gestoptt (client entfernt)

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(int client){
        clients.get(client).stopThread();
        clients.remove(client);
    }

    public void stopServer(){

        for(Client client : clients.values()){
            client.stopThread();
        }

        thread.stop();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInt(int num, int... clientsTo){
        if(clientsTo.length == 0){
            for(Client client : clients.values()){
                try {
                    new DataOutputStream(client.getSocket().getOutputStream()).writeInt(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for(int cl : clientsTo){
                try {
                    new DataOutputStream(clients.get(cl).getSocket().getOutputStream()).writeInt(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendIntExcept(int num, int... clientsExcept){
        List<Integer> list = new ArrayList<>();
        for(int cl : clientsExcept){
            list.add(cl);
        }

        for(int nums : clients.keySet()){
            if(list.contains(nums)) continue;
            try {
                new DataOutputStream(clients.get(nums).getSocket().getOutputStream()).writeInt(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDouble(double num, int... clientsTo){
        if(clientsTo.length == 0){
            for(Client client : clients.values()){
                try {
                    new DataOutputStream(client.getSocket().getOutputStream()).writeDouble(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for(int cl : clientsTo){
                try {
                    new DataOutputStream(clients.get(cl).getSocket().getOutputStream()).writeDouble(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendDoubleExcept(double num, int... clientsExcept){
        List<Integer> list = new ArrayList<>();
        for(int cl : clientsExcept){
            list.add(cl);
        }

        for(int nums : clients.keySet()){
            if(list.contains(nums)) continue;
            try {
                new DataOutputStream(clients.get(nums).getSocket().getOutputStream()).writeDouble(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendString(String num, int... clientsTo){
        if(clientsTo.length == 0){
            for(Client client : clients.values()){
                try {
                    new DataOutputStream(client.getSocket().getOutputStream()).writeUTF(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for(int cl : clientsTo){
                try {
                    new DataOutputStream(clients.get(cl).getSocket().getOutputStream()).writeUTF(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendStringExcept(String num, int... clientsExcept){
        List<Integer> list = new ArrayList<>();
        for(int cl : clientsExcept){
            list.add(cl);
        }

        for(int nums : clients.keySet()){
            if(list.contains(nums)) continue;
            try {
                new DataOutputStream(clients.get(nums).getSocket().getOutputStream()).writeUTF(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendBoolean(boolean num, int... clientsTo){
        if(clientsTo.length == 0){
            for(Client client : clients.values()){
                try {
                    new DataOutputStream(client.getSocket().getOutputStream()).writeBoolean(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for(int cl : clientsTo){
                try {
                    new DataOutputStream(clients.get(cl).getSocket().getOutputStream()).writeBoolean(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendBooleanExcept(boolean num, int... clientsExcept){
        List<Integer> list = new ArrayList<>();
        for(int cl : clientsExcept){
            list.add(cl);
        }

        for(int nums : clients.keySet()){
            if(list.contains(nums)) continue;
            try {
                new DataOutputStream(clients.get(nums).getSocket().getOutputStream()).writeBoolean(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HashMap<Integer, Client> getClients() {
        return clients;
    }

}
