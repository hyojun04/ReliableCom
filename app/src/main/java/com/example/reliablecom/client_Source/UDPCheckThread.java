package com.example.reliablecom.client_Source;
import java.util.Arrays;
public class UDPCheckThread implements Runnable {
    private final UDPReceive receiver_udp;
    private final TcpSocketConnection tcpConnection;

    public UDPCheckThread(UDPReceive receiver_udp, TcpSocketConnection tcpConnection) {
        this.receiver_udp = receiver_udp;
        this.tcpConnection = tcpConnection;
        
    }

    @Override
    public void run() {
        while (true) {
            
        	try {

                if(!Arrays.equals(receiver_udp.checkNewMessage, receiver_udp.lastMessage)) { // checkNewMessage �迭�� ��ȭ�� ������ ���� ack ����
                	
                	
                	//byte�迭�� Ack�޽����� �����Եȴ�. sendAckMessage�� �����ǵǾ�����
                	//tcpConnection.sendAckMessage(receiver_udp.checkNewMessage);
                	             
                	//printByteArrayAsBinary(receiver_udp.checkNewMessage); // ���� ack���� ���           	

                	// �迭�� ��� ��Ʈ�� 1���� Ȯ��
                	boolean allBitsOne = true;
                	for (byte b : receiver_udp.checkNewMessage) {
                	    if (b != (byte) 0xFF) { // ���� �� ����Ʈ�� 0xFF�� �ƴ϶��
                	        allBitsOne = false;
                	        break;
                	    }
                	}

                	if (allBitsOne) {
                	    System.out.println("all packet received");
                	    /*��� 1�̸� boolean�� Ack�޽����� �������� ����*/
                	    //true or false �� ����
                	    tcpConnection.sendAckMessage_alltrue(allBitsOne);
                	    
                	    //��ü Ÿ������ ����
                	    //tcpConnection.sendAckObject(receiver_udp.checkNewMessage);
                	    
                	    
                	    Arrays.fill(receiver_udp.checkNewMessage, (byte) 0); // checkNewMessage �迭�� 0���� �ʱ�ȭ
                	    //byte �迭 �ʱ�ȭ(�����ؾ��� ��Ʈ���� ��� 1��)
                        for(int i=UDPReceive.array_index*8 - UDPReceive.ignored_bits+1 ; i<= UDPReceive.array_index*8; i++){
                        	receiver_udp.SetNewMsgBit(i);     	
                        	} 
                        
                       
                	    UDPReceive.receivedMessageNum++; // ���� �޽����� ���� �غ�
                	    System.out.println("receivedMessageNum: " + UDPReceive.receivedMessageNum);
                	}
                	
                	// �迭�� ������ �����Ͽ� lastMessage�� ����
                	receiver_udp.lastMessage = Arrays.copyOf(receiver_udp.checkNewMessage, receiver_udp.checkNewMessage.length);
                
                
                	
                
                }
                
                // ������ �ð� ���� ���
                long interval = 50;
                
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // �����尡 �ߴܵǾ��� �� ���� ó��
                System.out.println("Thread was interrupted");
                break;
            }
        }
    }
    public static void printByteArrayAsBinary(byte[] byteArray) {
        for (byte b : byteArray) {
            // �� ����Ʈ�� 0�� 1�� ��ȯ
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); // ��ȯ�� ������ ���
        }
    }
}