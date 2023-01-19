import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChattingClient {
    static JButton sendbtn, exitbtn;
    static JFrame jf;
    static JPanel p1;
    static JTextArea output,input;
    static JScrollPane sp, sp1;
    static JLabel roomName;
    static String message;
    static Socket sock;

    public static void main(String[] args) {
        // 연결용도
        DataInputStream chatting = null; //입력하는 채팅이 들어감
        DataOutputStream printChat; //입력된 채팅을 출력하는 용도

        // UI설정
        jf = new JFrame(); //프레임생성

        // 메인 UI 패널
        p1 = new JPanel();   // 패널
        p1.setLayout(null);  // 패널의 레이어를 null로 설정함
        p1.setBackground(new Color(255, 255, 255));

        // 나가기 버튼
        exitbtn = new JButton("나가기");
        //exitbtn.setBackground(new Color(58, 40, 109));
        //exitbtn.setForeground(new Color(255,255,255));
        exitbtn.setForeground(new Color(58, 40, 109));
        exitbtn.setBounds(5, 5, 60, 25);
        MyActionListener exit = new MyActionListener();
        exitbtn.addActionListener(exit);
        p1.add(exitbtn);

        // 라벨에 적용할 폰트
        Font f1 = new Font("", Font.BOLD, 25);
        //Font f2 = new Font("", Font.PLAIN, 15);

        // 채팅방 이름
        roomName = new JLabel("000동", SwingConstants.CENTER);
        roomName.setBounds(125, 5, 100, 25);
        roomName.setForeground(new Color(58, 40, 109));
        roomName.setFont(f1);
        p1.add(roomName);

        //대화출력창
        output = new JTextArea();   //데이터 출력 될 화면생성
        output.setEditable(false);  //필드에 작성을 금지 시킴(오로지 출력만 가능하도록)
        output.setBackground(new Color(207, 202, 247));
        sp = new JScrollPane(output); //스크롤모드
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //수평 스크롤 제한
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        output.setLineWrap(true);
        sp.setBounds(5, 35, 340, 360);
        p1.add(sp);

        //대화작성창(입력창)
        input = new JTextArea();
        input.setBackground(new Color(255, 255, 255));
        sp1 = new JScrollPane(input);
        sp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //수평 스크롤 제한
        sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        input.setLineWrap(true);
        sp1.setBounds(5, 400, 260, 60);
        p1.add(sp1);

        //전송버튼
        sendbtn = new JButton("전송");
        sendbtn.setBackground(new Color(255, 253, 188));
        sendbtn.setBounds(265, 400, 80, 60);
        MyActionListener lis = new MyActionListener();
        sendbtn.addActionListener(lis);
        p1.add(sendbtn);

        //서브 UI
        //IP주소 입력창
        String iP= JOptionPane.showInputDialog(null,"서버IP를 입력하세요","192.168.0.9"); //IP를 메세지출력창을 통해 입력받음
        if(iP==null || iP.length()==0){  //주소입력되지않거나, 0이면 안전하게 종료시킴
            System.out.println("서버 IP가 입력되지 않았습니다.");
            System.exit(0);
        }
        //포트번호 입력창
        String portNum = JOptionPane.showInputDialog(null,"포트번호를 입력하세요","9996"); //포트번호를 메세지출력창을 통해 입력받음
        int portn =  Integer.parseInt(portNum); //문자열로 받았으므로 숫자로 형변환해줌
        if (portn > 9999) { //포트번호가 올바르지않으면 안전하게 종료시킴
            System.out.println("포트번호가 올바르지 않습니다.");
            System.exit(0);
        }
        //닉네임 입력창
        String loginName= JOptionPane.showInputDialog(null,"닉네임을 입력하세요","닉네임" ,JOptionPane.INFORMATION_MESSAGE); //닉네임을 메세지출력창을 통해 입력받음
        if(loginName == null || loginName.length()==0){ //닉네임을 입력하지않으면 Guest라는 이름 자동 할당
            loginName="Guest";
        }

        // 포트번호 별로 방이름 설정
        if( portn == 9996) {
            roomName.setText("101동");
        } else if (portn == 9997) {
            roomName.setText("102동");
        }else if (portn == 9998) {
            roomName.setText("103동");
        }else if (portn == 9999) {
            roomName.setText("104동");
        }

        //프레임 Visible
        jf.add(p1); //메인 UI추가
        jf.setLocation(550, 200);
        jf.setSize(350,500);
        jf.setVisible(true);

        try {
            InetAddress getIPaddr = null; //IP를 가져올 변수
            getIPaddr = InetAddress.getByName(iP); //메세지 출력창에서 받은 ip로 주소를 가져옴
            sock = new Socket(getIPaddr, portn); //ip주소와 메세지출력창에서 받은 포트번호로 소켓에 접근

            chatting = new DataInputStream(sock.getInputStream()); //소켓을 통해 전달된 데이터스트림을 읽음
            printChat = new DataOutputStream(sock.getOutputStream()); //입력스트림을 얻는 변수
            String log = loginName; //메세지 출력창을 통해 받은 유저네임

            printChat.writeUTF(log); //유저네임을 출력스트림에 넣음
            Thread t = new Thread(new SendMessage(printChat)); //새쓰레드에 printChat을 넣음
            t.start();
        }catch(IOException e) {

        }

        try {
            while(true) { //종료전까진 계속 대화를 하기위해서 무한루프로 실행
                String temp = chatting.readUTF(); //채팅을 읽어옴
                output.append(temp); //출력창(output)으로 지정해둔 Textarea에 가져온 채팅내용을 추가함.
                output.append("\n"); //가독성을 위한 줄바꿈
                sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum()); //채팅출력창에 채팅이 쌓여 스크롤이 생길때, 채팅입력시마다 스크롤아래로 이동시킴
            }
        }catch(IOException e) {

        }
    }

    public static class MyActionListener implements ActionListener //버튼이벤트 생성
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendbtn) //전송버튼을 클릭시
            {
                message = input.getText(); //채팅입력창(input)으로 지정된 textarea영역의 string을 받아와 message변수에 저장함.
                input.setText(null); // 전송버튼을 누르면 채팅입력창(input)영역 초기화하여 다시문자를 쓰기 편하게 설정

            } else if (e.getSource() == exitbtn) { //나가기버튼 클릭시
                try {
                    sock.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0); //안전하게 프로그램 종료
            }
        }

    }
}
