package com.adelaide.health.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Wenping(Deb) Du
 */
public class MD5Utils {
    public static void main(String[] args){
        try {
            String MD5String = MD5Utils.getMD5Checksum("/Users/deb/Downloads/apk/1321.apk");
            System.out.println(MD5String);
        } catch (Exception e) {
        }
    }

    /**
     * MD5
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);//加0x100是因为有的b[i]的十六进制只有1位
        }
        return result;

    }

    private static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);          //将流类型字符串转换为String类型字符串

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5"); //如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
        int numRead;

        do {
            numRead = fis.read(buffer);    //从文件读到buffer，最多装满buffer
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);  //用读到的字节进行MD5的计算，第二个参数是偏移量
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
}
