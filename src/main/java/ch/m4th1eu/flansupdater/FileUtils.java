package ch.m4th1eu.flansupdater;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileUtils {

    public static final ArrayList<String> langs = new ArrayList<>();

    /**
     * Get params from a file's path.
     *
     * @param argument argument's name
     * @param url      file's path
     * @return argument's value
     */
    public static String get(String argument, Path url) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(url.toString()));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith(argument)) {
                    return line.replaceFirst(argument + " ", "");
                }

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "null";
    }

    /**
     * Get params from a file's content
     *
     * @param argument argument's name
     * @param content  file's content
     * @return argument's value
     */
    public static String get(String argument, String content) {
        String str;
        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            while ((str = reader.readLine()) != null) {
                if (argument.equals("Name")) {
                    if (str.contains("Name") && !str.contains("ShortName")) {
                        return str.replaceFirst(argument + " ", "");
                    }
                } else if (str.startsWith(argument)) {
                    return str.replaceFirst(argument + " ", "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //doesn't work neither
        /*List<String> lines = new ArrayList<>();
        try {
            lines = IOUtils.readLines(new StringReader(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.stream().filter(s -> s.startsWith(argument)).findFirst().map(s -> s.replaceFirst(argument + " ", "")).orElse("null");*/


        //disabled because I think it skip the firstline
        /*Scanner scanner = new Scanner(content);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(argument)) {
                return line.replaceFirst(argument + " ", "");
            }
        }
        scanner.close();*/

        return "not found";
    }

    /**
     * Set params in a file's content
     *
     * @param argument argument's name
     * @param content  file's content
     * @return argument's value
     */
    public static String set(String argument, String value, String content) {
        Scanner scanner = new Scanner(content);
        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(argument)) {
                line = argument + " " + value;
            }

            stringBuilder.append(line).append("\n");
        }
        scanner.close();

        return stringBuilder.toString();
    }

    /**
     * Read a file and convert it to string.
     *
     * @param file file to read
     * @return file's content
     */
    public static String readFile(File file) {
        if (!file.exists()) {
            return "";
        }

        try {
            StringBuilder fileData = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file.toString()), StandardCharsets.UTF_8));
            char[] buf = new char[1024];
            int numRead;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            return fileData.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void addLang(String shortName, String name) {
        langs.add("item." + shortName + ".name=" + name);
    }

    public static void writeLangs(ArrayList<String> langs) {
        try {
            //check if the newpack folder exists, if not we create it.
            File langsFolder = new File(Main.LANG_NEWPATH);
            if (!langsFolder.exists()) {
                boolean isCreated = langsFolder.mkdirs();
                if (isCreated) {
                    Logger.info("Successfully created new langs folder.");
                } else {
                    Logger.error("Cannot create new langs folder!");
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            langs.forEach(str -> stringBuilder.append(str).append("\n"));

            Writer myWriter = new OutputStreamWriter(new FileOutputStream(Main.LANG_NEWPATH + "en_US.lang"), StandardCharsets.UTF_8);
            myWriter.write(stringBuilder.toString());
            myWriter.close();
            Logger.info("Successfully generated the new lang file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAllIcons() {
        try (Stream<Path> paths = Files.walk(Paths.get(Main.ICONS_PATH))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                Path newPath = Paths.get(Main.ICONS_NEWPATH + path.getFileName().toString().toLowerCase().replaceAll(" ", ""));

                try {
                    org.apache.commons.io.FileUtils.copyFile(path.toFile(), newPath.toFile(), false);
                } catch (IOException e) {
                    Logger.error("Cannot rename/move icons file : " + path.getFileName());
                    e.printStackTrace();
                }
            });
            Logger.info("Successfully copied and renamed the icons files.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createModels(String shortName, String icon) {
        //check if the models folder exists, if not we create it.
        File modelsFolder = new File(Main.JSON_NEWPATH);
        if (!modelsFolder.exists()) {
            boolean isCreated = modelsFolder.mkdirs();
            if (isCreated) {
                Logger.info("Successfully created jsons folder.");
            } else {
                Logger.error("Cannot create -jsons folder!");
            }
        }

        String jsonString = "{\"parent\":\"builtin/generated\",\"textures\":{\"layer0\":\"flansmod:items/your_item_texture\"},\"display\":{\"thirdperson_lefthand\":{\"rotation\":[0,90,-35],\"translation\":[0,1.25,-2.5],\"scale\":[0.8,0.8,0.8]},\"thirdperson_righthand\":{\"rotation\":[0,90,-35],\"translation\":[0,1.25,-2.5],\"scale\":[0.8,0.8,0.8]},\"firstperson_lefthand\":{\"rotation\":[0,-45,25],\"translation\":[0,4,2],\"scale\":[0.8,0.8,0.8]},\"firstperson_righthand\":{\"rotation\":[0,-45,25],\"translation\":[0,4,2],\"scale\":[0.8,0.8,0.8]}}}";
        jsonString = jsonString.replaceFirst("your_item_texture", icon);

        if (Paths.get(Main.JSON_PATH + shortName + ".json").toFile().exists()) {
            jsonString = readFile(new File(Main.JSON_PATH + shortName + ".json"));
        }

        try {
            Writer myWriter = new OutputStreamWriter(new FileOutputStream(Main.JSON_NEWPATH + shortName + ".json"), StandardCharsets.UTF_8);
            myWriter.write(jsonString);
            myWriter.close();
        } catch (IOException e) {
            Logger.error("Cannot create the json model file for : " + shortName);
            e.printStackTrace();
        }

    }

    public static void copyModels() {
        try {
            org.apache.commons.io.FileUtils.copyDirectory(new File(Main.MODELS_PATH), new File(Main.MODELS_NEWPATH));
            Logger.info("Successfully copied the models directory.");
        } catch (IOException e) {
            Logger.error("Cannot copy models directory!");
            e.printStackTrace();
        }
    }


    public static void deleteTempsFolder() {
        recursiveDelete(new File(Main.PACK_PATH));
        recursiveDelete(new File(Main.NEWPACK_PATH));
        Logger.info("Sucessfully deleted temporary directories.");
    }


    private static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;

        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            //call recursively
            Arrays.stream(file.listFiles()).forEach(FileUtils::recursiveDelete);
        }
        //call delete to delete files and empty directory
        file.delete();
    }

    public static void createPackInfo() {
        JsonObject jsonObject = new JsonObject();
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", 1);
        pack.addProperty("description", "Generated by Flans Updater.");
        jsonObject.add("pack", pack);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(jsonObject);

        try {
            Writer myWriter = new OutputStreamWriter(new FileOutputStream(Main.NEWPACK_PATH + "pack_info.mcmeta"), StandardCharsets.UTF_8);
            myWriter.write(jsonString);
            myWriter.close();
            Logger.info("Successfully created the pack_info file.");
        } catch (IOException e) {
            Logger.error("Cannot create the pack_info file!");
            e.printStackTrace();
        }
    }


}
