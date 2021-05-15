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

import java.util.Objects;

public class ServerMain extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("../javaFX/Server.fxml")));
        stage.setTitle("Chương trình chat room đơn giản - Server");
        Image icon = new Image("res/icon.jpg");
        stage.getIcons().add(icon);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            exit();
        });
    }

    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thoát chương trình");
        alert.setHeaderText("Bạn chuẩn bị thoát chương trình!");
        alert.setContentText("Bạn có chắc muốn thoát?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
}
