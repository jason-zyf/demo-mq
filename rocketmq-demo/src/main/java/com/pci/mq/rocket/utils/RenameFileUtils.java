package com.pci.mq.rocket.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author zyting
 * @sinne 2020-09-04
 */
public class RenameFileUtils {

    public static void main(String[] args) {

        //重命名文件
        renameFile("E:\\ganpicture\\gan1-0");
    }

    private static void renameFile(String path) {

        File file = new File(path);
        File[] tempList = file.listFiles();

        //对文件名进行排序
        List<File> list = Arrays.asList(tempList);
        list = sortFileByName(list);

        int a = 1;
        for (int i = 0; i < list.size(); i++){
            File file1 = list.get(i);
            if(file1.isDirectory()){
                renameFile(file1.getAbsolutePath());
                continue;
            }
            String oldPath = list.get(i).getAbsolutePath();
            String oldName = list.get(i).getName();
            String newName = oldName.substring(oldName.lastIndexOf("."));

            String newPath = "" ;
            if(list.get(i).getParent().endsWith("-0")){
                newPath = list.get(i).getParent()+"\\Y"+a+newName;
            }else if(list.get(i).getParent().endsWith("-25")){
                newPath = list.get(i).getParent()+"\\X"+a+newName;
            }

            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            boolean b = oldFile.renameTo(newFile);
            if(b){
                System.out.println("重命名成功");
            }else{
                System.out.println("重命名失败");
            }
            a++;
        }
    }

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
