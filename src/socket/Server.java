package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public ServerSocket serverSocket;
    public List<SendThread> clientThread = new ArrayList<>();
    int port;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    /**
     * Gửi tin nhắn từ serverFX tới tất cả client.
     *
     * @param msg nội dung tin nhắn cần gửi.
     */
    public void sendToAll(String msg) {
        for (SendThread client : clientThread) {
            client.sendMessage(msg);
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
