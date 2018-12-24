package sh.spinlock.bigbrother.core;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Configuration {
    private static final Gson GSON = new Gson();

    public static <T> T read(String path, Class<T> clazz) throws IOException {
        File file = Paths.get(path).toFile();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return GSON.fromJson(reader, clazz);
    }
}
