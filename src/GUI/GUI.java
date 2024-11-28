package GUI;

import main.*;
import server_Source.BroadcastAddressFinder;
import server_Source.UDPBeaconbroadcast;
import server_Source.UDPBroadcastSend;

import javax.swing.*;

import client_Source.UDPReceive;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;


public class GUI extends JFrame {

    public static JTextArea receivedMessagesArea;
    public static JTextArea sendMessageArea;
    public static JTextArea consoleArea;
    private JButton connection_Button;
    private JButton sendButton_UDP;
    private JButton sendStopButton_UDP;
    private JButton connectionSetup_Button;
    private JButton receiveButton_UDP;
    private JButton clearReceiveButton;
    private JButton clearSendButton;
    private JButton stopSetup_Button;
    private JButton resetProgram;
    
    
    
    public static JTextField inputIp;
    public static JTextField inputIp_udpBroad;
    
   
    
    

    
    public GUI() {

    	
        // GUI �⺻ ����
        setTitle("Reliable Com");
        setSize(1300, 600); // ũ�⸦ ���� �� �÷���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ���� �޽��� ���� (�� ���� ��ġ)
        receivedMessagesArea = new JTextArea();
        receivedMessagesArea.setEditable(false);
        receivedMessagesArea.setLineWrap(true);
        receivedMessagesArea.setWrapStyleWord(true);
        JScrollPane receivedScrollPane = new JScrollPane(receivedMessagesArea);
        receivedScrollPane.setBorder(BorderFactory.createTitledBorder("Received Messages"));

        // Clear ��ư �߰� (���� �޽���)
        clearReceiveButton = new JButton("Clear Received Messages");
        clearReceiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receivedMessagesArea.setText(""); // ���� �޽��� â�� �ؽ�Ʈ �ʱ�ȭ
                //edit
                UDPReceive.reset_message_num(); // ���� �޽��� ī���� �ʱ�ȭ
                
            }
        });

        // ���� �޽��� �Է� ���� (�߰��� ��ġ)
        sendMessageArea = new JTextArea();
        sendMessageArea.setLineWrap(true);
        sendMessageArea.setWrapStyleWord(true);
        JScrollPane sendScrollPane = new JScrollPane(sendMessageArea);
        sendScrollPane.setBorder(BorderFactory.createTitledBorder("Send Message"));

        // Clear ��ư �߰� (���� �޽���)
        clearSendButton = new JButton("Clear Send Messages");
        clearSendButton.addActionListener(new ActionListener() {
            @Override
            
            //edit
            public void actionPerformed(ActionEvent e) {
                sendMessageArea.setText(""); // ���� �޽��� â�� �ؽ�Ʈ �ʱ�ȭ
                //edit
                UDPBroadcastSend.resetCount();
            }
        });

        // �ܼ� ���� (�� �Ʒ��� ��ġ)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));

        // ��ư ����
        connection_Button = new JButton("Connection");
        sendButton_UDP = new JButton("Send UDP Message");
        connectionSetup_Button = new JButton("Connection Setup");
        receiveButton_UDP = new JButton("Wait for UDP");
        sendStopButton_UDP = new JButton("Stop UDP Msg");
        stopSetup_Button = new JButton("Stop Connection Setup");
        resetProgram = new JButton("Reset Program");
        
        // TCP ������ IP �Է� �ʵ�
        inputIp = new JTextField("192.168.0.228", 15);
        //�ڵ����� WifiBroadAddress�� ã�� Ŭ���� ����
        //edit
      		InetAddress BroadcastAddress = BroadcastAddressFinder.getWiFiBroadcastAddress();
      		if (BroadcastAddress != null) {
      			String BroadIP = BroadcastAddress.getHostAddress();
      			System.out.println("Found IP: "+ BroadIP);
      			inputIp_udpBroad = new JTextField(BroadIP,15);//���α׷��� ������ڸ��� IP�� �о�鿩 Broadcast �ּҸ� �Է��صд�.
      			
      		}
       
        
        
        // ��ư�� �ؽ�Ʈ �ʵ带 ���� �г�
        JPanel buttonPanel_main = new JPanel(new FlowLayout());
        JPanel buttonPanel_1 = new JPanel(new FlowLayout());
        JPanel buttonPanel_2 = new JPanel(new FlowLayout());
        JPanel buttonPanel_3 = new JPanel(new FlowLayout());
        JPanel buttonPanel_4 = new JPanel(new BorderLayout());
        JPanel buttonPanel_5 = new JPanel(new BorderLayout());
        JPanel buttonPanel_6 = new JPanel(new BorderLayout());
        //���ο� ��ư�� ���� �г� ����
        JPanel buttonSmallPanel = new JPanel(new BorderLayout());
        
        buttonPanel_1.add(new JLabel("Client1 IP:"));
        buttonPanel_1.add(inputIp);
        buttonSmallPanel.add(buttonPanel_1,BorderLayout.NORTH);       
        buttonPanel_main.add(buttonSmallPanel);
        
        buttonPanel_2.add(new JLabel("Broad IP:"));
        buttonPanel_2.add(inputIp_udpBroad);
        buttonPanel_main.add(buttonPanel_2);
          
        buttonPanel_3.add(connection_Button);        
        //buttonPanel_main.add(buttonPanel_3);
        
        buttonPanel_4.add(connectionSetup_Button,BorderLayout.NORTH);
        buttonPanel_4.add(stopSetup_Button,BorderLayout.SOUTH);
        buttonPanel_main.add(buttonPanel_4);
        buttonPanel_5.add(sendButton_UDP,BorderLayout.NORTH);
        buttonPanel_5.add(sendStopButton_UDP,BorderLayout.SOUTH);
        //buttonPanel_5.add(receiveButton_UDP,BorderLayout.SOUTH);
        buttonPanel_main.add(buttonPanel_5);
               
        
        buttonPanel_6.add(resetProgram);	
        buttonPanel_main.add(buttonPanel_6);
        

        // ���� ���̾ƿ� ����
        setLayout(new BorderLayout());

        // ���� �޽��� + Clear ��ư�� ���� �г�
        JPanel receivedPanel = new JPanel(new BorderLayout());
        receivedPanel.add(receivedScrollPane, BorderLayout.CENTER);
        receivedPanel.add(clearReceiveButton, BorderLayout.SOUTH);

        // ���� �޽��� + Clear ��ư�� ���� �г�
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(sendScrollPane, BorderLayout.CENTER);
        sendPanel.add(clearSendButton, BorderLayout.SOUTH);

        // Center Panel: ��� â�� ������ ũ��� �����ϱ� ���� GridLayout
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        centerPanel.add(receivedPanel);  // ���� �޽��� â + Clear ��ư
        centerPanel.add(sendPanel);      // ���� �޽��� â + Clear ��ư
        centerPanel.add(consoleScrollPane);   // �ܼ� â

        add(centerPanel, BorderLayout.CENTER);      // �߾ӿ� 3���� â�� ���� ũ��� ��ġ
        add(buttonPanel_main, BorderLayout.SOUTH);        // �ϴܿ� ��ư �г� ��ġ
        
        
        
        // ���� ��ư �̺�Ʈ ó��
        connection_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Main.ComSetupResponse();
            }
        });
        //�ش� ��ư�� ������ �����ư�� ���� ���Ͽ����� ������ 
        
     // TCP ���Ͽ��� ��ư �̺�Ʈ ó��
        connectionSetup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComSetup();
            	}
        });
        //SETUP ���� ��ư
        stopSetup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.StopSetup();
            }
        });

        sendButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComBroadcastSend();
  
            }
        });
        // UDP ���� ���� ��ư
        sendStopButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.StopBroadcastSend();
            }
        });
        receiveButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComReceive();
            }
        });
        
        
        resetProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComReset();
            }
        });
        
    }
    
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}
