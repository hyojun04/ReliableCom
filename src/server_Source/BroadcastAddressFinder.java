package server_Source;
import java.net.*;
import java.util.Enumeration;

public class BroadcastAddressFinder {

    public static InetAddress getWiFiBroadcastAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            //Enumeration�� hasMoreElements() �޼ҵ� ����ϱ����� ����Ÿ�� ���
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                
                // "Wi-Fi" ����� �̸��� ���� Ȱ��ȭ�� �������̽��� ã��
                if (networkInterface.isUp() && !networkInterface.isLoopback() 
                		&& networkInterface.getDisplayName().contains("Wi-Fi")) {
                	System.out.println(networkInterface.getDisplayName()); 
                	
                    for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                    	//IP�ּҷκ��� ��ε�ĳ��Ʈ �ּҸ� �޾ƿ�
                    	InetAddress broadcast = address.getBroadcast();
                        if (broadcast != null) {
                            return broadcast; // ��ε�ĳ��Ʈ �ּ� ��ȯ
                        }
                    }
                  }
                
            }
        } catch (SocketException e) {
            System.out.println("Error retrieving network interfaces: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // "Wi-Fi" ������� ��ε�ĳ��Ʈ �ּҸ� ã�� ���� ���
    }
}