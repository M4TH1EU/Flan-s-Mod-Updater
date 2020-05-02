package ch.m4th1eu.flansupdater;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.zeroturnaround.zip.ZipUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.UUID;

public class FrameController {
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

        registerLinkableButton(bug, "https://github.com/M4TH1EU/Flan-s-Mod-Updater/issues/new?assignees=&labels=&template=bug_report.md&title=");
        registerLinkableButton(idea, "https://github.com/M4TH1EU/Flan-s-Mod-Updater/issues/new?assignees=&labels=&template=feature_request.md&title=");

        packname.setEditable(false);
        convertir.setDisable(true);

        convertir.setOnAction(event -> new Thread(() -> {
            try {
                Main.PACK_PATH = Files.createTempDirectory("flans-pack-old" + UUID.randomUUID()).toString() + "\\";
                Main.NEWPACK_PATH = Files.createTempDirectory("flans-pack-updated" + UUID.randomUUID()).toString() + "\\";
                Logger.info("Successfully set the new path to temps directories.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                selectpack.setDisable(true);
                convertir.setDisable(true);
                packname.setDisable(true);

                convertir.setText("Extraction du pack...");
                ZipUtil.unpack(new File(Main.PACK_ZIP), new File(Main.PACK_PATH));
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Platform.runLater(() -> convertir.setText("Mise à jour..."));
                Main.convert();
                Platform.runLater(() -> convertir.setText("Terminé !"));
            } catch (Exception e) {
                Platform.runLater(() -> convertir.setText("Erreur, mise à jour échouée !"));
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Mise à jour terminée.");
                alert.setContentText("La mise à jour du pack " + packname.getText() + " est terminée !" + "\nChemin : " + selectedFile.getParent());

                alert.showAndWait();
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            FileUtils.deleteTempsFolder();

            Platform.runLater(() -> {
                packname.setText("");
                selectedFile = new File("");
                convertir.setText("Mettre à jour mon pack");
                convertir.setDisable(false);
                packname.setDisable(false);
                selectpack.setDisable(false);
            });
        }).start());

        selectpack.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Flans Pack", "*.zip", "*.jar", "*.rar"));
            selectedFile = fileChooser.showOpenDialog(Main.PRIMARY_STAGE);

            if (selectedFile != null) {
                packname.setText(selectedFile.getName());
                Main.PACK_ZIP = selectedFile.getAbsolutePath();
            }
            convertir.setDisable(false);
        });
    }

    public void registerHoverableButton(javafx.scene.control.Button button) {
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

    public void registerLinkableButton(javafx.scene.control.Button button, String url) {
        button.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}