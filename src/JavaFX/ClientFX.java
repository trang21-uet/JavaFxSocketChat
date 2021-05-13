package JavaFX;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import socket.ReadThread;
import socket.Client;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.control.ButtonType.CANCEL;

public class ClientFX {

    @FXML
    private TextField msgTextField;
    @FXML
    public TextArea msgTextArea;

    final static int DEFAULT_PORT = 0;
    final static String DEFAULT_NAME = "Client";
    public Client client;
    private int port;
    private String name;

    public ClientFX(){
        takePort();
        takeName();
        try {
            client = new Client(port, name);
            Platform.runLater(() -> {
                msgTextArea.appendText("Đã kết nối với máy chủ tại cổng " + port + "\n");
            });
            Thread thread = new Thread(new ReadThread(this));
            thread.start();
        } catch (IOException e) {
            Platform.runLater(()-> {
                msgTextArea.appendText("Có lỗi khi kết nối đến máy chủ!\n");
            });

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
        TextInputDialog portDialog = new TextInputDialog();
        portDialog.setTitle("Kết nối đến máy chủ");
        portDialog.setHeaderText("Mời nhập cổng của máy chủ");
        portDialog.setContentText("Cổng: ");
        Optional<String> result = portDialog.showAndWait();
        try {
            if (!result.isPresent()) {
                Platform.exit();
                System.exit(0);
            }
            else if (result.isEmpty()) {
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

    public void takeName() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Kết nối máy chủ thành công");
        nameDialog.setHeaderText("Mời nhập tên của bạn!");
        nameDialog.setContentText("Tên: ");
        Optional<String> result = nameDialog.showAndWait();
        if (!result.isPresent()) {
            Platform.exit();
        }
        else if (result.isEmpty()) {
            name = DEFAULT_NAME;
        } else {
            name = result.get();
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
