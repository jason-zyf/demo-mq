package com.pci.mq.rocket.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author zyting
 * @sinne 2020-10-12
 * 检查文件夹中的文件个数是否相同
 * 例如；gan1-0和gan1-25两个文件夹中的文件个数要相同，如果不同则记录
 */
public class CheckFileCountUtils {

    // 比较文件夹地址
    private static String path = "F:\\test\\gan1-146";

    // 记录输出文本地址
    private static String textPath = "C:\\Users\\jason\\Desktop\\text.txt";

    // 初始化记录字符串
    private static StringBuffer result = new StringBuffer();

    public static void main(String[] args) {

        // 获取文件夹下面的所有文件对象
        File file = new File(path);
        File[] fileArr = file.listFiles();

        //对文件数组转换成集合形式
        List<File> list = Arrays.asList(fileArr);

        // 调用检查文件夹里面文件个数的方法
        checkFileCount(list);

        // 将记录写入到文本中
        writerToText(result.toString());
        // 将记录打印在控制台上
        System.out.println(result.toString());
    }

    /**
     * 检查文件夹里面文件个数的方法
     * @param list
     */
    private static void checkFileCount(List<File> list) {
        // 遍历文件夹中的文件对象
        for(File file : list){
            // 获取文件名称
            String fileName = file.getName();
            // 如果文件对象是文件夹且文件夹以"gan"开始，以-0或-25结尾的
            if(file.isDirectory()&&fileName.startsWith("gan")
                &&(fileName.endsWith("-0")||fileName.endsWith("-25"))){
                // 获取该文件夹内的文件个数
                int size = file.listFiles().length;
                // 获取文件的上级文件夹路径
                String comparePath = file.getParent();
                // 初始化要比较的文件夹名称
                String compareFileName = "";
                // 如果文件夹以 -0 结尾
                if(fileName.endsWith("-0")){
                    // 获取要比较的文件夹名称，例如 fileName="gan1-0"，则 compareFileName="gan1-25"
                    compareFileName = fileName.replace("-0", "-25");
                    // 拼接文件夹的绝对路径
                    comparePath = comparePath +"//"+compareFileName;
                }else {
                    // 文件夹以-25结尾，获取要比较的文件夹名称，例如 fileName="gan1-25"，则 compareFileName="gan1-0"
                    compareFileName = fileName.replace("-25", "-0");
                    // 拼接文件夹的绝对路径
                    comparePath = comparePath +"//"+compareFileName;
                }

                // 将要比较的相应的文件路径转换为文件对象
                File compareFile = new File(comparePath);
                try {
                    // 获取另一个文件夹中文件的个数
                    int compareSize = compareFile.listFiles().length;
                    /**
                     * 如果两个比较的文件夹中个数不一致，则记录起来
                     * 例如gan1-0中有9个文件，gan1-25中有8个文件，则会记录gan1-0和gan1-25两个文件夹中的文件个数不一致
                     */
                    if(size != compareSize){
                        // 如果之前有记录了则跳过，防止重复记录
                        if(!result.toString().contains(fileName)){
                            result.append(fileName+"和"+compareFileName+"两个文件夹中的文件个数不一致").append("\n");
                        }
                    }
                }catch (Exception e){
                    // 如果捕获到异常，说明路径为comparePath里面参数值的文件夹不存在
                    result.append(compareFileName+"文件夹不存在").append("\n");
                }
            }else if(file.isDirectory()){
                // 如果文件对象是文件夹，则获取文件夹内的所有文件对象，并递归调用检查文件夹里面文件个数的方法
                File[] files = file.listFiles();
                //对文件数组转换成集合形式
                List<File> clist = Arrays.asList(files);

                // 递归调用检查文件夹里面文件个数的方法
                checkFileCount(clist);
            }
        }
    }

    /**
     * 将记录写入到文本中
     * @param str 要写入文本中的字符串
     */
    private static void writerToText(String str) {
        FileWriter fw = null;
        try {
            //创建字符输出流对象，负责向文件内写入
            fw = new FileWriter(textPath);
            //将str里面的内容读取到fw所指定的文件中
            fw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
