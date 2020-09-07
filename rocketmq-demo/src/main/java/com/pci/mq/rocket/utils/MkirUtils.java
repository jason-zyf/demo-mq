package com.pci.mq.rocket.utils;

import java.io.File;

/**
 * @author zyting
 * @sinne 2020-09-03
 */
public class MkirUtils {

    public static void main(String[] args) {

        // 批量建立文件件
        int[] arr = new int[]{0,25};
        for (int i = 1;i < 10; i++){
            for(int k : arr){
                StringBuilder sb = new StringBuilder();
                sb.append("E:\\ganpicture").append("\\gan").append(i).append("-").append(k);
                mkdir(sb.toString());
            }
        }
    }

    /**
     * 批量创建文件夹
     * @param path
     */
    public static void mkdir(String path) {
        File fd = null;
        try {
            fd = new File(path);
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
