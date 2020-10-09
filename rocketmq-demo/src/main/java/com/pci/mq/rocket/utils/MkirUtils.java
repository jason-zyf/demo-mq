package com.pci.mq.rocket.utils;

import java.io.File;

/**
 * @author zyting
 * @sinne 2020-09-03
 */
public class MkirUtils {

    // 创建文件夹的存放地址
    private static String fileStr = "E:\\ganpicture\\gan";

    public static void main(String[] args) {

        // 批量建立文件件，如果需要创建 0,25,50的文件夹则将{0,25}改成{0,25,50}
        int[] arr = new int[]{0,25};
        /**
         * 循环创建文件夹
         * 如果从11开始，则将 i = 1 改成 i = 11
         * 如果是创建文件夹到 gan50，则将 i <= 10 改成 i <= 50
         */
        for (int i = 1;i <= 10; i++){
            // 遍历数组{0,25}中的元素
            for(int k : arr){
                StringBuilder sb = new StringBuilder();
                // 拼接文件夹的绝对路径，如：E:\ganpicture\gan1-0
                sb.append(fileStr).append(i).append("-").append(k);
                // 调用创建文件夹的方法
                mkdir(sb.toString());
            }
        }
    }

    /**
     * 批量创建文件夹
     * @param path 文件夹的绝对路径
     */
    public static void mkdir(String path) {
        File fd = null;
        try {
            // 将绝对路径转换成文件对象
            fd = new File(path);
            // 判断文件夹是否存在，如果不存在则创建文件夹
            if (!fd.exists()) {
                fd.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fd = null;
        }
    }

}
