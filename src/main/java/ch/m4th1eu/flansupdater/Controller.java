package ch.m4th1eu.flansupdater;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ch.m4th1eu.flansupdater.Main.primaryStage;


public class Controller {


    @FXML
    TextField packname;
    @FXML
    Button bug;
    @FXML
    Button idea;
    @FXML
    Button convertir;
    @FXML
    Button selectpack;

    private File selectedFile;

    @FXML
    private void initialize() {
        registerHoverableButton(bug);
        registerHoverableButton(idea);
        registerHoverableButton(convertir);
        registerHoverableButton(selectpack);

        registerLinkableButton(bug, "xyz");
        registerLinkableButton(idea, "xyz");

        packname.setEditable(false);
        convertir.setDisable(true);

        convertir.setOnAction(event -> {

            new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        selectpack.setDisable(true);
                        convertir.setDisable(true);
                        packname.setDisable(true);

                        convertir.setText("Extraction du pack...");
                    });

                    new ZipFile(selectedFile).extractAll(selectedFile.getParent() + "\\");
                } catch (ZipException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> convertir.setText("Erreur, extraction échouée !"));
                }

                java.lang.String[] args = new java.lang.String[]{selectedFile.getParent()};
                try {
                    Platform.runLater(() -> convertir.setText("Mise à jour..."));
                    Updater.main(args);
                    Platform.runLater(() -> convertir.setText("Terminé !"));

                } catch (Exception e) {
                    Platform.runLater(() -> convertir.setText("Erreur, mise à jour échouée !"));
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    selectpack.setDisable(false);
                    selectpack.setText("");
                    selectedFile = new File("");
                    convertir.setText("Mettre à jour mon pack");
                    convertir.setDisable(false);
                    packname.setDisable(false);
                });
            }).start();


        });

        selectpack.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));
            selectedFile = fileChooser.showOpenDialog(primaryStage);

            if(selectedFile != null){
                packname.setText(selectedFile.getName());
            }
            convertir.setDisable(false);
        });
    }

    public void registerHoverableButton(Button button) {
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: grey; " +
                    "-fx-border-color: grey;" +
                    "-fx-text-fill: white;");
        });

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: grey;");
        });

    }

    public void registerLinkableButton(Button button, String url) {
        button.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}
