package com.ayuan.myutils.simpleUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;


/**
 * @ProjectName: Cars
 * @Package: com.realmax.cars.tcputil
 * @ClassName: EncodeAndDecode
 * @CreateDate: 2020/3/15 17:05
 */
public class EncodeAndDecode {

    /**
     * @param imgData 需要转换的base64编码
     * @return 返回Bitmap类型的图拍你
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap base64ToImage(String imgData) {  //对字节数组字符串进行Base64解码并生成图片
        //图像数据为空
        if (imgData == null) {
            return null;
        }

        try {
            // 获取Base64解码对象
            Decoder decoder = Base64.getDecoder();
            //Base64解码
            byte[] b = decoder.decode(imgData);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //使用BitmapFactory工厂类将流转换成图片
            return BitmapFactory.decodeStream(new ByteArrayInputStream(b));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转换成Unicode编码
     *
     * @param inStr 需要抓换的字符串
     * @return 返回Unicode编码的字符串（为编码和编码的长度可能不同）
     */
    public static String getStrUnicode(String inStr) {
        StringBuffer unicode = new StringBuffer();
        char c;
        int bit;
        String tmp = null;
        for (int i = 0; i < inStr.length(); i++) {
            c = inStr.charAt(i);
            if (c > 255) {
                unicode.append("\\u");
                bit = (c >>> 8);
                tmp = Integer.toHexString(bit);
                if (tmp.length() == 1) {
                    unicode.append("0");
                }
                unicode.append(tmp);
                bit = (c & 0xFF);
                tmp = Integer.toHexString(bit);
                if (tmp.length() == 1) {
                    unicode.append("0");
                }
                unicode.append(tmp);
            } else {
                unicode.append(c);
            }
        }
        return (new String(unicode));
    }

    /**
     * @param drawable
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(Math.min(width, 4096), Math.min(height, 4096), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;/**/

        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ByteArrayInputStream bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
        /*return baos.toByteArray();*/
    }
}
