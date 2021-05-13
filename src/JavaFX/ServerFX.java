package JavaFX;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import socket.SendThread;
import socket.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class ServerFX {

    @FXML
    public TextArea msgTextArea;

    final static int DEFAULT_PORT = 0;
    public Server server;
    int port;

    public ServerFX(){
        takePort();
        new Thread(() ->{
            try {
                server = new Server(port);
                Platform.runLater(() -> {
                    msgTextArea.appendText("Đã tạo máy chủ tại cổng " + port + "\n");
                });

                while (true) {
                    Socket s = server.serverSocket.accept();
                    SendThread sendThread = new SendThread(s, this);
                    server.clients.add(sendThread);
                    Thread thread = new Thread(sendThread);
                    thread.start();
                }
            } catch (IOException e) {
                Platform.runLater(() -> {
                    msgTextArea.appendText("Có lỗi khi khởi tạo máy chủ!\n");
                });
                e.printStackTrace();
            }
        }).start();
    }

    public void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không hợp lệ!");
        alert.setContentText("Vui lòng chỉ nhập ký tự số!");
        alert.showAndWait();
    }

    public void takePort() {
        TextInputDialog portDialog = new TextInputDialog();
        portDialog.setTitle("Tạo máy chủ mới");
        portDialog.setHeaderText("Mời nhập cổng của máy chủ");
        portDialog.setContentText("Cổng: ");
        Optional<String> result = portDialog.showAndWait();
        try {
            if (!result.isPresent()) {
                Platform.exit();
                System.exit(0);
            } else if (result.isEmpty()){
                port = DEFAULT_PORT;
            } else {
                port = Integer.parseInt(result.get());
            }
        } catch (NumberFormatException e) {
            errorAlert();
            takePort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void clearMessage(ActionEvent event) {
        msgTextArea.clear();
    }

    public void exit(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thoát chương trình");
        alert.setHeaderText("Bạn chuẩn bị thoát chương trình!");
        alert.setContentText("Bạn có chắc chắn muốn thoát?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            server.close();
            Platform.exit();
            System.exit(0);
        }
    }
}
