package nl.weeaboo.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.weeaboo.io.StreamUtil;

final class ResourceUtil {

    public static void extractResource(Class<?> baseClass, String resourcePath, File outFile)
            throws IOException {

        byte[] fileData;
        InputStream in = baseClass.getResourceAsStream(resourcePath);
        try {
            fileData = StreamUtil.readBytes(in);
        } finally {
            in.close();
        }

        FileOutputStream out = new FileOutputStream(outFile);
        try {
            out.write(fileData);
        } finally {
            out.close();
        }
    }

}
