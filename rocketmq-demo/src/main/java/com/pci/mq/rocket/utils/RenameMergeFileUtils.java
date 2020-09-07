package com.pci.mq.rocket.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author zyting
 * @sinne 2020-09-06
 */
public class RenameMergeFileUtils {

    public static int x = 1;
    public static int y = 1;

    public static void main(String[] args) {
        // 调用重命名并将同类的文件合并到一起的方法
        renameMergeFile("E:\\ganpicture");
        System.out.println("文件重命名并同类合并到一个文件夹成功");
    }

    /**
     * 重命名并将同类的文件合并到一起
     * @param path
     */
    private static void renameMergeFile(String path) {

        // 要合并到哪个文件夹的地址
        String mergeDir = "E:\\merge";

        File file = new File(path);
        File[] tempList = file.listFiles();

        //对文件名进行排序
        List<File> list = Arrays.asList(tempList);
        list = sortFileByName(list);

        for (int i = 0; i < list.size(); i++){
            File file1 = list.get(i);
            if(file1.isDirectory()){
                renameMergeFile(file1.getAbsolutePath());
                continue;
            }

            String oldPath = list.get(i).getAbsolutePath();
            String oldName = list.get(i).getName();
            String newName = oldName.substring(oldName.lastIndexOf("."));

            String newPath = "" ;
            if(list.get(i).getParent().endsWith("-0")){
                newPath = mergeDir+"\\Y"+y+newName;
                y = y + 1;
            }else if(list.get(i).getParent().endsWith("-25")){
                newPath = mergeDir+"\\X"+x+newName;
                x = x+1;
            }
            // 如果新文件的地址不为空则调用复制文件的方法
            if(!StringUtils.isEmpty(newPath)){
                copyFile(oldPath,newPath);
            }
        }
    }

    /**
     * 复制文件的方法
     * @param oldPath 源文件地址（要复制的文件地址）
     * @param newPath 目标文件地址（文件复制到哪里）
     */
    private static void copyFile(String oldPath, String newPath) {
        // 初始化输入流
        InputStream in = null;
        // 初始化输出流
        OutputStream out = null;
        try {
            in = new FileInputStream(new File(oldPath));
            out = new FileOutputStream(new File(newPath));

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件复制失败");
        } finally {
            // 关闭输出流
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 关闭输入流
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据文件名称进行排序，
     * @param list
     * @return
     */
    private static List<File> sortFileByName(List<File> list) {
        File[] files1 = list.toArray(new File[0]);
        Arrays.sort(files1, new Comparator<File>() {
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }
        });
        return new ArrayList<File>(Arrays.asList(files1));
    }

    /**
     * 将文件名的String 转换为int类型排序
     * @param name
     * @return
     */
    private static int extractNumber(String name) {
        int i;
        try {
            String number = name.replaceAll("[^\\d]", "");
            i = Integer.parseInt(number);
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }

}
