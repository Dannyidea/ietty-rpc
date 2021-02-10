package org.idea.netty.framework.server.common.utils;

/**
 * @Author linhao
 * @Date created in 11:10 上午 2021/2/8
 */
public class ConvertUtils {

    public static byte[] intToByte(int n) {
        byte[] buf = new byte[4];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    public static byte[] shortToByte(short n) {
        byte[] buf = new byte[2];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    /**
     * 字节数组转换为int数值
     * 十六进制的开头一般都是0x开头
     *
     * @param bytes
     * @return
     */
    public static int byteToInt(byte[] bytes) {
        if (bytes.length != 4) {
            return 0;
        }
        return (bytes[0]) & 0xff | (bytes[1] << 8) & 0xff00 | (bytes[2] << 16) & 0xff0000 | (bytes[3] << 24) & 0xff000000;
    }

    public static short byteToShort(byte[] bytes){
        if (bytes.length != 2) {
            return 0;
        }
        return (short) ((bytes[0]) & 0xff
                        | (bytes[1] << 8) & 0xff00);
    }


    public static void main(String[] args) {
        byte[] b = intToByte( 1246);
        int r = byteToInt(b);
        System.out.println(r);
    }
}
