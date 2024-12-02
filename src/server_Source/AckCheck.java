package server_Source;


public class AckCheck  {
    private final TCPReceive serverTcp;
    private final TcpConnectionAccepter.ClientHandler handler;
    
    
 	
    
    public AckCheck(TCPReceive serverTCP, TcpConnectionAccepter.ClientHandler handler) {
        this.serverTcp = serverTCP;
        this.handler = handler;
    }
    
    //������ byte�迭�� ó���ϴ� �޼ҵ�
    /*
	public void startChecking() {
		
		boolean allBitsOne = true;
	    // ��� ��Ʈ�� 1���� Ȯ��
		for (byte b : serverTcp.checkNewMessage) {
			if (b != (byte) 0xFF) { // ���� �� ����Ʈ�� 0xFF�� �ƴ϶��
				allBitsOne = false;
				break;
			}
		}
		System.out.println("Ack checking");
		if (allBitsOne) {
			// �ε��� ��ȣ�� �ش��ϴ� client Ŭ���� �迭 ȣ��
			ClientInfo clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
			clientinfo.setNewMsg(true);
			System.out.println("Client Num: " + handler.permanent_id + " Changed index value TRUE");
		}

	}*/
	public void startChecking() {

		// �ε��� ��ȣ�� �ش��ϴ� client Ŭ���� �迭 ȣ��
		ClientMember clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
		clientinfo.setNewMsg(true);
		System.out.println("Client Num: " + handler.permanent_id + " Changed index value TRUE");
		

	}

	public void stopChecking() {
		// �ش� �ε����� true�� �����Ͽ� ������� ���α׷��� �۵��ϵ�����

		// �ε��� ��ȣ�� �ش��ϴ� clientŬ���� �迭 ȣ�� �� ������� false, ���ο� �޽��� true ����
		ClientMember clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
		System.out.println("Client : " + clientinfo.getIp() + " is disconnected");
		clientinfo.setNewMsg(true);
		clientinfo.setConnected(false);

	}

}
