package com.imorning.senseinfohelper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtils extends File {
    private File file;

    public FileUtils(String pathName) {
        super(pathName);
    }

    public FileUtils(String parent, String child) {
        super(parent, child);
    }

    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */

    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                //使用readLine方法，一次读一行
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.toString();

    }
}
