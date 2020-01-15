package com.ayuan.myutils.okhttputil;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 基于okhttp的网络请求工具类
 */
public class HttpUtil {
    /**
     * URL地址采用注解的方式传递，必须基于JavaBean来配合使用此方法
     *
     * @param map    需要传递的参数
     * @param tClass 需要解析的javaBean(此参数不能为空)
     * @param result okhttp中callback回调中调用此接口的实现
     * @param <T>    类型
     */
    public static <T> void doPost(Map<String, Object> map, Class<T> tClass, Result<? super T> result) {

        POST annotation = tClass.getAnnotation(POST.class);
        String url = annotation.value();
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                T t = new Gson().fromJson(response.body().string(), tClass);
                result.getData(t);
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    /**
     * @param <T>
     */
    public interface Result<T> {
        void getData(T t);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface POST {
        String value();
    }
}
