package skillapi.utils;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import skillapi.skill.SkillRuntimeException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
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
    public static List<Class<?>> scanLocalClasses(URL classUrl) {
        return scanLocalClasses(classUrl, null, false);
    }

    public static List<Class<?>> scanLocalClasses(File codeFile) {
        return scanLocalClasses(codeFile, null, false);
    }

    public static List<Class<?>> scanLocalClasses(URL classUrl, boolean annotation) {
        return scanLocalClasses(classUrl, null, annotation);
    }

    public static List<Class<?>> scanLocalClasses(File codeFile, boolean annotation) {
        return scanLocalClasses(codeFile, null, annotation);
    }

    public static List<Class<?>> scanLocalClasses(File codeFile, String packageName, boolean annotation) {
        if (codeFile.getName().endsWith(".jar")) {
            final JarFile jarFile;
            try {
                jarFile = new JarFile(codeFile);
            } catch (IOException e) {
                throw new SkillRuntimeException(e, "Can not open file: %s", codeFile.getAbsolutePath());
            }
            return scanLocalClasses(jarFile, packageName, annotation);
        }

        URL codeUrl;
        try {
            codeUrl = codeFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new SkillRuntimeException("File is not absolute", e);
        }
        return scanLocalClasses(codeUrl, packageName, annotation);
    }

    public static List<Class<?>> scanLocalClasses(Class<?> anyLocalClass, String packageName, boolean annotation) {
        final URL codeUrl = anyLocalClass.getProtectionDomain().getCodeSource().getLocation();
        return scanLocalClasses(codeUrl, packageName, annotation);
    }

    public static List<Class<?>> scanLocalClasses(JarFile jar, String packageName, boolean annotation) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        List<String> classNames = new LinkedList<String>();
        scanClassByJar(jar, classNames);
        return getClasses(packageName, annotation, classes, classNames);
    }

    public static List<Class<?>> scanLocalClasses(URL classUrl, String packageName, boolean annotation) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();

        final String path;
        try {
            path = URLDecoder.decode(classUrl.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Never happen
            return classes;
        }

        List<String> classNames = new LinkedList<String>();

        final boolean isJar = "jar".equalsIgnoreCase(classUrl.getProtocol());
        if (!isJar) {
            scanClassByFile("", path, true, classNames);
        } else {
            scanClassByJar(classUrl, classNames);
        }

        return getClasses(packageName, annotation, classes, classNames);
    }

    private static List<Class<?>> getClasses(String packageName, boolean annotation, List<Class<?>> classes, List<String> classNames) {
        if (packageName != null) {
            classNames = filterClassName(classNames, packageName);
        }

        for (String className : classNames) {
            try {
                final Class<?> cls = Class.forName(className);
                if (!annotation || cls.getAnnotations().length > 0) {
                    classes.add(cls);
                }
            } catch (ClassNotFoundException e) {
                FMLLog.log(Level.WARN, e, "Failed to load class: %s", className);
            }
        }

        return classes;
    }

    private static List<String> filterClassName(List<String> list, String packageName) {
        final List<String> result = new LinkedList<String>();
        for (String s : list) {
            if (s.startsWith(packageName)) {
                result.add(s);
            }
        }
        return result;
    }
    private static void scanClassByJar(JarFile jarFile, List<String> classes) {
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String jarEntryName = entry.getName().replace('/', '.');
            if (jarEntryName.endsWith(".class")) {
                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".class"));
                classes.add(className);
            }
        }
    }

    private static void scanClassByJar(URL url, List<String> classes) {
        try {
            final JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection == null) {
                return;
            }
            JarFile jarFile = connection.getJarFile();
            if (jarFile == null) {
                return;
            }

            scanClassByJar(jarFile, classes);
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

    public static Object newInstance(Class<?> target, String msgFormat, Object... args) {
        try {
            return target.newInstance();
        } catch (InstantiationException e) {
            FMLLog.log(Level.ERROR, e, msgFormat, args);
            return null;
        } catch (IllegalAccessException e) {
            FMLLog.log(Level.ERROR, e, msgFormat, args);
            return null;
        }
    }
}
