package com.brasens;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.TimeZone;

public class Config {
    public static final String APP_VERSION = "SNAPSHOT 1.0";

    public static final String BACKEND_HOST = getBackendHost();
    public static final String BACKEND_HOST_CLIENT_REGISTER = BACKEND_HOST + "/client/register";
    public static final String BACKEND_HOST_CLIENT_LOGIN = BACKEND_HOST + "/login";
    public static final String BACKEND_HOST_CLIENT_REGISTER_SENSOR = BACKEND_HOST + "/reservatory/register";

    // Endpoints para gerenciamento de usuários
    public static final String BACKEND_HOST_GET_ALL_USERS = BACKEND_HOST + "/client/users";
    public static final String BACKEND_HOST_UPDATE_USER = BACKEND_HOST + "/client/users/update";
    public static final String BACKEND_HOST_DELETE_USER = BACKEND_HOST + "/client/users";

    // Endpoints para gerenciamento de tanques
    public static final String BACKEND_HOST_GET_USER_TANKS = BACKEND_HOST + "/client/users/tanks";
    public static final String BACKEND_HOST_ADD_TANK = BACKEND_HOST + "/client/tanks/add";

    public static final String BACKEND_HOST_CREATE_TANK = BACKEND_HOST + "/reservatory/update";
    public static final String BACKEND_HOST_RENAME_TANK = BACKEND_HOST + "/reservatory";
    public static final String BACKEND_HOST_DELETE_TANK = BACKEND_HOST + "/reservatory";

    public static final String BACKEND_HOST_UPDATE_ENDPOINT = BACKEND_HOST + "/api/update";

    public static final String CHART_DATA_PATTERN = "dd/MM HH:mm:ss";
    public static Image getIcon(String icon){
        return new Image(Config.class.getResource(icon).toString());
    }

    public static String getColorPalleteProperties(String key) {
        Properties properties = new Properties();
        try (
                InputStream inputStream = Vetor.class.getClassLoader().getResourceAsStream("mspm/color_pallate.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("File not found: color_pallate.properties");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(key);
    }

    public static String getTooltipTextProperties(String key) {
        Properties properties = new Properties();
        try (
                InputStream inputStream = Vetor.class.getClassLoader().getResourceAsStream("mspm/tooltips_texts.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("File not found: tooltips_texts.properties");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static String getBackendHost() {
        return "https://ec2.vtr1000.com.br:8443";
    }

}
