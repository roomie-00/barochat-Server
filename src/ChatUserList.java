import java.io.*;
import java.net.*;
import java.util.*;

public class ChatUserList {
    HashMap<String, DataOutputStream> cm = new HashMap<String, DataOutputStream>();
    // cm은 String타입의 key와 DataOutputStream타입의 Value를 받음

    public synchronized void JoinPeople(String chatuserName, Socket sock, String chatroom) { //클라이언트 입장 시 서버에 입장정보를 전달
        try {
            cm.put(chatuserName, new DataOutputStream(sock.getOutputStream()));
            sendCHAT(chatuserName + " 님이 입장하셨습니다. / 채팅방 인원 " + cm.size() + "명", "Server"); //입장정보 출력
            System.out.println(chatroom + "채팅 접속 인원: " + cm.size() + "명"); //채팅방 인원 출력
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void ExitPeople(String chatuserName, String chatroom) { //클라이언트 퇴장시 서버에 퇴장정보를 전달
        try {
            cm.remove(chatuserName); //chatuserName의 이름을 가진 client 제거
            sendCHAT(chatuserName + " 님이 퇴장하였습니다. / 채팅방 인원 " + cm.size() + "명", "Server"); //퇴장정보 출력
            System.out.println(chatroom + "채팅 접속 인원: " + cm.size() + "명"); //남은 인원 출력
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendCHAT(String message, String chatuserName) throws Exception {
        Iterator iter = cm.keySet().iterator(); //cm의 key 값들을 읽음
        while (iter.hasNext()) { // key에 next()존재시 무한루프
            String clientname = (String) iter.next(); //채팅 입력한 클라이언트 이름 받아옴
            cm.get(clientname).writeUTF(chatuserName + " : " + message); //고정된 양식으로 출력
        }
    }
}
