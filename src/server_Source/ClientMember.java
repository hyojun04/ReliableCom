package server_Source;
import java.net.Socket;
import java.util.ArrayList;

// Ŭ���̾�Ʈ�� ������ ���� Ŭ����
public class ClientMember {
    String ip;
    Socket mysocket;
    Boolean connected;
    Boolean newMsg;
    
    // ������
    public ClientMember(String ip,Socket mysocket, Boolean connected, Boolean newMsg) {
        this.ip = ip;
        this.mysocket = mysocket;
        this.connected = connected;
        this.newMsg = newMsg;        
    }

    // ���Ǽ��� ���� get�� set �޼ҵ带 �����س���
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public Socket getSocket() {
        return mysocket;
    }
   
    public Boolean getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(Boolean newMsg) {
        this.newMsg = newMsg;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}

