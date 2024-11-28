package com.example.reliablecom.main;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.reliablecom.client_Source.*;



public class Main implements Runnable {

    /* Client */
    public static UDPReceive receiverUdp;
    private static TcpSocketConnection tcpConnection;
    private ImageView imageView; // ImageView 추가
    private TextView consoleArea;

    public Main(ImageView imageView, TextView consoleArea) {
        this.imageView = imageView; // ImageView를 전달받음
        this.consoleArea = consoleArea;
    }

    @Override
    public void run() {
        ComReceive(); // 네트워크 소켓 프로그래밍 호출
    }

    /*   The methods are necessary by client */

    public static void ComSetupResponse () {
        //tcp_connection = new TcpSocketConnection();
        //receiver_udp = new UDPReceive();
        String serverIP = "192.168.10.40";

        //GUI.consoleArea.append("Client: "+serverIP+"   TCP    ϰ      Ǿ    ϴ . \n");
    }

    public void ComReceive() {
        String serverIP = "192.168.10.40";
        tcpConnection = new TcpSocketConnection();
        tcpConnection.startClient(serverIP);
        receiverUdp = new UDPReceive(consoleArea);
        new Thread(() -> receiverUdp.startServer()).start();

        // UDPCheckThread 생성 및 실행
        UDPCheckThread udpCheckThread = new UDPCheckThread(receiverUdp, tcpConnection, imageView);
        new Thread(udpCheckThread).start();
    }
}


