package ua.ithillel.expensetracker.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64Converter {

    public String encodeToString(InputStream inputStream) throws IOException {
        byte[] data = readBytesFromInputStream(inputStream);
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] encodeToBytes(InputStream inputStream) throws IOException {
        byte[] data = readBytesFromInputStream(inputStream);
        return Base64.getEncoder().encode(data);
    }

    private byte[] readBytesFromInputStream(InputStream inputStream) throws IOException {
        try(ByteArrayOutputStream buffer = new ByteArrayOutputStream()){
            int bytesRead;
            byte[] data = new byte[1024];

            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1){
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }

}
