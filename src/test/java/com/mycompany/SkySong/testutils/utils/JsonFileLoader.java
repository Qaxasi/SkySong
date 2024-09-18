package com.mycompany.SkySong.testutils.utils;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileLoader {

    public static String loadJson(String fileName) throws IOException {
        Path path = ResourceUtils.getFile("classpath:" + fileName).toPath();
        return Files.readString(path);
    }
}
