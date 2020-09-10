package com.pci.mq.rocket.utils;

import java.io.File;
import java.util.*;

/**
 * @author zyting
 * @sinne 2020-09-09
 * 重命名文件夹   gan0-0,gan0-25
 */
public class RenameDirUtils {

    public static void main(String[] args) {

        String path = "E:\\ganpicture";

        File file = new File(path);
        File[] fileArr = file.listFiles();

        //对文件数组转换成集合形式
        List<File> list = Arrays.asList(fileArr);

        // 将文件进行分组 -0 放一组  -25放一组
        Map<String,List<File>> fileMap = groupByFilter(list);

        // 判断是否可进行重命名，判断 map 中各value的list大小是否一致
        boolean flag = checkFileSize(fileMap);
        if(!flag){
            // 如果 -0 组和 -25组的文件夹个数不一致，则跳出循环
            System.out.println("分组后文件夹个数不一致！");
            return;
        }

        // 循环map里面的值进行重命名
        for(Map.Entry<String, List<File>> entry : fileMap.entrySet()){
            String key = entry.getKey();
            List<File> fileList = entry.getValue();
            // 对文件夹进行排序
            fileList = sortDirByName(fileList);
            // 对个文件夹进行重命名
            renameDir(key,fileList);
        }
        System.out.println("文件夹重命名成功");
    }

    /**
     * 对文件夹进行重命名
     * @param list
     */
    private static void renameDir(String key, List<File> list) {
        int a = 1;
        for (int i = 0; i < list.size(); i++){
            File file1 = list.get(i);
            if(file1.isDirectory()){
                String oldPath = list.get(i).getAbsolutePath();
                String newPath = file1.getParent()+"\\gan"+a+"-"+key ;

                File oldFile = new File(oldPath);
                File newFile = new File(newPath);
                boolean b = oldFile.renameTo(newFile);
                a++;
            }
        }
    }

    /**
     * 将list按照过滤片透光率进行分组
     * @param list
     * @return
     */
    private static Map<String, List<File>> groupByFilter(List<File> list) {
        Map<String, List<File>> map = new HashMap<>();
        for(File file :list){
            String fileName = file.getName();
            String key = fileName.substring(fileName.lastIndexOf("-")+1);
            List<File> files = map.get(key);
            if(files != null && !files.isEmpty()){
                // 不为空,则获取map中的list并添加这个文件对象
                files.add(file);
                map.put(key, files);
            }else {
                // 为空
                List<File> newFiles = new ArrayList<>();
                newFiles.add(file);
                map.put(key, newFiles);
            }
        }
        return map;
    }

    /**
     * 检查分组后各文件夹的大小
     * @param fileMap
     * @return
     */
    private static boolean checkFileSize(Map<String, List<File>> fileMap) {

        boolean flag = false;
        if(fileMap != null && !fileMap.isEmpty()){
            Set<String> keys = fileMap.keySet();
            List<String> keyList = new ArrayList<>(keys);
            int size = fileMap.get(keyList.get(0)).size();
            for (String key : keyList){
                int curSize = fileMap.get(key).size();
                if(curSize != size){
                    return false;
                }
            }
            flag = true;
        }
        return flag;
    }

    /**
     * 对文件夹进行排序
     * @param list
     * @return
     */
    private static List<File> sortDirByName(List<File> list) {
        if(list != null && list.size() > 0){
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    int n1 = extractNumber(file.getName());
                    int n2 = extractNumber(newFile.getName());
                    return n1 - n2;
                }
            });
        }
        return list;
    }

    /**
     * 将文件夹名称转换成int类型
     * @param name
     * @return
     */
    private static int extractNumber(String name) {
        int i;
        try {
            String number = name.substring(0, name.lastIndexOf("-")).replaceAll("[^\\d]", "");
            i = Integer.parseInt(number);
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }

}
