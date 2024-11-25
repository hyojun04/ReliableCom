package com.example.reliablecom.client_Source;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UDPReceive {

    private static final int PORT = 1996;
    private static final int BUFFER_SIZE = 1024;
    private static final int TOTAL_PACKETS = 61; // ��ü ��Ŷ �� (�ʿ信 �°� ����)
    private static final boolean MESSAGE_NUM = true;
    private static final boolean PACKET_NUM = false;
    //private JTextArea receivedMessagesArea;  // GUI�� receive message â
    
    private static int receive_message_num = 0;
    private volatile boolean newMessageReceived_udp = false;
    public static int receivedMessageNum = 1; //���� �ް� �ִ� �޽��� ��ȣ
    public static int array_index= 0;
    public static int ignored_bits = 0;
    private static int checkSerial; //���� UDP �޽����� �� ��° �޽������� ����
    //edit: Defined socket as a static 
    private static DatagramSocket socket;
    
    public byte[] checkNewMessage; // ���� ��Ŷ�� üũ�ϴ� �迭
    public byte[] lastMessage; // ���� �迭(�迭�� ��ȭ�� ������ ���� ack ����)
    
    
    // �����ڿ��� JTextArea ���� ����
    public UDPReceive() {
        //this.receivedMessagesArea = GUI.receivedMessagesArea;
    
    }
    
    public static int calculateBits(int total_packets, int mode) { //byte�迭�� ����ϱ� ������ ��Ŷ ���� 8�� ����� �ƴϸ� ������� �ʴ� bit�� ����
        if (mode == 0) {
            // mode�� 0�̸� byte �迭 �ε��� ���
        	return (total_packets + 7) / 8;
        } else if (mode == 1) {
            // mode�� 1�̸� ������ ���� ��Ʈ ���� ���
            return 8 - (total_packets % 8);
        } else {
            throw new IllegalArgumentException("Invalid mode: mode should be 0 or 1");
        }
    }
    
    //���� UDP �޽����� �� ��° �޽������� �����ϰ� �ִ� checkSerial ������ ����ϴ� �޼ҵ�
    public int Print_checkSerial() {
    	return checkSerial;
    }
    
    
    public static void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewMessage() {
        return newMessageReceived_udp;
    }
    
    public void resetNewMessageFlag() {
        newMessageReceived_udp = false;
    }
    // getLeading�� true�� �ָ� "_"�� �������� ���� ���ڸ�, false�� �ָ� ���� ���ڸ� return��
    public static int extractNumberPart(String input, boolean getLeading) {
        // ���Խ��� ����Ͽ� ���ڿ� `_`�� �������� ���ڿ��� �и�
        String[] parts = input.split("_");

        if (parts.length == 2) {
            String leadingNumber = parts[0].replaceAll("\\D", ""); // �պκ� ���ڸ� ����
            String trailingNumber = parts[1].replaceAll("\\D", ""); // �޺κ� ���ڸ� ����
           
            // getLeading�� true�� ��� �պκ� ���� ��ȯ, false�� ��� �޺κ� ���� ��ȯ
            String numberString = getLeading ? leadingNumber : trailingNumber;
           
            // �� ���ڿ��� ó���Ͽ� int�� ��ȯ
            return numberString.isEmpty() ? 0 : Integer.parseInt(numberString);
        } else {
            // ������ ���� ���� ��� 0 ��ȯ
            return 0;
        }
    }


    public void startServer() {
        socket = null;
        array_index = calculateBits(TOTAL_PACKETS, 0);
        ignored_bits = calculateBits(TOTAL_PACKETS, 1);
        //��Ŷ ���� �´� �迭 ����
        checkNewMessage = new byte[array_index]; // �׽�Ʈ�ϱ� ���� �̸� ����
        
        
        //byte �迭 �ʱ�ȭ(�����ؾ��� ��Ʈ���� ��� 1��)
        for(int i=array_index*8 - ignored_bits+1 ; i<= array_index*8; i++){
        	SetNewMsgBit(i);     	
        	} 
        //lastMessage = new byte[array_index];
        lastMessage = checkNewMessage.clone();
        
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started on port " + PORT + ". Waiting for messages...");

            // ���� ������ �޽��� ��� ����
            while (true) {
                try {
                    // ���� ����
                    byte[] buffer = new byte[BUFFER_SIZE];

                    // ������ ��Ŷ ����
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    // ������ ����
                    socket.receive(receivePacket);

                    // ���ŵ� ������ ó��
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // �۽��� IP �ּ� ��������
                    InetAddress senderAddress = receivePacket.getAddress();
                    String senderIP = senderAddress.getHostAddress();

                    // ���� �ð��� hh:mm:ss.SSS �������� ��������
                    String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

                    // �޽����� �պκ� 10���ڸ� �߶� ǥ��
                    String truncatedMessage = receivedMessage.length() > 10
                            ? receivedMessage.substring(0, 10)
                            : receivedMessage;
                    
                    //int before =0;
                    // ���� �޽��� GUI�� ǥ��
                    receive_message_num++;
              
                    //receivedMessagesArea.append("[" + receive_message_num + "] Received UDP message from " + senderIP + ": " + truncatedMessage + " [" + timeStamp + "]\n");
                    
                    System.out.println("I got Message: " + truncatedMessage);

                    // �޽��� ��ȣ�� ��Ŷ ��ȣ ����
                    int message_num = extractNumberPart(truncatedMessage,MESSAGE_NUM);
                    int packet_num = extractNumberPart(truncatedMessage,PACKET_NUM);
                    
                    
                    if (receivedMessageNum == message_num) { // �´� �޽��� ��ȣ�� ����
                        
               
                    	//���� ��Ŷ ��ȣ�� �´� �迭�� index�� set
                    	SetNewMsgBit(packet_num);
                                                    
                    } 
                    else {
                        System.out.println("Received wrong message");
                    }

                   

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in received message: " + e.getMessage());
                } catch (SocketException e) {
                    System.out.println("Socket error occurred: " + e.getMessage());
                    break; // ���Ͽ� ������ ����� ������ Ż���Ͽ� ������ �ߴ��մϴ�.
                } catch (SecurityException e) {
                    System.out.println("Security exception: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal argument: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error while receiving data: " + e.getMessage());
                    e.printStackTrace(); // �߰����� ���� �α׸� ����Ͽ� ������ �� ��Ȯ�� �ľ��� �� �ְ� �մϴ�.
                }
            }
        } catch (SocketException e) {
            System.out.println("Failed to bind UDP socket to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Server startup error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed.");
            }
        }
    }

    public void startConnect_to_tcp() {
        new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(PORT);
                System.out.println("UDP Server started on port " + PORT + ". Waiting for connect to TCP...");

                // 메시지를 저장할 버퍼 생성
                byte[] buffer = new byte[BUFFER_SIZE];

                // 수신 패킷 생성
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                // 클라이언트 메시지를 기다림 (수신)
                socket.receive(receivePacket);

                // 클라이언트의 IP 주소 가져오기
                InetAddress senderAddress = receivePacket.getAddress();
                String senderIP = senderAddress.getHostAddress();

                // 유효한 IP 주소일 경우 출력
                if (senderIP != null && !senderIP.isEmpty()) {
                    System.out.println("This is serverIP: " + senderIP);
                } else {
                    System.out.println("No server IP received.");
                }

            } catch (SocketException e) {
                System.out.println("Failed to bind UDP socket to port " + PORT + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Server startup error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // 소켓을 닫아 리소스를 정리
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("UDP Server socket closed.");
                }
            }
        }).start(); // 스레드 시작

    }


    // ������ ��Ŷ ��ȣ�� �´� bit�� set ��Ŵ
    public void SetNewMsgBit(int packet_num) {
            // packet_num�� ��ġ�� �ش��ϴ� ��Ʈ�� ����(0��° ��Ʈ���� ä��)
            int byteIndex = (packet_num-1) / 8;   // �ش� ��Ʈ�� ���� ����Ʈ �ε���
            int bitIndex = (packet_num-1) % 8;    // �ش� ����Ʈ ���� ��Ʈ ��ġ

            // �ش� ����Ʈ ������ bitIndex ��ġ�� ��Ʈ�� 0���� 1���� Ȯ��
            if ((checkNewMessage[byteIndex] & (1 << bitIndex)) == 0) {
                // ��Ʈ�� 0�̶�� 1�� ����
            	checkNewMessage[byteIndex] |= (1 << bitIndex);
                System.out.println("Set checkNewMessage[" + packet_num + "]:");
                UDPCheckThread.printByteArrayAsBinary(checkNewMessage); //�迭 ���    
            } else {
                // �̹� ��Ʈ�� 1�� ���
                System.out.println("checkNewMessage[" + packet_num + "] is already set to 1.");
              }
           }
    
    //Server���� Reset ��û�� �� ��
    public static void closeUDPbyReset() {
    	socket.close();
    }
  }            
        




