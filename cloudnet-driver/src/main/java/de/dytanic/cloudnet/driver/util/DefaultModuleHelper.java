package de.dytanic.cloudnet.driver.util;

import de.dytanic.cloudnet.common.Validate;
import de.dytanic.cloudnet.common.annotation.UnsafeClass;
import de.dytanic.cloudnet.common.io.FileUtils;
import de.dytanic.cloudnet.common.unsafe.ResourceResolver;
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class is for utility methods for the base modules in this multi module project
 */
@UnsafeClass
public final class DefaultModuleHelper {

    public static final String DEFAULT_CONFIGURATION_DATABASE_NAME = "cloudNet_module_configuration";

    private DefaultModuleHelper() {
        throw new UnsupportedOperationException();
    }

    public static boolean copyCurrentModuleInstanceFromClass(Class<?> clazz, File target) {
        Validate.checkNotNull(clazz);
        Validate.checkNotNull(target);

        try {
            target.getParentFile().mkdirs();

            if (!target.exists()) {
                target.createNewFile();
            }

            URLConnection connection = ResourceResolver.resolveURIFromResourceByClass(clazz).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(target)) {
                FileUtils.copy(inputStream, fileOutputStream);
            }
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void copyPluginConfigurationFileForEnvironment(Class<?> targetClass, ServiceEnvironmentType type, File file) {
        FileUtils.openZipFileSystem(file, fileSystem -> {
            Path pluginPath = fileSystem.getPath("plugin.yml");

            if (Files.exists(pluginPath)) {
                Files.delete(pluginPath);
            }

            try (OutputStream outputStream = Files.newOutputStream(pluginPath)) {
                switch (type) {
                    case VELOCITY:
                        break;
                    case BUNGEECORD:
                        try (InputStream inputStream = targetClass.getClassLoader().getResourceAsStream("plugin.bungee.yml")) {
                            if (inputStream != null) {
                                FileUtils.copy(inputStream, outputStream);
                            }
                        }
                        break;
                    case NUKKIT:
                        try (InputStream inputStream = targetClass.getClassLoader().getResourceAsStream("plugin.nukkit.yml")) {
                            if (inputStream != null) {
                                FileUtils.copy(inputStream, outputStream);
                            }
                        }
                        break;
                    default:
                        try (InputStream inputStream = targetClass.getClassLoader().getResourceAsStream("plugin.bukkit.yml")) {
                            if (inputStream != null) {
                                FileUtils.copy(inputStream, outputStream);
                            }
                        }
                        break;
                }
            }
            return null;
        });
    }
}