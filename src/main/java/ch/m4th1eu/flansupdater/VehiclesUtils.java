package ch.m4th1eu.flansupdater;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static ch.m4th1eu.flansupdater.FileUtils.*;

public class VehiclesUtils {

    /**
     * Create the new vehicle's file from the old one.
     *
     * @param path old vehicle's file path
     */
    public static void createNewVehicleFile(Path path) {
        //new vehicle file based on the newpack path and the current filename.
        String NEWFILENAME = Main.VEHICLES_NEWPATH + path.getFileName().toString().toLowerCase().replaceAll(" ", "");

        //check if the newpack folder exists, if not we create it.
        File vehiclesFolder = new File(Main.VEHICLES_NEWPATH);
        if (!vehiclesFolder.exists()) {
            boolean isCreated = vehiclesFolder.mkdirs();
            if (isCreated) {
                Logger.info("Successfully created new vehicles folder.");
            } else {
                Logger.error("Cannot create new vehicles folder!");
            }
        }

        try {
            Writer myWriter = new OutputStreamWriter(new FileOutputStream(NEWFILENAME), StandardCharsets.UTF_8);
            myWriter.write(processVehicleFile(readFile(path.toFile())));
            myWriter.close();
        } catch (IOException e) {
            Logger.error("An error occurred when creating vehicle's file : " + path.getFileName().toString().toLowerCase().replaceAll(" ", ""));
            e.printStackTrace();
        }
    }


    /**
     * Replace arguments to lowercase, replace space, etc...
     *
     * @param string the file content
     */
    public static String processVehicleFile(String string) {
        //ShortName
        String shortName = get("ShortName", string).toLowerCase().replaceAll(" ", "_");
        string = set("ShortName", shortName, string);

        //Texture
        String texture = get("Texture", string).toLowerCase().replaceAll(" ", "_");
        string = set("Texture", texture, string);

        //Icon
        String icon = get("Icon", string).toLowerCase().replaceAll(" ", "_");
        string = set("Icon", icon, string);

        //Add to lang array.
        addLang(shortName, get("Name", string));

        //Create json model file
        FileUtils.createModels(shortName, icon);

        return string;
    }

    public static void readAllVehiclesFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(Main.VEHICLES_PATH))) {
            paths.filter(Files::isRegularFile).forEach(VehiclesUtils::createNewVehicleFile);
            Logger.info("Successfully generated new vehicles files and filled the new lang file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAllVehiclesSkins() {
        try (Stream<Path> paths = Files.walk(Paths.get(Main.VEHICLESSKINS_PATH))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                Path newPath = Paths.get(Main.VEHICLESSKINS_NEWPATH + path.getFileName().toString().toLowerCase().replaceAll(" ", ""));

                try {
                    org.apache.commons.io.FileUtils.copyFile(path.toFile(), newPath.toFile(), false);
                } catch (IOException e) {
                    Logger.error("Cannot rename/move skins file : " + path.getFileName());
                    e.printStackTrace();
                }
            });
            Logger.info("Successfully copied and renamed the skins files.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
