package com.tcptest.server;

import java.io.DataInputStream;

public interface ReceiveDataServer {

    void processData(DataInputStream input, int client, Server server);

}
