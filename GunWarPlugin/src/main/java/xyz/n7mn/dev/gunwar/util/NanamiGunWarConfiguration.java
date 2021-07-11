package xyz.n7mn.dev.gunwar.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import xyz.n7mn.dev.gunwar.mysql.MySQLSettingBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class NanamiGunWarConfiguration implements GunWarConfiguration {

    private Plugin plugin;
    private File dataFolder;
    private FileConfiguration config;
    private FileConfiguration permissionSetting;
    private FileConfiguration lang;
    private Document detailConfig;
    private File configFile;
    private File permissionFile;
    private File detailConfigFile;
    private File langFile;
    private boolean nanamiNetwork;

    public NanamiGunWarConfiguration(Plugin plugin) {
        this.plugin = plugin;
        this.nanamiNetwork = false;
    }

    public void init() {
        try {
            String dataFolder = "plugins/" + plugin.getName();

            File pluginFile = new File(dataFolder);
            if (!pluginFile.exists()) {
                pluginFile.mkdir();
            }
            this.dataFolder = pluginFile;
            File playerDataFile = new File(dataFolder + "/players");
            if(!playerDataFile.exists()) {
                playerDataFile.mkdir();
            }
            File permissionsFolder = new File(dataFolder + "/permission");
            if(!permissionsFolder.exists()) {
                permissionsFolder.mkdir();
            }
            File cacheFolder = new File(dataFolder + "/cache");
            if(!cacheFolder.exists()) {
                cacheFolder.mkdir();
            }
            File langFolder = new File(dataFolder + "/lang");
            if(!langFolder.exists()) {
                langFolder.mkdir();
            }

            detailConfigFile = new File(dataFolder + "/detail_config.xml");
            if(!detailConfigFile.exists()) {
                detailConfigFile.createNewFile();
                try {
                    InputStream inputStream = plugin.getResource("detail_config.xml");
                    File file = detailConfigFile;
                    OutputStream out = new FileOutputStream(file);
                    byte[] buf = new byte['?'];
                    int length;
                    while ((length = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                    out.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                detailConfig = db.parse(detailConfigFile);
            } catch(ParserConfigurationException | SAXException throwables) {
                throwables.printStackTrace();
            }
            if(detailConfig != null) {
                Element element = detailConfig.getDocumentElement();
                if(element != null && element.getNodeName().equalsIgnoreCase("config")) {
                    if(!element.getAttribute("version").equalsIgnoreCase(plugin.getDescription().getVersion())) {
                        element.setAttribute("version", plugin.getDescription().getVersion());
                    }
                    NodeList nodeList1 = element.getChildNodes();
                    for(int i = 0; i < nodeList1.getLength(); i++) {
                        Node node1 = nodeList1.item(i);
                        if(node1.getNodeName().equalsIgnoreCase("chat")) {
                            NodeList nodeList2 = node1.getChildNodes();
                            for(int j = 0; j < nodeList2.getLength(); j++) {
                                Node node2 = nodeList2.item(j);
                                if(node2.getNodeName().equalsIgnoreCase("lang")) {
                                    String langPath = node2.getTextContent();
                                    if(langPath != null && !langPath.isEmpty()) langFile = new File(langPath);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if(langFile != null) {
                if(!langFile.exists()) {
                    langFile.createNewFile();
                    if(langFile.getName().equalsIgnoreCase("ja_jp.yml")) {
                        try {
                            InputStream inputStream = plugin.getResource("lang/ja_jp.yml");
                            File file = langFile;
                            OutputStream out = new FileOutputStream(file);
                            byte[] buf = new byte['?'];
                            int length;
                            while ((length = inputStream.read(buf)) > 0) {
                                out.write(buf, 0, length);
                            }
                            out.close();
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            InputStream inputStream = plugin.getResource("lang/en_us.yml");
                            File file = langFile;
                            OutputStream out = new FileOutputStream(file);
                            byte[] buf = new byte['?'];
                            int length;
                            while ((length = inputStream.read(buf)) > 0) {
                                out.write(buf, 0, length);
                            }
                            out.close();
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                langFile = new File(dataFolder + "/lang/ja_jp.yml");
                if(!langFile.exists()) {
                    langFile.createNewFile();
                    try {
                        InputStream inputStream = plugin.getResource("lang/ja_jp.yml");
                        File file = langFile;
                        OutputStream out = new FileOutputStream(file);
                        byte[] buf = new byte['?'];
                        int length;
                        while ((length = inputStream.read(buf)) > 0) {
                            out.write(buf, 0, length);
                        }
                        out.close();
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            lang = YamlConfiguration.loadConfiguration(langFile);

            configFile = new File(dataFolder + "/config.yml");
            if (!configFile.exists()) {
                configFile.createNewFile();
                try {
                    InputStream inputStream = plugin.getResource("config.yml");
                    File file = configFile;
                    OutputStream out = new FileOutputStream(file);
                    byte[] buf = new byte['?'];
                    int length;
                    while ((length = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                    out.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            config = YamlConfiguration.loadConfiguration(configFile);

            MySQLSettingBuilder.builder().withSetting(config.getString("mysql.host", "localhost"),
                    config.getInt("mysql.port", 3306),
                    config.getString("mysql.database", ""),
                    config.getString("mysql.option", "?allowPublicKeyRetrieval=true&useSSL=false"),
                    config.getString("mysql.username", ""),
                    config.getString("mysql.password", "")).build();

            String ppath = config.getString("permission-setting", dataFolder + "/permission/default.yml");
            if(ppath.startsWith(":nanami-network:>")) {
                if(MySQLSettingBuilder.isEnabled()) nanamiNetwork = true;
                ppath = ppath.substring(17);
            } else {
                nanamiNetwork = false;
            }

            permissionFile = new File(ppath);
            if (!permissionFile.exists()) {
                permissionFile.createNewFile();
                try {
                    InputStream inputStream = plugin.getResource("permission/default.yml");
                    File file = permissionFile;
                    OutputStream out = new FileOutputStream(file);
                    byte[] buf = new byte['?'];
                    int length;
                    while ((length = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                    out.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            permissionSetting = YamlConfiguration.loadConfiguration(permissionFile);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public FileConfiguration getPermissionSetting() {
        return permissionSetting;
    }

    @Override
    public Document getDetailConfig() {
        return detailConfig;
    }

    @Override
    public FileConfiguration getLang() {
        return lang;
    }

    @Override
    public File getConfigFile() {
        return configFile;
    }

    @Override
    public File getPermissionSettingFile() {
        return permissionFile;
    }

    @Override
    public File getDetailConfigFile() {
        return detailConfigFile;
    }

    @Override
    public File getLangFile() {
        return langFile;
    }

    public void setPermissionFile(File file) {
        permissionFile = file;
    }

    @Override
    public boolean isNanamiNetwork() {
        return nanamiNetwork;
    }

    @Override
    public void setNanamiNetwork(boolean nanamiNetwork) {
        this.nanamiNetwork = nanamiNetwork;
    }

    public File getDataFolder() {
        return dataFolder;
    }
}
