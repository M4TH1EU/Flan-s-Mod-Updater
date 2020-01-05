package ch.m4th1eu.flansupdater;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
    private static String OUTPUT_ZIP_FILE = "C:\\MyFile.zip";
    private static String SOURCE_FOLDER = "C:\\testzip";
    List<String> fileList;

    Zip() {
        fileList = new ArrayList<String>();
    }

    public static void main(String[] args) {
        OUTPUT_ZIP_FILE = args[0];
        SOURCE_FOLDER = args[1];

        Zip appZip = new Zip();
        appZip.generateFileList(new File(SOURCE_FOLDER));
        appZip.zipIt(OUTPUT_ZIP_FILE);
    }

    /**
     * Zip it
     *
     * @param zipFile output ZIP file location
     */
    public void zipIt(String zipFile) {

        byte[] buffer = new byte[1024];

        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);

            for (String file : this.fileList) {

                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in =
                        new FileInputStream(SOURCE_FOLDER + File.separator + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            System.out.println("Files added");

            zos.closeEntry();
            //remember close it
            zos.close();
            fos.close();

            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     *
     * @param node file or directory
     */
    public void generateFileList(File node) {

        //add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }

    }

    /**
     * Format the file path for zip
     *
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1);
    }


}