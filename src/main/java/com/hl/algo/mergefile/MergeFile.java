package com.hl.algo.mergefile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MergeFile {
    /**
     * 获取一个文件夹下的所有文件全路径
     *
     * @param path
     * @param listFileName
     */
    public static void getAllFileName(String path, ArrayList<String> listFileName, BufferedWriter writer) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                completNames[i] = path + names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }

        for (File a : files) {
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                getAllFileName(a.getAbsolutePath() + "\\", listFileName, writer);
            } else {
                if (a.getName().endsWith(".java")) {
                    readFileContent(a.getAbsolutePath(), writer);
                }
            }
        }
    }

    public static String readFileContent(String fileName, BufferedWriter writer) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
                writer.write(tempStr);
                writer.newLine();
            }
            writer.flush();
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void main(String[] args) throws IOException {
        String distFile = "D:\\rz\\test1.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(distFile));
        ArrayList<String> listFileName = new ArrayList<String>();
        getAllFileName("D:\\IdeaProjects\\MedicalDataDesensitization\\src\\main\\java", listFileName, writer);
        for (String name : listFileName) {
            if (name.contains(".txt") || name.contains(".properties")) {
                System.out.println(name);
            }
        }
    }


}
