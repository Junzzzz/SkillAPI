package skillapi.utils;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Jun
 * @date 2020/8/21.
 */
public class ClassUtils {


    public static List<Class<?>> scanLocalPackageClasses(Class<?> loadClass, String packageName) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();

        final URL codeURL = loadClass.getProtectionDomain().getCodeSource().getLocation();
        System.out.println();
        final String path;
        try {
            path = URLDecoder.decode(codeURL.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Never happen
            return classes;
        }

        final List<String> classNames = new LinkedList<String>();

        final boolean isJar = "jar".equalsIgnoreCase(codeURL.getProtocol());
        if (!isJar) {
            scanClassByFile(packageName, path, true, classNames);
        } else {
            scanClassByJar(packageName, codeURL, classNames);
        }

        for (String className : classNames) {
            try {
                final Class<?> cls = Class.forName(className, true, loadClass.getClassLoader());
                classes.add(cls);
            } catch (ClassNotFoundException e) {
                FMLLog.log(Level.WARN, e, "Failed to load class: %s", className);
            }
        }

        return classes;
    }

    private static void scanClassByJar(String packageName, URL url, List<String> classes) {
        try {
            final JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection == null) {
                return;
            }
            JarFile jarFile = connection.getJarFile();
            if (jarFile == null) {
                return;
            }

            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String jarEntryName = entry.getName().replace('/', '.');
                if (jarEntryName.endsWith(".class") && jarEntryName.startsWith(packageName)) {
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".class"));
                    classes.add(className);
                }
            }
        } catch (IOException ignore) {
            // ignore
        }
    }

    private static void scanClassByFile(String packageName, String filePath, final boolean recursive, List<String> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        if (dirFiles == null) {
            return;
        }
        for (File file : dirFiles) {
            if (recursive && file.isDirectory()) {
                scanClassByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                classes.add(packageName + "." + className);
            }
        }
    }

    public static String getClassPackage(String className) {
        return className.substring(0, className.lastIndexOf("."));
    }
}
