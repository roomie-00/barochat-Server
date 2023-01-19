import java.net.*;

public class ChattingServer {       // 서버를 위한 클래스
    public static void main(String[] args) {
        ServerSocket serverSock1, serverSock2, serverSock3, serverSock4 = null;   // 클라이언트들의 접속을 받는 소켓
        try {
            serverSock1 = new ServerSocket(9996); //포트설정
            serverSock2 = new ServerSocket(9997); //포트설정
            serverSock3 = new ServerSocket(9998); //포트설정
            serverSock4 = new ServerSocket(9999); //포트설정

            ChatThread chatThread1 = new ChatThread();
            chatThread1.sockInfo(serverSock1, "101동");
            ChatThread chatThread2 = new ChatThread();
            chatThread2.sockInfo(serverSock2, "102동");
            ChatThread chatThread3 = new ChatThread();
            chatThread3.sockInfo(serverSock3, "103동");
            ChatThread chatThread4 = new ChatThread();
            chatThread4.sockInfo(serverSock4, "104동");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
