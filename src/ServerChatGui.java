import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerChatGui {
    String chatroom;

    public ServerChatGui(int portNum) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNum);
        ChatThread chatThread = new ChatThread();
        ChatUserList chatuser = new ChatUserList(); //확인

        if(portNum == 9996) {
            chatroom = "101동";
        } else if (portNum == 9997) {
            chatroom = "102동";
        }else if (portNum == 9998) {
            chatroom = "103동";
        }else if (portNum == 9999) {
            chatroom = "104동";
        }
        chatThread.sockInfo(serverSocket, chatroom);

        // UI설정
        JFrame jf = new JFrame(); //프레임생성
        jf.setTitle("포트 번호: " + portNum);

        // 메인 UI 패널
        JPanel p1 = new JPanel();   // 패널
        p1.setLayout(null);  // 패널의 레이어를 null로 설정함
        p1.setBackground(new Color(255, 255, 255));

        // 라벨에 적용할 폰트
        Font f1 = new Font("", Font.BOLD, 25);
        //Font f2 = new Font("", Font.PLAIN, 15);

        // 채팅방 이름
        JLabel roomName = new JLabel(chatroom + " 서버", SwingConstants.CENTER);
        roomName.setBounds(75, 5, 200, 25);
        roomName.setForeground(new Color(58, 40, 109));
        roomName.setFont(f1);
        p1.add(roomName);

        //대화출력창
        JTextArea output = new JTextArea();   //데이터 출력 될 화면생성
        output.setEditable(false);  //필드에 작성을 금지 시킴(오로지 출력만 가능하도록)
        output.setBackground(new Color(207, 202, 247));
        JScrollPane sp = new JScrollPane(output); //스크롤모드
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //수평 스크롤 제한
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        output.setLineWrap(true);
        sp.setBounds(5, 35, 340, 360);
        p1.add(sp);

        //대화작성창(입력창)
        JTextArea input = new JTextArea();
        input.setBackground(new Color(255, 255, 255));
        JScrollPane sp1 = new JScrollPane(input);
        sp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //수평 스크롤 제한
        sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        input.setLineWrap(true);
        sp1.setBounds(5, 400, 260, 60);
        p1.add(sp1);

        //전송버튼
        JButton sendbtn = new JButton("전송");
        sendbtn.setBackground(new Color(255, 253, 188));
        sendbtn.setBounds(265, 400, 80, 60);
        sendbtn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("전송 버튼 클릭");
                String message = input.getText(); //채팅입력창(input)으로 지정된 textarea영역의 string을 받아와 message변수에 저장함.
                input.setText(null); // 전송버튼을 누르면 채팅입력창(input)영역 초기화하여 다시문자를 쓰기 편하게 설정
                try {
                    chatuser.sendCHAT(message, "Server");
                } catch (Exception ex) {
                }
                output.append(message); //출력창(output)으로 지정해둔 Textarea에 가져온 채팅내용을 추가함.
                output.append("\n"); //가독성을 위한 줄바꿈
                sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum()); //채팅출력창에 채팅이 쌓여 스크롤이 생길때, 채팅입력시마다 스크롤아래로 이동시킴
            }
        });
        p1.add(sendbtn);

        //프레임 Visible
        jf.add(p1); //메인 UI추가
        jf.setLocation(550, 200);
        jf.setSize(350,500);
        jf.setVisible(true);
    }
}
