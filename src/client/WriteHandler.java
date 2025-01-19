package client;

import static util.MyLogger.log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WriteHandler implements Runnable {

    private static final String DELIMITER = "|";

    private final DataOutputStream output;
    private final Client client;
    private boolean closed = false;

    public WriteHandler(DataOutputStream output, Client client) {
        this.output = output;
        this.client = client;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        try {
            // 채팅방 입장하기
            String username = inputUsername(sc);
            output.writeUTF("/join"+DELIMITER+username);

            // 채팅하기
            while (true){
                String toSend = sc.nextLine();
                if (toSend.isEmpty()) {
                    continue;
                }

                if (toSend.equals("/exit")){
                    output.writeUTF(toSend);
                    break;
                }

                // "/"로 시작하면 명령어, 나머지는 일반 메시지
                if (toSend.startsWith("/")) {
                    output.writeUTF(toSend);
                }
                else {
                    output.writeUTF("/message" + DELIMITER + toSend);
                }
            }
        }catch (IOException | NoSuchElementException e){
            log(e);
        }finally {
            client.close();
        }
    }

    private static String inputUsername(Scanner sc) {
        System.out.print("이름을 입력하세요: ");
        String username;
        do{
            username = sc.nextLine();
        }while (username.isEmpty());
        return username;
    }

    public synchronized void close(){
        if (closed){
            return;
        }

        try {
            System.in.close();  // Scanner 입력 중지
        } catch (IOException e) {
            log(e);
        }
        closed=true;
        log("writeHandler 종료");
    }
}
