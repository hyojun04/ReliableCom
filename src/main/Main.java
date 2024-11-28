package main;
import server_Source.*;
import client_Source.*;
import GUI.GUI;


import java.util.Timer;



import java.util.TimerTask;





public class Main {
	/*Server*/
	public static ClientManager clientManager = new ClientManager();
	public static int clients_tcp_index = 0; // ���ڸ޽����� �迭�� �ε���
	
    private static UDPBroadcastSend sender_udp;
    private static TcpConnectionAccepter tcp_accepter;
    private static Timer udpTimer_IP; //SETUP ������ ���� UDP broadcast�� ���� Ÿ�̸�
    private static Thread senderThread;// UDP ������ ���� Ÿ�̸�
    private static Thread accepterThread;
    public static int SIZE = 61440;
    /*Client*/
    public static UDPReceive receiver_udp;
    private static TcpSocketConnection tcp_connection;
    
	private Main() {
		
	}
	
	
	public static void ComSetup () {
		//������ ����: ���Ľ�����ó���� ���������� socket.accept�ϴ� �κп��� ���߰� �ȴ�.
    	if (tcp_accepter == null) {
    		tcp_accepter = new TcpConnectionAccepter();
    		accepterThread = new Thread(tcp_accepter);
   		 	accepterThread.start();
            GUI.consoleArea.append("TCP Connection Accepter thread started.\n");
    	}
    	else {
    		GUI.consoleArea.append("TCP Connection Accepter thread already running.\n");
    	}
        
        String broadIP = GUI.inputIp_udpBroad.getText();
        
        
        //TCP ������ ����, UDP Broad ����, stopUDPsend ��ư ������ �۽� ���� -> ���� ����Ǹ� ���ߵ����ϴ� ��Ŀ�������� ����
        /*
        UDPBeaconbroadcast sender_udp_ip = new UDPBeaconbroadcast();
        udpTimer_IP = new Timer();
        udpTimer_IP.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	sender_udp_ip.startSend(broadIP);
            }
        }, 0, 500); // 500ms �������� ����

        
        GUI.consoleArea.append("Connection Setup Ready \n");
        */
	}
	
	
	public static void StopSetup() {
		if (udpTimer_IP != null) {
            udpTimer_IP.cancel();  // Ÿ�̸� ����
            udpTimer_IP = null;    // Ÿ�̸� ��ü�� null�� �����Ͽ� ���� �ʱ�ȭ
            GUI.consoleArea.append("SETUP�� �����Ǿ����ϴ�.\n");
        }
	}

	public static void ComBroadcastSend () {
		//byte[] Data,int Size,int Option ���� ������Ʈ
        
        String serverIP = GUI.inputIp_udpBroad.getText();
        if(sender_udp == null && senderThread == null) {
        	sender_udp = new UDPBroadcastSend(serverIP);
            //sender_udp ������ȭ
            senderThread = new Thread(sender_udp);
            senderThread.start();
        }
        else {
        	GUI.consoleArea.append("UDP �޽��� �̹� �۽� ��..\n");
        }
        
        
	} 
	public static void StopBroadcastSend() {
		if (sender_udp != null) {
        	senderThread.interrupt();  // Ÿ�̸� ����
            senderThread = null;    // Ÿ�̸� ��ü�� null�� �����Ͽ� ���� �ʱ�ȭ
            sender_udp = null;
            GUI.consoleArea.append("UDP �޽��� ������ �����Ǿ����ϴ�.\n");
        }
	}

	public static void ComReset() {
		//Client List���� ���  Client ��ü ����
    	//System.out.println("Reset Program completed 1");
    	if (tcp_accepter != null) {
    		tcp_accepter.closeTcpSocket();
            accepterThread.interrupt();
          //tcp_accepter null�� �ʱ�ȭ �� ��� �޼��� â �ʱ�ȭ
            tcp_accepter = null;
    	}
    	
        //System.out.println("Reset Program completed 2");
        GUI.consoleArea.setText("Reset Program");
        GUI.receivedMessagesArea.setText("");
        GUI.sendMessageArea.setText("");
        //Client �ʱ�ȭ
        clientManager.AllClientsReset();
        clients_tcp_index = 0;
        System.out.println("TcpConnectionAccepter sets null");
        //Udp sender �ʱ�ȭ
        UDPBroadcastSend.resetCount();
        if(senderThread != null&& sender_udp != null) {
        	senderThread = null;
            sender_udp = null;
        }
        
	}
	
	/*   The methods are necessary by client */
	public static void ComSetupResponse () {
		tcp_connection = new TcpSocketConnection();
        receiver_udp = new UDPReceive();
        String serverIP = receiver_udp.startConnect_to_tcp();
        tcp_connection.startClient(serverIP);
        GUI.consoleArea.append("Client: "+serverIP+"�� TCP ���ϰ� ����Ǿ����ϴ�. \n");
	}

	public static void ComReceive() {
		receiver_udp = new UDPReceive();
        new Thread(() -> receiver_udp.startServer()).start();
        GUI.consoleArea.append("UDP ���� ��� ��...\n");
        //UDP Broad�޽����� �����Ͽ��� üũ�ϴ� ������ ���� 
        UDPCheckThread udpCheckThread = new UDPCheckThread(receiver_udp,tcp_connection);
        Thread udpCheck = new Thread(udpCheckThread);
        udpCheck.start();
	}

}