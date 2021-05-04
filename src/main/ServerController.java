package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class ServerController {

    @FXML
    public TextField msgTextField;
    @FXML
    private TextArea msgTextArea;

    private String name;
    private String clientName;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter pw;
    private InputStreamReader in;
    private BufferedReader bf;

    public ServerController() throws IOException {
        takePort();
        takeName();
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        pw = new PrintWriter(socket.getOutputStream(), true);
        in = new InputStreamReader(socket.getInputStream());
        bf = new BufferedReader(in);
        pw.println(name);
        clientName = bf.readLine();
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
        portDialog.setTitle("Create new server");
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
        nameDialog.setTitle("Enter server's name");
        nameDialog.setHeaderText("Server create successfully!");
        nameDialog.setContentText("Server's name: ");
        Optional<String> name = nameDialog.showAndWait();
        if (name.isPresent()) {
            this.name = name.get();
        } else {
            this.name = "Server";
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
                            Platform.runLater(()-> {
                                msgTextArea.appendText(clientName + ": " + msg + "\n");
                            });
//                            System.out.println(clientName + ": " + msg[0]);
                            return;
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
            serverSocket.close();
            socket.close();
            Platform.exit();
            System.exit(0);
        }
    }
}
