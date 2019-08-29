package com.chen.making.wheel.framework.spring.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
@UtilityClass
public class ScannerUtils {

    /**
     * 包扫描
     * 返回classNames
     * @param scanPackage
     */
    public static List<String> scanner(String scanPackage) {
        List<String> classNames = new ArrayList<>();
        doScanner(scanPackage, classNames);
        return classNames;
    }

    /**
     * 包扫描
     * 返回classNames
     * @param scanPackage
     */
    public static void doScanner(String scanPackage, List<String> classNames) {
        URL resource = ScannerUtils.class.getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File scanDir = new File(resource.getPath());
        File[] files = scanDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 是目录 递归扫描
                doScanner(scanPackage + "." + file.getName(), classNames);
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                classNames.add(scanPackage + "." + file.getName().replaceAll("\\.class", ""));
            }
        }
    }
}
