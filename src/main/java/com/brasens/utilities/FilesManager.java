package com.brasens.utilities;

import com.brasens.Vetor;

import java.io.*;
import java.util.Properties;

public class FilesManager {

    public static String ApplicationDataFolder = System.getenv("APPDATA") + "\\Vetor";
    public static String ApplicationSystemFolder = System.getenv("ProgramFiles") + "\\Vetor";
    public static String[] ImportantDirectories = new String[]{
            ApplicationDataFolder + "\\" + "data",
            ApplicationDataFolder + "\\" + "downloads",
            ApplicationDataFolder + "\\" + "update",
            ApplicationSystemFolder + "\\" + "libs",
            ApplicationSystemFolder + "\\" + "libs" + "\\" + "jbr",
            ApplicationSystemFolder + "\\" + "libs" + "\\" + "resources",
    };

    public static final String KEY_USERNAME = "login.username";
    public static final String KEY_PASSWORD = "login.password";
    public static final String KEY_REMEMBER_ME = "login.remember";

    public static final String LOCAL_FILE_REMEMBER_PASSWORD = "remember.fit";
    public static final String LOCAL_FILE_CONFIG = FilesManager.ApplicationDataFolder + "\\" +"config.txt";

    private static final Properties properties = new Properties();

    public static void write(Object o, String filename) throws IOException {
        try {
            File selectedFile = new File(ApplicationDataFolder + "\\"+ filename);

            FileOutputStream fileOutput = new FileOutputStream(selectedFile);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileOutput);

            objectStream.writeObject(o);

            objectStream.close();
            fileOutput.close();
        }catch (Exception e){
            Vetor.printNicerStackTrace(e);
        }
    }

    public static Object read(File file){
        Object o = null;
       try {
           File selectedFile = file;

           FileInputStream fileInput = new FileInputStream(selectedFile);
           ObjectInputStream objectStream = new ObjectInputStream(fileInput);

           o = objectStream.readObject();

           objectStream.close();
           fileInput.close();

       }catch (Exception e){
           Vetor.printNicerStackTrace(e);
       }
        return o;
    }

    public static void applicationDirCreator() {
        File mainAppDataRoot = new File(FilesManager.ApplicationDataFolder);
        File mainRoot = new File(FilesManager.ApplicationSystemFolder);

        if (!mainAppDataRoot.exists())
            mainAppDataRoot.mkdirs();

        if (!mainRoot.exists())
            mainRoot.mkdirs();

        for (String dir : FilesManager.ImportantDirectories) {
            File dirFile = new File(dir);
            if (!dirFile.exists())
                dirFile.mkdirs();
        }
    }

    // --- MODIFICADO: Agora armazena o caminho do arquivo ---
    public static void initializeConfig(String path) {
        String configFilePath = LOCAL_FILE_CONFIG; // Armazena o caminho para uso posterior
        File configFile = new File(path);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                // Valores padrão podem ser adicionados aqui se necessário
                System.out.println("Arquivo de configuração criado.");
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo de configuração: " + e.getMessage());
            }
        }
        // Sempre carrega as propriedades do arquivo, existindo ou não
        loadConfig(path);
    }

    private static void loadConfig(String path) {
        try (FileReader reader = new FileReader(path)) {
            properties.load(reader);
            System.out.println("Arquivo de configuração carregado.");
        } catch (IOException e) {
            // Não é um erro crítico se o arquivo estiver vazio ou não existir ainda
            System.out.println("Nenhum arquivo de configuração existente para carregar ou está vazio.");
        }
    }

    public static String getConfigValue(String key) {
        return properties.getProperty(key, ""); // Retorna string vazia se a chave não for encontrada
    }

    // --- NOVO MÉTODO: Para definir um valor de configuração e salvar ---
    public static void setConfigValue(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }

    // --- NOVO MÉTODO: Para salvar as propriedades no arquivo ---
    private static void saveConfig() {
        if (LOCAL_FILE_CONFIG == null) {
            System.err.println("Caminho do arquivo de configuração não inicializado. Não é possível salvar.");
            return;
        }
        try (FileWriter writer = new FileWriter(LOCAL_FILE_CONFIG)) {
            properties.store(writer, "CarCenter Application Configuration");
            System.out.println("Configurações salvas em: " + LOCAL_FILE_CONFIG);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de configuração: " + e.getMessage());
        }
    }

}
