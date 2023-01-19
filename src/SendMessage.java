import java.io.*;

public class SendMessage implements Runnable {
    DataOutputStream printChat;

    public SendMessage(DataOutputStream printChat) {
        this.printChat = printChat;
    }

    public void run() {

        while(true) { //무한루프 실행
            try {
                if (ChattingClient.message != "") //전송버튼을 통해 message변수에 입력창에 입력한 내용을 받아온상태일때만 if문 실행
                {
                    String text = ChattingClient.message; //전송버튼을 누름으로써 message변수에 저장된 문자열을 text로 가져옴
                    printChat.writeUTF(text); //메제시를 출력시킴
                    ChattingClient.message=""; //message변수를 다시 ""로 바꿈으로써 무한루프로 text가 출력되는것을 방지함.
                }
            }catch(Exception e) {

            }
        }
    }
}
