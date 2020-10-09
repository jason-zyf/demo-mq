package com.pci.mq.rocket.utils;

import java.io.File;
import java.util.*;

/**
 * @author zyting
 * @sinne 2020-09-04
 */
public class RenameFileUtils {

    public static void main(String[] args) {

        // 执行文件夹路径，获取所有文件
        String path = "F:\\test";
        File file = new File(path);
        File[] tempList = file.listFiles();

        //对文件数组转换成集合形式
        List<File> list = Arrays.asList(tempList);

        // 根据名称进行排序
        list = sortFileByName(list);

        // 根据最后修改时间进行排序
        // list = sortFileByLastModified(list);

        // 重命名文件夹中及子文件夹中的文件
        renameFile(list);
    }

    /**
     * 先判断是否是文件夹，如果是文件夹则递归调用命名方法
     * @param list
     */
    private static void renameFile(List<File> list) {
        // 命名开始数字，如果从0开始则将 a=1 改成 a=0
        int a = 1;
        // 循环获取集合中的文件
        for (int i = 0; i < list.size(); i++){
            // 获取当前的文件对象
            File file1 = list.get(i);
            // 判断文件对象是否是文件夹
            if(file1.isDirectory()){
                // 如果当前文件对象是文件夹，则获取当前文件夹中的所有文件对象
                File[] childFiles = file1.listFiles();
                // 将当前文件夹的文件数组转换成集合形式
                List<File> childFileList = Arrays.asList(childFiles);
                // 将当前文件夹的文件根据名称进行排序
                childFileList = sortFileByName(childFileList);
                // 递归调用重命名方法
                renameFile(childFileList);
                // 跳出本次循环
                continue;
            }
            // 获取文件对象的绝对路径
            String oldPath = file1.getAbsolutePath();
            // 获取文件对象的名称
            String oldName = file1.getName();
            // 获取文件对象的后缀名
            String extName = oldName.substring(oldName.lastIndexOf("."));

            // 初始化重新命名文件绝对地址
            String newPath = "" ;
            if(file1.getParent().endsWith("-0")){
                // 设置重新命名文件绝对地址，如果是-0 结束的文件夹则，重命名文件名前面加Y，如Y1
                newPath = file1.getParent()+"\\Y"+a+extName;
            }else if(file1.getParent().endsWith("-25")){
                // 设置重新命名文件绝对地址，如果是-25 结束的文件夹则，重命名文件名前面加X，如X1
                newPath = file1.getParent()+"\\X"+a+extName;
            }

            // 将文件的绝对地址转换成文件对象
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            // 调用重命名方法
            boolean b = oldFile.renameTo(newFile);
            if(b){
                System.out.println("重命名成功");
            }else{
                // 如果重命名失败则将失败路径打印出来，方便修改后重新命名
                System.out.println("重命名失败，文件路径为："+oldPath);
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
