package com.github.junzzzz.skillapi.utils;

import com.github.junzzzz.skillapi.common.SkillLog;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author Jun
 */
public class ClassUtils {
    private static final LaunchClassLoader LOADER = Launch.classLoader;
    private static final String SIDE = FMLLaunchHandler.side().name();

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
        final List<Class<?>> classes = new LinkedList<>();
        List<String> classNames = new LinkedList<>();
        scanClassByJar(jar, classNames, annotation);
        return getClasses(packageName, annotation, classes, classNames);
    }

    public static List<Class<?>> scanLocalClasses(URL classUrl, String packageName, boolean annotation) {
        final List<Class<?>> classes = new LinkedList<>();

        final String path;
        try {
            path = URLDecoder.decode(classUrl.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Never happen
            return classes;
        }

        List<String> classNames = new LinkedList<>();

        final boolean isJar = "jar".equalsIgnoreCase(classUrl.getProtocol());
        if (!isJar) {
            scanClassByFile("", path, true, classNames, annotation);
        } else {
            scanClassByJar(classUrl, classNames, annotation);
        }

        return getClasses(packageName, annotation, classes, classNames);
    }

    private static List<Class<?>> getClasses(String packageName, boolean annotation, List<Class<?>> classes, List<String> classNames) {
        if (packageName != null) {
            classNames = filterClassName(classNames, packageName);
        }

        for (String className : classNames) {
            try {
                final Class<?> cls = LOADER.findClass(className);
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
        final List<String> result = new LinkedList<>();
        for (String s : list) {
            if (s.startsWith(packageName)) {
                result.add(s);
            }
        }
        return result;
    }

    private static void scanClassByJar(JarFile jarFile, List<String> classes, boolean annotation) {
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String jarEntryName = entry.getName().replace('/', '.');

            if (!jarEntryName.endsWith(".class")) {
                continue;
            }

            // read class file
            final byte[] bytes;
            try {
                bytes = getBytes(jarFile, entry);
            } catch (IOException e) {
                SkillLog.error(e, "Read class file failed!");
                continue;
            }

            // Remove classes that do not need to be analyzed & check side annotation to avoid ClassNotFoundException
            if (checkClass(bytes, annotation)) {
                continue;
            }

            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".class"));
            classes.add(className);
        }
    }

    private static byte[] getBytes(JarFile jarFile, ZipEntry entry) throws IOException {
        final InputStream is = jarFile.getInputStream(entry);
        final byte[] bytes = new byte[(int) entry.getSize()];
        IOUtils.readFully(is, bytes);
        is.close();
        return bytes;
    }

    private static byte[] getBytes(File classFile) throws IOException {
        FileInputStream fis = new FileInputStream(classFile);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(fis, output);
        fis.close();
        return output.toByteArray();
    }

    /**
     * @param classBytes Class binary data
     * @param annotation {@code true} to remove classes without annotation
     * @return If you need to skip this class, output {@code true} otherwise output {@code false}
     */
    private static boolean checkClass(byte[] classBytes, boolean annotation) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classNode, 0);

        if (checkAnnotation(classNode)) {
            // Do not have annotation
            return annotation;
        }

        return checkSideAnnotation(classNode);
    }

    /**
     * Check whether there are annotations, so as to filter out classes that do not need to be dynamically loaded
     *
     * @param classNode Class node read from class binary data
     * @return If it is need to be loaded return {@code true} otherwise return {@code false}
     */
    private static boolean checkAnnotation(ClassNode classNode) {
        return classNode.visibleAnnotations == null;
    }

    /**
     * Verify the {@link SideOnly} annotation of the class file, check whether the file needs to be ignored
     *
     * @param classNode Class node read from class binary data
     * @return If it is different from the current side, return {@code true} otherwise return {@code false}
     */
    private static boolean checkSideAnnotation(ClassNode classNode) {
        for (AnnotationNode visibleAnnotation : classNode.visibleAnnotations) {
            if (visibleAnnotation.desc.equals(Type.getDescriptor(SideOnly.class))) {
                if (visibleAnnotation.values == null) {
                    continue;
                }
                for (int x = 0; x < visibleAnnotation.values.size() - 1; x += 2) {
                    Object key = visibleAnnotation.values.get(x);
                    Object value = visibleAnnotation.values.get(x + 1);

                    if (!("value".equals(key))) {
                        continue;
                    }

                    if (!(value instanceof String[])) {
                        continue;
                    }

                    String[] side = (String[]) value;

                    if (side.length != 2) {
                        continue;
                    }

                    if ("Lcpw/mods/fml/relauncher/Side;".equals(side[0]) && !(side[1].equals(SIDE))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private static void scanClassByJar(URL url, List<String> classes, boolean annotation) {
        try {
            final JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection == null) {
                return;
            }
            JarFile jarFile = connection.getJarFile();
            if (jarFile == null) {
                return;
            }

            scanClassByJar(jarFile, classes, annotation);
        } catch (IOException ignore) {
            // ignore
        }
    }

    private static void scanClassByFile(String packageName, String filePath, final boolean recursive, List<String> classes, boolean annotation) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (dirFiles == null) {
            return;
        }
        for (File file : dirFiles) {
            if (recursive && file.isDirectory()) {
                scanClassByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes, annotation);
            } else {
                // read class file
                final byte[] bytes;
                try {
                    bytes = getBytes(file);
                } catch (IOException e) {
                    SkillLog.error(e, "Read class file failed!");
                    continue;
                }

                // Remove classes that do not need to be analyzed & check side annotation to avoid ClassNotFoundException
                if (checkClass(bytes, annotation)) {
                    continue;
                }

                String className = file.getName().substring(0, file.getName().length() - 6);
                classes.add(packageName + "." + className);
            }
        }
    }

    public static String getClassPackage(Class<?> clz) {
        return getClassPackage(clz.getName());
    }

    public static String getClassPackage(String className) {
        return className.substring(0, className.lastIndexOf("."));
    }

    public static Class<?> getCallerClass() {
        final Class<?>[] caller = new Class<?>[1];
        new SecurityManager() {
            {
                caller[0] = getClassContext()[3];
            }
        };
        return caller[0];
    }
}
