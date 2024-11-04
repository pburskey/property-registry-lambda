package com.burskey.property;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileResourceLoader {


    public static String ReadFile(String path) {
        try {
            File file = ResourceUtils.getFile(path);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File FindFile(String path) {

        try {
            File file = ResourceUtils.getFile(path);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
