package server_Source;

import main.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import javax.swing.JTextArea;

public class TcpConnectionAccepter implements Runnable {
    private static final int PORT = 8189; // ������ ��Ʈ
    private ServerSocket serverSocket;
    
    private JTextArea receivedMessagesArea;
    private JTextArea consoleArea;
    private Thread clientHandlerThread;
    
    // ��� ClientHandler �ν��Ͻ��� ������ ����Ʈ -> ClientInfo�� clientHandler�� �ִ°� ���� ��
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    public TcpConnectionAccepter(JTextArea receivedMessagesArea, JTextArea consoleArea) {
        this.receivedMessagesArea = receivedMessagesArea;
        this.consoleArea = consoleArea;
    }
    

    public void run() {
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");

            // Ŭ���̾�Ʈ ������ ����ϸ鼭, �� ���ῡ ���� ���ο� �����带 ����
            
            
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ����
                
                /*UDP broad�� Server�� IP �����ϴ� ��Ŀ���� �߰�*/
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Client connected: " + clientIP);
                consoleArea.append("Client connected: " + clientIP+"\n");
              
                // �� Ŭ���̾�Ʈ�� ���� ���ο� �ڵ鷯 �����带 ����
                TcpConnectionManager.addClient(clientSocket.getInetAddress().getHostAddress(),clientSocket,true,false);                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this, receivedMessagesArea);
                //clientHandler��ü�� ��Ƶδ� ����Ʈ ���� -> ��� ��ü�� �ݳ��ϱ����ؼ� ����
                clientHandlers.add(clientHandler);
                clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
                
                
                	
                
            }
           
            
            
        
        } catch (IOException e) {
        	if(Thread.currentThread().isInterrupted()) {
            	//Interrupt�� �߻��ϸ� while�� ����
            	//�����尡 �ױ��� ��� clientHandler�� �����Ų��.
            	System.out.println("Program is clear by Thread Interruption");
            	killAllClientHandlers();  
            	
            	
            }
            e.printStackTrace();            
        }
        
    }
 // ��� ClientHandler�� stopHandler�� ȣ���Ͽ� ����
    private void killAllClientHandlers() {
        for (ClientHandler handler : clientHandlers) {
        	System.out.println("Handler of index "+ handler.permanent_id + " is killed");
            handler.stopHandler();
        }
        clientHandlers.clear(); // ����Ʈ�� �����
    }
    
    
    public void closeTcpSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("TCP socket closed successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error closing TCP socket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    
    

    public class ClientHandler implements Runnable {
        private final TCPReceive receiverTcp;
        public int permanent_id;
        private Socket clientSocket;
        
        
        private AckCheck tcpCheck;
        
        
        public ClientHandler(Socket clientSocket, TcpConnectionAccepter tcpAccepter, JTextArea receivedMessagesArea) {
			this.clientSocket = clientSocket;

			// �ε����� �����ϰ�, ����� Ŭ���̾�Ʈ�� ó���� �� �ֵ��� ����
			permanent_id = Main.clients_tcp_index;
			System.out.println("Client index: " + permanent_id + " is added");
			// TcpConnectionManager.addClient(clientSocket.getInetAddress().getHostAddress(),clientSocket,true,false);
			Main.clients_tcp_index++;
			System.out.println("Client: " + clientSocket.getInetAddress() + " is connected by TCP" + " & index: "
					+ (Main.clients_tcp_index - 1));

			this.receiverTcp = new TCPReceive(clientSocket, this, receivedMessagesArea);
        }

        @Override
        public void run() {
        	
            try {
            	
                // TCP Ack ����  ����
                receiverTcp.startReceiving();

                
                
            }catch (IOException e) {
                System.out.println("IOException in ClientHandler: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Exception in ClientHandler: " + e.getMessage());
                e.printStackTrace();
                
            } finally {
                // while���� ���������� Handler�� ������
                
                stopTCPCheckThread();
                stopHandler();
            }
            
            
            
        }

        // ClientHandler �����带 �����ϴ� �޼ҵ� 
        public void stopHandler() {
            
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Client socket closed for client: " + clientSocket.getInetAddress());
                }
            }catch (ClosedByInterruptException e) {
            	System.out.println("Thread ends by Interruption");
            }catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            } 
        }
        public void stopTCPCheckThread() {

        	tcpCheck.stopChecking();
        }
    }

}
