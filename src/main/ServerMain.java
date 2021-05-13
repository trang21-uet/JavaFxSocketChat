package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerMain extends Application {
    private static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 680;

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../JavaFX/Server.fxml"));
        stage.setTitle("Chương trình chat room đơn giản - Server");
        Image icon = new Image("Images/icon.jpg");
        stage.getIcons().add(icon);
        stage.setScene(new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT));
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            exit(stage);
        });
    }

    public void exit(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit program");
        alert.setHeaderText("You are about to close the program!");
        alert.setContentText("Are you sure about that?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
