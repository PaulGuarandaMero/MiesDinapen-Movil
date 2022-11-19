package com.example.mies_dinapen.Controller.UtilClass;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MetodosConvert {

    public static String convertAudioEncoded(String selectedPath) {
        String encoded = null;
        try {
            byte[] audioBytes;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();
            encoded = Base64.encodeToString(audioBytes, Base64.DEFAULT);
            return encoded;
        } catch (Exception e) {
            Log.e("TAG", "encodeAudio: ", e );
        }
        return encoded;
    }

/*    public static String convertAudioEncoded(String ruta) throws IOException {
        String encoded = null;
        try {
            File file = new File(ruta);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encoded = Base64.encodeToString(bytes, 0);
            return encoded;
        }catch (Exception e){
        }
        return encoded;
    }*/
    public static String convertImageEncoded(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] AudioBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(AudioBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
