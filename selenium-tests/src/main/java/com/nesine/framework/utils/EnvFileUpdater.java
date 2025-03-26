package com.nesine.framework.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public final class EnvFileUpdater {

    private static final String ENV_FILE_PATH = ".env";
    private EnvFileUpdater() { }
    public static void updateKeys(Map<String, String> updates) {
        Path envPath = Paths.get(ENV_FILE_PATH);
        try {
            List<String> lines = Files.readAllLines(envPath);
            for (int i = 0; i < lines.size(); i++) {
                for (Map.Entry<String, String> entry : updates.entrySet()) {
                    String key = entry.getKey().toUpperCase();
                    String newValue = entry.getValue();
                    if (lines.get(i).trim().startsWith(key + "=")) {
                        lines.set(i, key + "=" + newValue);
                    }
                }
            }
            Files.write(envPath, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error updating .env file", e);
        }
    }
}
