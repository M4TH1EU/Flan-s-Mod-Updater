package ch.m4th1eu.flansupdater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        Parent root = FXMLLoader.load(Updater.class.getResource("/menu.fxml"));

        Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("Flans Updater");
        Main.primaryStage.setResizable(false);
        Main.primaryStage.setScene(new Scene(root, 625, 365));
        Main.primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));
        Main.primaryStage.show();
    }
}
