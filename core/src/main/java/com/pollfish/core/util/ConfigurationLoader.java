package com.pollfish.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);
    private static final String CONFIGURATION_FILE = "configuration.yml";
    private static final ConfigurationLoader INSTANCE = new ConfigurationLoader();

    private Properties properties;

    private ConfigurationLoader() {
        loadConfigurationFile();
    }

    private void loadConfigurationFile() {
        properties = new Properties();
        try (InputStream confFileStream = ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE)) {
            if (confFileStream != null) {
                properties.load(confFileStream);
            } else {
                informForConfigurationFileError();
            }
        } catch (IOException e) {
            informForConfigurationFileError();
            LOG.error(e.getMessage());
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public static String getIp() {
        return INSTANCE.getProperties().getProperty("ip", "localhost");
    }

    public static int getPort() {
        return Integer.parseInt(INSTANCE.getProperties().getProperty("port", "9090"));
    }

    private static void informForConfigurationFileError() {
        LOG.error("Error reading configuration file. Using default values");
    }
}
