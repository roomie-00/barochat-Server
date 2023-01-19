import java.io.DataInputStream;
import java.net.Socket;

public class ReceiveMessage implements Runnable {
    Socket sock;
    DataInputStream chatting; //채팅내용
    String chatuserName, chatroom; //참여자이름
    ChatUserList chatuser; //채팅참여자 관리

    public ReceiveMessage(ChatUserList chatuser, Socket sock, String chatroom) throws Exception
    {
        this.chatuser = chatuser;
        this.sock = sock;
        this.chatroom = chatroom;
        chatting = new DataInputStream(sock.getInputStream());
        this.chatuserName = chatting.readUTF(); //사용자이름을 읽어옴
        chatuser.JoinPeople(chatuserName, sock, chatroom); //사용자를 등록함
    }

    public void run() {
        try {
            while(true) {
                String text = chatting.readUTF(); //chatting변수에 들어온 메세지를 읽어온다.
                chatuser.sendCHAT(text, chatuserName); // chatuserName 이름의 접속자가 text메세지를 보냄
            }
        } catch(Exception e) {
            chatuser.ExitPeople(this.chatuserName, chatroom); //에러가 발생하면 chatuserName을 ChattingClient에서 삭제
        }
    }
}
