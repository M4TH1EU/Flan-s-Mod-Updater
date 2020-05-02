package ch.m4th1eu.flansupdater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.nio.file.Paths;

public class Main extends Application {
    private static final Main INSTANCE = new Main();

    public static String PACK_ZIP = "C:\\Users\\Mathieu\\Downloads\\Altis Mine Pack.zip";
    public static String PACK_PATH = "C:\\Users\\Mathieu\\Downloads\\Altis Mine Pack\\";
    public static String VEHICLES_PATH = PACK_PATH + "vehicles\\";
    public static String ARMOR_PATH = PACK_PATH + "armorFiles\\";
    public static String ARMOR_TEXTURES_PATH = PACK_PATH + "assets\\flansmod\\armor\\";
    public static String VEHICLESSKINS_PATH = PACK_PATH + "assets\\flansmod\\skins\\";
    public static String MODELS_PATH = PACK_PATH + "com\\";
    public static String ICONS_PATH = PACK_PATH + "assets\\flansmod\\textures\\items\\";
    public static String NEWPACK_PATH = "C:\\Users\\Mathieu\\Downloads\\Altis Mine Pack NEW\\";
    public static String VEHICLES_NEWPATH = NEWPACK_PATH + "vehicles\\";
    public static String ARMOR_NEWPATH = NEWPACK_PATH + "armorFiles\\";
    public static String ARMOR_TEXTURES_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\armor\\";
    public static String VEHICLESSKINS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\skins\\";
    public static String JSON_PATH = PACK_PATH + "assets\\flansmod\\models\\item\\";
    public static String JSON_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\models\\item\\";
    public static String LANG_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\lang\\";
    public static String MODELS_NEWPATH = NEWPACK_PATH + "com\\";
    public static String ICONS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\textures\\items\\";
    public static Stage PRIMARY_STAGE = null;

    public static void convert() {
        Logger.info("Starting new pack update.");
        refreshVariables();

        FileUtils.copyModels();
        VehiclesUtils.readAllVehiclesFiles();
        VehiclesUtils.readAllVehiclesSkins();
        ArmorUtils.readAllArmorsFiles();
        ArmorUtils.readAllArmorsTextures();
        FileUtils.readAllIcons();
        FileUtils.createPackInfo();
        FileUtils.writeLangs(FileUtils.langs);

        ZipUtil.pack(new File(NEWPACK_PATH),
                new File(Paths.get(PACK_ZIP).getParent() + "\\" + Paths.get(PACK_ZIP).getFileName().toString().replaceAll(".zip", "")
                        .replaceAll(".jar", "")
                        .replaceAll(".rar", "")
                        + "_UPDATED.jar"));

        Logger.info("Update complete.");
    }

    private static void refreshVariables() {
        VEHICLES_PATH = PACK_PATH + "vehicles\\";
        ARMOR_PATH = PACK_PATH + "armorFiles\\";
        JSON_PATH = PACK_PATH + "assets\\flansmod\\models\\item\\";
        ARMOR_TEXTURES_PATH = PACK_PATH + "assets\\flansmod\\armor\\";
        VEHICLESSKINS_PATH = PACK_PATH + "assets\\flansmod\\skins\\";
        MODELS_PATH = PACK_PATH + "com\\";
        ICONS_PATH = PACK_PATH + "assets\\flansmod\\textures\\items\\";
        VEHICLES_NEWPATH = NEWPACK_PATH + "vehicles\\";
        ARMOR_NEWPATH = NEWPACK_PATH + "armorFiles\\";
        ARMOR_TEXTURES_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\armor\\";
        VEHICLESSKINS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\skins\\";
        JSON_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\models\\item\\";
        LANG_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\lang\\";
        MODELS_NEWPATH = NEWPACK_PATH + "com\\";
        ICONS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\textures\\items\\";
        Logger.info("Refreshed the variables.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/menu.fxml"));

        Main.PRIMARY_STAGE = primaryStage;
        Main.PRIMARY_STAGE.setTitle("Flans Updater");
        Main.PRIMARY_STAGE.setResizable(false);
        Main.PRIMARY_STAGE.setScene(new Scene(root, 625, 365));
        Main.PRIMARY_STAGE.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));
        Main.PRIMARY_STAGE.show();
    }
}
