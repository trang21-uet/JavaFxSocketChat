package javaFX;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import socket.SendThread;
import socket.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ServerFX {

    public static final String INFO =
            "Chương trình chat room đơn giản sử dụng Java Socket và JavaFx\n" +
            "\n" +
            "Chức năng:\n" +
            "    - Tạo server tại cổng do người dùng nhập.\n" +
            "    - Chấp nhận yêu cầu kết nối của các client.\n" +
            "    - Nhận tin nhắn của client gửi & chuyển đến các client khác.\n";
    private static final int DEFAULT_PORT = 0;
    @FXML
    public TextArea msgTextArea;
    public Server server;
    int port;

    public ServerFX() {
        port = DEFAULT_PORT;
        takePort();
        new Thread(() -> {
            try {
                server = new Server(port);
                Platform.runLater(() -> msgTextArea.appendText("Đã tạo máy chủ tại cổng " + port + ".\n"));

                while (true) {
                    Socket s = server.serverSocket.accept();
                    SendThread sendThread = new SendThread(s, this);
                    server.clientThread.add(sendThread);
                    Platform.runLater(() -> msgTextArea.appendText("Client đã kết nối.\n"));
                    Thread thread = new Thread(sendThread);
                    thread.start();
                }
            } catch (IOException e) {
                Platform.runLater(() -> msgTextArea.appendText("Có lỗi khi khởi tạo máy chủ!\n"));
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
        TextInputDialog portDialog = new TextInputDialog(String.valueOf(port));
        portDialog.setTitle("Tạo máy chủ mới");
        portDialog.setHeaderText("Mời nhập cổng của máy chủ");
        portDialog.setContentText("Cổng: ");
        Optional<String> result = portDialog.showAndWait();
        try {
            if (result.isEmpty()) {
                Platform.exit();
                System.exit(0);
            } else {
                port = Integer.parseInt(result.get().trim());
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

    public void info(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(400);
        alert.setHeight(300);
        alert.setTitle("Giới thiệu chương trình");
        alert.setHeaderText("Phần mềm chat room đơn giản - Server");
        alert.setContentText(INFO);
        alert.showAndWait();
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
