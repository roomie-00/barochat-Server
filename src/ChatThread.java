import java.net.ServerSocket;
import java.net.Socket;

public class ChatThread {
    Socket sock;
    ChatUserList chatuser = new ChatUserList(); // 채팅참여자 관리
    ServerSocket serverSock;
    String chatroom;
    int cnt = 0;
    Thread t[] = new Thread[50]; // 쓰레드를 50개를 두어 최대 50명이 들어올수있도록 설정

    public void sockInfo(ServerSocket ServerSock, String chatroom) {
        try {
            this.serverSock = ServerSock;
            this.chatroom = chatroom;
            ChatAccept chatAccept = new ChatAccept();
            chatAccept.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class ChatAccept extends Thread {
        public void run() {
            try {
                new ServerGui(chatroom, chatuser);
                while(true) {
                    sock = serverSock.accept();

                    t[cnt] = new Thread(new ReceiveMessage(chatuser, sock, chatroom));
                    t[cnt].start(); // 쓰레드 시작
                    cnt++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
