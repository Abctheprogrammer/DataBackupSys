package com.backup;

import java.io.*;
import java.util.zip.*;

public class CompressionUtils {

    public static void compress(String input, String output) throws Exception {
        try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(output));
             FileInputStream fis = new FileInputStream(input)) {

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                gos.write(buf, 0, len);
            }
        }
    }
}