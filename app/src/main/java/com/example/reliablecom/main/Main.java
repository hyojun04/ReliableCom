package com.example.reliablecom.main;

import com.example.reliablecom.client_Source.*;





public class Main implements Runnable{

    /*Client*/
    public static UDPReceive receiver_udp;
    private static TcpSocketConnection tcp_connection;


    public void run() {
        ComReceive(); // 네트워크 소켓 프로그래밍 호출
    }

	
	/*   The methods are necessary by client */

	public static void ComSetupResponse () {
        //tcp_connection = new TcpSocketConnection();
        //receiver_udp = new UDPReceive();
        String serverIP = "192.168.0.233";

        //GUI.consoleArea.append("Client: "+serverIP+"�� TCP ���ϰ� ����Ǿ����ϴ�. \n");
	}

    public static void ComReceive() {
        // Define the server IP address
        String serverIP = "172.30.1.73";
        tcp_connection = new TcpSocketConnection();
        tcp_connection.startClient(serverIP);
        receiver_udp = new UDPReceive();
        new Thread(() -> receiver_udp.startServer()).start();

        //UDP Broad메시지를 수신하였지 체크하는 스레드 생성
        UDPCheckThread udpCheckThread = new UDPCheckThread(receiver_udp,tcp_connection);
        Thread udpCheck = new Thread(udpCheckThread);
        udpCheck.start();
    }

}
