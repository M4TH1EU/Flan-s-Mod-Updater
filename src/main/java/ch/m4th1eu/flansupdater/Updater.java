/**
 *
 * @author Mathieu Broillet (M4TH1EU_#0001 (on discord))
 * @description This program update himself your flan's mod packs from 1.7.10/1.8/etc. to 1.12.2
 *
 * @important This program is not meant to be very pretty or well coded.
 * It just works, that's all we ask.
 */

package ch.m4th1eu.flansupdater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Updater {

    private static String ICON_NAME = "";
    private static File MODEL_FILE = new File("");

    private static String PATH = null;

    public static void main(String[] args) throws Exception {
        PATH = args[0];

        ArrayList<Path> files = new ArrayList<>();

        try (Stream<Path> filePathStream = Files.walk(Paths.get(PATH))) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    files.add(filePath);
                }
            });
        }

        generateLangs();

        //MCMETA
        if (!new File(PATH + "\\pack_info.mcmeta").exists()) {
            PrintWriter printWriter = new PrintWriter(new File(PATH + "\\pack_info.mcmeta"));
            printWriter.println("{\n" +
                    "    \"pack\": {\n" +
                    "        \"pack_format\": 1,\n" +
                    "        \"description\": \"Generated by M4TH1EU's Flans Updater.\"\n" +
                    "    }\n" +
                    "}");
            printWriter.close();
        }
        if (new File(PATH + "\\pack.mcmeta").exists()) {
            new File(PATH + "\\pack.mcmeta").delete();
        }

        /*
        Listen all files and check if they ends by ".txt".
         */
        for (int i = 0; i < files.size(); i++) {

            /* armorFiles / models / lang / icons */
            if (files.get(i).getParent().toString().endsWith("armorFiles")) {
                if (files.get(i).getFileName().toString().endsWith(".txt")) {

                    //ARMOR FILES / ICONS(replaceNames)
                    renameFileToLowercase(files.get(i).toFile());
                    replaceNames(Paths.get(files.get(i).toString().replaceAll(" ", "_").toLowerCase()));

                    //MODELS
                    renameFileToLowercase(MODEL_FILE);

                    if (hasModel(files.get(i))) {
                        replaceModelIcon(MODEL_FILE.toPath());
                    } else {
                        createModelFile(MODEL_FILE.toPath());
                        replaceModelIcon(MODEL_FILE.toPath());
                    }
                }
            }
            /* armors textures */
            if (files.get(i).getParent().toString().endsWith("armor")) {

                if (files.get(i).getFileName().toString().endsWith(".png")) {
                    renameFileToLowercase(files.get(i).toFile());
                }
            }

            /* vehicles / models / lang / icons */
            if (files.get(i).getParent().toString().endsWith("vehicles")) {
                if (files.get(i).getFileName().toString().endsWith(".txt")) {

                    //VEHICLES FILES / ICONS(replaceNames)
                    renameFileToLowercase(files.get(i).toFile());
                    replaceNames(Paths.get(files.get(i).toString().replaceAll(" ", "_").toLowerCase()));

                    //MODELS
                    renameFileToLowercase(MODEL_FILE);

                    if (hasModel(files.get(i))) {
                        replaceModelIcon(MODEL_FILE.toPath());
                    } else {
                        createModelFile(MODEL_FILE.toPath());
                        replaceModelIcon(MODEL_FILE.toPath());
                    }
                }
            }
            /* vehicles textures */
            if (files.get(i).getParent().toString().endsWith("skins")) {
                if (files.get(i).getFileName().toString().endsWith(".png")) {
                    renameFileToLowercase(files.get(i).toFile());
                }
            }

            /* items icons */
            if (files.get(i).getParent().toString().endsWith("items")) {
                if (files.get(i).getFileName().toString().endsWith(".png")) {
                    renameFileToLowercase(files.get(i).toFile());
                }
            }
        }

        System.out.println("UPDATED SUCCESSFULLY");
    }

    public static boolean hasModel(Path filePath) {
        return new File(PATH + "\\assets\\flansmod\\models\\item\\" + filePath.getFileName().toString().replaceAll(".txt", ".json")).exists();
    }

    /**
     * Read the file gave by @filePath and split all lines to find "ShortName" and "ArmourTexture"
     *
     * @param filePath
     */
    public static void replaceNames(Path filePath) {
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(String.valueOf(filePath));
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            int i = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("ShortName")) {
                    String[] parts = strLine.split(" ");
                    String oldName = "ShortName " + parts[1];
                    String newName = "ShortName " + parts[1].toLowerCase().replaceAll(" ", "_");
                    MODEL_FILE = new File(filePath.getParent().getParent() + "\\assets\\flansmod\\models\\item\\" + parts[1].toLowerCase().replaceAll(" ", "_") + ".json");
                    modifyFile(String.valueOf(filePath), oldName, newName);
                }
                if (strLine.contains("ArmourTexture")) {
                    String[] parts = strLine.split(" ");
                    String oldName = "ArmourTexture " + parts[1];
                    String newName = "ArmourTexture " + parts[1].toLowerCase();
                    modifyFile(String.valueOf(filePath), oldName, newName);
                }
                if (strLine.contains("Texture")) {
                    String[] parts = strLine.split(" ");
                    String oldName = "Texture " + parts[1];
                    String newName = "Texture " + parts[1].toLowerCase().replaceAll(" ", "_");
                    modifyFile(String.valueOf(filePath), oldName, newName);
                }
                if (strLine.contains("Icon")) {
                    String[] parts = strLine.split(" ");
                    String oldName = "Icon " + parts[1];
                    String newName = "Icon " + parts[1].toLowerCase().replaceAll(" ", "_");
                    ICON_NAME = parts[1].toLowerCase().replaceAll(" ", "_");
                    modifyFile(String.valueOf(filePath), oldName, newName);
                }
                i++;
            }

            //Close the input stream
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Read the file gave by @filePath and split all lines to find "ShortName" and "ArmourTexture"
     *
     * @param filePath
     */
    public static void replaceModelIcon(Path filePath) {

        Gson gson = new Gson();
        JsonObject jsonObject = null;
        try {
            jsonObject = gson.fromJson(new FileReader(filePath.toFile()), JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            String oldIcon = jsonObject.getAsJsonObject("textures").get("layer0").getAsString();
            String newIcon = "flansmod:items/" + ICON_NAME;

            modifyFile(String.valueOf(filePath), oldIcon, newIcon);
        } else {
            try {
                createModelFile(MODEL_FILE.toPath());
                replaceModelIcon(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Replace an old string by a new one in a file
     *
     * @param filePath
     * @param oldString
     * @param newString
     */
    static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            String newContent = oldContent.replaceAll(oldString, newString);
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Replace all uppercase to lowercase.
     *
     * @param file
     */
    private static void renameFileToLowercase(File file) {
        boolean rename = file.renameTo(new File(file.getPath().replaceAll(file.getName(), "") + file.getName().replaceAll(" ", "_").toLowerCase()));
        if (!rename) {
            for (int i = 0; i < 3; i++) {
                file.renameTo(new File(file.getPath().replaceAll(file.getName(), "") + file.getName().replaceAll(" ", "_").toLowerCase()));
                System.gc();
                Thread.yield();
            }
        }
    }

    /**
     * Get the model.json tempalte and create new file with.
     *
     * @param filePath
     * @throws Exception
     */
    private static void createModelFile(Path filePath) throws Exception {

        InputStream in = Updater.class.getResourceAsStream("/model.json");

        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer, Charset.forName("utf-8"));
        String theString = writer.toString();

        filePath.getParent().toFile().mkdirs();
        PrintWriter printWriter = new PrintWriter(filePath.toFile());
        printWriter.println(theString);
        printWriter.close();
    }

    public static void createLangFile(Path filePath) throws Exception {
        PrintWriter printWriter = new PrintWriter(filePath.toFile());
        printWriter.println(" ");
        printWriter.close();
    }

    /**
     * Lists all files and get their name & shortname to fill the lang file.
     *
     * @throws Exception
     */
    public static void generateLangs() throws Exception {
        File LANG_FILE = new File(PATH + "\\assets\\flansmod\\lang\\en_US.lang");

        PrintWriter printWriter = new PrintWriter(LANG_FILE);
        printWriter.println("");
        printWriter.close();

        ArrayList<Path> files = new ArrayList<>();
        try (Stream<Path> filePathStream = Files.walk(Paths.get(PATH))) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    files.add(filePath);
                }
            });
        }

        /*
        Listen all files and check if they ends by ".txt".
         */
        for (int i = 0; i < files.size(); i++) {
            if (!LANG_FILE.exists()) {
                new File(LANG_FILE.getParent()).mkdirs();
                LANG_FILE.createNewFile();
            }


            if (files.get(i).getParent().toString().endsWith("armorFiles")) {
                if (files.get(i).getFileName().toString().endsWith(".txt")) {
                    try {
                        // Open the file
                        FileInputStream fstream = new FileInputStream(String.valueOf(files.get(i)));
                        // Get the object of DataInputStream
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;


                        String id = "";
                        String name = "";
                        //Read File Line By Line
                        while ((strLine = br.readLine()) != null) {
                            if (strLine.startsWith("ShortName")) {
                                String[] parts = strLine.split(" ");
                                id = parts[1].toLowerCase();
                            }
                            if (strLine.startsWith("Name")) {
                                name = strLine.replaceAll("Name", "").replaceFirst(" ", "");
                            }
                        }

                        InputStream inputStream = new FileInputStream(LANG_FILE);

                        StringWriter writer = new StringWriter();
                        IOUtils.copy(inputStream, writer, Charset.forName("utf-8"));
                        String theString = writer.toString();
                        theString = theString + "item." + id + ".name=" + name;

                        PrintWriter writer1 = new PrintWriter(LANG_FILE);
                        writer1.println(theString);
                        writer1.close();


                        //Close the input stream
                        inputStream.close();
                    } catch (Exception e) {//Catch exception if any
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            }
        }
    }
}
