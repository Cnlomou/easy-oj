package org.example.judger.util;

import java.io.*;

/**
 * @author Cnlomou
 * @create 2020/7/11 18:05
 *
 * io工具类
 */
public class IOUtil {

    public static void copy(File des,File src) throws IOException {
        copy(new FileOutputStream(des),new FileInputStream(src));
    }

    public static void copy(File des,String src) throws IOException {
        copy(new FileOutputStream(des),src);
    }
    public static void copy(OutputStream des,InputStream src) throws IOException {
        BufferedOutputStream output = new BufferedOutputStream(des);
        BufferedInputStream input = new BufferedInputStream(src);
        byte[] buf=new byte[4096];
        int len=0;
        while((len=input.read(buf))!=-1){
            output.write(buf,0,len);
        }
        output.close();
        input.close();
    }
    public static void copy(OutputStream des,String src) throws IOException {
        copy(des,new ByteArrayInputStream(src.getBytes()));
    }
    public static void copy(OutputStream des,File src) throws IOException {
        copy(des,new FileInputStream(src));
    }
}
