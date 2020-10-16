package org.example.judger.util;

import java.io.*;

/**
 * @author Cnlomou
 * @create 2020/7/11 15:31
 */
public class NativeLibLoader {

    public static void loadLibrary(String name) throws IOException {
        try{
            System.loadLibrary(name);
        }catch (UnsatisfiedLinkError e){
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            String fileName = System.mapLibraryName(name);
            int index = fileName.lastIndexOf('.');
            File tempFile = File.createTempFile(fileName.substring(0, index), fileName.substring(index));
            tempFile.deleteOnExit();

            byte[] buffer = new byte[4096];
            InputStream inputStream = contextClassLoader.getResourceAsStream(fileName);
            OutputStream outputStream = new FileOutputStream(tempFile);

            try {
                while ( inputStream.available() > 0 ) {
                    int StreamLength = inputStream.read(buffer);
                    if ( StreamLength >= 0 ) {
                        outputStream.write(buffer, 0, StreamLength);
                    }
                }
            } finally {
                outputStream.close();
                inputStream.close();
            }

            System.load(tempFile.getAbsolutePath());
        }
    }
}
