package socket;

import javaFX.ServerFX;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendThread implements Runnable {
    Socket socket;
    ServerFX serverFX;
    Scanner in;
    PrintWriter out;

    public SendThread(Socket socket, ServerFX serverFX) {
        this.socket = socket;
        this.serverFX = serverFX;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());

            while (true) {
                if (in.hasNextLine()) {
                    String message = in.nextLine();
                    serverFX.server.sendToAll(message);
                    Platform.runLater(() -> serverFX.msgTextArea.appendText(message + "\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
}
