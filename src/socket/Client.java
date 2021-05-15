package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private String name;
    private final Socket socket;

    public Client(int port, String name) throws IOException {
        this.name = name;
        socket = new Socket(SERVER_IP, port);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }


    /**
     * Gửi tin nhắn lên server.
     *
     * @param msg - nội dung tin nhắn cần gửi.
     */
    public void sendMsg(String msg) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        socket.close();
    }
}
