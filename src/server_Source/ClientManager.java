package server_Source;

import java.net.Socket;
import java.util.ArrayList;

public class ClientManager {
	// ClienttInfo ArrayList�� ����
    public static ArrayList<ClientMember> clients_tcp = new ArrayList<>();

    // Method to add a client to the list
    public static void addClient(String ip,Socket mysocket ,Boolean connected ,Boolean newMsg) {
        clients_tcp.add(new ClientMember(ip, mysocket ,connected, newMsg));
        
    }
 // Method to add a client to the list
    public static void setClient(int index,ClientMember client) {
        clients_tcp.set(index,client);
        
    }

    // Method to retrieve a client's information by index
    public static ClientMember getClient(int index) {
        if (index >=0 && index < clients_tcp.size()) {
        	return clients_tcp.get(index);
        }else {
        	//�ε����� ������ ��� ��� ����ó�� �Ǵ� null ��ȯ
        	throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }
 // Method to retrieve the index of a specific client in the list
    public static int getIndex(ClientMember client) {
        int index = clients_tcp.indexOf(client); // ArrayList�� indexOf �޼ҵ带 ���
        if (index != -1) {
            return index; // ��ȿ�� �ε����� ��ȯ
        } else {
            throw new IllegalArgumentException("Client not found in the list.");
        }
    }

    
    public boolean checkAllClientsNewMessage() {
    	
        for (ClientMember client : clients_tcp) {
        	
            if (!client.getNewMsg()) {  // ������� ���� Ŭ���̾�Ʈ�� ������ false ��ȯ
                return false;
            }
        }
        System.out.println("Every client is connected.");
        return true;
    }
    
    public void AllClientsSetFalse() {
    	for (ClientMember client: clients_tcp) {
    		if(client.getConnected()) //����Ǿ� �ִ� Ŭ���̾�Ʈ�� ���Ͽ� ���ο�޽��������� boolean false�� �ʱ�ȭ
    		client.setNewMsg(false);
    	}
    }
    
    public void AllClientsReset() {
    	clients_tcp.clear();
    }
}