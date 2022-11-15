package com.example.mies_dinapen.UtilClass;

import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class MetodosConvert {

    public static String convertBinarioAudio(String ruta) throws IOException {
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

}
