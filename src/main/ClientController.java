package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class ClientController {

    @FXML
    private TextField msgTextField;
    @FXML
    public TextArea msgTextArea;

    private String name;
    private String serverName;
    private int port;
    private Socket socket;
    private PrintWriter pw;
    private InputStreamReader in;
    private BufferedReader bf;

    public ClientController() throws IOException {
        takePort();
        takeName();
        socket = new Socket("localhost", port);
        pw = new PrintWriter(socket.getOutputStream(),true);
        in = new InputStreamReader(socket.getInputStream());
        bf = new BufferedReader(in);
        pw.println(name);
        serverName = bf.readLine();
        receiveMsg();
    }

    public void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Notification!");
        alert.setContentText("Please enter number only!");
        alert.showAndWait();
    }

    public void takePort() {
        TextInputDialog portDialog = new TextInputDialog();
        portDialog.setTitle("Connect to server");
        portDialog.setHeaderText("Enter server's port");
        portDialog.setContentText("Port: ");
        Optional<String> result = portDialog.showAndWait();
        try {
            port = Integer.parseInt(result.get());
        } catch (NumberFormatException e) {
            errorAlert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takeName() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Enter your result");
        nameDialog.setHeaderText("You're now connected!");
        nameDialog.setContentText("Your name: ");
        Optional<String> result = nameDialog.showAndWait();
        if (result.isPresent()) {
            name = result.get();
        } else {
            name = "Client";
        }
    }

    public void sendMsg(ActionEvent e) {
        String msg = msgTextField.getText();
        if (!msg.isEmpty()) {
            pw.println(msg);
            msgTextArea.appendText("You: " + msg + "\n");
            msgTextField.clear();
        }
    }

    public void receiveMsg() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = bf.readLine();
                        if (msg != null) {
                            Platform.runLater(()->{
                                msgTextArea.appendText(serverName + ": " + msg + "\n");
                            });
//                            System.out.println(serverName + ": " + msg[0]);
                        }
                    } catch (IOException e) {
                    }
                }
            }
        };
        thread.start();
    }

    public void clearMessage(ActionEvent event) {
        msgTextArea.clear();
    }

    public void exit(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit program");
        alert.setHeaderText("You are about to close the program!");
        alert.setContentText("Are you sure about that?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            pw.close();
            in.close();
            bf.close();
            socket.close();
            Platform.exit();
            System.exit(0);
        }
    }
}
