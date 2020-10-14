package com.pci.mq.rocket.utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author zyting
 * @sinne 2020-09-09
 * 重命名文件夹   gan1-0,gan1-25
 * 用于中间有些文件夹被删除后，需要从1开始重新命名文件夹
 * 注意：没有递归调用
 */
public class RenameDirUtils {

    public static void main(String[] args) {

        // 需要重命名文件夹的地址
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
            // 如果 -0 组和 -25组的文件夹个数不一致，则结束代码运行
            System.out.println("分组后文件夹个数不一致！");
            return;
        }

        // 将重命名前面的文件夹名字记录到excel中
        WritableWorkbook workbook = null;
        try {
            File excelFile = new File("C:\\Users\\jason\\Desktop\\workbook.xls");
            excelFile.canRead();
            // 创建工作薄
            workbook = Workbook.createWorkbook(excelFile);
            // 创建sheet页面
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
            Label before = new Label(0,0,"前");
            sheet.addCell(before);
            Label later = new Label(1,0,"后");
            sheet.addCell(later);

            // 循环map里面的值进行重命名
            for(Map.Entry<String, List<File>> entry : fileMap.entrySet()){
                String key = entry.getKey();
                List<File> fileList = entry.getValue();
                // 对文件夹根据名称进行排序
                fileList = sortDirByName(fileList);
                // 对个文件夹进行重命名
                renameDir(key,fileList,sheet);
            }
            workbook.write();//写入数据
            System.out.println("文件夹重命名成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(workbook != null){
                try {
                    workbook.close(); //关闭流
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重命名文件夹
     * @param key 代表是0 还是25
     * @param list 表示-0的所有文件夹集合
     * @param sheet
     */
    private static void renameDir(String key, List<File> list, WritableSheet sheet) throws WriteException {
        // 重命名文件夹开始位置，如果从0开始则将 a=1 改成 a=0
        int a = 1;
        for (int i = 0; i < list.size(); i++){
            // 获取当前文件对象
            File file1 = list.get(i);
            // 判断当前文件对象是否是文件夹
            if(file1.isDirectory()){
                // 获取当前文件夹的绝对路径
                String oldPath = file1.getAbsolutePath();
                // 拼接重命名文件夹的绝对路径，如 E:\ganpicture\gan1-25
                String newPath = file1.getParent()+"\\gan"+a+"-"+key ;

                // 将重命名前后的文件名写入到excel表中
                Label before = new Label(0, a, file1.getName());
                sheet.addCell(before);
                Label later = new Label(1, a,"gan"+a+"-"+key);
                sheet.addCell(later);

                // 将文件绝对路径转换成文件对象
                File oldFile = new File(oldPath);
                File newFile = new File(newPath);
                // 重命名文件夹
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
        // 初始化一个map，用于根据滤光片分类保存各类的文件夹
        Map<String, List<File>> map = new HashMap<>();
        for(File file :list){
            String fileName = file.getName();
            // 获取文件夹是以0结束还是25结束
            String key = fileName.substring(fileName.lastIndexOf("-")+1);
            // 从map中获取对应类型的文件夹集合
            List<File> files = map.get(key);
            // 判断获取该类型的文件夹个数是否为空
            if(files != null && !files.isEmpty()){
                // 不为空,则获取map中的list并添加这个文件对象
                files.add(file);
                map.put(key, files);
            }else {
                // 为空，则创建一个list集合用来保存此类型的文件夹，并将此文件夹对象添加进去
                List<File> newFiles = new ArrayList<>();
                newFiles.add(file);
                map.put(key, newFiles);
            }
        }
        return map;
    }

    /**
     * 检查分组后各文件夹的大小；
     * 如果-0和-25的文件夹个数不一致，则不重命名文件夹，防止顺序弄错
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
