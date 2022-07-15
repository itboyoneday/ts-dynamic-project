package com.tsingsoft.executor.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author bask
 */
public class PackageScanner {

    public PackageScanner() {
    }

    /**
     * 处理类文件
     * @param packageName
     * @param file
     */
    public void dealClassFile(String packageName, File file) {
        //此时通过file.getName()得到的是文件名，形式为：XXX.XXXX
        String className = file.getName();
        //判断一下是否为class文件，即是否以".class"结尾
        if (className.endsWith(".class")) {
            //将末尾的.class去掉
            className = className.replace(".class", "");
            try {
                //这里通过反射机制，处理经过完善的该class文件的名字（此时已经是该class文件多对应的类名）
                Class<?> klass = Class.forName(packageName + "." + className);
                //调用dealClass()方法，该方法是一个抽象方法，需要在用户使用该工具时对其进行实现，可以在不同的场景下有不同的处理方式
//                dealClass(klass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理jar包文件
     * @param url
     */
    private void dealJarPackage(URL url) {
        try {
            //JarURLConnection类通过JAR协议建立了一个访问 jar包URL的连接，可以访问这个jar包或者这个包里的某个文件
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            //通过这个连接进一步先得到JarFile
            JarFile jarFile = connection.getJarFile();
            //得到该JarFile目录下所有项目
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            //遍历得到的jarEntries
            while (jarEntries.hasMoreElements()) {
                JarEntry jar = jarEntries.nextElement();
                //如果是目录路径或者不是class文件，不予处理
                if(jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }
                //以上是对于Jar包的特殊处理，由于Jar包与普通包不一样，其中一些文件也不能直接以普通文件的方式来处理

                //以下与前面处理的方式一样
                String jarName = jar.getName();
                jarName = jarName.replace(".class", "");
                jarName = jarName.replace("/", ".");

                try {
                    Class<?> klass = Class.forName(jarName);
//                    dealClass(klass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理jar包文件
     * @param url
     */
    public List<String> getClassesNamesByJar(String basePack,URL url) {
        List<String> classes = new ArrayList<>();
        try {
            //JarURLConnection类通过JAR协议建立了一个访问 jar包URL的连接，可以访问这个jar包或者这个包里的某个文件
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            //通过这个连接进一步先得到JarFile
            JarFile jarFile = connection.getJarFile();
            //得到该JarFile目录下所有项目
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            //遍历得到的jarEntries
            while (jarEntries.hasMoreElements()) {
                JarEntry jar = jarEntries.nextElement();
                //如果是目录路径或者不是class文件，不予处理
                if(jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }
                //以上是对于Jar包的特殊处理，由于Jar包与普通包不一样，其中一些文件也不能直接以普通文件的方式来处理

                String jarEntryName = jar.getName();
                //这里我们需要过滤不是class文件和不在basePack包名下的类
                if(StringUtils.isNotBlank(basePack)){
                    if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(basePack)) {
                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                        //以下与前面处理的方式一样
                        if("DemoService.class".contains(jar.getName())){
                            System.out.println("");
                        }
                        className = className.replace(".class", "");
                        className = className.replace("/", ".");

                        classes.add(className);
                    }
                }else {
                    if (jarEntryName.contains(".class")) {
                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                        //以下与前面处理的方式一样
                        if("DemoService.class".contains(jar.getName())){
                            System.out.println("");
                        }
                        className = className.replace(".class", "");
                        className = className.replace("/", ".");

                        classes.add(className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 处理目录
     * @param PackageName
     * @param file
     */
    public void dealDirectory(String PackageName, File file) {
        //通过当前目录即参数中的File类型的file，可以得到当前目录下的所有文件
        File[] fileList = file.listFiles();
        //便利这个list，以此处理每一个file
        for (File files : fileList) {
            //判断是否仍是目录路径，若是，调用dealDirectory()方法
            if (files.isDirectory()) {
                //但对于参数中的PackageName路径要增加一层，即在末尾追加“.files.getName()”，一定要注意不能省略中间的“.”
                dealDirectory(PackageName + "." + files.getName(), files);
            }
            //此时判断是否已经是class文件，即不再是目录，已经不再有分支
            else if (files.isFile()){
                //处理class文件
                dealClassFile(PackageName, files);
            }

        }
    }

    /**
     * 包扫描
     * @param PackageName
     */
    public void packageScann(String PackageName) {
        String rootname = "";
        //将包名转换为目录形式
        rootname = PackageName.replace('.', '/');
        //通过给出的包名转换成的目录名称得到当前目录的URL
        URL url = Thread.currentThread().getContextClassLoader().getResource(rootname);
        //通过url.getProtocol().equals()方法来判断是File文件目录还是Jar包
        if (url.getProtocol().equals("file")) {
            try {
                URI uri = url.toURI();
                File file = new File(uri);
                //已检测是目录路径，通过dealDirectory()方法来处理
                dealDirectory(PackageName, file);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (url.getProtocol().equals("jar")) {
            //否则是jar包，通过dealJarPackage()方法来处理，此方法只需URL作为参数
            dealJarPackage(url);
        }
    }

    public static void main(String[] args) throws Exception{
        PackageScanner scanner = new PackageScanner();
        URL url = new URL("jar:file:/E:/workspace/study/springs-boot-plugin/plugins/plugin-impl-0.0.1-SNAPSHOT.jar!/");

        scanner.dealJarPackage(url);
    }
}

