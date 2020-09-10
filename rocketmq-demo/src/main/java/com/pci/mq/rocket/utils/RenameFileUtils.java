package com.pci.mq.rocket.utils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyting
 * @sinne 2020-09-04
 */
public class RenameFileUtils {

    public static void main(String[] args) {

        // 执行文件夹路径，获取所有文件
        String path = "E:\\ganpicture\\gan1-0";

        File file = new File(path);
        File[] tempList = file.listFiles();

        //对文件数组转换成集合形式
        List<File> list = Arrays.asList(tempList);

        // 根据名称进行排序
        list = sortFileByName(list);

        // 根据最后修改时间进行排序
//        list = sortFileByLastModified(list);

        System.out.println(list);

        //重命名文件
//        renameFile(list);
    }

    /**
     * 对排序后的文件进行重命名
     * @param list
     */
    private static void renameFile(List<File> list) {

        int a = 1;
        for (int i = 0; i < list.size(); i++){
            File file1 = list.get(i);
            if(file1.isDirectory()){
                // 如果是
                File[] childFiles = file1.listFiles();
                List<File> childFileList = Arrays.asList(childFiles);
                renameFile(childFileList);
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

    /**
     * 按照随后修改时间进行排序
     * @param list  文件集合
     * @return
     */
    private static List<File> sortFileByLastModified(List<File> list) {
        if(list != null && list.size() > 0){
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 根据文件名进行排序
     * @param list
     * @return
     */
    public static List<File> sortFileByName(List<File> list) {
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
     * 将文件名非数字转换成空字符串
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
