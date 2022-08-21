package com.sh.datasharing.metadata;

import java.net.URLConnection;
import java.security.MessageDigest;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * Get basic information of a file.
 * Include: filename, filetype, size, com.sh.datasharing.metadata hash(md5)
 */
public class FileMetadataGenerator {
    public String fileName;
    public String fileType;
    public String size;
    public String metadataHash;

    public FileMetadataGenerator(String filePath){
        try {
            File f1 = new File(filePath);
            FileInputStream f1Stream = new FileInputStream(filePath);
            this.fileName = f1.getName();
            this.fileType = URLConnection.guessContentTypeFromStream(new BufferedInputStream(f1Stream));
            if (this.fileType == null) {this.fileType = "null";}
            this.size = String.valueOf(f1.length());
            this.metadataHash = generateMD5(filePath);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String generateMD5(String filePath) {
        if (!new File(filePath).isFile()) {
            System.err.println("Error: " + filePath
                    + " is not a valid file.");
            return null;
        }
        byte[] b = createChecksum(filePath);
        if (null == b) {
            System.err.println(("Error:create md5 string failure!"));
            return null;
        }
        StringBuilder result = new StringBuilder();

        for (byte value : b) {
            result.append(Integer.toString((value & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();
    }

    private static byte[] createChecksum(String filename) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead = -1;

            while ((numRead = fis.read(buffer)) != -1) {
                complete.update(buffer, 0, numRead);
            }
            return complete.digest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
