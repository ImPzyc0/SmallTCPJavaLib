package com.tcptest.client;

import java.io.DataInputStream;

public interface ReceiveDataClient {

    void processData(DataInputStream input, Client client);

}
