package com.nesine.framework.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigReader {

    private static final Dotenv dotenv = Dotenv.load();

    public static String getBrowser() {
        return dotenv.get("BROWSER");
    }

    public static String getBaseApiUrl() {
        String apiUrl = System.getenv("BASE_API_URL");

        if (apiUrl == null) {
            apiUrl = dotenv.get("BASE_API_URL");
        }

        return apiUrl;
    }

    public static String getBaseUrl(){
        return dotenv.get("BASE_URL");
    }
}