package server_Source;

import main.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JTextArea;

public class UDPBroadcastSend implements Runnable  {
    private static final int PORT = 1996;
    private static final int PACKET_SIZE = 1024; // ���� ��Ŷ ũ�� (1KB)
    private String serverIP;
    private JTextArea sendMessageArea;
    private JTextArea consoleArea;
    //edit: Defined count as a static  
    private static int sentMessageCount = 0;       // �޽����� ��ȣ 
    private static int sentMessageCount_actual = 0; //���� ���� �޽��� ī��
    private int messageSize;
    private DatagramSocket socket = null;
    
    public UDPBroadcastSend(String serverIP, JTextArea sendMessageArea,JTextArea consoleArea, int messageSize) {
    	this.serverIP = serverIP;
    	this.sendMessageArea = sendMessageArea; 
    	this.consoleArea = consoleArea;
    	this.messageSize = messageSize; // messageSize�� �������� �ϴ� �޽����� ũ��(60KB�� ����)
    }
    /*
    public void startSend(String serverIP, int messageNum, int messageSize) { 
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //System.out.println("UDP is connected.");

            // ������ ũ���� ���ӵ� "A" ���� ����
            StringBuilder messageBuilder = new StringBuilder(messageSize);
            for (int i = 0; i < messageSize; i++) {
                messageBuilder.append('A');
            }
            String fullMessage = messageBuilder.toString(); // ��ü �޽��� ����
            
            // �޽����� ����Ʈ �迭�� ��ȯ
            byte[] messageBytes = fullMessage.getBytes();
            int offset = 0;
            int packetCount = 0; // ��Ŷ ��ȣ ī���� �߰�

            // �޽����� 1024����Ʈ�� �ʰ��� ���, 1024����Ʈ ������ �����Ͽ� ����
            while (offset < messageBytes.length) { 
            	// �� ��Ŷ�� ���� 1024byte(-10�� ��� ����)�� (�޽��� ��ü ���� - offset)�� �ּڰ�
                int length = Math.min(PACKET_SIZE-10, messageBytes.length - offset); 
                byte[] buffer = new byte[length+10]; // ��Ŷ ��ȣ�� ������ ����(���) �߰�
                
                // ��Ŷ ��ȣ�� ����� ����
                String packetHeader = messageNum + "_" + (packetCount + 1); // 1���� �����ϴ� ��Ŷ ��ȣ
                byte[] headerBytes = packetHeader.getBytes();
                System.arraycopy(headerBytes, 0, buffer, 0, headerBytes.length); // ����� ���ۿ� ����
                
                // �迭 ����(������ ���� �迭, ���� �迭���� ���縦 ������ �ε���, ������ ��� �迭, ��� �迭���� �����͸� ������ ���� �ε���, ������ ������ ����)
                System.arraycopy(messageBytes, offset, buffer, headerBytes.length, length); 
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
                socket.send(packet);

                offset += length;
                packetCount++; // ��Ŷ ��ȣ ����
                //System.out.println(packetCount + "��° ��Ŷ ���� �Ϸ�"); // ��Ŷ ���� �Ϸ� �޽��� ���
            }

            //System.out.println("�޽����� ���۵Ǿ����ϴ�.");

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }*/

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
        
        try {
        		// ������ �ð� ���� ���
            	long interval = 50;
            	
            	while(true) {
            		if (Main.tcpconnectionManager.checkAllClientsNewMessage()) {
                    	
                        consoleArea.append("��� Ŭ���̾�Ʈ�κ��� "+"[" + sentMessageCount + "]�� ���� �޽����� �޾����Ƿ� ��ε�ĳ��Ʈ ����\n");                            
                        Main.tcpconnectionManager.AllClientsSetFalse(); //���ڸ޽��� ���ſ��� �ʱ�ȭ 
                        sentMessageCount++; // ���� �޽��� ī��Ʈ ����
                        
                    }
                   if (sentMessageCount == 0) sentMessageCount++; // ù �޽��� �߼۶��� ī��Ʈ ���� 
                   
                   		startSend();   // 50ms���� UDP �޽��� ����
                    
                    // sendMessageArea�� ������ �޽��� �߰�
                    sentMessageCount_actual++;
                    sendMessageArea.append("[" + sentMessageCount_actual +"][" +sentMessageCount + "] message via UDP: 'A' * 61440 bytes\n");
                 
                    
                    Thread.sleep(interval);
            	}
                
                
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }

            //System.out.println("�޽����� ���۵Ǿ����ϴ�.");

          
    
		
	}
    
	
	
	public void startSend() throws Exception { // messageSize�� �������� �ϴ� �޽����� ũ��(60KB�� ����)
        

        
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //System.out.println("UDP is connected.");

            // ������ ũ���� ���ӵ� "A" ���� ����
            StringBuilder messageBuilder = new StringBuilder(messageSize);
            for (int i = 0; i < messageSize; i++) {
                messageBuilder.append('A');
            }
            String fullMessage = messageBuilder.toString(); // ��ü �޽��� ����
            
            // �޽����� ����Ʈ �迭�� ��ȯ
            byte[] messageBytes = fullMessage.getBytes();
            int offset = 0;
            int packetCount = 0; // ��Ŷ ��ȣ ī���� �߰�

            // �޽����� 1024����Ʈ�� �ʰ��� ���, 1024����Ʈ ������ �����Ͽ� ����
            while (offset < messageBytes.length) { 
            	// �� ��Ŷ�� ���� 1024byte(-10�� ��� ����)�� (�޽��� ��ü ���� - offset)�� �ּڰ�
                int length = Math.min(PACKET_SIZE-10, messageBytes.length - offset); 
                byte[] buffer = new byte[length+10]; // ��Ŷ ��ȣ�� ������ ����(���) �߰�
                
                // ��Ŷ ��ȣ�� ����� ����
                String packetHeader = sentMessageCount + "_" + (packetCount + 1); // 1���� �����ϴ� ��Ŷ ��ȣ
                byte[] headerBytes = packetHeader.getBytes();
                System.arraycopy(headerBytes, 0, buffer, 0, headerBytes.length); // ����� ���ۿ� ����
                
                // �迭 ����(������ ���� �迭, ���� �迭���� ���縦 ������ �ε���, ������ ��� �迭, ��� �迭���� �����͸� ������ ���� �ε���, ������ ������ ����)
                System.arraycopy(messageBytes, offset, buffer, headerBytes.length, length); 
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
                socket.send(packet);

                offset += length;
                packetCount++; // ��Ŷ ��ȣ ����
                //System.out.println(packetCount + "��° ��Ŷ ���� �Ϸ�"); // ��Ŷ ���� �Ϸ� �޽��� ���
            }

            //System.out.println("�޽����� ���۵Ǿ����ϴ�.");

          
    }
	public static void resetCount () {
		sentMessageCount = 0;
		sentMessageCount_actual =0;
	}
    
}
