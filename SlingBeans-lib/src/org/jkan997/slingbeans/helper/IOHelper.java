package org.jkan997.slingbeans.helper;

import java.io.*;

/**
 *
 * @author jakaniew
 */
public class IOHelper {

    public static final int BUFFER_SIZE = 1024;

    public static String readReaderToString(Reader reader)
            throws IOException {
        StringBuilder res = new StringBuilder();
        char[] buf = new char[1024];
        int count = 0;
        while ((count = reader.read(buf)) != -1) {
            res.append(buf, 0, count);
        }
        return res.toString();
    }

    public static String readInputStreamToString(InputStream is) throws IOException {
        return readReaderToString(new InputStreamReader(is));
    }

    public static void readInputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int count;
        while ((count = is.read(buf)) > 0) {
            os.write(buf, 0, count);
        }
    }
    
    

    public static byte[] readInputStreamToBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        readInputStreamToOutputStream(is, bos);
        bos.close();
        return bos.toByteArray();
    }

    public static void readReaderToWriter(Reader reader, Writer writer)
            throws IOException {
        char[] buf = new char[1024];
        int count = 0;
        while ((count = reader.read(buf)) != -1) {
            writer.write(buf, 0, count);
        }
    }

    public static void readReaderToOutputStream(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int count = 0;
        while ((count = reader.read(buf)) != -1) {
            writer.write(buf, 0, count);
        }
    }
    
    public static void writeBytesToFile(byte[] bytes, String fileName){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(bytes);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
