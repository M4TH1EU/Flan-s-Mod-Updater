package ch.m4th1eu.flansupdater;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    public static final String PACK_PATH = "C:\\Users\\Mathieu\\Downloads\\Altis Mine Pack\\";
    public static final String NEWPACK_PATH = "C:\\Users\\Mathieu\\Downloads\\Altis Mine Pack NEW\\";

    public static final String VEHICLES_PATH = PACK_PATH + "vehicles\\";
    public static final String VEHICLES_NEWPATH = NEWPACK_PATH + "vehicles\\";

    public static final String ARMOR_PATH = PACK_PATH + "armorFiles\\";
    public static final String ARMOR_NEWPATH = NEWPACK_PATH + "armorFiles\\";

    public static final String ARMOR_TEXTURES_PATH = PACK_PATH + "assets\\flansmod\\armor\\";
    public static final String ARMOR_TEXTURES_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\armor\\";

    public static final String VEHICLESSKINS_PATH = PACK_PATH + "assets\\flansmod\\skins\\";
    public static final String VEHICLESSKINS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\skins\\";

    public static final String JSON_PATH = NEWPACK_PATH + "assets\\flansmod\\models\\item\\";

    public static final String LANG_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\lang\\";

    public static final String MODELS_PATH = PACK_PATH + "com\\";
    public static final String MODELS_NEWPATH = NEWPACK_PATH + "com\\";

    public static final String ICONS_PATH = PACK_PATH + "assets\\flansmod\\textures\\items\\";
    public static final String ICONS_NEWPATH = NEWPACK_PATH + "assets\\flansmod\\textures\\items\\";

    private static final Main INSTANCE = new Main();

    public static void main(String[] args) {
        FileUtils.copyModels();
        VehiclesUtils.readAllVehiclesFiles();
        VehiclesUtils.readAllVehiclesSkins();
        ArmorUtils.readAllArmorsFiles();
        ArmorUtils.readAllArmorsTextures();
        FileUtils.readAllIcons();
        FileUtils.createPackInfo();
        FileUtils.writeLangs(FileUtils.langs);

        ZipUtil.pack(new File(NEWPACK_PATH), new File(Paths.get(NEWPACK_PATH).getParent() + "\\" + Paths.get(NEWPACK_PATH).getFileName() + ".zip"));
        Logger.info("Update complete.");
    }


}
