package com.ayuan.myutils.tencentCloud.orc;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.ayuan.myutils.simpleUtils.EncodeAndDecode;

import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TencentCloudAPIInitUtil {
    private final static String CHARSET = "UTF-8";
    private static final String TAG = "哈哈哈";
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
    private static final char[] encodeMap = initEncodeMap();

    // 请替换自己的腾讯云
    private static String SecretKey = "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc";
    private static String SecretId = "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8";

    public static TreeMap<String, Object> init(Bitmap bitmap) throws Exception {
        // TreeMap可以自动排序
        TreeMap<String, Object> params = new TreeMap<>();
        // 公共参数
        params.put("Action", "LicensePlateOCR");
        // 公共参数
        params.put("SecretId", SecretId);
        // 公共参数
        params.put("Timestamp", System.currentTimeMillis() / 1000);
        // 公共参数
        // 实际调用时应当使用随机数，例如：params.put("Nonce", new Random().nextInt(java.lang.Integer.MAX_VALUE));
        params.put("Nonce", new Random().nextInt(Integer.MAX_VALUE));
        // 公共参数
        params.put("Region", "ap-shanghai");
        // 公共参数
        params.put("Version", "2018-11-19");
        // 业务参数
        params.put("ImageBase64", EncodeAndDecode.bitmapToBase64(compressMatrix(bitmap)));
        // 实际调用时应当使用系统当前时间，例如：   params.put("Timestamp", System.currentTimeMillis() / 1000);
        // 公共参数
        params.put("Signature", sign(getStringToSign(params), SecretKey, "HmacSHA1"));
        return params;
    }

    /**
     * 压缩图片到腾讯云可使用大小
     *
     * @param bitmap 需要压缩的图片
     * @return 返回压缩后的图片
     */
    private static Bitmap compressMatrix(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 计算签名
     *
     * @param s      拼接好的待签名字符串
     * @param key    SecretKey
     * @param method 算法
     * @return 返回签名计算结果
     * @throws Exception 抛出异常
     */
    private static String sign(String s, String key, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes(CHARSET));
        String str = printBase64Binary(hash, 0, hash.length);
        Log.d(TAG, "签名" + str);
        return str;
    }

    @SuppressLint("Assert")
    private static String printBase64Binary(byte[] input, int offset, int len) {
        char[] buf = new char[((len + 2) / 3) * 4];
        int ptr = printBase64Binary(input, offset, len, buf, 0);
        assert ptr == buf.length;
        return new String(buf);
    }

    private static int printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr) {
        // encode elements until only 1 or 2 elements are left to encode
        int remaining = len;
        int i;
        for (i = offset; remaining >= 3; remaining -= 3, i += 3) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(
                    ((input[i] & 0x3) << 4)
                            | ((input[i + 1] >> 4) & 0xF));
            buf[ptr++] = encode(
                    ((input[i + 1] & 0xF) << 2)
                            | ((input[i + 2] >> 6) & 0x3));
            buf[ptr++] = encode(input[i + 2] & 0x3F);
        }
        // encode when exactly 1 element (left) to encode
        if (remaining == 1) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(((input[i]) & 0x3) << 4);
            buf[ptr++] = '=';
            buf[ptr++] = '=';
        }
        // encode when exactly 2 elements (left) to encode
        if (remaining == 2) {
            buf[ptr++] = encode(input[i] >> 2);
            buf[ptr++] = encode(((input[i] & 0x3) << 4)
                    | ((input[i + 1] >> 4) & 0xF));
            buf[ptr++] = encode((input[i + 1] & 0xF) << 2);
            buf[ptr++] = '=';
        }
        return ptr;
    }

    private static char encode(int i) {
        return encodeMap[i & 0x3F];
    }

    private static char[] initEncodeMap() {
        char[] map = new char[64];
        int i;
        for (i = 0; i < 26; i++) {
            map[i] = (char) ('A' + i);
        }
        for (i = 26; i < 52; i++) {
            map[i] = (char) ('a' + (i - 26));
        }
        for (i = 52; i < 62; i++) {
            map[i] = (char) ('0' + (i - 52));
        }
        map[62] = '+';
        map[63] = '/';

        return map;
    }

    /**
     * 获取要签名的字符串
     *
     * @param params 包含所有参数的TreeMap
     * @return 返回拼接好的字符串
     */
    private static String getStringToSign(TreeMap<String, Object> params) {
        StringBuilder s2s = new StringBuilder("POSTocr.tencentcloudapi.com/?");
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        String substring = s2s.toString().substring(0, s2s.length() - 1);
        Log.d(TAG, substring);
        return substring;
    }
}