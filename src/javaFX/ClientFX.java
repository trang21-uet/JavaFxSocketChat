package javaFX;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import socket.Client;
import socket.ReadThread;

import java.io.IOException;
import java.util.Optional;

public class ClientFX {

    public static final String INFO =
            "Chương trình chat room đơn giản sử dụng Java Socket và JavaFx\n" +
            "\n" +
            "Chức năng:\n" +
            "    - Kết nối đến server tại cổng người dùng nhập.\n" +
            "    - Đặt tên & đổi tên cho client.\n" +
            "    - Gửi tin nhắn đến server.\n";
    private final static int DEFAULT_PORT = 1;
    private final static String DEFAULT_NAME = "Client";
    @FXML
    public TextArea msgTextArea;
    public Client client;
    @FXML
    private TextField msgTextField;
    private int port;
    private String name;

    public ClientFX() {
        port = DEFAULT_PORT;
        name = DEFAULT_NAME;
        takePort();
        takeName();
        try {
            client = new Client(port, name);
            Platform.runLater(() -> msgTextArea.appendText("Đã kết nối với máy chủ tại cổng " + port + "\n"));
            Thread thread = new Thread(new ReadThread(this));
            thread.start();
        } catch (IOException e) {
            Platform.runLater(() -> msgTextArea.appendText("Có lỗi khi kết nối đến máy chủ!\n"));

            e.printStackTrace();
        }
    }

    public void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không hợp lệ!");
        alert.setContentText("Vui lòng chỉ nhập ký tự số!");
        alert.showAndWait();
    }

    public void takePort() {
        TextInputDialog portDialog = new TextInputDialog(String.valueOf(port));
        portDialog.setTitle("Kết nối đến máy chủ");
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

    public void takeName() {
        TextInputDialog nameDialog = new TextInputDialog(name);
        nameDialog.setTitle("Kết nối máy chủ thành công");
        nameDialog.setHeaderText("Mời nhập tên của bạn!");
        nameDialog.setContentText("Tên: ");
        Optional<String> result = nameDialog.showAndWait();
        if (result.isEmpty()) {
            Platform.exit();
        } else {
            name = result.get().trim();
        }
    }

    public void changeName(ActionEvent e) {
        takeName();
        client.setName(name);
    }

    public void sendMsg(ActionEvent e) {
        String msg = msgTextField.getText().trim();
        if (!msg.isEmpty()) {
            client.sendMsg(name + ": " + msg);
            msgTextField.clear();
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
        alert.setHeaderText("Phần mềm chat room đơn giản - Client");
        alert.setContentText(INFO);
        alert.showAndWait();
    }

    public void exit(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thoát chương trình");
        alert.setHeaderText("Bạn chuẩn bị thoát chương trình!");
        alert.setContentText("Bạn có chắc chắn muốn thoát?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            client.close();
            Platform.exit();
            System.exit(0);
        }
    }
}
