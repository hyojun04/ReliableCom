package server_Source;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

public class TCPReceive {
    /* �޽����� �ޱ⸸ �ϴ� ��� ���� */
    private Socket socket;
    private final TcpConnectionAccepter.ClientHandler handler;
    
    private static final int TOTAL_PACKETS = 61;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private volatile boolean newAckReceived_tcp = false; // ���� �޽��� ���� ����
    private int receive_message_num = 0;
    
    public int array_index =0;
    public int ignored_bits =0;

    //public byte[] checkNewMessage;
    // �����ڿ��� JTextArea ���� ����
    
    public static int calculateBits(int total_packets, int mode) {
    	if (mode == 0) {
    		//mode�� 0�̸� byte �迭 �ε��� ���
    		return (total_packets+7)/8;
    	}else if(mode ==1) {
    		return 8-(total_packets % 8);
    	}else {
    		throw new IllegalArgumentException("Invalid mode: mode should be 0 or 1");
    	}
    }
    
    
    
    public TCPReceive(Socket socket,TcpConnectionAccepter.ClientHandler clientHandler ,JTextArea receivedMessagesArea) {
        this.socket = socket;
        handler = clientHandler;
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    
    
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewEchoMessage() {
        return newAckReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newAckReceived_tcp = false;
    }
    //������ ����Ʈ �迭�� �޴� �޼ҵ� 
    /*
    public void startReceiving() throws IOException { //���� throw�Ͽ� ClientHandler���� ó���ϵ�����
    	
    	StartTCPCheck startCheck = new StartTCPCheck(this, handler);
    	
    	
        BufferedReader in = null;
        PrintWriter out = null;
        // ������ ������ ���� DataInputStream ���
        DataInputStream dataInputStream = null;
        array_index = calculateBits(TOTAL_PACKETS,0);
        ignored_bits = calculateBits(TOTAL_PACKETS,1);
        checkNewMessage = new byte[array_index];
        try {
            // BufferedReader�� ����Ͽ� �����͸� �ۼ���
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            dataInputStream =new DataInputStream(socket.getInputStream());
            
            
            
            
            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            
            
            while (!socket.isClosed()) {//Ŭ���̾�Ʈ���� ������ �����ϸ鼭 �޽����� ���������� ����
                // ���ŵ� �޽��� ó��
            	// byte �迭 ����
                int byteArrayLength = dataInputStream.readInt(); // ���� ������ �迭�� ���̸� ����
                byte[] receivedData = new byte[byteArrayLength];
                dataInputStream.readFully(receivedData); // byte �迭 ����
                String receivedMessage = dataInputStream.readUTF();
                checkNewMessage = receivedData;
            	//Ŭ���̾�Ʈ�κ��� ���� ����Ʈ�迭�� append 
            	StringBuilder temp = printByteArrayAsBinary(checkNewMessage);
            	receive_message_num++;
                receivedMessagesArea.append("[" + receive_message_num + "] ���ŵ� �޽��� from " + clientIP + ": " +temp +receivedMessage +"\n" );
                //newAckReceived_tcp = true; // ���� �޽����� �޾��� ���
                System.out.println("newAckMessage was coming");
                
                //notify() ��� checking�޼ҵ� ȣ��
                startCheck.startChecking();
                
                	
                
            }
        } finally {
            // Exception�� throw�Ͽ� �ܺο��� ó���ϵ��� ��.
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP ������ �������ϴ�.");
        }
    }*/
    public void startReceiving() throws IOException { // ���ܸ� throw�Ͽ� ClientHandler���� ó���ϵ��� ����
        AckCheck startCheck = new AckCheck(this, handler);

        DataInputStream dataInputStream = null; // ������ ������ ���� DataInputStream
       

        try {
            // DataInputStream �ʱ�ȭ
            dataInputStream = new DataInputStream(socket.getInputStream());

            String clientIP = socket.getInetAddress().getHostAddress(); // Ŭ���̾�Ʈ IP ��������
            System.out.println("Server_TCP is open");

            while (!socket.isClosed()) { // Ŭ���̾�Ʈ�� ������ �����Ǵ� ���� �޽��� ����
                // **1��Ʈ ������ ����**
                byte receivedBit = dataInputStream.readByte(); // 1��Ʈ�� ���� , �ڹٿ����� 1��Ʈ�� ������ ���� �Ұ� 1byte�� ������ ��
                boolean receivedBoolean = (receivedBit == 1); // 1 -> true, 0 -> false�� ��ȯ

                // **UTF �޽��� ����**
                String receivedMessage = dataInputStream.readUTF();

                

                // **���� �޽��� ���**
                receive_message_num++;
                receivedMessagesArea.append("[" + receive_message_num + "] ���ŵ� �޽��� from " + clientIP + 
                                            ": " + receivedBoolean + " - " + receivedMessage + "\n");

                System.out.println("���ο� Ȯ�� �޽����� ���ŵǾ����ϴ�: " + receivedBoolean);

                //��� ����Ʈ�迭�� 1�̾��ٸ�, StartTCPCheck�� checking �޼��� ȣ��
                if(receivedBoolean) {
                	startCheck.startChecking();
                }
                
            }
        } finally {
            // �ڿ� ����
            if (dataInputStream != null) dataInputStream.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP ������ �������ϴ�.");
        }
    }

    private StringBuilder printByteArrayAsBinary(byte[] byteArray) {
    	StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            // �� ����Ʈ�� 0�� 1�� ��ȯ
        	// 16���� 1111 1111�� &����
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); // ��ȯ�� ������ ���
            binaryStringBuilder.append(binaryString).append(" ");
            
        }
        return binaryStringBuilder;
    }
    
    
   

}
