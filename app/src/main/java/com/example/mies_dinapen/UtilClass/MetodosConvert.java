package com.example.mies_dinapen.UtilClass;

import android.graphics.Bitmap;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MetodosConvert {

    public static String convertAudioEncoded(String ruta) throws IOException {
        String encoded = null;
        try {
            File file = new File(ruta);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encoded = Base64.encodeToString(bytes, 0);
            return encoded;
        }catch (Exception e){
        }
        return encoded;
    }
    public static String convertImageEncoded(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] AudioBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(AudioBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
