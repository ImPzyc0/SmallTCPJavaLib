package com.tcptest.server;

import java.net.Socket;

class Client {

    private final Thread thread;
    private final Socket socket;

    public Client(Socket socket, Thread thread){
        this.socket = socket;
        this.thread = thread;

        thread.start();
    }

    public void stopThread(){
        if(thread.isAlive()) thread.stop();
    }

    public Socket getSocket(){
        return socket;
    }
}
