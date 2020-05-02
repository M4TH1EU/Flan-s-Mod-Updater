package ch.m4th1eu.flansupdater;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static ch.m4th1eu.flansupdater.FileUtils.*;

public class ArmorUtils {


    /**
     * Create the new armor's file from the old one.
     *
     * @param path old armor's file path
     */
    public static void createNewArmorFile(Path path) {
        //new armor file based on the newpack path and the current filename.
        String NEWFILENAME = Main.ARMOR_NEWPATH + path.getFileName().toString().toLowerCase().replaceAll(" ", "");

        //check if the newpack folder exists, if not we create it.
        File armorsFolder = new File(Main.ARMOR_NEWPATH);
        if (!armorsFolder.exists()) {
            boolean isCreated = armorsFolder.mkdirs();
            if (isCreated) {
                Logger.info("Successfully created new armors folder.");
            } else {
                Logger.error("Cannot create new armors folder!");
            }
        }

        try {
            Writer myWriter = new OutputStreamWriter(new FileOutputStream(NEWFILENAME), StandardCharsets.UTF_8);
            myWriter.write(processArmorFile(readFile(path.toFile())));
            myWriter.close();
        } catch (IOException e) {
            Logger.error("An error occurred when creating armor's file : " + path.getFileName().toString().toLowerCase().replaceAll(" ", ""));
            e.printStackTrace();
        }
    }


    /**
     * Replace arguments to lowercase, replace space, etc...
     *
     * @param string the file content
     */
    public static String processArmorFile(String string) {
        //ShortName
        String shortName = get("ShortName", string).toLowerCase().replaceAll(" ", "_");
        string = set("ShortName", shortName, string);

        //Texture
        String texture = get("ArmourTexture", string).toLowerCase().replaceAll(" ", "_");
        string = set("ArmourTexture", texture, string);

        //Icon
        String icon = get("Icon", string).toLowerCase().replaceAll(" ", "_");
        string = set("Icon", icon, string);

        //Add to lang array.
        addLang(shortName, get("Name", string));

        //Create json model file
        FileUtils.createModels(shortName, icon);

        return string;
    }

    public static void readAllArmorsFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(Main.ARMOR_PATH))) {
            paths.filter(Files::isRegularFile).forEach(ArmorUtils::createNewArmorFile);
            Logger.info("Successfully generated new armors files and filled the lang file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAllArmorsTextures() {
        try (Stream<Path> paths = Files.walk(Paths.get(Main.ARMOR_TEXTURES_PATH))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                Path newPath = Paths.get(Main.ARMOR_TEXTURES_NEWPATH + path.getFileName().toString().toLowerCase().replaceAll(" ", ""));

                try {
                    org.apache.commons.io.FileUtils.copyFile(path.toFile(), newPath.toFile(), false);
                } catch (IOException e) {
                    Logger.error("Cannot rename/move armors textures file : " + path.getFileName());
                    e.printStackTrace();
                }
            });
            Logger.info("Successfully copied and renamed the armors textures files.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
