package socket;

import JavaFX.ClientFX;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ReadThread implements Runnable {
    ClientFX clientFX;
    Scanner in;
    PrintWriter out;

    public ReadThread(ClientFX clientFX) {
        this.clientFX = clientFX;
    }

    /**
     * Nhận tin nhắn từ server.
     */
    @Override
    public void run() {
        while (true) {
            try {
                in = new Scanner(clientFX.client.getSocket().getInputStream());
                while (in.hasNextLine()) {
                    String message = in.nextLine();
                    Platform.runLater(() -> {
                        clientFX.msgTextArea.appendText(message + "\n");
                    });
                }
            } catch (IOException e) {
                System.out.println("Có lỗi khi đọc dữ liệu từ máy chủ!");
                e.printStackTrace();
            }
        }
    }

}
