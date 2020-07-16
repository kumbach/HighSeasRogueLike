package com.schemafactor.rogueserver.config;

import com.schemafactor.rogueserver.common.JavaTools;

import java.io.*;
import java.util.Properties;

public class ServerConfig {
    public static final String CONFIG_FILENAME = "config" + File.separatorChar + "server.properties";

    private static Properties properties = new Properties();

    static {
        properties.setProperty("level.data", "./data");
        properties.setProperty("listen.port", "3006");
        properties.setProperty("websocket.port", "3007");
        properties.setProperty("u64.listen.port", "3008");
        properties.setProperty("network.timeout", "300");
        properties.setProperty("network.update.time", "1");
    }

    public static String getLevelPath() {
        return properties.getProperty("level.data");
    }

    public static int getListenPort() {
        return Integer.parseInt(properties.getProperty("listen.port"));
    }

    public static int getWebSocketPort() {
        return Integer.parseInt(properties.getProperty("websocket.port"));
    }

    public static int getU64ListenPort() {
        return Integer.parseInt(properties.getProperty("u64.listen.port"));
    }

    public static int getNetworkTimeout() {
        return Integer.parseInt(properties.getProperty("network.timeout"));
    }

    public static int getNetworkUpdateTime() {
        return Integer.parseInt(properties.getProperty("network.update.time"));
    }

    /**
     * Loads server configuration from a Java properties disk file.
     * First looks in the server install directory and then in the project 'config' directory
     * to support running either in an IDE or standalone.
     */
    public static void load() throws IOException {
        createDefaultConfigFile();
        JavaTools.printlnTime("Loading server configuration...");
        try (InputStream input = new FileInputStream(CONFIG_FILENAME)) {
            properties.load(input);
        }
        catch (IOException ex) {
            JavaTools.printlnTime("Could not read server.properties file. Using default settings!");
        }
    }

    private static void createDefaultConfigFile() {
        if (new File(CONFIG_FILENAME).exists()) {
            return;
        }
        try {
            new File("config").mkdir();
            properties.store(new FileOutputStream(CONFIG_FILENAME), "High Seas Multiplayer RogueLike Server Configuration");
            JavaTools.printlnTime("Created new default server.properties file");
        }
        catch (IOException e) {
            JavaTools.printlnTime("Could not create default server.properties file!");
            e.printStackTrace();
        }
    }
}
